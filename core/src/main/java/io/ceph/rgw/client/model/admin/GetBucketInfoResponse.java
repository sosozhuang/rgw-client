package io.ceph.rgw.client.model.admin;

import org.apache.commons.lang3.StringUtils;
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
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by zhuangshuo on 2020/7/28.
 */
public class GetBucketInfoResponse extends S3Response implements AdminResponse, ToCopyableBuilder<GetBucketInfoResponse.Builder, GetBucketInfoResponse> {
    private static final SdkField<List<BucketInfo>> BUCKET_INFO_LIST_FIELD = SdkField
            .<List<BucketInfo>>builder(MarshallingType.LIST)
            .getter(getter(GetBucketInfoResponse::getBucketInfoList))
            .setter(setter(Builder::withBucketInfoList))
            .traits(PayloadTrait.create(),
                    LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("buckets")
                            .unmarshallLocationName("buckets").build(),
                    ListTrait
                            .builder()
                            .memberLocationName("stats")
                            .memberFieldInfo(
                                    SdkField.builder(MarshallingType.SDK_POJO)
                                            .constructor(BucketInfo::new)
                                            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD)
                                                    .locationName("stats").unmarshallLocationName("stats").build()).build())
                            .build()).build();
    private static final SdkField<List<String>> BUCKETS_FIELD = SdkField
            .<List<String>>builder(MarshallingType.LIST)
            .getter(getter(GetBucketInfoResponse::getBuckets))
            .setter(setter(Builder::withBuckets))
            .traits(PayloadTrait.create(),
                    LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("buckets")
                            .unmarshallLocationName("buckets").build(),
                    ListTrait
                            .builder()
                            .memberLocationName("bucket")
                            .memberFieldInfo(
                                    SdkField.builder(MarshallingType.STRING)
                                            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD)
                                                    .locationName("bucket").unmarshallLocationName("bucket").build()).build())
                            .build()).build();
    private static final SdkField<BucketInfo> BUCKET_INFO_FIELD = SdkField
            .<BucketInfo>builder(MarshallingType.SDK_POJO)
            .getter(getter(GetBucketInfoResponse::getBucketInfo))
            .setter(setter(Builder::withBucketInfo))
            .constructor(BucketInfo::new)
            .traits(PayloadTrait.create(),
                    LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("stats")
                            .unmarshallLocationName("stats").build()).build();
    private static final List<SdkField<?>> SDK_FIELDS = Collections.unmodifiableList(Collections.singletonList(BUCKET_INFO_LIST_FIELD));
    private final List<BucketInfo> bucketInfoList;

    private GetBucketInfoResponse(Builder builder) {
        super(builder);
        this.bucketInfoList = AdminRequest.unmodifiableList(builder.bucketInfoList);
    }

    private static <T> Function<Object, T> getter(Function<GetBucketInfoResponse, T> g) {
        return obj -> g.apply((GetBucketInfoResponse) obj);
    }

    private static <T> BiConsumer<Object, T> setter(BiConsumer<Builder, T> s) {
        return (obj, val) -> s.accept((Builder) obj, val);
    }

    public List<BucketInfo> getBucketInfoList() {
        return bucketInfoList;
    }

    private BucketInfo getBucketInfo() {
        return Optional.ofNullable(bucketInfoList).map(l -> l.get(0)).orElse(null);
    }

    private List<String> getBuckets() {
        return bucketInfoList.stream().map(BucketInfo::getBucket).collect(Collectors.toList());
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
        return "GetBucketInfoResponse{" +
                "bucketInfoList=" + bucketInfoList +
                "} " + super.toString();
    }

    public static class Builder extends BuilderImpl implements SdkPojo, CopyableBuilder<Builder, GetBucketInfoResponse> {
        private final List<SdkField<?>> sdkFields;
        private List<BucketInfo> bucketInfoList;

        public Builder(GetBucketInfoRequest request) {
            Objects.requireNonNull(request);
            if (StringUtils.isNotBlank(request.getUid())) {
                this.sdkFields = Collections.singletonList(request.isStats() ? BUCKET_INFO_LIST_FIELD : BUCKETS_FIELD);
            } else {
                this.sdkFields = Collections.singletonList(BUCKET_INFO_FIELD);
            }
        }

        private Builder(GetBucketInfoResponse response) {
            super(response);
            this.sdkFields = Collections.singletonList(BUCKET_INFO_LIST_FIELD);
            withBucketInfoList(response.bucketInfoList);
        }

        private Builder withBucketInfoList(List<BucketInfo> bucketInfoList) {
            this.bucketInfoList = bucketInfoList;
            return this;
        }

        private Builder withBucketInfo(BucketInfo bucketInfoList) {
            return withBucketInfoList(Collections.singletonList(bucketInfoList));
        }

        private Builder withBuckets(List<String> buckets) {
            if (buckets == null || buckets.size() == 0) {
                return this;
            }
            return withBucketInfoList(buckets.stream().map(bucket -> new BucketInfo().setBucket(bucket)).collect(Collectors.toList()));
        }

        @Override
        public List<SdkField<?>> sdkFields() {
            return sdkFields;
        }

        @Override
        public GetBucketInfoResponse build() {
            return new GetBucketInfoResponse(this);
        }
    }
}
