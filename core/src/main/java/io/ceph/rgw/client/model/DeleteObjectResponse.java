package io.ceph.rgw.client.model;

/**
 * Created by zhuangshuo on 2020/3/8.
 */
public class DeleteObjectResponse implements ObjectResponse {
    public static final DeleteObjectResponse INSTANCE = new DeleteObjectResponse();

    private DeleteObjectResponse() {
    }

    @Override
    public String toString() {
        return "RemoveObjectResponse{}";
    }
}
