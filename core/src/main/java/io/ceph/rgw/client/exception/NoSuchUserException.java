package io.ceph.rgw.client.exception;

import software.amazon.awssdk.core.SdkPojo;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.utils.builder.CopyableBuilder;
import software.amazon.awssdk.utils.builder.ToCopyableBuilder;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/8/5.
 */
public class NoSuchUserException extends S3Exception implements ToCopyableBuilder<NoSuchUserException.Builder, NoSuchUserException> {
    private static final long serialVersionUID = -7039857937452644229L;

    private NoSuchUserException(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Builder toBuilder() {
        return new Builder(this);
    }

    public static class Builder extends BuilderImpl implements SdkPojo, CopyableBuilder<Builder, NoSuchUserException> {
        private Builder() {
        }

        public Builder(NoSuchUserException e) {
        }

        @Override
        public NoSuchUserException build() {
            return new NoSuchUserException(this);
        }
    }
}
