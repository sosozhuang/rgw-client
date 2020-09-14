package io.ceph.rgw.client.model;

import io.ceph.rgw.client.BucketClient;
import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;

/**
 * Created by zhuangshuo on 2020/3/17.
 */
public class GetBucketNotificationRequest extends BaseBucketRequest {

    public GetBucketNotificationRequest(String bucketName) {
        super(bucketName);
    }

    @Override
    public String toString() {
        return "GetNotificationRequest{} " + super.toString();
    }

    public static class Builder extends BaseBucketRequest.Builder<Builder, GetBucketNotificationRequest, GetNotificationResponse> {

        public Builder(BucketClient client) {
            super(client);
        }

        @Override
        public GetBucketNotificationRequest build() {
            return new GetBucketNotificationRequest(bucketName);
        }

        @Override
        public GetNotificationResponse run() {
            return client.getNotification(build());
        }

        @Override
        public ActionFuture<GetNotificationResponse> execute() {
            return client.getNotificationAsync(build());
        }

        @Override
        public void execute(ActionListener<GetNotificationResponse> listener) {
            client.getNotificationAsync(build(), listener);
        }
    }
}
