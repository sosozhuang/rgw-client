package io.ceph.rgw.client.exception;

import software.amazon.awssdk.core.SdkPojo;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.utils.builder.CopyableBuilder;
import software.amazon.awssdk.utils.builder.ToCopyableBuilder;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/8/5.
 */
public class InCompleteBodyException extends S3Exception implements ToCopyableBuilder<InCompleteBodyException.Builder, InCompleteBodyException> {
    private static final long serialVersionUID = -870373807575974755L;

    private InCompleteBodyException(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Builder toBuilder() {
        return new Builder(this);
    }

    public static class Builder extends BuilderImpl implements SdkPojo, CopyableBuilder<Builder, InCompleteBodyException> {
        private Builder() {
        }

        public Builder(InCompleteBodyException e) {
        }

        @Override
        public InCompleteBodyException build() {
            return new InCompleteBodyException(this);
        }
    }
}
