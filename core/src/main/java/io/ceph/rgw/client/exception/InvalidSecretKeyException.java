package io.ceph.rgw.client.exception;

import software.amazon.awssdk.core.SdkPojo;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.utils.builder.CopyableBuilder;
import software.amazon.awssdk.utils.builder.ToCopyableBuilder;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/8/5.
 */
public class InvalidSecretKeyException extends S3Exception implements ToCopyableBuilder<InvalidSecretKeyException.Builder, InvalidSecretKeyException> {
    private static final long serialVersionUID = -6838140537668085499L;

    private InvalidSecretKeyException(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Builder toBuilder() {
        return new Builder(this);
    }

    public static class Builder extends BuilderImpl implements SdkPojo, CopyableBuilder<Builder, InvalidSecretKeyException> {
        private Builder() {
        }

        public Builder(InvalidSecretKeyException e) {
        }

        @Override
        public InvalidSecretKeyException build() {
            return new InvalidSecretKeyException(this);
        }
    }
}
