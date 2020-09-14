package io.ceph.rgw.client.model;

import io.ceph.rgw.client.BucketClient;
import org.apache.commons.lang3.Validate;

/**
 * A base class for the request of bucket operation.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/3/23.
 */
abstract class BaseBucketRequest implements BucketRequest {
    private final String bucketName;

    protected BaseBucketRequest(String bucketName) {
        this.bucketName = Validate.notBlank(bucketName, "bucket name cannot be empty string");
    }

    public String getBucketName() {
        return bucketName;
    }

    @Override
    public String toString() {
        return "BaseBucketRequest{" +
                "bucketName='" + bucketName + '\'' +
                '}';
    }

    public static abstract class Builder<T extends Builder<T, REQ, RESP>, REQ extends BaseBucketRequest, RESP extends BucketResponse> extends BucketRequestBuilder<T, REQ, RESP> {
        String bucketName;

        Builder(BucketClient client) {
            super(client);
        }

        public T withBucketName(String bucketName) {
            this.bucketName = bucketName;
            return self();
        }
    }
}
