package io.ceph.rgw.client.model;

import java.util.Date;

/**
 * Created by zhuangshuo on 2020/3/16.
 */
public class InitiateMultipartUploadResponse implements ObjectResponse {
    private final String bucketName;
    private final String key;
    private final String uploadId;
    private final Date abortDate;

    public InitiateMultipartUploadResponse(String bucketName, String key, String uploadId, Date abortDate) {
        this.bucketName = bucketName;
        this.key = key;
        this.uploadId = uploadId;
        this.abortDate = abortDate;
    }

    public String getBucketName() {
        return bucketName;
    }

    public String getKey() {
        return key;
    }

    public String getUploadId() {
        return uploadId;
    }

    public Date getAbortDate() {
        return abortDate;
    }

    @Override
    public String toString() {
        return "InitiateMultipartUploadResponse{" +
                "bucketName='" + bucketName + '\'' +
                ", key='" + key + '\'' +
                ", uploadId='" + uploadId + '\'' +
                ", abortDate=" + abortDate +
                '}';
    }
}
