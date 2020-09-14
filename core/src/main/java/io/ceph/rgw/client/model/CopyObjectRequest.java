package io.ceph.rgw.client.model;

import io.ceph.rgw.client.ObjectClient;
import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;
import org.apache.commons.lang3.Validate;

/**
 * Created by zhuangshuo on 2020/3/8.
 */
public class CopyObjectRequest implements ObjectRequest {
    private final String srcBucketName;
    private final String srcKey;
    private final String srcVersionId;
    private final String destBucketName;
    private final String destKey;

    public CopyObjectRequest(String srcBucketName, String srcKey, String srcVersionId, String destBucketName, String destKey) {
        this.srcBucketName = Validate.notBlank(srcBucketName, "srcBucketName cannot be empty string");
        this.srcKey = Validate.notBlank(srcKey, "srcKey cannot be empty string");
        this.srcVersionId = srcVersionId;
        this.destBucketName = Validate.notBlank(destBucketName, "destBucketName cannot be empty string");
        this.destKey = Validate.notBlank(destKey, "destKey cannot be empty string");
    }

    public String getSrcBucketName() {
        return srcBucketName;
    }

    public String getSrcKey() {
        return srcKey;
    }

    public String getSrcVersionId() {
        return srcVersionId;
    }

    public String getDestBucketName() {
        return destBucketName;
    }

    public String getDestKey() {
        return destKey;
    }

    @Override
    public String toString() {
        return "CopyObjectRequest{" +
                "srcBucketName='" + srcBucketName + '\'' +
                ", srcKey='" + srcKey + '\'' +
                ", srcVersionId='" + srcVersionId + '\'' +
                ", destBucketName='" + destBucketName + '\'' +
                ", destKey='" + destKey + '\'' +
                '}';
    }

    public static class Builder extends ObjectRequestBuilder<Builder, CopyObjectRequest, CopyObjectResponse> {
        private String srcBucketName;
        private String srcKey;
        private String srcVersionId;
        private String destBucketName;
        private String destKey;

        public Builder(ObjectClient client) {
            super(client);
        }

        public Builder withSrcBucketName(String srcBucketName) {
            this.srcBucketName = srcBucketName;
            return self();
        }

        public Builder withSrcKey(String srcKey) {
            this.srcKey = srcKey;
            return self();
        }

        public Builder withSrcVersionId(String srcVersionId) {
            this.srcVersionId = srcVersionId;
            return self();
        }

        public Builder withDestBucketName(String destBucketName) {
            this.destBucketName = destBucketName;
            return self();
        }

        public Builder withDestKey(String destKey) {
            this.destKey = destKey;
            return self();
        }

        @Override
        public CopyObjectRequest build() {
            return new CopyObjectRequest(srcBucketName, srcKey, srcVersionId, destBucketName, destKey);
        }

        @Override
        public CopyObjectResponse run() {
            return client.copyObject(build());
        }

        @Override
        public ActionFuture<CopyObjectResponse> execute() {
            return client.copyObjectAsync(build());
        }

        @Override
        public void execute(ActionListener<CopyObjectResponse> listener) {
            client.copyObjectAsync(build(), listener);
        }
    }
}
