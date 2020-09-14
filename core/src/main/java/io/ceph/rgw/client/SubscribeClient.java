package io.ceph.rgw.client;

import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;
import io.ceph.rgw.client.model.notification.SubscribeObjectRequest;
import io.ceph.rgw.client.model.notification.SubscribeObjectResponse;

/**
 * A client that provides <a href="https://docs.ceph.com/docs/master/radosgw/s3/objectops/">subscribe operations</a>, sends SubscribeRequest and receives SubscribeResponse.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/6/4.
 * @see Clients#getSubscribe()
 * @see io.ceph.rgw.client.model.notification.SubscribeRequest
 * @see io.ceph.rgw.client.model.notification.SubscribeResponse
 */
public interface SubscribeClient {
    /**
     * Subscribe object notification asynchronously.
     *
     * @param request  the subscribe object request
     * @param listener the callback listener after action is done
     * @return an ActionFuture that cancels the subscription when calling {@link ActionFuture#cancel(boolean)}
     */
    ActionFuture<Void> subscribeObjectAsync(SubscribeObjectRequest request, ActionListener<SubscribeObjectResponse> listener);

    /**
     * Fluent api to subscribe object notification.
     *
     * @return the request builder
     */
    default SubscribeObjectRequest.Builder prepareSubscribeObject() {
        return new SubscribeObjectRequest.Builder(this);
    }
}
