package io.ceph.rgw.client.action;

import io.ceph.rgw.client.util.IOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * An {@link ActionExecutor} that wraps {@link Action} and scheduling retry executions.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/3/9.
 */
public final class RetryActionExecutor implements ActionExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(RetryActionExecutor.class);
    private final ActionExecutor delegate;
    private final ScheduledExecutorService scheduler;
    private final int maxRetries;
    private final int baseDelayTime;
    private final int maxBackoffTime;

    public RetryActionExecutor(ActionExecutor delegate, ScheduledExecutorService scheduler,
                               int maxRetries, int baseDelayTime, int maxBackoffTime) {
        this.delegate = Objects.requireNonNull(delegate);
        this.scheduler = Objects.requireNonNull(scheduler);
        this.maxRetries = maxRetries;
        this.baseDelayTime = baseDelayTime;
        this.maxBackoffTime = maxBackoffTime;
    }

    @Override
    public <R> void execute(SyncAction<R> action, ActionListener<R> listener) {
        delegate.execute(action, wrap(action, listener));
    }

    @Override
    public <R> R run(SyncAction<R> action) {
        return delegate.run(action);
    }

    @Override
    public <R> void execute(AsyncAction<R> action, ActionListener<R> listener) {
        delegate.execute(action, wrap(action, listener));
    }

    @Override
    public <R> SubscribeActionFuture<R> execute(SubscribeAction<R> action, ActionListener<R> listener) {
        return delegate.execute(action, wrap(action, listener));
    }

    private <R> ActionListener<R> wrap(SyncAction<R> action, ActionListener<R> listener) {
        if (listener instanceof RetryActionListener) {
            return listener;
        }
        return new RetryActionListener<>(action, listener);
    }

    private <R> ActionListener<R> wrap(AsyncAction<R> action, ActionListener<R> listener) {
        if (listener instanceof RetryActionListener) {
            return listener;
        }
        return new RetryActionListener<>(action, listener);
    }

    private <R> ActionListener<R> wrap(SubscribeAction<R> action, ActionListener<R> listener) {
        if (listener instanceof RetryActionListener) {
            return listener;
        }
        return new RetryActionListener<>(action, listener);
    }

    private class RetryActionListener<R> implements ActionListener<R> {
        private final Runnable runnable;
        private final ActionListener<R> listener;
        private int retries;

        private RetryActionListener(SyncAction<R> action, ActionListener<R> listener) {
            this.runnable = () -> delegate.execute(action, this);
            this.listener = Objects.requireNonNull(listener);
            this.retries = 0;
        }

        private RetryActionListener(AsyncAction<R> action, ActionListener<R> listener) {
            this.runnable = () -> delegate.execute(action, this);
            this.listener = Objects.requireNonNull(listener);
            this.retries = 0;
        }

        private RetryActionListener(SubscribeAction<R> action, ActionListener<R> listener) {
            this.runnable = () -> delegate.execute(action, this);
            this.listener = Objects.requireNonNull(listener);
            this.retries = 0;
        }

        @Override
        public void onSuccess(R response) {
            if (retries > 0) {
                LOGGER.debug("Action succeed in [{}] retry.", retries);
            }
            listener.onSuccess(response);
        }

        @Override
        public void onFailure(Throwable cause) {
            if (IOUtil.isIOException(cause)) {
                if (++retries < maxRetries) {
                    LOGGER.info("Action failed, going to start [{}] retry.", retries, cause);
                } else {
                    LOGGER.warn("Abort action retries after [{}] times, reach max retries.", retries, cause);
                    listener.onFailure(cause);
                }
                try {
                    scheduler.schedule(runnable, calculateDelay(retries), TimeUnit.MILLISECONDS);
                } catch (Exception inner) {
                    inner.addSuppressed(cause);
                    listener.onFailure(inner);
                }
            } else {
                listener.onFailure(cause);
            }
        }
    }

    private int calculateDelay(int retries) {
        return (int) Math.min((1L << Math.min(retries, maxRetries)) * baseDelayTime, maxBackoffTime);
    }
}
