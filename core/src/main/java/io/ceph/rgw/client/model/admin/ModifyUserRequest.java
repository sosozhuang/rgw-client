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
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Created by zhuangshuo on 2020/7/28.
 */
public class ModifyUserRequest extends AdminRequest {
    private static final SdkField<String> UID_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(ModifyUserRequest::getUid))
            .setter(setter(Builder::withUid))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("uid")
                    .unmarshallLocationName("uid").build()).build();
    private static final SdkField<String> DISPLAY_NAME_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(ModifyUserRequest::getDisplayName))
            .setter(setter(Builder::withDisplayName))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("display-name")
                    .unmarshallLocationName("display-name").build()).build();
    private static final SdkField<String> EMAIL_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(ModifyUserRequest::getEmail))
            .setter(setter(Builder::withEmail))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("email")
                    .unmarshallLocationName("email").build()).build();
    private static final SdkField<Boolean> GENERATE_KEY_FIELD = SdkField
            .builder(MarshallingType.BOOLEAN)
            .getter(getter(ModifyUserRequest::isGenerateKey))
            .setter(setter(Builder::withGenerateKey))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("generate-key")
                    .unmarshallLocationName("generate-key").build()).build();
    private static final SdkField<String> ACCESS_KEY_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(ModifyUserRequest::getAccessKey))
            .setter(setter(Builder::withAccessKey))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("access-key")
                    .unmarshallLocationName("access-key").build()).build();
    private static final SdkField<String> SECRET_KEY_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(ModifyUserRequest::getSecretKey))
            .setter(setter(Builder::withSecretKey))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("secret-key")
                    .unmarshallLocationName("secret-key").build()).build();
    private static final SdkField<String> KEY_TYPE_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(ModifyUserRequest::getKeyTypeAsString))
            .setter(setter(Builder::withKeyType))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("uid")
                    .unmarshallLocationName("uid").build()).build();
    private static final SdkField<String> USER_CAPS_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(ModifyUserRequest::getUserCapsAsString))
            .setter(setter(Builder::withUserCaps))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("user-caps")
                    .unmarshallLocationName("user-caps").build()).build();
    private static final SdkField<Integer> MAX_BUCKETS_FIELD = SdkField
            .builder(MarshallingType.INTEGER)
            .getter(getter(ModifyUserRequest::getMaxBuckets))
            .setter(setter(Builder::withMaxBuckets))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("max-buckets")
                    .unmarshallLocationName("max-buckets").build()).build();
    private static final SdkField<Boolean> SUSPENDED_FIELD = SdkField
            .builder(MarshallingType.BOOLEAN)
            .getter(getter(ModifyUserRequest::isSuspended))
            .setter(setter(Builder::withSuspended))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("suspended")
                    .unmarshallLocationName("suspended").build()).build();
    private static final SdkField<String> TENANT_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(ModifyUserRequest::getTenant))
            .setter(setter(Builder::withTenant))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("tenant")
                    .unmarshallLocationName("tenant").build()).build();
    private static final List<SdkField<?>> SDK_FIELDS = Collections.unmodifiableList(Arrays.asList(UID_FIELD, DISPLAY_NAME_FIELD, EMAIL_FIELD, GENERATE_KEY_FIELD, ACCESS_KEY_FIELD, SECRET_KEY_FIELD, KEY_TYPE_FIELD, USER_CAPS_FIELD, MAX_BUCKETS_FIELD, SUSPENDED_FIELD, TENANT_FIELD));

    private final String uid;
    private final String displayName;
    private final String email;
    private final Boolean generateKey;
    private final String accessKey;
    private final String secretKey;
    private final KeyType keyType;
    private final List<UserCap> userCaps;
    private final Integer maxBuckets;
    private final Boolean suspended;
    private final String tenant;

    public ModifyUserRequest(Builder builder) {
        super(builder);
        this.uid = Validate.notBlank(builder.uid, "uid cannot be empty string");
        this.displayName = builder.displayName;
        this.email = builder.email;
        this.generateKey = builder.generateKey;
        this.accessKey = builder.accessKey;
        this.secretKey = builder.secretKey;
        this.keyType = builder.keyType;
        this.userCaps = unmodifiableList(Validate.noNullElements(builder.userCaps));
        for (UserCap cap : this.userCaps) {
            Validate.notNull(cap.getType(), "type cannot be null");
            Validate.notNull(cap.getPerm(), "perm cannot be null");
        }
        this.maxBuckets = builder.maxBuckets;
        this.suspended = builder.suspended;
        this.tenant = builder.tenant;
    }

    private static <T> Function<Object, T> getter(Function<ModifyUserRequest, T> g) {
        return obj -> g.apply((ModifyUserRequest) obj);
    }

    private static <T> BiConsumer<Object, T> setter(BiConsumer<Builder, T> s) {
        return (obj, val) -> s.accept((Builder) obj, val);
    }

    public String getUid() {
        return uid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public Boolean isGenerateKey() {
        return generateKey;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public KeyType getKeyType() {
        return keyType;
    }

    public String getKeyTypeAsString() {
        return toString(keyType);
    }

    public List<UserCap> getUserCaps() {
        return userCaps;
    }

    private String getUserCapsAsString() {
        return UserCap.format(userCaps);
    }

    public Integer getMaxBuckets() {
        return maxBuckets;
    }

    public Boolean isSuspended() {
        return suspended;
    }

    public String getTenant() {
        return tenant;
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
        return "ModifyUserRequest{" +
                "uid='" + uid + '\'' +
                ", displayName='" + displayName + '\'' +
                ", email='" + email + '\'' +
                ", generateKey=" + generateKey +
                ", accessKey='" + accessKey + '\'' +
                ", secretKey='" + secretKey + '\'' +
                ", keyType='" + keyType + '\'' +
                ", userCaps=" + userCaps +
                ", maxBuckets=" + maxBuckets +
                ", suspended=" + suspended +
                ", tenant='" + tenant + '\'' +
                "} " + super.toString();
    }

    public static class Builder extends AdminRequestBuilder<Builder, ModifyUserRequest, ModifyUserResponse> {
        private String uid;
        private String displayName;
        private String email;
        private Boolean generateKey;
        private String accessKey;
        private String secretKey;
        private KeyType keyType;
        private List<UserCap> userCaps;
        private Integer maxBuckets;
        private Boolean suspended;
        private String tenant;

        public Builder(AdminClient client) {
            super(client);
            this.userCaps = new LinkedList<>();
        }

        private Builder(ModifyUserRequest request) {
            this.userCaps = new LinkedList<>();
            withUid(request.uid);
            withDisplayName(request.displayName);
            withEmail(request.email);
            withGenerateKey(request.generateKey);
            withAccessKey(request.accessKey);
            withSecretKey(request.secretKey);
            withKeyType(request.keyType);
            withUserCaps(request.userCaps);
            withMaxBuckets(request.maxBuckets);
            withTenant(request.tenant);
        }

        public Builder withUid(String uid) {
            this.uid = uid;
            return self();
        }

        public Builder withDisplayName(String displayName) {
            this.displayName = displayName;
            return self();
        }

        public Builder withEmail(String email) {
            this.email = email;
            return self();
        }

        public Builder withGenerateKey(Boolean generateKey) {
            this.generateKey = generateKey;
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

        public Builder withKeyType(KeyType keyType) {
            this.keyType = keyType;
            return self();
        }

        public Builder withKeyType(String keyType) {
            this.keyType = KeyType.fromString(keyType);
            return self();
        }

        public Builder withUserCaps(List<UserCap> userCaps) {
            this.userCaps.clear();
            this.userCaps.addAll(userCaps);
            return self();
        }

        private Builder withUserCaps(String userCaps) {
            return withUserCaps(UserCap.parse(userCaps));
        }

        public Builder addUserCap(UserCap.Type type, UserCap.Perm perm) {
            this.userCaps.add(new UserCap(type, perm));
            return self();
        }

        public Builder withMaxBuckets(Integer maxBuckets) {
            this.maxBuckets = maxBuckets;
            return self();
        }

        public Builder withSuspended(Boolean suspended) {
            this.suspended = suspended;
            return self();
        }

        public Builder withTenant(String tenant) {
            this.tenant = tenant;
            return self();
        }

        @Override
        public ModifyUserRequest build() {
            return new ModifyUserRequest(this);
        }

        @Override
        public ModifyUserResponse run() {
            return client.modifyUser(build());
        }

        @Override
        public ActionFuture<ModifyUserResponse> execute() {
            return client.modifyUserAsync(build());
        }

        @Override
        public void execute(ActionListener<ModifyUserResponse> listener) {
            client.modifyUserAsync(build(), listener);
        }
    }
}
