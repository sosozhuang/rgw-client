package io.ceph.rgw.client.exception;

import software.amazon.awssdk.core.SdkPojo;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.utils.builder.CopyableBuilder;
import software.amazon.awssdk.utils.builder.ToCopyableBuilder;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/8/5.
 */
public class InvalidKeyTypeException extends S3Exception implements ToCopyableBuilder<InvalidKeyTypeException.Builder, InvalidKeyTypeException> {
    private static final long serialVersionUID = -1446102119522269149L;

    private InvalidKeyTypeException(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Builder toBuilder() {
        return new Builder(this);
    }

    public static class Builder extends BuilderImpl implements SdkPojo, CopyableBuilder<Builder, InvalidKeyTypeException> {
        private Builder() {
        }

        public Builder(InvalidKeyTypeException e) {
        }

        @Override
        public InvalidKeyTypeException build() {
            return new InvalidKeyTypeException(this);
        }
    }
}
