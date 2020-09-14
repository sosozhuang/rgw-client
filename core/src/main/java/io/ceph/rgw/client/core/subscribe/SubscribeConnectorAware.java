package io.ceph.rgw.client.core.subscribe;

import io.ceph.rgw.client.action.ActionExecutor;
import io.ceph.rgw.client.action.ActionListener;
import io.ceph.rgw.client.action.SubscribeActionFuture;
import io.ceph.rgw.client.core.ConnectorAware;
import io.ceph.rgw.client.core.Connectors;
import io.ceph.rgw.client.model.notification.SubscribeRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/6/4.
 */
public abstract class SubscribeConnectorAware extends ConnectorAware<SubscribeConnector> {
    protected SubscribeConnectorAware(Connectors<SubscribeConnector> connectors, ActionExecutor executor) {
        super(connectors, executor);
    }

    <R> ActionListener<FullHttpResponse> httpResponseListener(ActionListener<R> listener, Function<FullHttpResponse, R> converter) {
        Objects.requireNonNull(listener);
        return new ActionListener<FullHttpResponse>() {
            @Override
            public void onSuccess(FullHttpResponse response) {
                listener.onSuccess(converter.apply(response));
            }

            @Override
            public void onFailure(Throwable cause) {
                listener.onFailure(cause);
            }
        };
    }

    private ActionListener<FullHttpResponse> connectorAwareListener(SubscribeConnector connector) {
        return new ActionListener<FullHttpResponse>() {
            @Override
            public void onSuccess(FullHttpResponse response) {
                connectors.markSuccess(connector);
            }

            @Override
            public void onFailure(Throwable cause) {
                handleException(connector, cause);
            }
        };
    }

    <REQ extends SubscribeRequest> SubscribeActionFuture<FullHttpResponse> doAction(REQ request, Function<REQ, FullHttpRequest> reqConverter, ActionListener<FullHttpResponse> listener) {
        SubscribeActionFuture<FullHttpResponse> future = new SubscribeActionFuture<>();
        try {
            SubscribeConnector connector = connectors.get();
            future.addListener(connectorAwareListener(connector));
            future.addListener(listener);
            connector.execute(reqConverter.apply(request), future);
        } catch (Throwable cause) {
            future.onFailure(handleException(cause));
        }
        return future;
    }

//    protected <REQ extends Request, RESP> Void doAction(REQ request, ChunkedInput<HttpContent> chunkedInput, ActionListener<RESP> listener, Function<REQ, HttpRequest> reqConverter, Function<FullHttpResponse, RESP> respConverter) {
//        SubscribeConnector connector = null;
//        try {
//            connector = connectors.get();
//            connector.execute(reqConverter.apply(request), chunkedInput, httpResponseListener(connector, listener, respConverter));
//        } catch (Throwable cause) {
//            listener.onFailure(handleException(connector, cause));
//        }
//        return null;
//    }
//
//    protected <REQ extends Request, RESP> Void doChunkedAction(REQ request, ActionListener<RESP> listener, Function<REQ, FullHttpRequest> reqConverter, Function<HttpObject, RESP> respConverter) {
//        SubscribeConnector connector = null;
//        try {
//            connector = connectors.get();
//            connector.executeChunkedResponse(reqConverter.apply(request), httpResponseListener(connector, listener, respConverter));
//        } catch (Throwable cause) {
//            listener.onFailure(handleException(connector, cause));
//        }
//        return null;
//    }
//
//    protected <REQ extends Request, RESP> Void doChunkedAction(REQ request, ChunkedInput<HttpContent> chunkedInput, ActionListener<RESP> listener, Function<REQ, FullHttpRequest> reqConverter, Function<HttpObject, RESP> respConverter) {
//        SubscribeConnector connector = null;
//        try {
//            connector = connectors.get();
//            connector.executeChunkedResponse(reqConverter.apply(request), chunkedInput, httpResponseListener(connector, listener, respConverter));
//        } catch (Throwable cause) {
//            listener.onFailure(handleException(connector, cause));
//        }
//        return null;
//    }
}
