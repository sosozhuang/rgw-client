package io.ceph.rgw.client.model;


import io.ceph.rgw.client.BucketClient;

import java.util.Objects;

/**
 * Created by zhuangshuo on 2020/3/14.
 */
abstract class BucketRequestBuilder<T extends BucketRequestBuilder<T, REQ, RESP>, REQ extends BucketRequest, RESP extends BucketResponse> extends GenericBuilder<T, REQ> implements RequestBuilder<REQ, RESP> {
    protected final BucketClient client;

    BucketRequestBuilder(BucketClient client) {
        this.client = Objects.requireNonNull(client);
    }
}
