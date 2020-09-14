package io.ceph.rgw.client.exception;

import software.amazon.awssdk.core.SdkPojo;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.utils.builder.CopyableBuilder;
import software.amazon.awssdk.utils.builder.ToCopyableBuilder;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/8/5.
 */
public class BucketUnlinkFailedException extends S3Exception implements ToCopyableBuilder<BucketUnlinkFailedException.Builder, BucketUnlinkFailedException> {
    private static final long serialVersionUID = 5936952141905613472L;

    private BucketUnlinkFailedException(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Builder toBuilder() {
        return new Builder(this);
    }

    public static class Builder extends BuilderImpl implements SdkPojo, CopyableBuilder<Builder, BucketUnlinkFailedException> {
        private Builder() {
        }

        public Builder(BucketUnlinkFailedException e) {
        }

        @Override
        public BucketUnlinkFailedException build() {
            return new BucketUnlinkFailedException(this);
        }
    }
}
