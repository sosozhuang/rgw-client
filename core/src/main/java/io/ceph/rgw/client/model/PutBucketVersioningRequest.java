package io.ceph.rgw.client.model;

import io.ceph.rgw.client.BucketClient;
import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;
import org.apache.commons.lang3.Validate;

import java.util.Objects;

/**
 * Created by zhuangshuo on 2020/3/17.
 */
public class PutBucketVersioningRequest extends BaseBucketRequest {
    private final BucketVersioning bucketVersioning;

    public PutBucketVersioningRequest(String bucketName, BucketVersioning bucketVersioning) {
        super(bucketName);
        Validate.isTrue(bucketVersioning != BucketVersioning.OFF);
        this.bucketVersioning = Objects.requireNonNull(bucketVersioning);
    }

    @Override
    public String toString() {
        return "SetBucketVersioningRequest{" +
                "bucketVersioning=" + bucketVersioning +
                "} " + super.toString();
    }

    public BucketVersioning getBucketVersioning() {
        return bucketVersioning;
    }

    public static class Builder extends BaseBucketRequest.Builder<Builder, PutBucketVersioningRequest, PutBucketVersioningResponse> {
        private BucketVersioning bucketVersioning;

        public Builder(BucketClient client) {
            super(client);
            this.bucketVersioning = BucketVersioning.OFF;
        }

        public Builder withBucketVersioning(BucketVersioning bucketVersioning) {
            this.bucketVersioning = bucketVersioning;
            return self();
        }

        @Override
        public PutBucketVersioningRequest build() {
            return new PutBucketVersioningRequest(bucketName, bucketVersioning);
        }

        @Override
        public PutBucketVersioningResponse run() {
            return client.putBucketVersioning(build());
        }

        @Override
        public ActionFuture<PutBucketVersioningResponse> execute() {
            return client.putBucketVersioningAsync(build());
        }

        @Override
        public void execute(ActionListener<PutBucketVersioningResponse> listener) {
            client.putBucketVersioningAsync(build(), listener);
        }
    }
}
