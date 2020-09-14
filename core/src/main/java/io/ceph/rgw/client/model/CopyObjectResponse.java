package io.ceph.rgw.client.model;

import java.util.Date;

/**
 * Created by zhuangshuo on 2020/3/8.
 */
public class CopyObjectResponse implements ObjectResponse {
    private final String eTag;
    private final Date expirationTime;
    private final String expirationTimeRuleId;
    private final Date lastModifiedDate;
    private final String versionId;

    public CopyObjectResponse(String eTag, Date expirationTime, String expirationTimeRuleId, Date lastModifiedDate, String versionId) {
        this.eTag = eTag;
        this.expirationTime = expirationTime;
        this.expirationTimeRuleId = expirationTimeRuleId;
        this.lastModifiedDate = lastModifiedDate;
        this.versionId = versionId;
    }

    public String getETag() {
        return eTag;
    }

    public Date getExpirationTime() {
        return expirationTime;
    }

    public String getExpirationTimeRuleId() {
        return expirationTimeRuleId;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public String getVersionId() {
        return versionId;
    }

    @Override
    public String toString() {
        return "CopyObjectResponse{" +
                "eTag='" + eTag + '\'' +
                ", expirationTime=" + expirationTime +
                ", expirationTimeRuleId=" + expirationTimeRuleId +
                ", lastModifiedDate=" + lastModifiedDate +
                ", versionId='" + versionId + '\'' +
                '}';
    }
}
