package io.ceph.rgw.client.exception;

import software.amazon.awssdk.core.SdkPojo;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.utils.builder.CopyableBuilder;
import software.amazon.awssdk.utils.builder.ToCopyableBuilder;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/8/5.
 */
public class IndexRepairFailedException extends S3Exception implements ToCopyableBuilder<IndexRepairFailedException.Builder, IndexRepairFailedException> {
    private static final long serialVersionUID = 7434953903863123284L;

    private IndexRepairFailedException(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Builder toBuilder() {
        return new Builder(this);
    }

    public static class Builder extends BuilderImpl implements SdkPojo, CopyableBuilder<Builder, IndexRepairFailedException> {
        private Builder() {
        }

        public Builder(IndexRepairFailedException e) {
        }

        @Override
        public IndexRepairFailedException build() {
            return new IndexRepairFailedException(this);
        }
    }
}
