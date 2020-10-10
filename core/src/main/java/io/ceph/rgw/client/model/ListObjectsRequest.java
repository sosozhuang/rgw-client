package io.ceph.rgw.client.model;

import io.ceph.rgw.client.BucketClient;
import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;

/**
 * Created by zhuangshuo on 2020/10/10.
 */
public class ListObjectsRequest extends BaseBucketRequest {
    private final String prefix;
    private final String delimiter;
    private final Integer maxKeys;
    private final String encodingType;
    private final String continuationToken;
    private final boolean fetchOwner;
    private final String startAfter;

    public ListObjectsRequest(String bucketName, String prefix, String delimiter, Integer maxKeys, String encodingType, String continuationToken, boolean fetchOwner, String startAfter) {
        super(bucketName);
        this.prefix = prefix;
        this.delimiter = delimiter;
        this.maxKeys = maxKeys;
        this.encodingType = encodingType;
        this.continuationToken = continuationToken;
        this.fetchOwner = fetchOwner;
        this.startAfter = startAfter;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public Integer getMaxKeys() {
        return maxKeys;
    }

    public String getEncodingType() {
        return encodingType;
    }

    public String getContinuationToken() {
        return continuationToken;
    }

    public boolean isFetchOwner() {
        return fetchOwner;
    }

    public String getStartAfter() {
        return startAfter;
    }

    @Override
    public String toString() {
        return "ListObjectsRequest{" +
                "prefix='" + prefix + '\'' +
                ", delimiter='" + delimiter + '\'' +
                ", maxKeys=" + maxKeys +
                ", encodingType='" + encodingType + '\'' +
                ", continuationToken='" + continuationToken + '\'' +
                ", fetchOwner=" + fetchOwner +
                ", startAfter='" + startAfter + '\'' +
                "} " + super.toString();
    }

    public static class Builder extends BaseBucketRequest.Builder<Builder, ListObjectsRequest, ListObjectsResponse> {
        private String prefix;
        private String delimiter;
        private Integer maxKeys;
        private String encodingType;
        private String continuationToken;
        private boolean fetchOwner;
        private String startAfter;

        public Builder(BucketClient client) {
            super(client);
        }

        public Builder withPrefix(String prefix) {
            this.prefix = prefix;
            return self();
        }

        public Builder withDelimiter(String delimiter) {
            this.delimiter = delimiter;
            return self();
        }

        public Builder withMaxKeys(Integer maxKeys) {
            this.maxKeys = maxKeys;
            return self();
        }

        public Builder withEncodingType(String encodingType) {
            this.encodingType = encodingType;
            return self();
        }

        public Builder withContinuationToken(String continuationToken) {
            this.continuationToken = continuationToken;
            return self();
        }

        public Builder withFetchOwner(boolean fetchOwner) {
            this.fetchOwner = fetchOwner;
            return self();
        }

        public Builder withStartAfter(String startAfter) {
            this.startAfter = startAfter;
            return self();
        }

        @Override
        public ListObjectsRequest build() {
            return new ListObjectsRequest(bucketName, prefix, delimiter, maxKeys, encodingType, continuationToken, fetchOwner, startAfter);
        }

        @Override
        public ListObjectsResponse run() {
            return client.listObjects(build());
        }

        @Override
        public ActionFuture<ListObjectsResponse> execute() {
            return client.listObjectsAsync(build());
        }

        @Override
        public void execute(ActionListener<ListObjectsResponse> listener) {
            client.listObjectsAsync(build(), listener);
        }
    }
}
