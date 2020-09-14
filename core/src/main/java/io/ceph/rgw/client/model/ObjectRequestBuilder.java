package io.ceph.rgw.client.model;

import io.ceph.rgw.client.ObjectClient;

import java.util.Objects;

/**
 * Created by zhuangshuo on 2020/3/2.
 */
abstract class ObjectRequestBuilder<T extends ObjectRequestBuilder<T, REQ, RESP>, REQ extends ObjectRequest, RESP extends ObjectResponse> extends GenericBuilder<T, REQ> implements RequestBuilder<REQ, RESP> {
    protected final ObjectClient client;

    ObjectRequestBuilder(ObjectClient client) {
        this.client = Objects.requireNonNull(client);
    }
}
