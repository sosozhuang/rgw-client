package io.ceph.rgw.client.util;

import org.slf4j.Logger;

import java.io.Closeable;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/3/14.
 */
public abstract class AbstractClosable implements Closeable {
    private final String name;
    private final AtomicBoolean closed;

    protected AbstractClosable() {
        this.name = getClass().getSimpleName();
        this.closed = new AtomicBoolean(false);
    }

    @Override
    public void close() {
        if (!notClosed()) {
            return;
        }
        doClose();
        getLogger().info("{} closed.", name);
    }

    private boolean notClosed() {
        return closed.compareAndSet(false, true);
    }

    public boolean isClosed() {
        return closed.get();
    }

    protected abstract void doClose();

    protected abstract Logger getLogger();
}
