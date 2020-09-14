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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Created by zhuangshuo on 2020/7/28.
 */
public class CreateKeyResponse extends S3Response implements AdminResponse, ToCopyableBuilder<CreateKeyResponse.Builder, CreateKeyResponse> {
    private static final SdkField<List<S3Credential>> S3_KEYS_FIELD = SdkField
            .<List<S3Credential>>builder(MarshallingType.LIST)
            .getter(getter(CreateKeyResponse::getS3Credentials))
            .setter(setter(Builder::withS3Credentials))
            .traits(PayloadTrait.create(),
                    LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("keys")
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
    private static final SdkField<List<SwiftCredential>> SWIFT_KEYS_FIELD = SdkField
            .<List<SwiftCredential>>builder(MarshallingType.LIST)
            .getter(getter(CreateKeyResponse::getSwiftCredentials))
            .setter(setter(Builder::withSwiftCredentials))
            .traits(PayloadTrait.create(),
                    LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("swift_keys")
                            .unmarshallLocationName("swift_keys").build(),
                    ListTrait
                            .builder()
                            .memberLocationName("key")
                            .memberFieldInfo(
                                    SdkField.builder(MarshallingType.SDK_POJO)
                                            .constructor(SwiftCredential::new)
                                            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD)
                                                    .locationName("key").unmarshallLocationName("key").build()).build())
                            .build()).build();
    private static final List<SdkField<?>> SDK_FIELDS = Collections.unmodifiableList(Arrays.asList(S3_KEYS_FIELD, SWIFT_KEYS_FIELD));
    private final List<S3Credential> s3Credentials;
    private final List<SwiftCredential> swiftCredentials;

    private CreateKeyResponse(Builder builder) {
        super(builder);
        this.s3Credentials = AdminRequest.unmodifiableList(builder.s3Credentials);
        this.swiftCredentials = AdminRequest.unmodifiableList(builder.swiftCredentials);
    }

    private static <T> Function<Object, T> getter(Function<CreateKeyResponse, T> g) {
        return obj -> g.apply((CreateKeyResponse) obj);
    }

    private static <T> BiConsumer<Object, T> setter(BiConsumer<Builder, T> s) {
        return (obj, val) -> s.accept((Builder) obj, val);
    }

    public List<S3Credential> getS3Credentials() {
        return s3Credentials;
    }

    public List<SwiftCredential> getSwiftCredentials() {
        return swiftCredentials;
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
        return "CreateKeyResponse{" +
                "s3Credentials=" + s3Credentials +
                ", swiftCredentials=" + swiftCredentials +
                "} " + super.toString();
    }

    public static class Builder extends BuilderImpl implements SdkPojo, CopyableBuilder<Builder, CreateKeyResponse> {
        private final List<SdkField<?>> sdkFields;
        private List<S3Credential> s3Credentials;
        private List<SwiftCredential> swiftCredentials;

        public Builder(CreateKeyRequest request) {
            if (request.getKeyType() == KeyType.S3) {
                this.sdkFields = Collections.singletonList(S3_KEYS_FIELD);
            } else {
                this.sdkFields = Collections.singletonList(SWIFT_KEYS_FIELD);
            }
        }

        private Builder(CreateKeyResponse response) {
            super(response);
            this.sdkFields = Collections.emptyList();
            withS3Credentials(response.s3Credentials);
            withSwiftCredentials(response.swiftCredentials);
        }

        private Builder withS3Credentials(List<S3Credential> s3Credentials) {
            this.s3Credentials = s3Credentials;
            return this;
        }

        private Builder withSwiftCredentials(List<SwiftCredential> swiftCredentials) {
            this.swiftCredentials = swiftCredentials;
            return this;
        }

        @Override
        public List<SdkField<?>> sdkFields() {
            return sdkFields;
        }

        @Override
        public CreateKeyResponse build() {
            return new CreateKeyResponse(this);
        }
    }
}
