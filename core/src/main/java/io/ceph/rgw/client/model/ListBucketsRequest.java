package io.ceph.rgw.client.model;

import io.ceph.rgw.client.BucketClient;
import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;

/**
 * Created by zhuangshuo on 2020/3/17.
 */
public class ListBucketsRequest implements BucketRequest {
    public static final ListBucketsRequest INSTANCE = new ListBucketsRequest();

    private ListBucketsRequest() {
    }

    @Override
    public String toString() {
        return "ListBucketsRequest{}";
    }

    public static class Builder extends BucketRequestBuilder<Builder, ListBucketsRequest, ListBucketsResponse> {

        public Builder(BucketClient client) {
            super(client);
        }

        @Override
        public ListBucketsRequest build() {
            return INSTANCE;
        }

        @Override
        public ListBucketsResponse run() {
            return client.listBuckets(build());
        }

        @Override
        public ActionFuture<ListBucketsResponse> execute() {
            return client.listBucketsAsync(build());
        }

        @Override
        public void execute(ActionListener<ListBucketsResponse> listener) {
            client.listBucketsAsync(build(), listener);
        }
    }
}
