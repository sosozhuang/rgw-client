package io.ceph.rgw.client.model;

/**
 * Created by zhuangshuo on 2020/3/16.
 */
public class AbortMultipartUploadResponse implements ObjectResponse {
    public static final AbortMultipartUploadResponse INSTANCE = new AbortMultipartUploadResponse();

    private AbortMultipartUploadResponse() {
    }

    @Override
    public String toString() {
        return "AbortMultipartUploadResponse{}";
    }
}
