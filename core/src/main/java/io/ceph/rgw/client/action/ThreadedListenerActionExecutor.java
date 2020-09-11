package io.ceph.rgw.client.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * An {@link ActionExecutor} that wraps {@link ActionListener} and threading its execution.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/3/9.
 */
public abstract class ThreadedListenerActionExecutor implements ActionExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadedListenerActionExecutor.class);
    private final ActionExecutor delegate;
    private final ExecutorService executor;

    ThreadedListenerActionExecutor(ActionExecutor delegate, ExecutorService executor) {
        this.delegate = Objects.requireNonNull(delegate);
        this.executor = Objects.requireNonNull(executor);
    }

    protected abstract <R> ListenableActionFuture<R> createActionFuture();

    @Override
    public <R> ActionFuture<R> execute(SyncAction<R> action) {
        ListenableActionFuture<R> future = createActionFuture();
        execute(action, future);
        return future;
    }

    @Override
    public <R> void execute(SyncAction<R> action, ActionListener<R> listener) {
        delegate.execute(action, wrap(listener));
    }

    @Override
    public <R> R run(SyncAction<R> action) {
        return delegate.run(action);
    }

    @Override
    public <R> ActionFuture<R> execute(AsyncAction<R> action) {
        ListenableActionFuture<R> future = createActionFuture();
        execute(action, future);
        return future;
    }

    @Override
    public <R> void execute(AsyncAction<R> action, ActionListener<R> listener) {
        delegate.execute(action, wrap(listener));
    }

    @Override
    public <R> SubscribeActionFuture<R> execute(SubscribeAction<R> action, ActionListener<R> listener) {
        return delegate.execute(action, wrap(listener));
    }

    private <R> ActionListener<R> wrap(ActionListener<R> listener) {
        if (listener instanceof Future) {
            return listener;
        }
        if (listener instanceof ThreadedActionListener) {
            return listener;
        }
        return new ThreadedActionListener<>(listener);
    }

    private class ThreadedActionListener<R> implements ActionListener<R> {
        private final ActionListener<R> listener;

        private ThreadedActionListener(ActionListener<R> listener) {
            this.listener = Objects.requireNonNull(listener);
        }

        @Override
        public void onSuccess(R response) {
            executor.execute(() -> {
                try {
                    listener.onSuccess(response);
                } catch (Throwable cause) {
                    LOGGER.error("Failed to execute success callback.", cause);
                    try {
                        listener.onFailure(cause);
                    } catch (Exception inner) {
                        inner.addSuppressed(cause);
                        LOGGER.error("Failed to execute failure callback", inner);
                    }
                }
            });
        }

        @Override
        public void onFailure(Throwable cause) {
            executor.execute(() -> {
                try {
                    listener.onFailure(cause);
                } catch (Throwable e) {
                    e.addSuppressed(cause);
                    LOGGER.error("Failed to execute failure callback.", e);
                }
            });
        }
    }
}
