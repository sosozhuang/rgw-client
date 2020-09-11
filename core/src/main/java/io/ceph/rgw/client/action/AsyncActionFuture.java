package io.ceph.rgw.client.action;

import java.lang.ref.WeakReference;
import java.util.concurrent.Future;

/**
 * Implements {@link ListenableActionFuture#interruptTask()} that is designed to cancel {@link AsyncAction}.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/6/15.
 */
public class AsyncActionFuture<R> extends ListenableActionFuture<R> {
    private WeakReference<Future<?>> futureRef;

    AsyncActionFuture() {
    }

    public void setFuture(Future<?> future) {
        futureRef = new WeakReference<>(future);
    }

    @Override
    public void onSuccess(R response) {
        WeakReference<Future<?>> ref = futureRef;
        if (ref != null) {
            ref.clear();
        }
        super.onSuccess(response);
    }

    @Override
    public void onFailure(Throwable cause) {
        WeakReference<Future<?>> ref = futureRef;
        if (ref != null) {
            ref.clear();
        }
        super.onFailure(cause);
    }

    @Override
    protected void interruptTask() {
        WeakReference<Future<?>> ref = futureRef;
        if (ref != null) {
            Future<?> f = ref.get();
            if (f != null) {
                f.cancel(true);
                ref.clear();
            }
        }
    }
}
