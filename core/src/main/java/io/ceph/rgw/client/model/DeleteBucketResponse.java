package io.ceph.rgw.client.model;

/**
 * Created by zhuangshuo on 2020/3/16.
 */
public class DeleteBucketResponse implements BucketResponse {
    public static final DeleteBucketResponse INSTANCE = new DeleteBucketResponse();

    private DeleteBucketResponse() {
    }

    @Override
    public String toString() {
        return "RemoveBucketResponse{}";
    }
}
