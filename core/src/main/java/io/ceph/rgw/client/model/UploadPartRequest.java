package io.ceph.rgw.client.model;

import io.ceph.rgw.client.ObjectClient;
import org.apache.commons.lang3.Validate;

import java.util.Objects;

/**
 * Created by zhuangshuo on 2020/3/16.
 */
public abstract class UploadPartRequest<V> extends BaseObjectRequest {
    final V upload;
    private final boolean lastPart;
    private final String md5;
    private final int partNumber;
    private final long partSize;
    private final String uploadId;

    protected UploadPartRequest(String bucketName, String key, boolean lastPart, String md5, int partNumber, long partSize, String uploadId, V upload) {
        super(bucketName, key, null);
        this.upload = Objects.requireNonNull(upload);
        this.lastPart = lastPart;
        this.md5 = md5;
        Validate.isTrue(partNumber > 0, "partNumber cannot be non positive");
        this.partNumber = partNumber;
        this.partSize = partSize;
        this.uploadId = uploadId;
    }

    public V getUpload() {
        return upload;
    }

    public String getUploadId() {
        return uploadId;
    }

    public boolean isLastPart() {
        return lastPart;
    }

    public String getMD5() {
        return md5;
    }

    public int getPartNumber() {
        return partNumber;
    }

    public long getPartSize() {
        return partSize;
    }

    @Override
    public String toString() {
        return "UploadPartRequest{" +
                "upload=" + upload +
                ", lastPart=" + lastPart +
                ", md5='" + md5 + '\'' +
                ", partNumber=" + partNumber +
                ", partSize=" + partSize +
                ", uploadId='" + uploadId + '\'' +
                "} " + super.toString();
    }

    static abstract class Builder<T extends Builder<T, V>, V> extends BaseObjectRequest.Builder<T, UploadPartRequest<V>, MultipartUploadPartResponse> {
        V upload;
        boolean lastPart;
        String md5;
        int partNumber;
        long partSize;
        String uploadId;

        public Builder(ObjectClient client) {
            super(client);
        }

        public T withUpload(V upload) {
            this.upload = upload;
            return self();
        }

        public T withLastPart(boolean lastPart) {
            this.lastPart = lastPart;
            return self();
        }

        public T withMD5(String md5) {
            this.md5 = md5;
            return self();
        }

        public T withPartNumber(int partNumber) {
            this.partNumber = partNumber;
            return self();
        }

        public T withPartSize(long partSize) {
            this.partSize = partSize;
            return self();
        }

        public T withUploadId(String uploadId) {
            this.uploadId = uploadId;
            return self();
        }

        @Override
        public abstract UploadPartRequest<V> build();
    }
}
