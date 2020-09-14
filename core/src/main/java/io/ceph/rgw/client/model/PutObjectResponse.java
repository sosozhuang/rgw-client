package io.ceph.rgw.client.model;

import java.util.Date;

/**
 * Created by zhuangshuo on 2020/3/1.
 */
public class PutObjectResponse extends BasePutObjectResponse {
    private final String md5;
    private final Metadata metadata;

    public PutObjectResponse(String md5, String eTag, Date expirationTime,
                             String expirationTimeRuleId, Metadata metadata, String versionId) {
        super("", "", versionId, eTag, expirationTime, expirationTimeRuleId);
        this.md5 = md5;
        this.metadata = metadata;
    }

    public String getContentMD5() {
        return md5;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    @Override
    public String toString() {
        return "PutObjectResponse{" +
                "md5='" + md5 + '\'' +
                ", metadata=" + metadata +
                "} " + super.toString();
    }
}
