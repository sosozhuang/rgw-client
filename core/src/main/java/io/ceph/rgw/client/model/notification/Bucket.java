package io.ceph.rgw.client.model.notification;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/5/13.
 */
public class Bucket {
    private final String bucketId;
    private final String name;
    private final String tenant;

    public Bucket(@JsonProperty("bucket_id") String bucketId,
                  String name,
                  String tenant) {
        this.bucketId = bucketId;
        this.name = name;
        this.tenant = tenant;
    }

    public String getBucketId() {
        return bucketId;
    }

    public String getName() {
        return name;
    }

    public String getTenant() {
        return tenant;
    }

    @Override
    public String toString() {
        return "Bucket{" +
                "bucketId='" + bucketId + '\'' +
                ", name='" + name + '\'' +
                ", tenant='" + tenant + '\'' +
                '}';
    }
}
