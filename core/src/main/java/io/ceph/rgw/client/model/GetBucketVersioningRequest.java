package io.ceph.rgw.client.model;

import io.ceph.rgw.client.BucketClient;
import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;

/**
 * Created by zhuangshuo on 2020/3/17.
 */
public class GetBucketVersioningRequest extends BaseBucketRequest {

    public GetBucketVersioningRequest(String bucketName) {
        super(bucketName);
    }

    @Override
    public String toString() {
        return "GetBucketVersioningRequest{} " + super.toString();
    }

    public static class Builder extends BaseBucketRequest.Builder<Builder, GetBucketVersioningRequest, GetBucketVersioningResponse> {

        public Builder(BucketClient client) {
            super(client);
        }

        @Override
        public GetBucketVersioningRequest build() {
            return new GetBucketVersioningRequest(bucketName);
        }

        @Override
        public GetBucketVersioningResponse run() {
            return client.getBucketVersioning(build());
        }

        @Override
        public ActionFuture<GetBucketVersioningResponse> execute() {
            return client.getBucketVersioningAsync(build());
        }

        @Override
        public void execute(ActionListener<GetBucketVersioningResponse> listener) {
            client.getBucketVersioningAsync(build(), listener);
        }
    }
}
