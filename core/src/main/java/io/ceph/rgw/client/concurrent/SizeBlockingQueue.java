package io.ceph.rgw.client.concurrent;

import org.apache.commons.lang3.Validate;

import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Fix Size of BlockingQueue.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/3/19.
 */
public class SizeBlockingQueue<E> extends AbstractQueue<E> implements BlockingQueue<E> {
    private final BlockingQueue<E> queue;
    private final int capacity;
    private final AtomicInteger size;

    public SizeBlockingQueue(BlockingQueue<E> queue, int capacity) {
        this.queue = Objects.requireNonNull(queue);
        Validate.isTrue(capacity >= 0, "capacity cannot be negative");
        this.capacity = capacity;
        this.size = new AtomicInteger();
    }

    @Override
    public int size() {
        return size.get();
    }

    @Override
    public Iterator<E> iterator() {
        final Iterator<E> it = queue.iterator();
        return new Iterator<E>() {
            E current;

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public E next() {
                current = it.next();
                return current;
            }

            @Override
            public void remove() {
                if (queue.remove(current)) {
                    size.decrementAndGet();
                }
            }
        };
    }

    @Override
    public E peek() {
        return queue.peek();
    }

    @Override
    public E poll() {
        E e = queue.poll();
        if (e != null) {
            size.decrementAndGet();
        }
        return e;
    }

    @Override
    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        E e = queue.poll(timeout, unit);
        if (e != null) {
            size.decrementAndGet();
        }
        return e;
    }

    @Override
    public boolean remove(Object o) {
        boolean v = queue.remove(o);
        if (v) {
            size.decrementAndGet();
        }
        return v;
    }

    @Override
    public boolean offer(E e) {
        while (true) {
            final int current = size.get();
            if (current >= capacity) {
                return false;
            }
            if (size.compareAndSet(current, 1 + current)) {
                break;
            }
        }
        boolean offered = queue.offer(e);
        if (!offered) {
            size.decrementAndGet();
        }
        return offered;
    }

    @Override
    public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
        throw new IllegalStateException("offer with timeout not allowed on size queue");
    }

    @Override
    public void put(E e) throws InterruptedException {
        throw new IllegalStateException("put not allowed on size queue");
    }

    @Override
    public E take() throws InterruptedException {
        E e;
        e = queue.take();
        size.decrementAndGet();
        return e;
    }

    @Override
    public int remainingCapacity() {
        return capacity - size.get();
    }

    @Override
    public int drainTo(Collection<? super E> c) {
        int v = queue.drainTo(c);
        size.addAndGet(-v);
        return v;
    }

    @Override
    public int drainTo(Collection<? super E> c, int maxElements) {
        int v = queue.drainTo(c, maxElements);
        size.addAndGet(-v);
        return v;
    }

    @Override
    public Object[] toArray() {
        return queue.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return queue.toArray(a);
    }

    @Override
    public boolean contains(Object o) {
        return queue.contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return queue.containsAll(c);
    }
}
