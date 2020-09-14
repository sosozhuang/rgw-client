package io.ceph.rgw.client.model.admin;

import software.amazon.awssdk.core.SdkField;
import software.amazon.awssdk.core.SdkPojo;
import software.amazon.awssdk.core.protocol.MarshallLocation;
import software.amazon.awssdk.core.protocol.MarshallingType;
import software.amazon.awssdk.core.traits.ListTrait;
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
public class CreateSubUserResponse extends S3Response implements AdminResponse, ToCopyableBuilder<CreateSubUserResponse.Builder, CreateSubUserResponse> {
    private static final SdkField<List<SubUser>> SUB_USERS_FIELD = SdkField
            .<List<SubUser>>builder(MarshallingType.LIST)
            .getter(getter(CreateSubUserResponse::getSubUsers))
            .setter(setter(Builder::withSubUsers))
            .traits(PayloadTrait.create(),
                    LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("subusers")
                            .unmarshallLocationName("subusers").build(),
                    ListTrait
                            .builder()
                            .memberLocationName("user")
                            .memberFieldInfo(
                                    SdkField.builder(MarshallingType.SDK_POJO)
                                            .constructor(SubUser::new)
                                            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD)
                                                    .locationName("user").unmarshallLocationName("user").build()).build())
                            .build()).build();
    private static final List<SdkField<?>> SDK_FIELDS = Collections.unmodifiableList(Collections.singletonList(SUB_USERS_FIELD));
    private final List<SubUser> subUsers;

    private CreateSubUserResponse(Builder builder) {
        super(builder);
        this.subUsers = AdminRequest.unmodifiableList(builder.subUsers);
    }

    private static <T> Function<Object, T> getter(Function<CreateSubUserResponse, T> g) {
        return obj -> g.apply((CreateSubUserResponse) obj);
    }

    private static <T> BiConsumer<Object, T> setter(BiConsumer<Builder, T> s) {
        return (obj, val) -> s.accept((Builder) obj, val);
    }

    public List<SubUser> getSubUsers() {
        return subUsers;
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
        return "CreateSubUserResponse{" +
                "subUsers=" + subUsers +
                "} " + super.toString();
    }

    public static class Builder extends BuilderImpl implements SdkPojo, CopyableBuilder<Builder, CreateSubUserResponse> {
        private List<SubUser> subUsers;

        public Builder() {
        }

        private Builder(CreateSubUserResponse response) {
            super(response);
            withSubUsers(response.subUsers);
        }

        private Builder withSubUsers(List<SubUser> subUsers) {
            this.subUsers = subUsers;
            return this;
        }

        @Override
        public List<SdkField<?>> sdkFields() {
            return SDK_FIELDS;
        }

        @Override
        public CreateSubUserResponse build() {
            return new CreateSubUserResponse(this);
        }
    }
}
