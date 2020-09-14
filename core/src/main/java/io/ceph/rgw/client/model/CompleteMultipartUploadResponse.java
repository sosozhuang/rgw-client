package io.ceph.rgw.client.model;

import java.util.Date;

/**
 * Created by zhuangshuo on 2020/3/16.
 */
public class CompleteMultipartUploadResponse extends BasePutObjectResponse {
    private final String location;

    public CompleteMultipartUploadResponse(String bucketName, String key, String versionId, String eTag, String location, Date expirationTime, String expirationTimeRuleId) {
        super(bucketName, key, versionId, eTag, expirationTime, expirationTimeRuleId);
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "CompleteMultipartUploadResponse{" +
                "location='" + location + '\'' +
                "} " + super.toString();
    }
}
