package io.ceph.rgw.client.model;

/**
 * Created by zhuangshuo on 2020/3/17.
 */
public class PutBucketACLResponse implements BucketResponse {
    public static final PutBucketACLResponse INSTANCE = new PutBucketACLResponse();

    private PutBucketACLResponse() {
    }

    @Override
    public String toString() {
        return "SetBucketACLResponse{}";
    }
}
