package io.ceph.rgw.client.model.admin;

import software.amazon.awssdk.core.SdkField;
import software.amazon.awssdk.core.SdkPojo;
import software.amazon.awssdk.services.s3.model.S3Response;
import software.amazon.awssdk.utils.builder.CopyableBuilder;
import software.amazon.awssdk.utils.builder.ToCopyableBuilder;

import java.util.Collections;
import java.util.List;

/**
 * Created by zhuangshuo on 2020/7/27.
 */
public class TrimUsageResponse extends S3Response implements AdminResponse, ToCopyableBuilder<TrimUsageResponse.Builder, TrimUsageResponse> {

    private TrimUsageResponse(Builder builder) {
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

    @Override
    public String toString() {
        return "TrimUsageResponse{} " + super.toString();
    }

    public static class Builder extends BuilderImpl implements SdkPojo, CopyableBuilder<Builder, TrimUsageResponse> {
        public Builder() {
        }

        private Builder(TrimUsageResponse response) {
            super(response);
        }

        @Override
        public TrimUsageResponse build() {
            return new TrimUsageResponse(this);
        }

        @Override
        public List<SdkField<?>> sdkFields() {
            return Collections.emptyList();
        }
    }
}
