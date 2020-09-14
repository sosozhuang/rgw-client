package io.ceph.rgw.client.core.async;

import io.ceph.rgw.client.action.ActionExecutor;
import io.ceph.rgw.client.core.ConnectorAware;
import io.ceph.rgw.client.core.Connectors;
import io.ceph.rgw.client.exception.RGWClientException;
import io.ceph.rgw.client.exception.RGWException;
import io.ceph.rgw.client.exception.RGWServerException;
import io.ceph.rgw.client.util.IOUtil;
import io.netty.channel.ConnectTimeoutException;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.exception.SdkServiceException;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Function;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/7/1.
 */
public abstract class AsyncConnectorAware<C> extends ConnectorAware<C> {
    protected AsyncConnectorAware(Connectors<C> connectors, ActionExecutor executor) {
        super(connectors, executor);
    }

    @Override
    protected RGWException handleException(C connector, Throwable cause) {
        while (cause != null && cause instanceof CompletionException && cause.getCause() != cause) {
            cause = cause.getCause();
        }
        if (cause instanceof SdkServiceException) {
            if (IOUtil.isIOException(cause)) {
                connectors.markFailure(connector, cause);
            }
            RGWServerException exception = new RGWServerException(cause);
            exception.setStatus(((SdkServiceException) cause).statusCode());
            exception.setRequestId(((SdkServiceException) cause).requestId());
            if (cause instanceof AwsServiceException) {
                exception.setDetail(((AwsServiceException) cause).awsErrorDetails().errorCode());
            }
            return exception;
        } else if (cause instanceof SdkClientException) {
            if (IOUtil.isIOException(cause) || IOUtil.isException(Collections.singletonList(ConnectTimeoutException.class), cause)) {
                connectors.markFailure(connector, cause);
            }
            return new RGWClientException(cause);
        }
        return super.handleException(connector, cause);
    }

    <T, R> CompletableFuture<R> doAction(Function<C, CompletableFuture<T>> function, Function<T, R> converter) {
        CompletableFuture<R> future;
        try {
            C connector = connectors.get();
            future = function.apply(connector).thenApply(converter).handle((r, t) -> {
                if (t != null) {
                    throw handleException(connector, t);
                }
                connectors.markSuccess(connector);
                return r;
            });
        } catch (Throwable cause) {
            future = new CompletableFuture<>();
            future.completeExceptionally(handleException(cause));
        }
        return future;
    }

    <R> CompletableFuture<R> doAction(Function<C, CompletableFuture<R>> function) {
        CompletableFuture<R> future;
        try {
            C connector = connectors.get();
            future = function.apply(connector).handle((r, t) -> {
                if (t != null) {
                    throw handleException(connector, t);
                }
                connectors.markSuccess(connector);
                return r;
            });
        } catch (Throwable cause) {
            future = new CompletableFuture<>();
            future.completeExceptionally(handleException(cause));
        }
        return future;
    }
}
