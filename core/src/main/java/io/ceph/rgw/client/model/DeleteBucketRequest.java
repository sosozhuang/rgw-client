package io.ceph.rgw.client.model;

import io.ceph.rgw.client.BucketClient;
import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;

/**
 * Created by zhuangshuo on 2020/3/16.
 */
public class DeleteBucketRequest extends BaseBucketRequest {
    public DeleteBucketRequest(String bucketName) {
        super(bucketName);
    }

    @Override
    public String toString() {
        return "RemoveBucketRequest{} " + super.toString();
    }

    public static class Builder extends BaseBucketRequest.Builder<Builder, DeleteBucketRequest, DeleteBucketResponse> {

        public Builder(BucketClient client) {
            super(client);
        }

        @Override
        public DeleteBucketRequest build() {
            return new DeleteBucketRequest(bucketName);
        }

        @Override
        public DeleteBucketResponse run() {
            return client.deleteBucket(build());
        }

        @Override
        public ActionFuture<DeleteBucketResponse> execute() {
            return client.deleteBucketAsync(build());
        }

        @Override
        public void execute(ActionListener<DeleteBucketResponse> listener) {
            client.deleteBucketAsync(build(), listener);
        }
    }
}
