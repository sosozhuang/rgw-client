package io.ceph.rgw.client.model.notification;

import io.ceph.rgw.client.model.Request;

/**
 * A request of an <a href="https://docs.ceph.com/docs/master/radosgw/pubsub-module/">subscribe operation</a>.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/8/24.
 * @see SubscribeResponse
 * @see io.ceph.rgw.client.SubscribeClient
 */
public interface SubscribeRequest extends Request {
}
