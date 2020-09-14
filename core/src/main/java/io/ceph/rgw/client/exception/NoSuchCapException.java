package io.ceph.rgw.client.exception;

import software.amazon.awssdk.core.SdkPojo;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.utils.builder.CopyableBuilder;
import software.amazon.awssdk.utils.builder.ToCopyableBuilder;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/8/5.
 */
public class NoSuchCapException extends S3Exception implements ToCopyableBuilder<NoSuchCapException.Builder, NoSuchCapException> {
    private static final long serialVersionUID = 681782859169411317L;

    private NoSuchCapException(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Builder toBuilder() {
        return new Builder(this);
    }

    public static class Builder extends BuilderImpl implements SdkPojo, CopyableBuilder<Builder, NoSuchCapException> {
        private Builder() {
        }

        public Builder(NoSuchCapException e) {
        }

        @Override
        public NoSuchCapException build() {
            return new NoSuchCapException(this);
        }
    }
}
