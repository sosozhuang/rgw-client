package io.ceph.rgw.client.model;

import io.ceph.rgw.client.ObjectClient;
import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;

import java.nio.ByteBuffer;

/**
 * Created by zhuangshuo on 2020/7/20.
 */
public class UploadByteBufferRequest extends UploadPartRequest<ByteBuffer> {
    public UploadByteBufferRequest(String bucketName, String key, boolean lastPart, String md5, int partNumber, long partSize, String uploadId, ByteBuffer buffer) {
        super(bucketName, key, lastPart, md5, partNumber, partSize, uploadId, buffer);
    }

    @Override
    public String toString() {
        return "UploadByteBufferRequest{" +
                "} " + super.toString();
    }

    public static class Builder extends UploadPartRequest.Builder<Builder, ByteBuffer> {
        public Builder(ObjectClient client) {
            super(client);
        }

        @Override
        public UploadByteBufferRequest build() {
            return new UploadByteBufferRequest(bucketName, key, lastPart, md5, partNumber, partSize, uploadId, upload);
        }

        @Override
        public MultipartUploadPartResponse run() {
            return client.uploadByteBuffer(build());
        }

        @Override
        public ActionFuture<MultipartUploadPartResponse> execute() {
            return client.uploadByteBufferAsync(build());
        }

        @Override
        public void execute(ActionListener<MultipartUploadPartResponse> listener) {
            client.uploadByteBufferAsync(build(), listener);
        }
    }
}
