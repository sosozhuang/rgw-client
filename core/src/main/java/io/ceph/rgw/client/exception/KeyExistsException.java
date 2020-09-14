package io.ceph.rgw.client.exception;

import software.amazon.awssdk.core.SdkPojo;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.utils.builder.CopyableBuilder;
import software.amazon.awssdk.utils.builder.ToCopyableBuilder;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/8/5.
 */
public class KeyExistsException extends S3Exception implements ToCopyableBuilder<KeyExistsException.Builder, KeyExistsException> {
    private static final long serialVersionUID = 1215190360591413779L;

    private KeyExistsException(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Builder toBuilder() {
        return new Builder(this);
    }

    public static class Builder extends BuilderImpl implements SdkPojo, CopyableBuilder<Builder, KeyExistsException> {
        private Builder() {
        }

        public Builder(KeyExistsException e) {
        }

        @Override
        public KeyExistsException build() {
            return new KeyExistsException(this);
        }
    }
}
