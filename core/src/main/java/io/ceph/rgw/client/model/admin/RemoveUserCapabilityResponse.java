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
public class RemoveUserCapabilityResponse extends S3Response implements AdminResponse, ToCopyableBuilder<RemoveUserCapabilityResponse.Builder, RemoveUserCapabilityResponse> {
    private static final SdkField<List<UserCap>> USER_CAPS_FIELD = SdkField
            .<List<UserCap>>builder(MarshallingType.LIST)
            .getter(getter(RemoveUserCapabilityResponse::getUserCaps))
            .setter(setter(Builder::withUserCaps))
            .traits(PayloadTrait.create(),
                    LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("caps")
                            .unmarshallLocationName("caps").build(),
                    ListTrait
                            .builder()
                            .memberLocationName("cap")
                            .memberFieldInfo(
                                    SdkField.builder(MarshallingType.SDK_POJO)
                                            .constructor(UserCap::new)
                                            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD)
                                                    .locationName("cap").unmarshallLocationName("cap").build()).build())
                            .build()).build();

    private static final List<SdkField<?>> SDK_FIELDS = Collections.unmodifiableList(Collections.singletonList(USER_CAPS_FIELD));
    private final List<UserCap> userCaps;

    private RemoveUserCapabilityResponse(Builder builder) {
        super(builder);
        this.userCaps = AdminRequest.unmodifiableList(builder.userCaps);
    }

    private static <T> Function<Object, T> getter(Function<RemoveUserCapabilityResponse, T> g) {
        return obj -> g.apply((RemoveUserCapabilityResponse) obj);
    }

    private static <T> BiConsumer<Object, T> setter(BiConsumer<Builder, T> s) {
        return (obj, val) -> s.accept((Builder) obj, val);
    }

    public List<UserCap> getUserCaps() {
        return userCaps;
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
        return "RemoveUserCapabilityResponse{" +
                "userCaps=" + userCaps +
                "} " + super.toString();
    }

    public static class Builder extends BuilderImpl implements SdkPojo, CopyableBuilder<Builder, RemoveUserCapabilityResponse> {
        private List<UserCap> userCaps;

        public Builder() {
        }

        private Builder(RemoveUserCapabilityResponse response) {
            super(response);
            withUserCaps(response.userCaps);
        }

        private Builder withUserCaps(List<UserCap> userCaps) {
            this.userCaps = userCaps;
            return this;
        }

        @Override
        public List<SdkField<?>> sdkFields() {
            return SDK_FIELDS;
        }

        @Override
        public RemoveUserCapabilityResponse build() {
            return new RemoveUserCapabilityResponse(this);
        }
    }
}
