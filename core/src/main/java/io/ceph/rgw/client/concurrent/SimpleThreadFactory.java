package io.ceph.rgw.client.concurrent;

import org.apache.commons.lang3.Validate;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A {@link ThreadFactory} that constructs {@link Thread}s based on given prefix string.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/8/26.
 */
public class SimpleThreadFactory implements ThreadFactory {
    private final String prefix;
    private final boolean daemon;
    private final AtomicInteger counter;

    public SimpleThreadFactory(String prefix) {
        this(prefix, false);
    }

    public SimpleThreadFactory(String prefix, boolean daemon) {
        this.prefix = Validate.notBlank(prefix);
        this.daemon = daemon;
        this.counter = new AtomicInteger();
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, prefix + "-" + counter.incrementAndGet());
        t.setDaemon(daemon);
        return t;
    }
}
