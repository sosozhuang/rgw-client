package io.ceph.rgw.client.exception;

import software.amazon.awssdk.core.SdkPojo;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.utils.builder.CopyableBuilder;
import software.amazon.awssdk.utils.builder.ToCopyableBuilder;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/8/5.
 */
public class AccessDeniedException extends S3Exception implements ToCopyableBuilder<AccessDeniedException.Builder, AccessDeniedException> {
    private static final long serialVersionUID = 613212619020746282L;

    private AccessDeniedException(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Builder toBuilder() {
        return new Builder(this);
    }

    public static class Builder extends BuilderImpl implements SdkPojo, CopyableBuilder<Builder, AccessDeniedException> {
        private Builder() {
        }

        public Builder(AccessDeniedException e) {
        }

        @Override
        public AccessDeniedException build() {
            return new AccessDeniedException(this);
        }
    }
}
