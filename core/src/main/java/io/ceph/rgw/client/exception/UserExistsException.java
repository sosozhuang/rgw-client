package io.ceph.rgw.client.exception;

import software.amazon.awssdk.core.SdkPojo;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.utils.builder.CopyableBuilder;
import software.amazon.awssdk.utils.builder.ToCopyableBuilder;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/8/5.
 */
public class UserExistsException extends S3Exception implements ToCopyableBuilder<UserExistsException.Builder, UserExistsException> {
    private static final long serialVersionUID = -5720427190774398233L;

    private UserExistsException(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Builder toBuilder() {
        return new Builder(this);
    }

    public static class Builder extends BuilderImpl implements SdkPojo, CopyableBuilder<Builder, UserExistsException> {
        private Builder() {
        }

        public Builder(UserExistsException e) {
        }

        @Override
        public UserExistsException build() {
            return new UserExistsException(this);
        }
    }
}
