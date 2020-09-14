package io.ceph.rgw.client.exception;

import software.amazon.awssdk.core.SdkPojo;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.utils.builder.CopyableBuilder;
import software.amazon.awssdk.utils.builder.ToCopyableBuilder;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/8/5.
 */
public class BucketNotEmptyException extends S3Exception implements ToCopyableBuilder<BucketNotEmptyException.Builder, BucketNotEmptyException> {
    private static final long serialVersionUID = -2612232479768599480L;

    private BucketNotEmptyException(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Builder toBuilder() {
        return new Builder(this);
    }

    public static class Builder extends BuilderImpl implements SdkPojo, CopyableBuilder<Builder, BucketNotEmptyException> {
        private Builder() {
        }

        public Builder(BucketNotEmptyException e) {
        }

        @Override
        public BucketNotEmptyException build() {
            return new BucketNotEmptyException(this);
        }
    }
}
