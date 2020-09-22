package io.ceph.rgw.client.core;

import io.ceph.rgw.client.config.RGWClientProperties;
import io.ceph.rgw.client.exception.RGWClientException;
import io.ceph.rgw.client.util.AbstractClosable;
import io.ceph.rgw.client.util.ReflectionUtil;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Unsafe;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/3/12.
 */
public abstract class GenericConnectors<C> extends AbstractClosable implements Connectors<C> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GenericConnectors.class);
    private static final ZoneId ZONE_ID = ZoneOffset.ofHours(8);
    protected final RGWClientProperties.ConnectorProperties properties;
    private final Map<C, Metrics> map;

    protected GenericConnectors(RGWClientProperties.ConnectorProperties properties) {
        this.properties = Objects.requireNonNull(properties);
        this.map = new ConcurrentHashMap<>();
    }

    @Override
    public void markSuccess(C connector) {
        try {
            Metrics metrics = map.computeIfAbsent(connector, k -> new Metrics(64));
            metrics.markSuccess();
            LOGGER.debug("Connector[{}] marked success, metrics: [{}].", connector, metrics);
        } catch (Throwable cause) {
            LOGGER.error("Failed to mark success.", cause);
        }
    }

    @Override
    public void markFailure(C connector, Throwable cause) {
        if (connector == null) {
            return;
        }
        try {
            Metrics metrics = map.computeIfAbsent(connector, k -> new Metrics(64));
            metrics.markFailure();
            LOGGER.warn("Connector[{}] marked failure, metrics: [{}].", connector, metrics, cause);
        } catch (Throwable t) {
            LOGGER.error("Failed to mark failure.", t);
        }
    }

    @Override
    public C get() {
        C c1 = doGet();
        if (isAllowRequest(c1)) {
            return c1;
        }
        C c2 = doGet();
        if (c2 != c1 && isAllowRequest(c2)) {
            return c2;
        }
        throw new RGWClientException("connector not allow request");
    }

    protected abstract C doGet();

    protected boolean isAllowRequest(C connector) {
        if (connector == null) {
            return false;
        }
        Metrics metrics = map.get(connector);
        return metrics == null || metrics.isAllowRequest();
    }

    protected static int getNext(AtomicInteger counter, int size) {
        for (; ; ) {
            int current = counter.get();
            int next = (current + 1) % size;
            if (counter.compareAndSet(current, next))
                return next;
        }
    }

    private static class Metrics {
        private static final Unsafe UNSAFE;
        private static final long ARRAY_BASE;
        private static final int ARRAY_SHIFT;
        private static final long CTL;
        private static final int THRESHOLD = 3;
        private static final int PERCENTAGE = 50;
        private static final long SLEEP = 30_000;

        private final int size;
        private final int mask;
        private final long interval;
        private volatile Entry[] entries;
        private volatile int ctl;
        private volatile long start;
        private volatile int seq;
        private final AtomicBoolean opened;
        private final AtomicLong last;

        static {
            try {
                UNSAFE = ReflectionUtil.getUnsafe();

                CTL = UNSAFE.objectFieldOffset(Metrics.class.getDeclaredField("ctl"));
                Class<?> arrayClazz = Entry[].class;
                ARRAY_BASE = UNSAFE.arrayBaseOffset(arrayClazz);
                int scale = UNSAFE.arrayIndexScale(arrayClazz);
                if ((scale & (scale - 1)) != 0)
                    throw new Error("scale not 2^n");
                ARRAY_SHIFT = 31 - Integer.numberOfLeadingZeros(scale);
            } catch (Exception e) {
                throw new Error(e);
            }
        }

        private Metrics(int size) {
            this(size, 1000);
        }

        private Metrics(int size, int interval) {
            Validate.isTrue(size > 0, "size cannot be non positive");
            this.size = tableSize(size);
            this.mask = size - 1;
            this.interval = Math.min(Math.max(interval, 1000), 5000);
            this.opened = new AtomicBoolean(false);
            this.last = new AtomicLong(0);
        }

        private static int tableSize(int size) {
            int n = size - 1;
            n |= n >>> 1;
            n |= n >>> 2;
            n |= n >>> 4;
            n |= n >>> 8;
            n |= n >>> 16;
            return (n < 0) ? 16 : (n >= 128) ? 128 : n + 1;
        }

        private void markSuccess() {
            if (opened.get()) {
                if (opened.compareAndSet(true, false)) {
                    reset();
                }
            }
            mark(true, System.currentTimeMillis());
        }

        private void markFailure() {
            if (!opened.get()) {
                mark(false, System.currentTimeMillis());
            }
        }

        private void mark(boolean success, long now) {
            int n, i;
            long s;
            Entry entry;
            for (Entry[] ent = entries; ; ) {
                if (ent == null) {
                    ent = init(now);
                } else if (now < (s = start)) {
                    break;
                } else if ((n = calculate(now, s)) < size) {
                    i = (n + seq) & mask;
                    if ((entry = entryAt(ent, i)) == null) {
                        entry = new Entry(s + interval * n);
                        if (!casEntryAt(ent, i, null, entry)) {
                            continue;
                        }
                    }
                    if (success && entry.markSuccess(now) || !success && entry.markFailure(now)) {
                        break;
                    }
                } else {
                    if ((n -= mask) >= size) {
                        ent = init(now);
                    } else {
                        roll(n, s);
                    }
                }
            }
        }

        private int calculate(long now, long start) {
            return (int) ((now - start) / interval);
        }

        private Entry[] init(long now) {
            Entry[] ent;
            int c;
            while ((ent = entries) == null || (start + interval * size) < now) {
                if ((c = ctl) > 0) {
                    Thread.yield();
                } else if (UNSAFE.compareAndSwapInt(this, CTL, c, 1)) {
                    try {
                        Entry[] a = new Entry[size];
                        entries = ent = a;
                        start = now;
                        seq = 0;
                    } finally {
                        ctl = c;
                    }
                    break;
                }
            }
            return ent;
        }

        private void roll(int n, long s) {
            int c;
            if ((c = ctl) > 0) {
                Thread.yield();
            } else if (UNSAFE.compareAndSwapInt(this, CTL, c, 2) && s == start) {
                try {
                    Entry entry;
                    for (int i = seq; i < n; i++) {
                        if ((entry = entryAt(entries, i & mask)) != null) {
                            entry.reset(start + interval * (i + size));
                        }
                    }
                    start += interval * n;
                    seq = (n + seq) & mask;
                } finally {
                    ctl = c;
                }
            }
        }

        private static Entry entryAt(Entry[] entries, int i) {
            return (Entry) UNSAFE.getObjectVolatile(entries, ((long) i << ARRAY_SHIFT) + ARRAY_BASE);
        }

        private static boolean casEntryAt(Entry[] entries, int i, Entry prev, Entry next) {
            return UNSAFE.compareAndSwapObject(entries, ((long) i << ARRAY_SHIFT) + ARRAY_BASE, prev, next);
        }

        private boolean isAllowRequest() {
            return !isOpened() || isAllowSingleRequest();
        }

        private boolean isOpened() {
            if (opened.get()) {
                return true;
            }
            Entry[] ent = entries;
            long f = failure(ent);
            if (f == 0) {
                return false;
            }
            long total = success(ent) + f;
            if (total < THRESHOLD) {
                return false;
            }
            if (((double) f / total) * 100 < PERCENTAGE) {
                return false;
            }
            if (opened.compareAndSet(false, true)) {
                last.set(System.currentTimeMillis());
            }
            return true;
        }

        private boolean isAllowSingleRequest() {
            long l, now;
            if (opened.get() && (now = System.currentTimeMillis()) > (l = last.get()) + SLEEP) {
                if (last.compareAndSet(l, now)) {
                    return true;
                }
            }
            return false;
        }

        private long success(Entry[] entries) {
            return Stream.of(entries).filter(Objects::nonNull).map(e -> e.success.get()).reduce((i, j) -> i + j).orElse(0);
        }

        private long failure(Entry[] entries) {
            return Stream.of(entries).filter(Objects::nonNull).map(e -> e.failure.get()).reduce((i, j) -> i + j).orElse(0);
        }

        private void reset() {
            entries = null;
        }

        @Override
        public String toString() {
            Entry[] e = entries;
            return "Metrics{" +
                    "entries=" + Arrays.toString(Stream.of(e).filter(Objects::nonNull).toArray()) +
                    ", last=" + (opened.get() ? Instant.ofEpochMilli(last.get()).atZone(ZONE_ID) : "null") +
                    ", opened=" + opened.get() +
                    '}';
        }

        private static class Entry {
            private static final long CTL;
            private volatile int ctl;
            private volatile long start;
            private final AtomicInteger success;
            private final AtomicInteger failure;

            static {
                try {
                    CTL = UNSAFE.objectFieldOffset
                            (Entry.class.getDeclaredField("ctl"));
                } catch (Exception e) {
                    throw new Error(e);
                }
            }

            private Entry(long start) {
                this.start = start;
                this.success = new AtomicInteger();
                this.failure = new AtomicInteger();
            }

            private boolean markSuccess(long now) {
                int c;
                for (; ; ) {
                    if ((c = ctl) >= 0 && UNSAFE.compareAndSwapInt(this, CTL, c, c + 1)) {
                        try {
                            if (now < start) {
                                return false;
                            }
                            success.incrementAndGet();
                        } finally {
                            UNSAFE.getAndAddInt(this, CTL, -1);
                        }
                        return true;
                    }
                }
            }

            private boolean markFailure(long now) {
                int c;
                for (; ; ) {
                    if ((c = ctl) >= 0 && UNSAFE.compareAndSwapInt(this, CTL, c, c + 1)) {
                        try {
                            if (now < start) {
                                return false;
                            }
                            failure.incrementAndGet();
                        } finally {
                            UNSAFE.getAndAddInt(this, CTL, -1);
                        }
                        return true;
                    }
                }
            }

            private void reset(long s) {
                for (; ; ) {
                    if (ctl == 0 && UNSAFE.compareAndSwapInt(this, CTL, 0, Integer.MIN_VALUE)) {
                        try {
                            start = s;
                            success.set(0);
                            failure.set(0);
                        } finally {
                            ctl = 0;
                        }
                        break;
                    }
                }
            }

            @Override
            public String toString() {
                return "Entry{" +
                        "start=" + Instant.ofEpochMilli(start).atZone(ZONE_ID) +
                        ", success=" + success.get() +
                        ", failure=" + failure.get() +
                        '}';
            }
        }
    }
}
