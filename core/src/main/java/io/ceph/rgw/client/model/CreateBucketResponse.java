package io.ceph.rgw.client.model;

/**
 * Created by zhuangshuo on 2020/3/16.
 */
public class CreateBucketResponse implements BucketResponse {
    public static final CreateBucketResponse INSTANCE = new CreateBucketResponse();

    private CreateBucketResponse() {
    }

    @Override
    public String toString() {
        return "PutBucketResponse{}";
    }
}
