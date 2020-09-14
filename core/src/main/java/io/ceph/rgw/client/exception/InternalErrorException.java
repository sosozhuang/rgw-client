package io.ceph.rgw.client.exception;

import software.amazon.awssdk.core.SdkPojo;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.utils.builder.CopyableBuilder;
import software.amazon.awssdk.utils.builder.ToCopyableBuilder;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/8/5.
 */
public class InternalErrorException extends S3Exception implements ToCopyableBuilder<InternalErrorException.Builder, InternalErrorException> {
    private static final long serialVersionUID = 681782859169411317L;

    private InternalErrorException(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Builder toBuilder() {
        return new Builder(this);
    }

    public static class Builder extends BuilderImpl implements SdkPojo, CopyableBuilder<Builder, InternalErrorException> {
        private Builder() {
        }

        public Builder(InternalErrorException e) {
        }

        @Override
        public InternalErrorException build() {
            return new InternalErrorException(this);
        }
    }
}
