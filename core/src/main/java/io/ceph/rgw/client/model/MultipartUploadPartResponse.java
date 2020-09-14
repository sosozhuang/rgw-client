package io.ceph.rgw.client.model;

/**
 * Created by zhuangshuo on 2020/3/16.
 */
public class MultipartUploadPartResponse implements ObjectResponse {
    private final String eTag;

    public MultipartUploadPartResponse(String eTag) {
        this.eTag = eTag;
    }

    public String getETag() {
        return eTag;
    }

    @Override
    public String toString() {
        return "MultipartUploadPartResponse{" +
                "eTag='" + eTag + '\'' +
                '}';
    }
}
