package io.ceph.rgw.client.action;

import java.lang.ref.WeakReference;

/**
 * Implements {@link ListenableActionFuture#interruptTask()} that is designed to cancel {@link SyncAction}.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/6/15.
 */
class SyncActionFuture<R> extends ListenableActionFuture<R> {
    private WeakReference<Thread> threadRef;

    SyncActionFuture() {
    }

    void setRunner(Thread runner) {
        threadRef = new WeakReference<>(runner);
    }

    @Override
    public void onSuccess(R response) {
        WeakReference<Thread> ref = threadRef;
        if (ref != null) {
            ref.clear();
        }
        super.onSuccess(response);
    }

    @Override
    public void onFailure(Throwable cause) {
        WeakReference<Thread> ref = threadRef;
        if (ref != null) {
            ref.clear();
        }
        super.onFailure(cause);
    }

    @Override
    protected void interruptTask() {
        WeakReference<Thread> ref = threadRef;
        if (ref != null) {
            Thread t = ref.get();
            if (t != null) {
                t.interrupt();
                ref.clear();
            }
        }
    }
}
