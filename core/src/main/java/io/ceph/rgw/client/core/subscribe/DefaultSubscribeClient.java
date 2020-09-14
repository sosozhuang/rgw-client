package io.ceph.rgw.client.core.subscribe;

import io.ceph.rgw.client.SubscribeClient;
import io.ceph.rgw.client.action.ActionExecutor;
import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;
import io.ceph.rgw.client.action.SubscribeActionFuture;
import io.ceph.rgw.client.converter.SubscribeRequestConverter;
import io.ceph.rgw.client.converter.SubscribeResponseConverter;
import io.ceph.rgw.client.core.Connectors;
import io.ceph.rgw.client.model.notification.SubscribeObjectRequest;
import io.ceph.rgw.client.model.notification.SubscribeObjectResponse;
import io.netty.handler.codec.http.FullHttpResponse;

/**
 * A default implementation of SubscribeClient.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/6/5.
 */
public class DefaultSubscribeClient extends SubscribeConnectorAware implements SubscribeClient {
    public DefaultSubscribeClient(Connectors<SubscribeConnector> connectors, ActionExecutor executor) {
        super(connectors, executor);
    }

    @Override
    public ActionFuture<Void> subscribeObjectAsync(SubscribeObjectRequest request, ActionListener<SubscribeObjectResponse> listener) {
        return executor.execute(l -> doSubscribeObject(request, l), httpResponseListener(listener, SubscribeResponseConverter::subscribeObject));
    }

    private SubscribeActionFuture<FullHttpResponse> doSubscribeObject(SubscribeObjectRequest request, ActionListener<FullHttpResponse> listener) {
        return doAction(request, SubscribeRequestConverter::subscribeObject, listener);
    }
}
