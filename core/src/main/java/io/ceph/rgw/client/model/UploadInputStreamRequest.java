package io.ceph.rgw.client.model;

import io.ceph.rgw.client.ObjectClient;
import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;

import java.io.InputStream;

/**
 * Created by zhuangshuo on 2020/4/30.
 */
public class UploadInputStreamRequest extends UploadPartRequest<InputStream> {
    public UploadInputStreamRequest(String bucketName, String key, boolean lastPart, String md5, int partNumber, long partSize, String uploadId, InputStream inputStream) {
        super(bucketName, key, lastPart, md5, partNumber, partSize, uploadId, inputStream);
    }

    @Override
    public String toString() {
        return "UploadInputStreamRequest{" +
                "} " + super.toString();
    }

    public static class Builder extends UploadPartRequest.Builder<Builder, InputStream> {
        public Builder(ObjectClient client) {
            super(client);
        }

        @Override
        public UploadInputStreamRequest build() {
            return new UploadInputStreamRequest(bucketName, key, lastPart, md5, partNumber, partSize, uploadId, upload);
        }

        @Override
        public MultipartUploadPartResponse run() {
            return client.uploadInputStream(build());
        }

        @Override
        public ActionFuture<MultipartUploadPartResponse> execute() {
            return client.uploadInputStreamAsync(build());
        }

        @Override
        public void execute(ActionListener<MultipartUploadPartResponse> listener) {
            client.uploadInputStreamAsync(build(), listener);
        }
    }
}
