package io.ceph.rgw.client.action;

import io.netty.channel.ChannelFuture;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/8/27.
 */
public class SubscribeActionFuture<R> implements ActionFuture<Void>, ActionListener<R> {
    private final List<ActionListener<R>> listeners;
    private volatile boolean cancelled;
    private WeakReference<ChannelFuture> futureRef;

    public SubscribeActionFuture() {
        this.listeners = new LinkedList<>();
        this.cancelled = false;
    }

    public void addListener(ActionListener<R> listener) {
        listeners.add(listener);
    }

    public void setFuture(ChannelFuture future) {
        futureRef = new WeakReference<>(future);
    }

    @Override
    public Void actionGet() {
        return null;
    }

    @Override
    public Void actionGet(long timeout, TimeUnit unit) {
        return null;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        if (cancelled) {
            return false;
        }
        cancelled = true;
        WeakReference<ChannelFuture> ref = futureRef;
        if (ref != null) {
            ChannelFuture f = ref.get();
            if (f != null) {
                f.cancel(mayInterruptIfRunning);
                ref.clear();
            }
        }
        return true;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public boolean isDone() {
        return cancelled;
    }

    @Override
    public Void get() throws InterruptedException, ExecutionException {
        return null;
    }

    @Override
    public Void get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }

    @Override
    public void onSuccess(R response) {
        for (ActionListener<R> listener : listeners) {
            listener.onSuccess(response);
        }
    }

    @Override
    public void onFailure(Throwable cause) {
        for (ActionListener<R> listener : listeners) {
            listener.onFailure(cause);
        }
    }
}
