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
public class CheckBucketIndexResponse extends S3Response implements AdminResponse, ToCopyableBuilder<CheckBucketIndexResponse.Builder, CheckBucketIndexResponse> {
    private static final SdkField<List<String>> INVALID_MULTIPART_ENTRIES_FIELD = SdkField
            .<List<String>>builder(MarshallingType.LIST)
            .getter(getter(CheckBucketIndexResponse::getInvalidMultipartEntries))
            .setter(setter(Builder::withInvalidMultipartEntries))
            .traits(PayloadTrait.create(),
                    LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("invalid_multipart_entries")
                            .unmarshallLocationName("invalid_multipart_entries").build(),
                    ListTrait
                            .builder()
                            .memberLocationName("object")
                            .memberFieldInfo(
                                    SdkField.builder(MarshallingType.STRING)
                                            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD)
                                                    .locationName("object").unmarshallLocationName("object").build()).build())
                            .build()).build();
    private static final List<SdkField<?>> SDK_FIELDS = Collections.unmodifiableList(Collections.singletonList(INVALID_MULTIPART_ENTRIES_FIELD));
    private final List<String> invalidMultipartEntries;

    private CheckBucketIndexResponse(Builder builder) {
        super(builder);
        this.invalidMultipartEntries = AdminRequest.unmodifiableList(builder.invalidMultipartEntries);
    }

    private static <T> Function<Object, T> getter(Function<CheckBucketIndexResponse, T> g) {
        return obj -> g.apply((CheckBucketIndexResponse) obj);
    }

    private static <T> BiConsumer<Object, T> setter(BiConsumer<Builder, T> s) {
        return (obj, val) -> s.accept((Builder) obj, val);
    }

    public List<String> getInvalidMultipartEntries() {
        return invalidMultipartEntries;
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
        return "CheckBucketIndexResponse{" +
                "invalidMultipartEntries=" + invalidMultipartEntries +
                "} " + super.toString();
    }

    public static class Builder extends BuilderImpl implements SdkPojo, CopyableBuilder<Builder, CheckBucketIndexResponse> {
        private List<String> invalidMultipartEntries;

        public Builder() {
        }

        private Builder(CheckBucketIndexResponse response) {
            super(response);
            withInvalidMultipartEntries(response.invalidMultipartEntries);
        }

        private Builder withInvalidMultipartEntries(List<String> invalidMultipartEntries) {
            this.invalidMultipartEntries = invalidMultipartEntries;
            return this;
        }

        @Override
        public List<SdkField<?>> sdkFields() {
            return SDK_FIELDS;
        }

        @Override
        public CheckBucketIndexResponse build() {
            return new CheckBucketIndexResponse(this);
        }
    }
}
