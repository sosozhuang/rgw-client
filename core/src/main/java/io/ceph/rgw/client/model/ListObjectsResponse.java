package io.ceph.rgw.client.model;

import java.util.Collections;
import java.util.List;

/**
 * Created by zhuangshuo on 2020/10/10.
 */
public class ListObjectsResponse implements BucketResponse {
    private final String bucketName;
    private final boolean truncated;
    private final int keyCount;
    private final String nextContinuationToken;
    private final String prefix;
    private final String delimiter;
    private final int maxKeys;
    private final String encodingType;
    private final String continuationToken;
    private final String startAfter;
    private final List<S3Object> contents;

    public ListObjectsResponse(String bucketName, boolean truncated, int keyCount, String nextContinuationToken, String prefix, String delimiter, int maxKeys, String encodingType, String continuationToken, String startAfter, List<S3Object> contents) {
        this.truncated = truncated;
        this.bucketName = bucketName;
        this.keyCount = keyCount;
        this.nextContinuationToken = nextContinuationToken;
        this.prefix = prefix;
        this.delimiter = delimiter;
        this.maxKeys = maxKeys;
        this.encodingType = encodingType;
        this.continuationToken = continuationToken;
        this.startAfter = startAfter;
        if (contents != null && contents.size() > 0) {
            this.contents = Collections.unmodifiableList(contents);
        } else {
            this.contents = Collections.emptyList();
        }
    }

    public String getBucketName() {
        return bucketName;
    }

    public boolean isTruncated() {
        return truncated;
    }

    public int getKeyCount() {
        return keyCount;
    }

    public String getNextContinuationToken() {
        return nextContinuationToken;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public int getMaxKeys() {
        return maxKeys;
    }

    public String getEncodingType() {
        return encodingType;
    }

    public String getContinuationToken() {
        return continuationToken;
    }

    public String getStartAfter() {
        return startAfter;
    }

    public List<S3Object> getContents() {
        return contents;
    }

    @Override
    public String toString() {
        return "ListObjectsResponse{" +
                "bucketName='" + bucketName + '\'' +
                ", truncated=" + truncated +
                ", keyCount=" + keyCount +
                ", nextContinuationToken='" + nextContinuationToken + '\'' +
                ", prefix='" + prefix + '\'' +
                ", delimiter='" + delimiter + '\'' +
                ", maxKeys=" + maxKeys +
                ", encodingType='" + encodingType + '\'' +
                ", continuationToken='" + continuationToken + '\'' +
                ", startAfter='" + startAfter + '\'' +
                ", contents=" + contents +
                '}';
    }
}
