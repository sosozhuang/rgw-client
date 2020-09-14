package io.ceph.rgw.client.model;

import io.ceph.rgw.client.ObjectClient;
import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;
import org.apache.commons.lang3.Validate;

/**
 * Created by zhuangshuo on 2020/3/8.
 */
public class DeleteObjectRequest implements ObjectRequest {
    private final String bucketName;
    private final String key;

    public DeleteObjectRequest(String bucketName, String key) {
        this.bucketName = Validate.notBlank(bucketName, "bucketName cannot be empty string");
        this.key = Validate.notBlank(key, "key cannot be empty string");
    }

    public String getBucketName() {
        return bucketName;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return "RemoveObjectRequest{" +
                "bucketName='" + bucketName + '\'' +
                ", key='" + key + '\'' +
                '}';
    }

    public static class Builder extends ObjectRequestBuilder<Builder, DeleteObjectRequest, DeleteObjectResponse> {
        private String bucketName;
        private String key;

        public Builder(ObjectClient client) {
            super(client);
        }

        public Builder withBucketName(String bucketName) {
            this.bucketName = bucketName;
            return this;
        }

        public Builder withKey(String key) {
            this.key = key;
            return this;
        }

        @Override
        public DeleteObjectRequest build() {
            return new DeleteObjectRequest(bucketName, key);
        }

        @Override
        public DeleteObjectResponse run() {
            return client.deleteObject(build());
        }

        @Override
        public ActionFuture<DeleteObjectResponse> execute() {
            return client.deleteObjectAsync(build());
        }

        @Override
        public void execute(ActionListener<DeleteObjectResponse> listener) {
            client.deleteObjectAsync(build(), listener);
        }
    }
}
