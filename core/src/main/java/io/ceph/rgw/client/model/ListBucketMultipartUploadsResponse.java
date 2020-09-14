package io.ceph.rgw.client.model;

import java.util.List;

/**
 * Created by zhuangshuo on 2020/3/16.
 */
public class ListBucketMultipartUploadsResponse implements BucketResponse {
    private final String bucketName;
    private final String keyMarker;
    private final String delimiter;
    private final String prefix;
    private final String uploadIdMarker;
    private final int maxUploads;
    private final String encodingType;
    private final boolean truncated;
    private final String nextKeyMarker;
    private final String nextUploadIdMarker;
    private final List<MultipartUpload> multipartUploads;

    public ListBucketMultipartUploadsResponse(String bucketName, String keyMarker, String delimiter, String prefix, String uploadIdMarker, int maxUploads, String encodingType, boolean truncated, String nextKeyMarker, String nextUploadIdMarker, List<MultipartUpload> multipartUploads) {
        this.bucketName = bucketName;
        this.keyMarker = keyMarker;
        this.delimiter = delimiter;
        this.prefix = prefix;
        this.uploadIdMarker = uploadIdMarker;
        this.maxUploads = maxUploads;
        this.encodingType = encodingType;
        this.truncated = truncated;
        this.nextKeyMarker = nextKeyMarker;
        this.nextUploadIdMarker = nextUploadIdMarker;
        this.multipartUploads = multipartUploads;
    }

    public String getBucketName() {
        return bucketName;
    }

    public String getKeyMarker() {
        return keyMarker;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getUploadIdMarker() {
        return uploadIdMarker;
    }

    public int getMaxUploads() {
        return maxUploads;
    }

    public String getEncodingType() {
        return encodingType;
    }

    public boolean isTruncated() {
        return truncated;
    }

    public String getNextKeyMarker() {
        return nextKeyMarker;
    }

    public String getNextUploadIdMarker() {
        return nextUploadIdMarker;
    }

    public List<MultipartUpload> getMultipartUploads() {
        return multipartUploads;
    }

    @Override
    public String toString() {
        return "ListBucketMultipartUploadsResponse{" +
                "bucketName='" + bucketName + '\'' +
                ", keyMarker='" + keyMarker + '\'' +
                ", delimiter='" + delimiter + '\'' +
                ", prefix='" + prefix + '\'' +
                ", uploadIdMarker='" + uploadIdMarker + '\'' +
                ", maxUploads=" + maxUploads +
                ", encodingType='" + encodingType + '\'' +
                ", truncated=" + truncated +
                ", nextKeyMarker='" + nextKeyMarker + '\'' +
                ", nextUploadIdMarker='" + nextUploadIdMarker + '\'' +
                ", multipartUploads=" + multipartUploads +
                '}';
    }
}
