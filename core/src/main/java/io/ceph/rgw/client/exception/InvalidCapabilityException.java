package io.ceph.rgw.client.exception;

import software.amazon.awssdk.core.SdkPojo;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.utils.builder.CopyableBuilder;
import software.amazon.awssdk.utils.builder.ToCopyableBuilder;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/8/5.
 */
public class InvalidCapabilityException extends S3Exception implements ToCopyableBuilder<InvalidCapabilityException.Builder, InvalidCapabilityException> {
    private static final long serialVersionUID = 2645347404647856089L;

    private InvalidCapabilityException(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Builder toBuilder() {
        return new Builder(this);
    }

    public static class Builder extends BuilderImpl implements SdkPojo, CopyableBuilder<Builder, InvalidCapabilityException> {
        private Builder() {
        }

        public Builder(InvalidCapabilityException e) {
        }

        @Override
        public InvalidCapabilityException build() {
            return new InvalidCapabilityException(this);
        }
    }
}
