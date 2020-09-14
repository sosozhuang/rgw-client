package io.ceph.rgw.client.model;

import io.ceph.rgw.client.BucketClient;
import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;

/**
 * Created by zhuangshuo on 2020/3/17.
 */
public class GetBucketACLRequest extends BaseBucketRequest {

    public GetBucketACLRequest(String bucketName) {
        super(bucketName);
    }

    @Override
    public String toString() {
        return "GetBucketACLRequest{} " + super.toString();
    }

    public static class Builder extends BaseBucketRequest.Builder<Builder, GetBucketACLRequest, GetBucketACLResponse> {

        public Builder(BucketClient client) {
            super(client);
        }

        @Override
        public GetBucketACLRequest build() {
            return new GetBucketACLRequest(bucketName);
        }

        @Override
        public GetBucketACLResponse run() {
            return client.getBucketACL(build());
        }

        @Override
        public ActionFuture<GetBucketACLResponse> execute() {
            return client.getBucketACLAsync(build());
        }

        @Override
        public void execute(ActionListener<GetBucketACLResponse> listener) {
            client.getBucketACLAsync(build(), listener);
        }
    }
}
