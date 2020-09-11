package io.ceph.rgw.client.action;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

/**
 * Executes an {@link AsyncAction}.
 * When action completes, will call the given listener with the result.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/6/16.
 */
public class AsyncActionExecutor implements ActionExecutor {
    @Override
    public <R> void execute(AsyncAction<R> action, ActionListener<R> listener) {
        AsyncActionFuture<R> actionFuture = null;
        if (listener instanceof AsyncActionFuture) {
            actionFuture = (AsyncActionFuture<R>) listener;
            if (actionFuture.isCancelled()) {
                return;
            }
        }
        CompletableFuture<R> future = action.call();
        future.whenComplete((r, t) -> {
            if (t != null) {
                if (t instanceof CompletionException) {
                    t = t.getCause();
                }
                listener.onFailure(t);
            } else {
                listener.onSuccess(r);
            }
        });

        if (actionFuture != null) {
            actionFuture.setFuture(future);
        }
    }
}
