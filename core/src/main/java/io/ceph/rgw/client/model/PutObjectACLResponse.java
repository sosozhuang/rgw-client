package io.ceph.rgw.client.model;

/**
 * Created by zhuangshuo on 2020/3/16.
 */
public class PutObjectACLResponse implements ObjectResponse {
    public static final PutObjectACLResponse INSTANCE = new PutObjectACLResponse();

    private PutObjectACLResponse() {
    }

    @Override
    public String toString() {
        return "SetObjectACLResponse{}";
    }
}
