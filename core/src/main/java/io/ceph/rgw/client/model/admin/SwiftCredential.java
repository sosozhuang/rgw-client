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
public class SwiftCredential implements SdkPojo, Buildable {
    private static final SdkField<String> USER_NAME_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(SwiftCredential::getUserName))
            .setter(setter(SwiftCredential::setUserName))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("user")
                    .unmarshallLocationName("user").build()).build();
    private static final SdkField<String> SECRET_KEY_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(SwiftCredential::getSecretKey))
            .setter(setter(SwiftCredential::setSecretKey))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("secret_key")
                    .unmarshallLocationName("secret_key").build()).build();

    private static final List<SdkField<?>> SDK_FIELDS = Collections.unmodifiableList(Arrays.asList(USER_NAME_FIELD, SECRET_KEY_FIELD));

    private String userName;
    private String secretKey;

    SwiftCredential() {
    }

    public String getUserName() {
        return userName;
    }

    private void setUserName(String userName) {
        this.userName = userName;
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

    private static <T> Function<Object, T> getter(Function<SwiftCredential, T> g) {
        return obj -> g.apply((SwiftCredential) obj);
    }

    private static <T> BiConsumer<Object, T> setter(BiConsumer<SwiftCredential, T> s) {
        return (obj, val) -> s.accept((SwiftCredential) obj, val);
    }

    @Override
    public String toString() {
        return "SwiftCredential{" +
                "userName='" + userName + '\'' +
                ", secretKey='" + secretKey + '\'' +
                '}';
    }

    @Override
    public Object build() {
        return this;
    }
}
