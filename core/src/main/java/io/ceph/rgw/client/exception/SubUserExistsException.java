package io.ceph.rgw.client.exception;

import software.amazon.awssdk.core.SdkPojo;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.utils.builder.CopyableBuilder;
import software.amazon.awssdk.utils.builder.ToCopyableBuilder;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/8/5.
 */
public class SubUserExistsException extends S3Exception implements ToCopyableBuilder<SubUserExistsException.Builder, SubUserExistsException> {
    private static final long serialVersionUID = 708792361728924136L;

    private SubUserExistsException(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Builder toBuilder() {
        return new Builder(this);
    }

    public static class Builder extends BuilderImpl implements SdkPojo, CopyableBuilder<Builder, SubUserExistsException> {
        private Builder() {
        }

        public Builder(SubUserExistsException e) {
        }

        @Override
        public SubUserExistsException build() {
            return new SubUserExistsException(this);
        }
    }
}
