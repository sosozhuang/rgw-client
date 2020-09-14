package io.ceph.rgw.client.exception;

import software.amazon.awssdk.core.SdkPojo;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.utils.builder.CopyableBuilder;
import software.amazon.awssdk.utils.builder.ToCopyableBuilder;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/8/5.
 */
public class InvalidAccessException extends S3Exception implements ToCopyableBuilder<InvalidAccessException.Builder, InvalidAccessException> {
    private static final long serialVersionUID = -3794288337640755153L;

    private InvalidAccessException(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Builder toBuilder() {
        return new Builder(this);
    }

    public static class Builder extends BuilderImpl implements SdkPojo, CopyableBuilder<Builder, InvalidAccessException> {
        private Builder() {
        }

        public Builder(InvalidAccessException e) {
        }

        @Override
        public InvalidAccessException build() {
            return new InvalidAccessException(this);
        }
    }
}
