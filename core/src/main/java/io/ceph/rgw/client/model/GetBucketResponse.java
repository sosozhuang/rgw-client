package io.ceph.rgw.client.model;

/**
 * Created by zhuangshuo on 2020/7/2.
 */
public class GetBucketResponse implements BucketResponse {
    private final boolean exist;

    public GetBucketResponse(boolean exist) {
        this.exist = exist;
    }

    public boolean isExist() {
        return exist;
    }

    @Override
    public String toString() {
        return "GetBucketResponse{" +
                "exist=" + exist +
                '}';
    }
}
