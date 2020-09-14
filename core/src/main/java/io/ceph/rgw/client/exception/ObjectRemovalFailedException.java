package io.ceph.rgw.client.exception;

import software.amazon.awssdk.core.SdkPojo;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.utils.builder.CopyableBuilder;
import software.amazon.awssdk.utils.builder.ToCopyableBuilder;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/8/5.
 */
public class ObjectRemovalFailedException extends S3Exception implements ToCopyableBuilder<ObjectRemovalFailedException.Builder, ObjectRemovalFailedException> {
    private static final long serialVersionUID = -9055757764949229158L;

    private ObjectRemovalFailedException(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Builder toBuilder() {
        return new Builder(this);
    }

    public static class Builder extends BuilderImpl implements SdkPojo, CopyableBuilder<Builder, ObjectRemovalFailedException> {
        private Builder() {
        }

        public Builder(ObjectRemovalFailedException e) {
        }

        @Override
        public ObjectRemovalFailedException build() {
            return new ObjectRemovalFailedException(this);
        }
    }
}
