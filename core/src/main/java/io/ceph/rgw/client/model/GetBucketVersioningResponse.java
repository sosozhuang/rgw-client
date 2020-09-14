package io.ceph.rgw.client.model;

/**
 * Created by zhuangshuo on 2020/3/17.
 */
public class GetBucketVersioningResponse implements BucketResponse {
    private final BucketVersioning bucketVersioning;

    public GetBucketVersioningResponse(BucketVersioning bucketVersioning) {
        this.bucketVersioning = bucketVersioning;
    }

    public BucketVersioning getBucketVersioning() {
        return bucketVersioning;
    }

    @Override
    public String toString() {
        return "GetBucketVersioningResponse{" +
                "bucketVersioning=" + bucketVersioning +
                '}';
    }
}
