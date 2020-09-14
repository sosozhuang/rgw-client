package io.ceph.rgw.client.model.admin;

import software.amazon.awssdk.core.SdkField;
import software.amazon.awssdk.core.SdkPojo;
import software.amazon.awssdk.core.protocol.MarshallLocation;
import software.amazon.awssdk.core.protocol.MarshallingType;
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
public class GetBucketQuotaResponse extends S3Response implements AdminResponse, ToCopyableBuilder<GetBucketQuotaResponse.Builder, GetBucketQuotaResponse> {
    private static final SdkField<Quota> BUCKET_QUOTA_FIELD = SdkField
            .<Quota>builder(MarshallingType.SDK_POJO)
            .getter(getter(GetBucketQuotaResponse::getQuota))
            .setter(setter(Builder::withQuota))
            .constructor(Quota::new)
            .traits(PayloadTrait.create(),
                    LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("bucket_quota")
                            .unmarshallLocationName("bucket_quota").build()).build();
    private static final List<SdkField<?>> SDK_FIELDS = Collections.unmodifiableList(Collections.singletonList(BUCKET_QUOTA_FIELD));
    private final Quota quota;

    private GetBucketQuotaResponse(Builder builder) {
        super(builder);
        this.quota = builder.quota;
    }

    private static <T> Function<Object, T> getter(Function<GetBucketQuotaResponse, T> g) {
        return obj -> g.apply((GetBucketQuotaResponse) obj);
    }

    private static <T> BiConsumer<Object, T> setter(BiConsumer<Builder, T> s) {
        return (obj, val) -> s.accept((Builder) obj, val);
    }

    public Quota getQuota() {
        return quota;
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
        return "GetBucketQuotaResponse{" +
                "quota=" + quota +
                "} " + super.toString();
    }

    public static class Builder extends BuilderImpl implements SdkPojo, CopyableBuilder<Builder, GetBucketQuotaResponse> {
        private Quota quota;

        public Builder() {
        }

        private Builder(GetBucketQuotaResponse response) {
            super(response);
            withQuota(response.quota);
        }

        private Builder withQuota(Quota quota) {
            this.quota = quota;
            return this;
        }

        @Override
        public List<SdkField<?>> sdkFields() {
            return SDK_FIELDS;
        }

        @Override
        public GetBucketQuotaResponse build() {
            return new GetBucketQuotaResponse(this);
        }
    }
}
