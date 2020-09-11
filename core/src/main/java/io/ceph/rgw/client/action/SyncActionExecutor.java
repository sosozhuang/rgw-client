package io.ceph.rgw.client.action;

import java.util.Objects;
import java.util.concurrent.ExecutorService;

/**
 * Executes an {@link SyncAction}.
 * If a listener is provided, when action completes, will be called with the result.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/6/16.
 */
public class SyncActionExecutor implements ActionExecutor {
    private final ExecutorService executor;

    public SyncActionExecutor(ExecutorService executor) {
        this.executor = Objects.requireNonNull(executor);
    }

    @Override
    public <R> void execute(SyncAction<R> action, ActionListener<R> listener) {
        executor.execute(() -> {
            if (listener instanceof SyncActionFuture) {
                SyncActionFuture<R> future = (SyncActionFuture<R>) listener;
                if (future.isCancelled()) {
                    return;
                }
                future.setRunner(Thread.currentThread());
            }
            try {
                listener.onSuccess(action.call());
            } catch (Throwable e) {
                listener.onFailure(e);
            }
        });
    }

    @Override
    public <R> R run(SyncAction<R> action) {
        return action.call();
    }
}
