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
public class TrimUsageRequest extends AdminRequest {
    private static final SdkField<String> UID_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(TrimUsageRequest::getUid))
            .setter(setter(Builder::withUid))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("uid")
                    .unmarshallLocationName("uid").build()).build();
    private static final SdkField<String> START_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(TrimUsageRequest::getStartAsString))
            .setter(setter(Builder::withStart))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("start")
                    .unmarshallLocationName("start").build()).build();
    private static final SdkField<String> END_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(TrimUsageRequest::getEndAsString))
            .setter(setter(Builder::withEnd))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("end")
                    .unmarshallLocationName("end").build()).build();
    private static final SdkField<Boolean> REMOVE_ALL_FIELD = SdkField
            .builder(MarshallingType.BOOLEAN)
            .getter(getter(TrimUsageRequest::isRemoveAll))
            .setter(setter(Builder::withRemoveAll))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("remove-all")
                    .unmarshallLocationName("remove-all").build()).build();

    private static final List<SdkField<?>> SDK_FIELDS = Collections.unmodifiableList(Arrays.asList(UID_FIELD, START_FIELD, END_FIELD, REMOVE_ALL_FIELD));

    private final String uid;
    private final Date start;
    private final Date end;
    private final boolean removeAll;

    private TrimUsageRequest(Builder builder) {
        super(builder);
        this.uid = builder.uid;
        this.start = builder.start;
        this.end = builder.end;
        this.removeAll = builder.removeAll;
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

    public boolean isRemoveAll() {
        return removeAll;
    }

    @Override
    public String toString() {
        return "TrimUsageRequest{" +
                "uid='" + uid + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", removeAll=" + removeAll +
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

    private static <T> Function<Object, T> getter(Function<TrimUsageRequest, T> g) {
        return obj -> g.apply((TrimUsageRequest) obj);
    }

    private static <T> BiConsumer<Object, T> setter(BiConsumer<Builder, T> s) {
        return (obj, val) -> s.accept((Builder) obj, val);
    }

    public static class Builder extends AdminRequestBuilder<Builder, TrimUsageRequest, TrimUsageResponse> {
        private String uid;
        private Date start;
        private Date end;
        private boolean removeAll;

        public Builder(AdminClient client) {
            super(client);
            this.removeAll = true;
        }

        private Builder(TrimUsageRequest request) {
            withUid(request.getUid());
            withStart(stringToDate(request.getStartAsString()));
            withEnd(stringToDate(request.getEndAsString()));
            withRemoveAll(request.isRemoveAll());
        }

        public Builder withUid(String uid) {
            this.uid = uid;
            return self();
        }

        public Builder withStart(Date start) {
            this.start = start;
            return self();
        }

        public Builder withStart(String start) {
            this.start = stringToDate(start);
            return self();
        }

        public Builder withEnd(Date end) {
            this.end = end;
            return self();
        }

        public Builder withEnd(String end) {
            this.end = stringToDate(end);
            return self();
        }

        public Builder withRemoveAll(boolean removeAll) {
            this.removeAll = removeAll;
            return self();
        }

        @Override
        public TrimUsageRequest build() {
            return new TrimUsageRequest(this);
        }

        @Override
        public TrimUsageResponse run() {
            return client.trimUsage(build());
        }

        @Override
        public ActionFuture<TrimUsageResponse> execute() {
            return client.trimUsageAsync(build());
        }

        @Override
        public void execute(ActionListener<TrimUsageResponse> listener) {
            client.trimUsageAsync(build(), listener);
        }
    }
}
