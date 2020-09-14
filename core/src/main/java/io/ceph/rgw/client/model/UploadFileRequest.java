package io.ceph.rgw.client.model;

import io.ceph.rgw.client.ObjectClient;
import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;

import java.io.File;

/**
 * Created by zhuangshuo on 2020/4/30.
 */
public class UploadFileRequest extends UploadPartRequest<File> {
    private final long fileOffset;

    public UploadFileRequest(String bucketName, String key, boolean lastPart, String md5, int partNumber, long partSize, String uploadId, File file, long fileOffset) {
        super(bucketName, key, lastPart, md5, partNumber, partSize, uploadId, file);
        this.fileOffset = fileOffset;
    }

    public long getFileOffset() {
        return fileOffset;
    }

    @Override
    public String toString() {
        return "UploadFileRequest{" +
                ", fileOffset=" + fileOffset +
                "} " + super.toString();
    }

    public static class Builder extends UploadPartRequest.Builder<Builder, File> {
        private long fileOffset;

        public Builder(ObjectClient client) {
            super(client);
        }

        public Builder withUpload(String file) {
            this.upload = new File(file);
            return self();
        }

        public Builder withFileOffset(long fileOffset) {
            this.fileOffset = fileOffset;
            return self();
        }

        @Override
        public UploadFileRequest build() {
            return new UploadFileRequest(bucketName, key, lastPart, md5, partNumber, partSize, uploadId, upload, fileOffset);
        }

        @Override
        public MultipartUploadPartResponse run() {
            return client.uploadFile(build());
        }

        @Override
        public ActionFuture<MultipartUploadPartResponse> execute() {
            return client.uploadFileAsync(build());
        }

        @Override
        public void execute(ActionListener<MultipartUploadPartResponse> listener) {
            client.uploadFileAsync(build(), listener);
        }
    }
}
