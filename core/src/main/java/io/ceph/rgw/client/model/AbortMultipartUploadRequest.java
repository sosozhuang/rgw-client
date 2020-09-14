package io.ceph.rgw.client.model;

import io.ceph.rgw.client.ObjectClient;
import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;
import org.apache.commons.lang3.Validate;

/**
 * Created by zhuangshuo on 2020/3/16.
 */
public class AbortMultipartUploadRequest extends BaseObjectRequest {
    private final String uploadId;

    public AbortMultipartUploadRequest(String bucketName, String key, String uploadId) {
        super(bucketName, key, null);
        this.uploadId = Validate.notBlank(uploadId);
    }

    public String getUploadId() {
        return uploadId;
    }

    @Override
    public String toString() {
        return "AbortMultipartUploadRequest{" +
                "uploadId='" + uploadId + '\'' +
                "} " + super.toString();
    }

    public static class Builder extends BaseObjectRequest.Builder<Builder, AbortMultipartUploadRequest, AbortMultipartUploadResponse> {
        private String uploadId;

        public Builder(ObjectClient client) {
            super(client);
        }

        public Builder withUploadId(String uploadId) {
            this.uploadId = uploadId;
            return self();
        }

        @Override
        public AbortMultipartUploadRequest build() {
            return new AbortMultipartUploadRequest(bucketName, key, uploadId);
        }

        @Override
        public AbortMultipartUploadResponse run() {
            return client.abortMultipartUpload(build());
        }

        @Override
        public ActionFuture<AbortMultipartUploadResponse> execute() {
            return client.abortMultipartUploadAsync(build());
        }

        @Override
        public void execute(ActionListener<AbortMultipartUploadResponse> listener) {
            client.abortMultipartUploadAsync(build(), listener);
        }
    }
}
