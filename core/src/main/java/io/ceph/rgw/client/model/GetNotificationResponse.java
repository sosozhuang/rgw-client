package io.ceph.rgw.client.model;

/**
 * Created by zhuangshuo on 2020/3/17.
 */
public class GetNotificationResponse implements BucketResponse {
    private final BucketNotificationConfiguration bucketNotificationConfiguration;

    public GetNotificationResponse(BucketNotificationConfiguration bucketNotificationConfiguration) {
        this.bucketNotificationConfiguration = bucketNotificationConfiguration;
    }

    public BucketNotificationConfiguration getBucketNotificationConfiguration() {
        return bucketNotificationConfiguration;
    }

    @Override
    public String toString() {
        return "GetNotificationResponse{" +
                "bucketNotificationConfiguration=" + bucketNotificationConfiguration +
                '}';
    }
}
