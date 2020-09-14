package io.ceph.rgw.client.model;

import io.ceph.rgw.client.ObjectClient;
import org.apache.commons.lang3.Validate;

/**
 * Created by zhuangshuo on 2020/3/13.
 */
public abstract class BaseObjectRequest implements ObjectRequest {
    private final String bucketName;
    private final String key;
    private final String versionId;

    protected BaseObjectRequest(String bucketName, String key, String versionId) {
        this.bucketName = Validate.notBlank(bucketName, "bucketName cannot be empty string");
        this.key = Validate.notBlank(key, "key cannot be empty string");
        this.versionId = versionId;
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

    @Override
    public String toString() {
        return "BaseObjectRequest{" +
                "bucketName='" + bucketName + '\'' +
                ", key='" + key + '\'' +
                ", versionId='" + versionId + '\'' +
                '}';
    }

    static abstract class Builder<T extends Builder<T, REQ, RESP>, REQ extends BaseObjectRequest, RESP extends ObjectResponse> extends ObjectRequestBuilder<T, REQ, RESP> {
        protected String bucketName;
        protected String key;
        protected String versionId;

        Builder(ObjectClient client) {
            super(client);
        }

        public T withBucketName(String bucketName) {
            this.bucketName = bucketName;
            return self();
        }

        public T withKey(String key) {
            this.key = key;
            return self();
        }

        public T withVersionId(String versionId) {
            this.versionId = versionId;
            return self();
        }
    }
}
