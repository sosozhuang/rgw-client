package io.ceph.rgw.client.model;

import java.util.Date;

/**
 * Created by zhuangshuo on 2020/7/21.
 */
public class BasePutObjectResponse implements ObjectResponse {
    private final String bucketName;
    private final String key;
    private final String versionId;
    private final String eTag;
    private final Date expirationTime;
    private final String expirationTimeRuleId;

    BasePutObjectResponse(String bucketName, String key, String versionId, String eTag, Date expirationTime, String expirationTimeRuleId) {
        this.bucketName = bucketName;
        this.key = key;
        this.versionId = versionId;
        this.eTag = eTag;
        this.expirationTime = expirationTime;
        this.expirationTimeRuleId = expirationTimeRuleId;
    }

    public String getBucketName() {
        return bucketName;
    }

    public String getKey() {
        return key;
    }

    public String getVersionId() {
        return versionId;
    }

    public String geteTag() {
        return eTag;
    }

    public Date getExpirationTime() {
        return expirationTime;
    }

    public String getExpirationTimeRuleId() {
        return expirationTimeRuleId;
    }

    @Override
    public String toString() {
        return "BasePutObjectResponse{" +
                "bucketName='" + bucketName + '\'' +
                ", key='" + key + '\'' +
                ", versionId='" + versionId + '\'' +
                ", eTag='" + eTag + '\'' +
                ", expirationTime=" + expirationTime +
                ", expirationTimeRuleId='" + expirationTimeRuleId + '\'' +
                '}';
    }
}
