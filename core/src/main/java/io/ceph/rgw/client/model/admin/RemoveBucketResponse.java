package io.ceph.rgw.client.model.admin;

import software.amazon.awssdk.core.SdkField;
import software.amazon.awssdk.core.SdkPojo;
import software.amazon.awssdk.services.s3.model.S3Response;
import software.amazon.awssdk.utils.builder.CopyableBuilder;
import software.amazon.awssdk.utils.builder.ToCopyableBuilder;

import java.util.Collections;
import java.util.List;

/**
 * Created by zhuangshuo on 2020/7/28.
 */
public class RemoveBucketResponse extends S3Response implements AdminResponse, ToCopyableBuilder<RemoveBucketResponse.Builder, RemoveBucketResponse> {
    private RemoveBucketResponse(Builder builder) {
        super(builder);
    }

    @Override
    public Builder toBuilder() {
        return new Builder(this);
    }

    @Override
    public List<SdkField<?>> sdkFields() {
        return Collections.emptyList();
    }

    public static class Builder extends BuilderImpl implements SdkPojo, CopyableBuilder<Builder, RemoveBucketResponse> {
        public Builder() {
        }

        private Builder(RemoveBucketResponse response) {
            super(response);
        }

        @Override
        public List<SdkField<?>> sdkFields() {
            return Collections.emptyList();
        }

        @Override
        public RemoveBucketResponse build() {
            return new RemoveBucketResponse(this);
        }
    }
}
