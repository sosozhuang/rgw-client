package io.ceph.rgw.client.action;

import io.ceph.rgw.client.exception.RGWException;
import org.apache.commons.lang3.Validate;

import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * An object that implements {@link ActionFuture} and {@link ActionListener}
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/3/2.
 * @see SyncActionFuture
 * @see AsyncActionFuture
 */
public class ListenableActionFuture<R> implements ActionFuture<R>, ActionListener<R> {
    protected final CountDownLatch latch;
    protected final AtomicReference<R> response;
    protected final AtomicReference<Throwable> cause;
    protected final AtomicBoolean cancelled;

    public ListenableActionFuture() {
        this.latch = new CountDownLatch(1);
        this.response = new AtomicReference<>();
        this.cause = new AtomicReference<>();
        this.cancelled = new AtomicBoolean();
    }

    @Override
    public void onSuccess(R response) {
        Validate.isTrue(this.response.compareAndSet(null, Objects.requireNonNull(response)), "response is already set");
        latch.countDown();
    }

    @Override
    public void onFailure(Throwable cause) {
        Validate.isTrue(this.cause.compareAndSet(null, Objects.requireNonNull(cause)), "throwable is already set");
        latch.countDown();
    }

    @Override
    public R actionGet() {
        try {
            return get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("future interrupted", e);
        } catch (ExecutionException e) {
            throw rethrowExecutionException(e);
        }
    }

    @Override
    public R actionGet(long timeout, TimeUnit unit) {
        try {
            return get(timeout, unit);
        } catch (TimeoutException e) {
            throw new RGWException(e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("future interrupted", e);
        } catch (ExecutionException e) {
            throw rethrowExecutionException(e);
        }
    }

    private static RuntimeException rethrowExecutionException(ExecutionException e) {
        Throwable cause = e.getCause();
        while (!(cause instanceof RuntimeException) && cause != null && cause.getCause() != cause) {
            cause = cause.getCause();
        }
        if (cause instanceof RuntimeException) {
            return (RuntimeException) cause;
        }
        return new IllegalStateException("failed to execute", e);
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        if (latch.getCount() == 0 || cancelled.compareAndSet(false, true)) {
            return false;
        }
        latch.countDown();
        if (mayInterruptIfRunning) {
            interruptTask();
        }
        return true;
    }

    protected void interruptTask() {

    }

    @Override
    public boolean isCancelled() {
        return cancelled.get();
    }

    @Override
    public boolean isDone() {
        return latch.getCount() == 0 || cancelled.get();
    }

    @Override
    public R get() throws InterruptedException, ExecutionException {
        latch.await();
        if (cancelled.get()) {
            throw new CancellationException("task was cancelled");
        }
        R response = this.response.get();
        Throwable e = this.cause.get();
        if (e != null) {
            if (response != null) {
                IllegalStateException ex = new IllegalStateException("both response and exception are set");
                ex.addSuppressed(e);
                throw ex;
            }
            throw new ExecutionException("exception caught while executing task", e);
        }
        if (response == null) {
            throw new IllegalStateException("both response and exception not set");
        }
        return response;
    }

    @Override
    public R get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (!latch.await(timeout, unit)) {
            throw new TimeoutException("timeout after waiting for [" + unit.toMillis(timeout) + "] ms");
        }
        if (cancelled.get()) {
            throw new CancellationException("task was cancelled");
        }
        R response = this.response.get();
        Throwable e = this.cause.get();
        if (e != null) {
            if (response != null) {
                IllegalStateException ex = new IllegalStateException("both response and exception are set");
                ex.addSuppressed(e);
                throw ex;
            }
            throw new ExecutionException("exception caught while executing request", e);
        }
        if (response == null) {
            throw new IllegalStateException("both response and exception not set");
        }
        return response;
    }
}
