package io.ceph.rgw.client.model.admin;

import software.amazon.awssdk.core.SdkField;
import software.amazon.awssdk.core.SdkPojo;
import software.amazon.awssdk.core.protocol.MarshallLocation;
import software.amazon.awssdk.core.protocol.MarshallingType;
import software.amazon.awssdk.core.traits.LocationTrait;
import software.amazon.awssdk.core.traits.PayloadTrait;
import software.amazon.awssdk.services.s3.model.S3Response;
import software.amazon.awssdk.utils.builder.CopyableBuilder;
import software.amazon.awssdk.utils.builder.ToCopyableBuilder;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Created by zhuangshuo on 2020/7/28.
 */
public class CreateUserResponse extends S3Response implements AdminResponse, ToCopyableBuilder<CreateUserResponse.Builder, CreateUserResponse> {
    private static final SdkField<UserInfo> USER_INFO_FIELD = SdkField
            .<UserInfo>builder(MarshallingType.SDK_POJO)
            .getter(getter(CreateUserResponse::getUserInfo))
            .setter(setter(Builder::withUserInfo))
            .constructor(UserInfo::new)
            .traits(PayloadTrait.create(),
                    LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("user_info")
                            .unmarshallLocationName("user_info").build()).build();
    private static final List<SdkField<?>> SDK_FIELDS = Collections.unmodifiableList(Collections.singletonList(USER_INFO_FIELD));
    private final UserInfo userInfo;

    private CreateUserResponse(Builder builder) {
        super(builder);
        this.userInfo = builder.userInfo;
    }

    private static <T> Function<Object, T> getter(Function<CreateUserResponse, T> g) {
        return obj -> g.apply((CreateUserResponse) obj);
    }

    private static <T> BiConsumer<Object, T> setter(BiConsumer<Builder, T> s) {
        return (obj, val) -> s.accept((Builder) obj, val);
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    @Override
    public Builder toBuilder() {
        return new Builder(this);
    }

    @Override
    public List<SdkField<?>> sdkFields() {
        return SDK_FIELDS;
    }

    @Override
    public String toString() {
        return "CreateUserResponse{" +
                "userInfo=" + userInfo +
                "} " + super.toString();
    }

    public static class Builder extends BuilderImpl implements SdkPojo, CopyableBuilder<Builder, CreateUserResponse> {
        private UserInfo userInfo;

        public Builder() {
        }

        private Builder(CreateUserResponse response) {
            super(response);
            withUserInfo(response.userInfo);
        }

        private Builder withUserInfo(UserInfo userInfo) {
            this.userInfo = userInfo;
            return this;
        }

        @Override
        public List<SdkField<?>> sdkFields() {
            return SDK_FIELDS;
        }

        @Override
        public CreateUserResponse build() {
            return new CreateUserResponse(this);
        }
    }
}
