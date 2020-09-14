package io.ceph.rgw.client.model;

import io.ceph.rgw.client.BucketClient;
import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;

/**
 * Created by zhuangshuo on 2020/3/17.
 */
public class GetBucketLocationRequest extends BaseBucketRequest {

    public GetBucketLocationRequest(String bucketName) {
        super(bucketName);
    }

    @Override
    public String toString() {
        return "GetBucketLocationRequest{} " + super.toString();
    }

    public static class Builder extends BaseBucketRequest.Builder<Builder, GetBucketLocationRequest, GetBucketLocationResponse> {

        public Builder(BucketClient client) {
            super(client);
        }

        @Override
        public GetBucketLocationRequest build() {
            return new GetBucketLocationRequest(bucketName);
        }

        @Override
        public GetBucketLocationResponse run() {
            return client.getBucketLocation(build());
        }

        @Override
        public ActionFuture<GetBucketLocationResponse> execute() {
            return client.getBucketLocationAsync(build());
        }

        @Override
        public void execute(ActionListener<GetBucketLocationResponse> listener) {
            client.getBucketLocationAsync(build(), listener);
        }
    }
}
