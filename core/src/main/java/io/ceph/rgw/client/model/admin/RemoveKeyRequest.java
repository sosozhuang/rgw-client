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
public class RemoveKeyRequest extends AdminRequest {
    private static final SdkField<String> ACCESS_KEY_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(RemoveKeyRequest::getAccessKey))
            .setter(setter(Builder::withAccessKey))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("access-key")
                    .unmarshallLocationName("access-key").build()).build();
    private static final SdkField<String> UID_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(RemoveKeyRequest::getUid))
            .setter(setter(Builder::withUid))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("uid")
                    .unmarshallLocationName("uid").build()).build();
    private static final SdkField<String> SUB_USER_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(RemoveKeyRequest::getSubUser))
            .setter(setter(Builder::withSubUser))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("subuser")
                    .unmarshallLocationName("subuser").build()).build();
    private static final SdkField<String> KEY_TYPE_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(RemoveKeyRequest::getKeyTypeAsString))
            .setter(setter(Builder::withKeyType))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("key-type")
                    .unmarshallLocationName("key-type").build()).build();
    private static final List<SdkField<?>> SDK_FIELDS = Collections.unmodifiableList(Arrays.asList(ACCESS_KEY_FIELD, UID_FIELD, SUB_USER_FIELD, KEY_TYPE_FIELD));
    private final String accessKey;
    private final String uid;
    private final String subUser;
    private final KeyType keyType;

    public RemoveKeyRequest(Builder builder) {
        super(builder);
        this.accessKey = Validate.notBlank(builder.accessKey, "accessKey cannot be empty string");
        this.uid = builder.uid;
        this.subUser = builder.subUser;
        this.keyType = builder.keyType;
    }

    private static <T> Function<Object, T> getter(Function<RemoveKeyRequest, T> g) {
        return obj -> g.apply((RemoveKeyRequest) obj);
    }

    private static <T> BiConsumer<Object, T> setter(BiConsumer<Builder, T> s) {
        return (obj, val) -> s.accept((Builder) obj, val);
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getUid() {
        return uid;
    }

    public String getSubUser() {
        return subUser;
    }

    public KeyType getKeyType() {
        return keyType;
    }

    private String getKeyTypeAsString() {
        return toString(keyType);
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
        return "RemoveKeyRequest{" +
                "accessKey='" + accessKey + '\'' +
                ", uid='" + uid + '\'' +
                ", subUser='" + subUser + '\'' +
                ", keyType=" + keyType +
                "} " + super.toString();
    }

    public static class Builder extends AdminRequestBuilder<Builder, RemoveKeyRequest, RemoveKeyResponse> {
        private String accessKey;
        private String uid;
        private String subUser;
        private KeyType keyType;

        public Builder(AdminClient client) {
            super(client);
        }

        private Builder(RemoveKeyRequest request) {
            withAccessKey(request.accessKey);
            withUid(request.uid);
            withSubUser(request.subUser);
            withKeyType(request.keyType);
        }

        public Builder withAccessKey(String accessKey) {
            this.accessKey = accessKey;
            return self();
        }

        public Builder withUid(String uid) {
            this.uid = uid;
            return self();
        }

        public Builder withSubUser(String subUser) {
            this.subUser = subUser;
            return self();
        }

        public Builder withKeyType(KeyType keyType) {
            this.keyType = keyType;
            return self();
        }

        private Builder withKeyType(String keyType) {
            return withKeyType(KeyType.fromString(keyType));
        }

        @Override
        public RemoveKeyRequest build() {
            return new RemoveKeyRequest(this);
        }

        @Override
        public RemoveKeyResponse run() {
            return client.removeKey(build());
        }

        @Override
        public ActionFuture<RemoveKeyResponse> execute() {
            return client.removeKeyAsync(build());
        }

        @Override
        public void execute(ActionListener<RemoveKeyResponse> listener) {
            client.removeKeyAsync(build(), listener);
        }
    }
}
