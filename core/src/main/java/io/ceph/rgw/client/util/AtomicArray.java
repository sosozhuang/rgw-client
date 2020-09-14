package io.ceph.rgw.client.util;

import sun.misc.Unsafe;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/8/24.
 */
public class AtomicArray<T> {
    private static final Unsafe UNSAFE;
    private static final long ARRAY_BASE_OFFSET;
    private static final int ARRAY_SHIFT;
    private static final long ARRAY_FIELD_OFFSET;

    static {
        UNSAFE = ReflectionUtil.getUnsafe();
        try {
            ARRAY_BASE_OFFSET = UNSAFE.arrayBaseOffset(Object[].class);
            int scale = UNSAFE.arrayIndexScale(Object[].class);
            if ((scale & (scale - 1)) != 0)
                throw new Error("data type scale not a power of two");
            ARRAY_SHIFT = 31 - Integer.numberOfLeadingZeros(scale);
            ARRAY_FIELD_OFFSET = UNSAFE.objectFieldOffset(AtomicArray.class.getDeclaredField("array"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private volatile Object[] array;
    private final AtomicInteger size;

    public AtomicArray() {
        this(4);
    }

    public AtomicArray(int length) {
        this.array = new Object[length];
        this.size = new AtomicInteger(0);
    }

    public AtomicArray(T[] array) {
        this.array = Arrays.copyOf(array, array.length, Object[].class);
        this.size = new AtomicInteger(array.length);
    }

    private long checkedByteOffset(int i) {
        if (i < 0 || i >= array.length)
            throw new IndexOutOfBoundsException("index " + i);

        return byteOffset(i);
    }

    private static long byteOffset(int i) {
        return ((long) i << ARRAY_SHIFT) + ARRAY_BASE_OFFSET;
    }

    public final int length() {
        return array.length;
    }

    @SuppressWarnings("unchecked")
    public final T[] getAll() {
        return (T[]) array;
    }

    public final T get(int i) {
        return getRaw(checkedByteOffset(i));
    }

    @SuppressWarnings("unchecked")
    private T getRaw(long offset) {
        return (T) UNSAFE.getObjectVolatile(array, offset);
    }

    private long checkLength(int i) {
        Object[] prev, update;
        int len;
        do {
            prev = array;
            if ((len = prev.length) > i) {
                break;
            }
            do {
                len <<= 1;
            } while (len <= i);
            update = new Object[len];
            System.arraycopy(prev, 0, update, 0, prev.length);
        } while (!UNSAFE.compareAndSwapObject(this, ARRAY_FIELD_OFFSET, prev, update));
        return byteOffset(i);
    }

    public final void set(int i, T value) {
        UNSAFE.putObjectVolatile(array, checkLength(i), value);
    }

    public final void lazySet(int i, T value) {
        UNSAFE.putOrderedObject(array, checkLength(i), value);
    }

    public final void add(T value) {
        set(size.incrementAndGet(), value);
    }

    @SuppressWarnings("unchecked")
    public final T getAndSet(int i, T value) {
        return (T) UNSAFE.getAndSetObject(array, checkLength(i), value);
    }

    public final boolean compareAndSet(int i, T expect, T update) {
        return compareAndSetRaw(checkLength(i), expect, update);
    }

    private boolean compareAndSetRaw(long offset, T expect, T update) {
        return UNSAFE.compareAndSwapObject(array, offset, expect, update);
    }

    public final boolean weakCompareAndSet(int i, T expect, T update) {
        return compareAndSet(i, expect, update);
    }

    public final T getAndUpdate(int i, UnaryOperator<T> updateFunction) {
        long offset = checkLength(i);
        T prev, next;
        do {
            prev = getRaw(offset);
            next = updateFunction.apply(prev);
        } while (!compareAndSetRaw(offset, prev, next));
        return prev;
    }

    public final T updateAndGet(int i, UnaryOperator<T> updateFunction) {
        long offset = checkLength(i);
        T prev, next;
        do {
            prev = getRaw(offset);
            next = updateFunction.apply(prev);
        } while (!compareAndSetRaw(offset, prev, next));
        return next;
    }

    public final T getAndAccumulate(int i, T x, BinaryOperator<T> accumulatorFunction) {
        long offset = checkLength(i);
        T prev, next;
        do {
            prev = getRaw(offset);
            next = accumulatorFunction.apply(prev, x);
        } while (!compareAndSetRaw(offset, prev, next));
        return prev;
    }

    public final T accumulateAndGet(int i, T x, BinaryOperator<T> accumulatorFunction) {
        long offset = checkLength(i);
        T prev, next;
        do {
            prev = getRaw(offset);
            next = accumulatorFunction.apply(prev, x);
        } while (!compareAndSetRaw(offset, prev, next));
        return next;
    }

    @Override
    public String toString() {
        int iMax = array.length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0; ; i++) {
            b.append(getRaw(byteOffset(i)));
            if (i == iMax)
                return b.append(']').toString();
            b.append(',').append(' ');
        }
    }
}
