package io.ceph.rgw.client.model;

import io.ceph.rgw.client.ObjectClient;
import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;

import java.nio.charset.Charset;

/**
 * Created by zhuangshuo on 2020/7/21.
 */
public class UploadStringRequest extends UploadPartRequest<String> {
    private final Charset charset;

    public UploadStringRequest(String bucketName, String key, boolean lastPart, String md5, int partNumber, long partSize, String uploadId, String upload, Charset charset) {
        super(bucketName, key, lastPart, md5, partNumber, partSize, uploadId, upload);
        this.charset = charset;
    }

    public Charset getCharset() {
        return charset;
    }

    @Override
    public String toString() {
        return "UploadStringRequest{} " + super.toString();
    }

    public static class Builder extends UploadPartRequest.Builder<Builder, String> {
        private Charset charset;

        public Builder(ObjectClient client) {
            super(client);
        }

        public Builder withCharset(Charset charset) {
            this.charset = charset;
            return self();
        }

        @Override
        public UploadStringRequest build() {
            return new UploadStringRequest(bucketName, key, lastPart, md5, partNumber, partSize, uploadId, upload, charset);
        }

        @Override
        public MultipartUploadPartResponse run() {
            return client.UploadString(build());
        }

        @Override
        public ActionFuture<MultipartUploadPartResponse> execute() {
            return client.UploadStringAsync(build());
        }

        @Override
        public void execute(ActionListener<MultipartUploadPartResponse> listener) {
            client.UploadStringAsync(build(), listener);
        }
    }
}
