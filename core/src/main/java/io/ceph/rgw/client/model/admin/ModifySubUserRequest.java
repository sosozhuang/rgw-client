package io.ceph.rgw.client.model.admin;

import io.ceph.rgw.client.AdminClient;
import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;
import org.apache.commons.lang3.Validate;
import software.amazon.awssdk.core.SdkField;
import software.amazon.awssdk.core.protocol.MarshallLocation;
import software.amazon.awssdk.core.protocol.MarshallingType;
import software.amazon.awssdk.core.traits.LocationTrait;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Created by zhuangshuo on 2020/7/28.
 */
public class ModifySubUserRequest extends AdminRequest {
    private static final SdkField<String> UID_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(ModifySubUserRequest::getUid))
            .setter(setter(Builder::withUid))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("uid")
                    .unmarshallLocationName("uid").build()).build();
    private static final SdkField<String> SUB_USER_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(ModifySubUserRequest::getSubUser))
            .setter(setter(Builder::withSubUser))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("subuser")
                    .unmarshallLocationName("subuser").build()).build();
    private static final SdkField<String> SECRET_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(ModifySubUserRequest::getSecret))
            .setter(setter(Builder::withSecret))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("secret")
                    .unmarshallLocationName("secret").build()).build();
    private static final SdkField<String> KEY_TYPE_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(ModifySubUserRequest::getKeyTypeAsString))
            .setter(setter(Builder::withKeyType))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("key-type")
                    .unmarshallLocationName("key-type").build()).build();
    private static final SdkField<String> ACCESS_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(ModifySubUserRequest::getAccessAsString))
            .setter(setter(Builder::withAccess))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("access")
                    .unmarshallLocationName("access").build()).build();
    private static final SdkField<Boolean> GENERATE_SECRET_FIELD = SdkField
            .builder(MarshallingType.BOOLEAN)
            .getter(getter(ModifySubUserRequest::getGenerateSecret))
            .setter(setter(Builder::withGenerateSecret))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("generate-secret")
                    .unmarshallLocationName("generate-secret").build()).build();
    private static final List<SdkField<?>> SDK_FIELDS = Collections.unmodifiableList(Arrays.asList(UID_FIELD, SUB_USER_FIELD, SECRET_FIELD, KEY_TYPE_FIELD, ACCESS_FIELD, GENERATE_SECRET_FIELD));

    private final String uid;
    private final String subUser;
    private final Boolean generateSecret;
    private final String secret;
    private final KeyType keyType;
    private final SubUser.Permission access;

    public ModifySubUserRequest(Builder builder) {
        super(builder);
        this.uid = Validate.notBlank(builder.uid, "uid cannot be empty string");
        this.subUser = Validate.notBlank(builder.subUser, "subUser cannot be empty string");
        this.generateSecret = builder.generateSecret;
        this.secret = builder.secret;
        this.keyType = builder.keyType;
        this.access = builder.access;
    }

    private static <T> Function<Object, T> getter(Function<ModifySubUserRequest, T> g) {
        return obj -> g.apply((ModifySubUserRequest) obj);
    }

    private static <T> BiConsumer<Object, T> setter(BiConsumer<Builder, T> s) {
        return (obj, val) -> s.accept((Builder) obj, val);
    }

    public String getUid() {
        return uid;
    }

    public String getSubUser() {
        return subUser;
    }

    public Boolean getGenerateSecret() {
        return generateSecret;
    }

    public String getSecret() {
        return secret;
    }

    public KeyType getKeyType() {
        return keyType;
    }

    private String getKeyTypeAsString() {
        return toString(keyType);
    }

    public SubUser.Permission getAccess() {
        return access;
    }

    private String getAccessAsString() {
        return toString(access);
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
        return "ModifySubUserRequest{" +
                "uid='" + uid + '\'' +
                ", subUser='" + subUser + '\'' +
                ", generateSecret=" + generateSecret +
                ", secret='" + secret + '\'' +
                ", keyType=" + keyType +
                ", access=" + access +
                "} " + super.toString();
    }

    public static class Builder extends AdminRequestBuilder<Builder, ModifySubUserRequest, ModifySubUserResponse> {
        private String uid;
        private String subUser;
        private Boolean generateSecret;
        private String secret;
        private KeyType keyType;
        private SubUser.Permission access;

        public Builder(AdminClient client) {
            super(client);
        }

        private Builder(ModifySubUserRequest request) {
            withUid(request.uid);
            withSubUser(request.subUser);
            withGenerateSecret(request.generateSecret);
            withSecret(request.secret);
            withKeyType(request.keyType);
            withAccess(request.access);
        }

        public Builder withUid(String uid) {
            this.uid = uid;
            return self();
        }

        public Builder withSubUser(String subUser) {
            this.subUser = subUser;
            return self();
        }

        public Builder withGenerateSecret(Boolean generateSecret) {
            this.generateSecret = generateSecret;
            return self();
        }

        public Builder withSecret(String secret) {
            this.secret = secret;
            return self();
        }

        public Builder withKeyType(KeyType keyType) {
            this.keyType = keyType;
            return self();
        }

        private Builder withKeyType(String keyType) {
            return withKeyType(KeyType.fromString(keyType));
        }

        public Builder withAccess(SubUser.Permission access) {
            this.access = access;
            return self();
        }

        private Builder withAccess(String access) {
            return withAccess(SubUser.Permission.fromString(access));
        }

        @Override
        public ModifySubUserRequest build() {
            return new ModifySubUserRequest(this);
        }

        @Override
        public ModifySubUserResponse run() {
            return client.modifySubUser(build());
        }

        @Override
        public ActionFuture<ModifySubUserResponse> execute() {
            return client.modifySubUserAsync(build());
        }

        @Override
        public void execute(ActionListener<ModifySubUserResponse> listener) {
            client.modifySubUserAsync(build(), listener);
        }
    }
}
