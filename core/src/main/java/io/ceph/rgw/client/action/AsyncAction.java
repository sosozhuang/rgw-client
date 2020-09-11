package io.ceph.rgw.client.action;

import net.jodah.typetools.TypeResolver;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

/**
 * An AsyncAction represents asynchronous action, which is executed and returns a CompletableFuture, however does not throw a checked exception.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/7/1.
 * @see SyncAction
 */
@FunctionalInterface
public interface AsyncAction<R> extends Action, Callable<CompletableFuture<R>> {
    /**
     * Executes a task.
     *
     * @return executed CompletableFuture
     */
    @Override
    CompletableFuture<R> call();

    @Override
    default String name() {
        try {
            String className = TypeResolver.resolveRawArgument(AsyncAction.class, this.getClass()).getSimpleName();
            return className.endsWith("Response") ? className.substring(className.lastIndexOf('.') + 1, className.length() - 8) : className;
        } catch (Exception ignored) {
        }
        return "AsyncAction";
    }
}
