package io.ceph.rgw.client.exception;

import software.amazon.awssdk.core.SdkPojo;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.utils.builder.CopyableBuilder;
import software.amazon.awssdk.utils.builder.ToCopyableBuilder;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/8/5.
 */
public class NoSuchObjectException extends S3Exception implements ToCopyableBuilder<NoSuchObjectException.Builder, NoSuchObjectException> {
    private static final long serialVersionUID = 6839156272845347379L;

    private NoSuchObjectException(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Builder toBuilder() {
        return new Builder(this);
    }

    public static class Builder extends BuilderImpl implements SdkPojo, CopyableBuilder<Builder, NoSuchObjectException> {
        private Builder() {
        }

        public Builder(NoSuchObjectException e) {
        }

        @Override
        public NoSuchObjectException build() {
            return new NoSuchObjectException(this);
        }
    }
}
