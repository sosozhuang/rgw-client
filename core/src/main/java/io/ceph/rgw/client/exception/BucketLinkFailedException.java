package io.ceph.rgw.client.exception;

import software.amazon.awssdk.core.SdkPojo;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.utils.builder.CopyableBuilder;
import software.amazon.awssdk.utils.builder.ToCopyableBuilder;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/8/5.
 */
public class BucketLinkFailedException extends S3Exception implements ToCopyableBuilder<BucketLinkFailedException.Builder, BucketLinkFailedException> {
    private static final long serialVersionUID = -7068584209204902294L;

    private BucketLinkFailedException(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Builder toBuilder() {
        return new Builder(this);
    }

    public static class Builder extends BuilderImpl implements SdkPojo, CopyableBuilder<Builder, BucketLinkFailedException> {
        private Builder() {
        }

        public Builder(BucketLinkFailedException e) {
        }

        @Override
        public BucketLinkFailedException build() {
            return new BucketLinkFailedException(this);
        }
    }
}
