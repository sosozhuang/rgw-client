package io.ceph.rgw.client.model;

import io.ceph.rgw.client.BucketClient;
import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;

import java.util.Objects;

/**
 * Created by zhuangshuo on 2020/3/17.
 */
public class PutBucketNotificationRequest extends BaseBucketRequest {
    private final BucketNotificationConfiguration bucketNotificationConfiguration;

    public PutBucketNotificationRequest(String bucketName, BucketNotificationConfiguration bucketNotificationConfiguration) {
        super(bucketName);
        this.bucketNotificationConfiguration = Objects.requireNonNull(bucketNotificationConfiguration);
    }

    public BucketNotificationConfiguration getBucketNotificationConfiguration() {
        return bucketNotificationConfiguration;
    }

    @Override
    public String toString() {
        return "SetBucketNotificationRequest{" +
                "bucketNotificationConfiguration=" + bucketNotificationConfiguration +
                "} " + super.toString();
    }

    public static class Builder extends BaseBucketRequest.Builder<Builder, PutBucketNotificationRequest, PutNotificationResponse> {
        private BucketNotificationConfiguration bucketNotificationConfiguration;

        public Builder(BucketClient client) {
            super(client);
        }

        public BucketNotificationConfigurationBuilder withBucketNotificationConfiguration() {
            return new BucketNotificationConfigurationBuilder(this);
        }

        public Builder withBucketNotificationConfiguration(BucketNotificationConfiguration configuration) {
            this.bucketNotificationConfiguration = configuration;
            return self();
        }

        @Override
        public PutBucketNotificationRequest build() {
            return new PutBucketNotificationRequest(bucketName, bucketNotificationConfiguration);
        }

        @Override
        public PutNotificationResponse run() {
            return client.putNotification(build());
        }

        @Override
        public ActionFuture<PutNotificationResponse> execute() {
            return client.putNotificationAsync(build());
        }

        @Override
        public void execute(ActionListener<PutNotificationResponse> listener) {
            client.putNotificationAsync(build(), listener);
        }
    }

    public static class BucketNotificationConfigurationBuilder extends BucketNotificationConfiguration.NestedBuilder<BucketNotificationConfigurationBuilder> {
        private final Builder builder;

        BucketNotificationConfigurationBuilder(Builder builder) {
            this.builder = builder;
        }

        public Builder endBucketNotificationConfiguration() {
            return builder.withBucketNotificationConfiguration(build());
        }
    }
}
