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
public class RemoveKeyResponse extends S3Response implements AdminResponse, ToCopyableBuilder<RemoveKeyResponse.Builder, RemoveKeyResponse> {
    private RemoveKeyResponse(Builder builder) {
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

    public static class Builder extends BuilderImpl implements SdkPojo, CopyableBuilder<Builder, RemoveKeyResponse> {
        public Builder() {
        }

        private Builder(RemoveKeyResponse response) {
            super(response);
        }

        @Override
        public List<SdkField<?>> sdkFields() {
            return Collections.emptyList();
        }

        @Override
        public RemoveKeyResponse build() {
            return new RemoveKeyResponse(this);
        }
    }
}
