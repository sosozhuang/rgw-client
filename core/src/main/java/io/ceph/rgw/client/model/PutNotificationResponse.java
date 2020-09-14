package io.ceph.rgw.client.model;

/**
 * Created by zhuangshuo on 2020/3/17.
 */
public class PutNotificationResponse implements BucketResponse {
    public static final PutNotificationResponse INSTANCE = new PutNotificationResponse();

    private PutNotificationResponse() {
    }

    @Override
    public String toString() {
        return "SetNotificationResponse{}";
    }
}
