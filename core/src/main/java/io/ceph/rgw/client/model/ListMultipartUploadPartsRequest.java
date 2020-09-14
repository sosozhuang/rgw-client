package io.ceph.rgw.client.model;

import io.ceph.rgw.client.BucketClient;
import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;

/**
 * Created by zhuangshuo on 2020/3/16.
 */
public class ListMultipartUploadPartsRequest extends BaseBucketRequest {
    private final String delimiter;
    private final String encodingType;
    private final String keyMarker;
    private final int maxUploads;
    private final String prefix;
    private final String uploadIdMarker;

    public ListMultipartUploadPartsRequest(String bucketName, String delimiter, String encodingType, String keyMarker, int maxUploads, String prefix, String uploadIdMarker) {
        super(bucketName);
        this.delimiter = delimiter;
        this.encodingType = encodingType;
        this.keyMarker = keyMarker;
        this.maxUploads = maxUploads;
        this.prefix = prefix;
        this.uploadIdMarker = uploadIdMarker;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public String getEncodingType() {
        return encodingType;
    }

    public String getKeyMarker() {
        return keyMarker;
    }

    public int getMaxUploads() {
        return maxUploads;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getUploadIdMarker() {
        return uploadIdMarker;
    }

    @Override
    public String toString() {
        return "ListMultipartUploadPartsRequest{" +
                "delimiter='" + delimiter + '\'' +
                ", encodingType='" + encodingType + '\'' +
                ", keyMarker='" + keyMarker + '\'' +
                ", maxUploads=" + maxUploads +
                ", prefix='" + prefix + '\'' +
                ", uploadIdMarker='" + uploadIdMarker + '\'' +
                "} " + super.toString();
    }

    public static class Builder extends BaseBucketRequest.Builder<Builder, ListMultipartUploadPartsRequest, ListBucketMultipartUploadsResponse> {
        private String delimiter;
        private String encodingType;
        private String keyMarker;
        private int maxUploads;
        private String prefix;
        private String uploadIdMarker;

        public Builder(BucketClient client) {
            super(client);
        }

        public Builder withDelimiter(String delimiter) {
            this.delimiter = delimiter;
            return self();
        }

        public Builder withEncodingType(String encodingType) {
            this.encodingType = encodingType;
            return self();
        }

        public Builder withKeyMarker(String keyMarker) {
            this.keyMarker = keyMarker;
            return self();
        }

        public Builder withMaxUploads(int maxUploads) {
            this.maxUploads = maxUploads;
            return self();
        }

        public Builder withPrefix(String prefix) {
            this.prefix = prefix;
            return self();
        }

        public Builder withUploadIdMarker(String uploadIdMarker) {
            this.uploadIdMarker = uploadIdMarker;
            return self();
        }

        @Override
        public ListMultipartUploadPartsRequest build() {
            return new ListMultipartUploadPartsRequest(bucketName, delimiter, encodingType, keyMarker, maxUploads, prefix, uploadIdMarker);
        }

        @Override
        public ListBucketMultipartUploadsResponse run() {
            return client.listBucketMultipartUploads(build());
        }

        @Override
        public ActionFuture<ListBucketMultipartUploadsResponse> execute() {
            return client.listBucketMultipartUploadsAsync(build());
        }

        @Override
        public void execute(ActionListener<ListBucketMultipartUploadsResponse> listener) {
            client.listBucketMultipartUploadsAsync(build(), listener);
        }
    }
}
