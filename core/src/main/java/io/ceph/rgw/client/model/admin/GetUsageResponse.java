package io.ceph.rgw.client.model.admin;

import software.amazon.awssdk.core.SdkField;
import software.amazon.awssdk.core.SdkPojo;
import software.amazon.awssdk.core.protocol.MarshallLocation;
import software.amazon.awssdk.core.protocol.MarshallingType;
import software.amazon.awssdk.core.traits.ListTrait;
import software.amazon.awssdk.core.traits.LocationTrait;
import software.amazon.awssdk.services.s3.model.S3Response;
import software.amazon.awssdk.utils.builder.CopyableBuilder;
import software.amazon.awssdk.utils.builder.ToCopyableBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Created by zhuangshuo on 2020/7/27.
 */
public class GetUsageResponse extends S3Response implements AdminResponse, ToCopyableBuilder<GetUsageResponse.Builder, GetUsageResponse> {
    private static final SdkField<List<Entries>> ENTRIES_FIELD = SdkField
            .<List<Entries>>builder(MarshallingType.LIST)
            .getter(getter(GetUsageResponse::getEntriesList))
            .setter(setter(Builder::withEntriesList))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("usage")
                            .unmarshallLocationName("usage").build(),
                    ListTrait
                            .builder()
                            .memberLocationName("entries")
                            .memberFieldInfo(
                                    SdkField.builder(MarshallingType.SDK_POJO)
                                            .constructor(Entries::new)
                                            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD)
                                                    .locationName("entries").unmarshallLocationName("entries").build()).build())
                            .build()).build();
    private static final SdkField<List<Summary>> SUMMARIES_FIELD = SdkField
            .<List<Summary>>builder(MarshallingType.LIST)
            .getter(getter(GetUsageResponse::getSummaryList))
            .setter(setter(Builder::withSummaryList))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("usage")
                            .unmarshallLocationName("usage").build(),
                    ListTrait
                            .builder()
                            .memberLocationName("summary")
                            .memberFieldInfo(
                                    SdkField.builder(MarshallingType.SDK_POJO)
                                            .constructor(Summary::new)
                                            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD)
                                                    .locationName("summary").unmarshallLocationName("summary").build()).build())
                            .build()).build();
    private static final List<SdkField<?>> SDK_FIELDS = Collections.unmodifiableList(Arrays.asList(ENTRIES_FIELD, SUMMARIES_FIELD));
    private final List<Entries> entriesList;
    private final List<Summary> summaryList;

    private GetUsageResponse(Builder builder) {
        super(builder);
        this.entriesList = unmodifiableList(builder.entriesList);
        this.summaryList = unmodifiableList(builder.summaryList);
    }

    private static <T> List<T> unmodifiableList(List<T> list) {
        return Optional.ofNullable(list).map(Collections::unmodifiableList).orElse(Collections.emptyList());
    }

    private static <T> Function<Object, T> getter(Function<GetUsageResponse, T> g) {
        return obj -> g.apply((GetUsageResponse) obj);
    }

    private static <T> BiConsumer<Object, T> setter(BiConsumer<Builder, T> s) {
        return (obj, val) -> s.accept((Builder) obj, val);
    }

    public List<Entries> getEntriesList() {
        return entriesList;
    }

    public List<Summary> getSummaryList() {
        return summaryList;
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
        return "GetUsageResponse{" +
                "entriesList=" + entriesList +
                ", summaryList=" + summaryList +
                "} " + super.toString();
    }

    public static class Builder extends BuilderImpl implements SdkPojo, CopyableBuilder<Builder, GetUsageResponse> {
        private List<Entries> entriesList;
        private List<Summary> summaryList;

        public Builder() {
        }

        private Builder(GetUsageResponse response) {
            super(response);
            withEntriesList(response.entriesList);
            withSummaryList(response.summaryList);
        }

        private Builder withEntriesList(List<Entries> entriesList) {
            this.entriesList = entriesList;
            return this;
        }

        private Builder withSummaryList(List<Summary> summaryList) {
            this.summaryList = summaryList;
            return this;
        }

        @Override
        public GetUsageResponse build() {
            return new GetUsageResponse(this);
        }

        @Override
        public List<SdkField<?>> sdkFields() {
            return SDK_FIELDS;
        }
    }
}
