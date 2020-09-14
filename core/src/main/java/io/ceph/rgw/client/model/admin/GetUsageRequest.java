package io.ceph.rgw.client.model.admin;

import io.ceph.rgw.client.AdminClient;
import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;
import software.amazon.awssdk.core.SdkField;
import software.amazon.awssdk.core.protocol.MarshallLocation;
import software.amazon.awssdk.core.protocol.MarshallingType;
import software.amazon.awssdk.core.traits.LocationTrait;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Created by zhuangshuo on 2020/7/27.
 */
public class GetUsageRequest extends AdminRequest {
    private static final SdkField<String> UID_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(GetUsageRequest::getUid))
            .setter(setter(Builder::withUid))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("uid")
                    .unmarshallLocationName("uid").build()).build();
    private static final SdkField<String> START_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(GetUsageRequest::getStartAsString))
            .setter(setter(Builder::withStart))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("start")
                    .unmarshallLocationName("start").build()).build();
    private static final SdkField<String> END_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(GetUsageRequest::getEndAsString))
            .setter(setter(Builder::withEnd))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("end")
                    .unmarshallLocationName("end").build()).build();
    private static final SdkField<Boolean> SHOW_ENTRIES_FIELD = SdkField
            .builder(MarshallingType.BOOLEAN)
            .getter(getter(GetUsageRequest::isShowEntries))
            .setter(setter(Builder::withShowEntries))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("show-entries")
                    .unmarshallLocationName("show-entries").build()).build();
    private static final SdkField<Boolean> SHOW_SUMMARY_FIELD = SdkField
            .builder(MarshallingType.BOOLEAN)
            .getter(getter(GetUsageRequest::isShowSummary))
            .setter(setter(Builder::withShowSummary))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("show-summary")
                    .unmarshallLocationName("show-summary").build()).build();
    private static final List<SdkField<?>> SDK_FIELDS = Collections.unmodifiableList(Arrays.asList(UID_FIELD, START_FIELD, END_FIELD, SHOW_ENTRIES_FIELD, SHOW_SUMMARY_FIELD));
    private final String uid;
    private final Date start;
    private final Date end;
    private final Boolean showEntries;
    private final Boolean showSummary;

    public GetUsageRequest(Builder builder) {
        super(builder);
        this.uid = builder.uid;
        this.start = builder.start;
        this.end = builder.end;
        this.showEntries = builder.showEntries;
        this.showSummary = builder.showSummary;
    }

    private static <T> Function<Object, T> getter(Function<GetUsageRequest, T> g) {
        return obj -> g.apply((GetUsageRequest) obj);
    }

    private static <T> BiConsumer<Object, T> setter(BiConsumer<Builder, T> s) {
        return (obj, val) -> s.accept((Builder) obj, val);
    }

    public String getUid() {
        return uid;
    }

    public Date getStart() {
        return start;
    }

    private String getStartAsString() {
        return dateToString(start);
    }

    public Date getEnd() {
        return end;
    }

    private String getEndAsString() {
        return dateToString(end);
    }

    public Boolean isShowEntries() {
        return showEntries;
    }

    public Boolean isShowSummary() {
        return showSummary;
    }

    @Override
    public String toString() {
        return "GetUsageRequest{" +
                "uid='" + uid + '\'' +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
                ", showEntries=" + showEntries +
                ", showSummary=" + showSummary +
                "} " + super.toString();
    }

    @Override
    public Builder toBuilder() {
        return new Builder(this);
    }

    @Override
    public List<SdkField<?>> sdkFields() {
        return SDK_FIELDS;
    }

    public static class Builder extends AdminRequestBuilder<Builder, GetUsageRequest, GetUsageResponse> {
        private String uid;
        private Date start;
        private Date end;
        private Boolean showEntries;
        private Boolean showSummary;

        public Builder(AdminClient client) {
            super(client);
        }

        private Builder(GetUsageRequest request) {
            withUid(request.uid);
            withStart(request.start);
            withEnd(request.end);
            withShowEntries(request.showEntries);
            withShowSummary(request.showSummary);
        }

        public Builder withUid(String uid) {
            this.uid = uid;
            return self();
        }

        public Builder withStart(Date start) {
            this.start = start;
            return self();
        }

        private Builder withStart(String start) {
            return withStart(stringToDate(start));
        }

        public Builder withEnd(Date end) {
            this.end = end;
            return self();
        }

        private Builder withEnd(String end) {
            return withEnd(stringToDate(end));
        }

        public Builder withShowEntries(Boolean showEntries) {
            this.showEntries = showEntries;
            return self();
        }

        public Builder withShowSummary(Boolean showSummary) {
            this.showSummary = showSummary;
            return self();
        }

        @Override
        public GetUsageRequest build() {
            return new GetUsageRequest(this);
        }

        @Override
        public GetUsageResponse run() {
            return client.getUsage(build());
        }

        @Override
        public ActionFuture<GetUsageResponse> execute() {
            return client.getUsageAsync(build());
        }

        @Override
        public void execute(ActionListener<GetUsageResponse> listener) {
            client.getUsageAsync(build(), listener);
        }
    }
}
