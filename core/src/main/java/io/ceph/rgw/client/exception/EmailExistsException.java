package io.ceph.rgw.client.exception;

import software.amazon.awssdk.core.SdkPojo;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.utils.builder.CopyableBuilder;
import software.amazon.awssdk.utils.builder.ToCopyableBuilder;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/8/5.
 */
public class EmailExistsException extends S3Exception implements ToCopyableBuilder<EmailExistsException.Builder, EmailExistsException> {
    private static final long serialVersionUID = 247644200845057808L;

    private EmailExistsException(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Builder toBuilder() {
        return new Builder(this);
    }

    public static class Builder extends BuilderImpl implements SdkPojo, CopyableBuilder<Builder, EmailExistsException> {
        private Builder() {
        }

        public Builder(EmailExistsException e) {
        }

        @Override
        public EmailExistsException build() {
            return new EmailExistsException(this);
        }
    }
}
