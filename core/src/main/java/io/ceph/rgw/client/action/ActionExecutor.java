package io.ceph.rgw.client.action;

/**
 * An object that executes submitted {@link Action} tasks.
 * This interface provides a way of decoupling action execution from the mechanics of how each task will be run,
 * including details of thread use, scheduling, retry policy, circuit breaking, etc.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/3/9.
 * @see SyncActionExecutor
 * @see AsyncActionExecutor
 * @see SubscribeActionExecutor
 */
public interface ActionExecutor {
    default <R> ActionFuture<R> execute(SyncAction<R> action) {
        throw new UnsupportedOperationException();
    }

    default <R> void execute(SyncAction<R> action, ActionListener<R> listener) {
        throw new UnsupportedOperationException();
    }

    default <R> R run(SyncAction<R> action) {
        throw new UnsupportedOperationException();
    }

    default <R> ActionFuture<R> execute(AsyncAction<R> action) {
        throw new UnsupportedOperationException();
    }

    default <R> void execute(AsyncAction<R> action, ActionListener<R> listener) {
        throw new UnsupportedOperationException();
    }

    default <R> SubscribeActionFuture<R> execute(SubscribeAction<R> action, ActionListener<R> listener) {
        throw new UnsupportedOperationException();
    }
}
