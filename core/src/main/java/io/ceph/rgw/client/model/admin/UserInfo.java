package io.ceph.rgw.client.model.admin;

import software.amazon.awssdk.core.SdkField;
import software.amazon.awssdk.core.SdkPojo;
import software.amazon.awssdk.core.protocol.MarshallLocation;
import software.amazon.awssdk.core.protocol.MarshallingType;
import software.amazon.awssdk.core.traits.ListTrait;
import software.amazon.awssdk.core.traits.LocationTrait;
import software.amazon.awssdk.utils.builder.Buildable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Created by zhuangshuo on 2020/8/3.
 */
public class UserInfo implements SdkPojo, Buildable {
    private static final SdkField<String> USER_ID_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(UserInfo::getUserId))
            .setter(setter(UserInfo::setUserId))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("user_id")
                    .unmarshallLocationName("user_id").build()).build();
    private static final SdkField<String> DISPLAY_NAME_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(UserInfo::getDisplayName))
            .setter(setter(UserInfo::setDisplayName))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("display_name")
                    .unmarshallLocationName("display_name").build()).build();
    private static final SdkField<String> EMAIL_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(UserInfo::getEmail))
            .setter(setter(UserInfo::setEmail))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("email")
                    .unmarshallLocationName("email").build()).build();
    private static final SdkField<Boolean> SUSPENDED_FIELD = SdkField
            .builder(MarshallingType.BOOLEAN)
            .getter(getter(UserInfo::isSuspended))
            .setter(setter(UserInfo::setSuspended))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("suspended")
                    .unmarshallLocationName("suspended").build()).build();
    private static final SdkField<Integer> MAX_BUCKETS_FIELD = SdkField
            .builder(MarshallingType.INTEGER)
            .getter(getter(UserInfo::getMaxBuckets))
            .setter(setter(UserInfo::setMaxBuckets))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("max_buckets")
                    .unmarshallLocationName("max_buckets").build()).build();
    private static final SdkField<List<SubUser>> SUB_USERS_FIELD = SdkField
            .<List<SubUser>>builder(MarshallingType.LIST)
            .getter(getter(UserInfo::getSubUsers))
            .setter(setter(UserInfo::setSubUsers))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("subusers")
                            .unmarshallLocationName("subusers").build(),
                    ListTrait
                            .builder()
                            .memberLocationName("subuser")
                            .memberFieldInfo(
                                    SdkField.builder(MarshallingType.SDK_POJO)
                                            .constructor(SubUser::new)
                                            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD)
                                                    .locationName("subuser").unmarshallLocationName("subuser").build()).build())
                            .build()).build();
    private static final SdkField<List<S3Credential>> S3_CREDENTIALS_FIELD = SdkField
            .<List<S3Credential>>builder(MarshallingType.LIST)
            .getter(getter(UserInfo::getS3Credentials))
            .setter(setter(UserInfo::setS3Credentials))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("keys")
                            .unmarshallLocationName("keys").build(),
                    ListTrait
                            .builder()
                            .memberLocationName("key")
                            .memberFieldInfo(
                                    SdkField.builder(MarshallingType.SDK_POJO)
                                            .constructor(S3Credential::new)
                                            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD)
                                                    .locationName("key").unmarshallLocationName("key").build()).build())
                            .build()).build();
    private static final SdkField<List<SwiftCredential>> SWIFT_CREDENTIALS_FIELD = SdkField
            .<List<SwiftCredential>>builder(MarshallingType.LIST)
            .getter(getter(UserInfo::getSwiftCredentials))
            .setter(setter(UserInfo::setSwiftCredentials))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("swift_keys")
                            .unmarshallLocationName("swift_keys").build(),
                    ListTrait
                            .builder()
                            .memberLocationName("swift_keys")
                            .memberFieldInfo(
                                    SdkField.builder(MarshallingType.SDK_POJO)
                                            .constructor(SwiftCredential::new)
                                            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD)
                                                    .locationName("key").unmarshallLocationName("key").build()).build())
                            .build()).build();
    private static final SdkField<List<UserCap>> USER_CAPS_FIELD = SdkField
            .<List<UserCap>>builder(MarshallingType.LIST)
            .getter(getter(UserInfo::getUserCaps))
            .setter(setter(UserInfo::setUserCaps))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("caps")
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
    private static final SdkField<String> OP_MASK_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(UserInfo::getOpMask))
            .setter(setter(UserInfo::setOpMask))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("op_mask")
                    .unmarshallLocationName("op_mask").build()).build();
    private static final SdkField<Boolean> SYSTEM_FIELD = SdkField
            .builder(MarshallingType.BOOLEAN)
            .getter(getter(UserInfo::isSystem))
            .setter(setter(UserInfo::setSystem))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("system")
                    .unmarshallLocationName("system").build()).build();
    private static final SdkField<Boolean> ADMIN_FIELD = SdkField
            .builder(MarshallingType.BOOLEAN)
            .getter(getter(UserInfo::isAdmin))
            .setter(setter(UserInfo::setAdmin))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("admin")
                    .unmarshallLocationName("admin").build()).build();
    private static final SdkField<String> TENANT_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(UserInfo::getTenant))
            .setter(setter(UserInfo::setTenant))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("tenant")
                    .unmarshallLocationName("tenant").build()).build();
    private static final SdkField<Quota> BUCKET_QUOTA_FIELD = SdkField
            .<Quota>builder(MarshallingType.SDK_POJO)
            .getter(getter(UserInfo::getBucketQuota))
            .setter(setter(UserInfo::setBucketQuota))
            .constructor(Quota::new)
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("bucket_quota")
                    .unmarshallLocationName("bucket_quota").build()).build();
    private static final SdkField<Quota> USER_QUOTA_FIELD = SdkField
            .<Quota>builder(MarshallingType.SDK_POJO)
            .getter(getter(UserInfo::getUserQuota))
            .setter(setter(UserInfo::setUserQuota))
            .constructor(Quota::new)
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("user_quota")
                    .unmarshallLocationName("user_quota").build()).build();
    private static final SdkField<String> TYPE_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(UserInfo::getType))
            .setter(setter(UserInfo::setType))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("type")
                    .unmarshallLocationName("type").build()).build();
    private static final List<SdkField<?>> SDK_FIELDS = Collections.unmodifiableList(Arrays.asList(USER_ID_FIELD, DISPLAY_NAME_FIELD, EMAIL_FIELD, SUSPENDED_FIELD, MAX_BUCKETS_FIELD,
            SUB_USERS_FIELD, S3_CREDENTIALS_FIELD, SWIFT_CREDENTIALS_FIELD, USER_CAPS_FIELD, OP_MASK_FIELD, SYSTEM_FIELD, ADMIN_FIELD, TENANT_FIELD, BUCKET_QUOTA_FIELD, USER_QUOTA_FIELD, TYPE_FIELD));
    private String userId;
    private String displayName;
    private String email;
    private Boolean suspended;
    private Integer maxBuckets;
    private List<SubUser> subUsers;
    private List<S3Credential> s3Credentials;
    private List<SwiftCredential> swiftCredentials;
    private List<UserCap> userCaps;
    private String opMask;
    private Boolean system;
    private Boolean admin;
    private String tenant;
    private Quota bucketQuota;
    private Quota userQuota;
    private String type;

    UserInfo() {
    }

    private static <T> Function<Object, T> getter(Function<UserInfo, T> g) {
        return obj -> g.apply((UserInfo) obj);
    }

    private static <T> BiConsumer<Object, T> setter(BiConsumer<UserInfo, T> s) {
        return (obj, val) -> s.accept((UserInfo) obj, val);
    }

    public String getUserId() {
        return userId;
    }

    private void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    private void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    private void setEmail(String email) {
        this.email = email;
    }

    public Boolean isSuspended() {
        return suspended;
    }

    private void setSuspended(Boolean suspended) {
        this.suspended = suspended;
    }

    public Integer getMaxBuckets() {
        return maxBuckets;
    }

    private void setMaxBuckets(Integer maxBuckets) {
        this.maxBuckets = maxBuckets;
    }

    public List<SubUser> getSubUsers() {
        return subUsers;
    }

    private void setSubUsers(List<SubUser> subUsers) {
        this.subUsers = AdminRequest.unmodifiableList(subUsers);
    }

    public List<S3Credential> getS3Credentials() {
        return s3Credentials;
    }

    private void setS3Credentials(List<S3Credential> s3Credentials) {
        this.s3Credentials = AdminRequest.unmodifiableList(s3Credentials);
    }

    public List<SwiftCredential> getSwiftCredentials() {
        return swiftCredentials;
    }

    private void setSwiftCredentials(List<SwiftCredential> swiftCredentials) {
        this.swiftCredentials = AdminRequest.unmodifiableList(swiftCredentials);
    }

    public List<UserCap> getUserCaps() {
        return userCaps;
    }

    private void setUserCaps(List<UserCap> userCaps) {
        this.userCaps = AdminRequest.unmodifiableList(userCaps);
    }

    public String getOpMask() {
        return opMask;
    }

    private void setOpMask(String opMask) {
        this.opMask = opMask;
    }

    public Boolean isSystem() {
        return system;
    }

    private void setSystem(Boolean system) {
        this.system = system;
    }

    public Boolean isAdmin() {
        return admin;
    }

    private void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public String getTenant() {
        return tenant;
    }

    private void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public Quota getBucketQuota() {
        return bucketQuota;
    }

    private void setBucketQuota(Quota bucketQuota) {
        this.bucketQuota = bucketQuota;
    }

    public Quota getUserQuota() {
        return userQuota;
    }

    private void setUserQuota(Quota userQuota) {
        this.userQuota = userQuota;
    }

    public String getType() {
        return type;
    }

    private void setType(String type) {
        this.type = type;
    }

    @Override
    public List<SdkField<?>> sdkFields() {
        return SDK_FIELDS;
    }

    @Override
    public Object build() {
        return this;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "userId='" + userId + '\'' +
                ", displayName='" + displayName + '\'' +
                ", email='" + email + '\'' +
                ", suspended=" + suspended +
                ", maxBuckets=" + maxBuckets +
                ", subUsers=" + subUsers +
                ", s3Credentials=" + s3Credentials +
                ", swiftCredentials=" + swiftCredentials +
                ", userCaps=" + userCaps +
                ", opMask='" + opMask + '\'' +
                ", system=" + system +
                ", admin=" + admin +
                ", tenant='" + tenant + '\'' +
                ", bucketQuota=" + bucketQuota +
                ", userQuota=" + userQuota +
                ", type='" + type + '\'' +
                '}';
    }
}
