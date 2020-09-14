package io.ceph.rgw.client.model.admin;

import software.amazon.awssdk.core.SdkField;
import software.amazon.awssdk.core.SdkPojo;
import software.amazon.awssdk.core.protocol.MarshallLocation;
import software.amazon.awssdk.core.protocol.MarshallingType;
import software.amazon.awssdk.core.traits.LocationTrait;
import software.amazon.awssdk.services.s3.model.S3Response;
import software.amazon.awssdk.utils.builder.CopyableBuilder;
import software.amazon.awssdk.utils.builder.ToCopyableBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Created by zhuangshuo on 2020/7/28.
 */
public class GetPolicyResponse extends S3Response implements AdminResponse, ToCopyableBuilder<GetPolicyResponse.Builder, GetPolicyResponse> {
    private static final SdkField<ACL> ACL_FIELD = SdkField
            .<ACL>builder(MarshallingType.SDK_POJO)
            .getter(getter(GetPolicyResponse::getACL))
            .setter(setter(Builder::withACL))
            .constructor(ACL::new)
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("acl")
                    .unmarshallLocationName("acl").build()).build();
    private static final SdkField<Owner> OWNER_FIELD = SdkField
            .<Owner>builder(MarshallingType.SDK_POJO)
            .getter(getter(GetPolicyResponse::getOwner))
            .setter(setter(Builder::withOwner))
            .constructor(Owner::new)
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("owner")
                    .unmarshallLocationName("owner").build()).build();
    private static final List<SdkField<?>> SDK_FIELDS = Collections.unmodifiableList(Arrays.asList(ACL_FIELD, OWNER_FIELD));

    private final ACL acl;
    private final Owner owner;

    private GetPolicyResponse(Builder builder) {
        super(builder);
        this.owner = builder.owner;
        this.acl = builder.acl;
    }

    private static <T> Function<Object, T> getter(Function<GetPolicyResponse, T> g) {
        return obj -> g.apply((GetPolicyResponse) obj);
    }

    private static <T> BiConsumer<Object, T> setter(BiConsumer<Builder, T> s) {
        return (obj, val) -> s.accept((Builder) obj, val);
    }

    public ACL getACL() {
        return acl;
    }

    public Owner getOwner() {
        return owner;
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
        return "GetPolicyResponse{" +
                "acl=" + acl +
                ", owner=" + owner +
                "} " + super.toString();
    }

    public static class Builder extends BuilderImpl implements SdkPojo, CopyableBuilder<Builder, GetPolicyResponse> {
        private ACL acl;
        private Owner owner;

        public Builder() {
        }

        private Builder(GetPolicyResponse response) {
            super(response);
            withOwner(response.owner);
            withACL(response.acl);
        }

        private Builder withACL(ACL acl) {
            this.acl = acl;
            return this;
        }

        private Builder withOwner(Owner owner) {
            this.owner = owner;
            return this;
        }

        @Override
        public List<SdkField<?>> sdkFields() {
            return SDK_FIELDS;
        }

        @Override
        public GetPolicyResponse build() {
            return new GetPolicyResponse(this);
        }
    }
}
