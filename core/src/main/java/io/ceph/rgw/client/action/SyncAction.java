package io.ceph.rgw.client.action;

import java.util.concurrent.Callable;

/**
 * A SyncAction represents synchronous action, which is executed and returns a response, however does not throw a checked exception.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/3/7.
 * @see AsyncAction
 */
@FunctionalInterface
public interface SyncAction<R> extends Action, Callable<R> {
    /**
     * Executes a task.
     *
     * @return executed response
     */
    @Override
    R call();

    @Override
    default String name() {
        return "SyncAction";
    }
}
