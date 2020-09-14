package io.ceph.rgw.client.model;

/**
 * Created by zhuangshuo on 2020/3/17.
 */
public class PutBucketVersioningResponse implements BucketResponse {
    public static final PutBucketVersioningResponse INSTANCE = new PutBucketVersioningResponse();

    private PutBucketVersioningResponse() {
    }

    @Override
    public String toString() {
        return "SetBucketVersioningResponse{}";
    }
}
