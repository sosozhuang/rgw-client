package io.ceph.rgw.client.spring.trace;

import brave.Span;
import brave.Tracing;
import brave.propagation.ThreadLocalSpan;
import io.ceph.rgw.client.action.*;
import io.ceph.rgw.client.spring.autoconfigure.RGWClientAutoProperties;
import org.springframework.core.annotation.Order;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * An {@link ActionExecutor} that wraps {@link Action} and traces its execution.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/4/8.
 * @see RGWClientAutoProperties#isEnableTrace()
 */
@Order(1)
public class TraceActionExecutor implements ActionExecutor {
    private final ActionExecutor delegate;

    public TraceActionExecutor(ActionExecutor delegate) {
        this.delegate = Objects.requireNonNull(delegate);
    }

    @Override
    public <R> void execute(SyncAction<R> action, ActionListener<R> listener) {
        delegate.execute(wrap(action), listener);
    }

    @Override
    public <R> R run(SyncAction<R> action) {
        return delegate.run(wrap(action));
    }

    @Override
    public <R> void execute(AsyncAction<R> action, ActionListener<R> listener) {
        TracedAsyncAction<R> futureAction = wrap(action);
        delegate.execute(futureAction, wrap(futureAction, listener));
    }

    @Override
    public <R> SubscribeActionFuture<R> execute(SubscribeAction<R> action, ActionListener<R> listener) {
        return delegate.execute(action, listener);
    }

    private static <R> SyncAction<R> wrap(SyncAction<R> action) {
        if (action instanceof TracedSyncAction) {
            return action;
        }
        return new TracedSyncAction<>(action);
    }

    private static <R> TracedAsyncAction<R> wrap(AsyncAction<R> action) {
        if (action instanceof TracedAsyncAction) {
            return (TracedAsyncAction<R>) action;
        }
        return new TracedAsyncAction<>(action);
    }

    private static <R> ActionListener<R> wrap(TracedAsyncAction<R> action, ActionListener<R> listener) {
        if (listener instanceof TracedActionListener) {
            return listener;
        }
        return new TracedActionListener<>(action, listener);
    }

    private static class TracedSyncAction<R> implements SyncAction<R> {
        private final SyncAction<R> action;

        private TracedSyncAction(SyncAction<R> action) {
            this.action = Objects.requireNonNull(action);
        }

        @Override
        public R call() {
            try {
                Span span = ThreadLocalSpan.CURRENT_TRACER.next();
                span.name(action.name());
                span.tag("type", "sync");
                span.start();
                return action.call();
            } finally {
                Span span = ThreadLocalSpan.CURRENT_TRACER.remove();
                span.finish();
            }
        }
    }

    private static class TracedAsyncAction<R> implements AsyncAction<R> {
        private final AsyncAction<R> action;
        private volatile Span span;

        private TracedAsyncAction(AsyncAction<R> action) {
            this.action = Objects.requireNonNull(action);
        }

        @Override
        public CompletableFuture<R> call() {
            try {
                span = Tracing.currentTracer().nextSpan();
                span.name(action.name());
                span.tag("type", "async");
                span.start();
            } catch (Throwable ignored) {
            }
            return action.call();
        }
    }

    private static class TracedActionListener<R> implements ActionListener<R> {
        private final TracedAsyncAction<R> action;
        private final ActionListener<R> listener;

        private TracedActionListener(TracedAsyncAction<R> action, ActionListener<R> listener) {
            this.action = Objects.requireNonNull(action);
            this.listener = Objects.requireNonNull(listener);
        }

        @Override
        public void onSuccess(R response) {
            try {
                action.span.finish();
            } finally {
                listener.onSuccess(response);
            }
        }

        @Override
        public void onFailure(Throwable cause) {
            try {
                action.span.finish();
            } finally {
                listener.onFailure(cause);
            }
        }
    }
}
