package io.ceph.rgw.client.model;

import io.ceph.rgw.client.BucketClient;
import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;

/**
 * Created by zhuangshuo on 2020/7/2.
 */
public class GetBucketRequest extends BaseBucketRequest {
    public GetBucketRequest(String bucketName) {
        super(bucketName);
    }

    @Override
    public String toString() {
        return "GetBucketRequest{} " + super.toString();
    }

    public static class Builder extends BaseBucketRequest.Builder<Builder, GetBucketRequest, GetBucketResponse> {

        public Builder(BucketClient client) {
            super(client);
        }

        @Override
        public GetBucketRequest build() {
            return new GetBucketRequest(bucketName);
        }

        @Override
        public GetBucketResponse run() {
            return client.getBucket(build());
        }

        @Override
        public ActionFuture<GetBucketResponse> execute() {
            return client.getBucketAsync(build());
        }

        @Override
        public void execute(ActionListener<GetBucketResponse> listener) {
            client.getBucketAsync(build(), listener);
        }
    }
}
