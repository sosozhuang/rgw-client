package io.ceph.rgw.client.exception;

import software.amazon.awssdk.core.SdkPojo;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.utils.builder.CopyableBuilder;
import software.amazon.awssdk.utils.builder.ToCopyableBuilder;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/8/5.
 */
public class InvalidAccessKeyException extends S3Exception implements ToCopyableBuilder<InvalidAccessKeyException.Builder, InvalidAccessKeyException> {
    private static final long serialVersionUID = 3849022461250247669L;

    private InvalidAccessKeyException(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Builder toBuilder() {
        return new Builder(this);
    }

    public static class Builder extends BuilderImpl implements SdkPojo, CopyableBuilder<Builder, InvalidAccessKeyException> {
        private Builder() {
        }

        public Builder(InvalidAccessKeyException e) {
        }

        @Override
        public InvalidAccessKeyException build() {
            return new InvalidAccessKeyException(this);
        }
    }
}
