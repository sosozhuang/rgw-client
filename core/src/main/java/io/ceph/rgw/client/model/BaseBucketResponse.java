package io.ceph.rgw.client.model;

/**
 * Created by zhuangshuo on 2020/3/23.
 */
abstract class BaseBucketResponse implements BucketResponse {
    private final String bucketName;

    BaseBucketResponse(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return bucketName;
    }

    @Override
    public String toString() {
        return "BaseBucketResponse{" +
                "bucketName='" + bucketName + '\'' +
                '}';
    }
}
