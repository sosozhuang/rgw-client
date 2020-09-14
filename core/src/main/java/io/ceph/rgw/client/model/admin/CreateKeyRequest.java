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
public class CreateKeyRequest extends AdminRequest {
    private static final SdkField<String> UID_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(CreateKeyRequest::getUid))
            .setter(setter(Builder::withUid))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("uid")
                    .unmarshallLocationName("uid").build()).build();
    private static final SdkField<String> SUB_USER_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(CreateKeyRequest::getSubUser))
            .setter(setter(Builder::withSubUser))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("subuser")
                    .unmarshallLocationName("subuser").build()).build();
    private static final SdkField<String> KEY_TYPE_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(CreateKeyRequest::getKeyTypeAsString))
            .setter(setter(Builder::withKeyType))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("key-type")
                    .unmarshallLocationName("key-type").build()).build();
    private static final SdkField<String> ACCESS_KEY_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(CreateKeyRequest::getAccessKey))
            .setter(setter(Builder::withAccessKey))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("access-key")
                    .unmarshallLocationName("access-key").build()).build();
    private static final SdkField<String> SECRET_KEY_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(CreateKeyRequest::getSecretKey))
            .setter(setter(Builder::withSecretKey))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("secret-key")
                    .unmarshallLocationName("secret-key").build()).build();
    private static final SdkField<Boolean> GENERATE_KEY_FIELD = SdkField
            .builder(MarshallingType.BOOLEAN)
            .getter(getter(CreateKeyRequest::isGenerateKey))
            .setter(setter(Builder::withGenerateKey))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("generate-key")
                    .unmarshallLocationName("generate-key").build()).build();

    private static final List<SdkField<?>> SDK_FIELDS = Collections.unmodifiableList(Arrays.asList(UID_FIELD, SUB_USER_FIELD, KEY_TYPE_FIELD, ACCESS_KEY_FIELD, SECRET_KEY_FIELD, GENERATE_KEY_FIELD));

    private final String uid;
    private final String subUser;
    private final KeyType keyType;
    private final String accessKey;
    private final String secretKey;
    private final Boolean generateKey;

    public CreateKeyRequest(Builder builder) {
        super(builder);
        this.uid = Validate.notBlank(builder.uid, "uid cannot be empty string");
        this.subUser = builder.subUser;
        this.keyType = builder.keyType;
        this.accessKey = builder.accessKey;
        this.secretKey = builder.secretKey;
        this.generateKey = builder.generateKey;
    }

    private static <T> Function<Object, T> getter(Function<CreateKeyRequest, T> g) {
        return obj -> g.apply((CreateKeyRequest) obj);
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

    public KeyType getKeyType() {
        return keyType;
    }

    private String getKeyTypeAsString() {
        return toString(keyType);
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public Boolean isGenerateKey() {
        return generateKey;
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
        return "CreateKeyRequest{" +
                "uid='" + uid + '\'' +
                ", subUser='" + subUser + '\'' +
                ", keyType=" + keyType +
                ", accessKey='" + accessKey + '\'' +
                ", secretKey='" + secretKey + '\'' +
                ", generateKey=" + generateKey +
                "} " + super.toString();
    }

    public static class Builder extends AdminRequestBuilder<Builder, CreateKeyRequest, CreateKeyResponse> {
        private String uid;
        private String subUser;
        private KeyType keyType;
        private String accessKey;
        private String secretKey;
        private Boolean generateKey;

        public Builder(AdminClient client) {
            super(client);
        }

        public Builder(CreateKeyRequest request) {
            withUid(request.uid);
            withSubUser(request.subUser);
            withKeyType(request.keyType);
            withAccessKey(request.accessKey);
            withSecretKey(request.secretKey);
            withGenerateKey(request.generateKey);
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

        public Builder withKeyType(String keyType) {
            this.keyType = KeyType.fromString(keyType);
            return self();
        }

        public Builder withAccessKey(String accessKey) {
            this.accessKey = accessKey;
            return self();
        }

        public Builder withSecretKey(String secretKey) {
            this.secretKey = secretKey;
            return self();
        }

        public Builder withGenerateKey(Boolean generateKey) {
            this.generateKey = generateKey;
            return self();
        }

        @Override
        public CreateKeyRequest build() {
            return new CreateKeyRequest(this);
        }

        @Override
        public CreateKeyResponse run() {
            return client.createKey(build());
        }

        @Override
        public ActionFuture<CreateKeyResponse> execute() {
            return client.createKeyAsync(build());
        }

        @Override
        public void execute(ActionListener<CreateKeyResponse> listener) {
            client.createKeyAsync(build(), listener);
        }
    }
}
