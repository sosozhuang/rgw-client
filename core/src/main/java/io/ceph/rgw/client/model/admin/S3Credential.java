package io.ceph.rgw.client.model.admin;

import software.amazon.awssdk.core.SdkField;
import software.amazon.awssdk.core.SdkPojo;
import software.amazon.awssdk.core.protocol.MarshallLocation;
import software.amazon.awssdk.core.protocol.MarshallingType;
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
public class S3Credential implements SdkPojo, Buildable {
    private static final SdkField<String> USER_ID_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(S3Credential::getUserId))
            .setter(setter(S3Credential::setUserId))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("user")
                    .unmarshallLocationName("user").build()).build();
    private static final SdkField<String> ACCESS_KEY_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(S3Credential::getAccessKey))
            .setter(setter(S3Credential::setAccessKey))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("access_key")
                    .unmarshallLocationName("access_key").build()).build();

    private static final SdkField<String> SECRET_KEY_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(S3Credential::getSecretKey))
            .setter(setter(S3Credential::setSecretKey))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("secret_key")
                    .unmarshallLocationName("secret_key").build()).build();
    private static final List<SdkField<?>> SDK_FIELDS = Collections.unmodifiableList(Arrays.asList(USER_ID_FIELD, ACCESS_KEY_FIELD, SECRET_KEY_FIELD));

    private String userId;
    private String accessKey;
    private String secretKey;

    S3Credential() {
    }

    public String getUserId() {
        return userId;
    }

    private void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccessKey() {
        return accessKey;
    }

    private void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    private void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public List<SdkField<?>> sdkFields() {
        return SDK_FIELDS;
    }

    private static <T> Function<Object, T> getter(Function<S3Credential, T> g) {
        return obj -> g.apply((S3Credential) obj);
    }

    private static <T> BiConsumer<Object, T> setter(BiConsumer<S3Credential, T> s) {
        return (obj, val) -> s.accept((S3Credential) obj, val);
    }

    @Override
    public String toString() {
        return "S3Credential{" +
                "userId='" + userId + '\'' +
                ", accessKey='" + accessKey + '\'' +
                ", secretKey='" + secretKey + '\'' +
                '}';
    }

    @Override
    public Object build() {
        return this;
    }
}
