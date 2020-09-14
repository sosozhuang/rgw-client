package io.ceph.rgw.client.model.admin;

import io.ceph.rgw.client.AdminClient;
import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;
import org.apache.commons.lang3.StringUtils;
import software.amazon.awssdk.core.SdkField;
import software.amazon.awssdk.core.protocol.MarshallLocation;
import software.amazon.awssdk.core.protocol.MarshallingType;
import software.amazon.awssdk.core.traits.LocationTrait;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Created by zhuangshuo on 2020/7/28.
 */
public class GetBucketInfoRequest extends AdminRequest {
    private static final SdkField<String> BUCKET_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(GetBucketInfoRequest::getBucket))
            .setter(setter(Builder::withBucket))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("bucket")
                    .unmarshallLocationName("bucket").build()).build();
    private static final SdkField<String> UID_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(GetBucketInfoRequest::getUid))
            .setter(setter(Builder::withUid))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("uid")
                    .unmarshallLocationName("uid").build()).build();
    private static final SdkField<Boolean> STATS_FIELD = SdkField
            .builder(MarshallingType.BOOLEAN)
            .getter(getter(GetBucketInfoRequest::isStats))
            .setter(setter(Builder::withStats))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("stats")
                    .unmarshallLocationName("stats").build()).build();
    private static final List<SdkField<?>> SDK_FIELDS = Collections.unmodifiableList(Arrays.asList(BUCKET_FIELD, UID_FIELD, STATS_FIELD));
    private final String bucket;
    private final String uid;
    private final Boolean stats;

    public GetBucketInfoRequest(Builder builder) {
        super(builder);
        if (StringUtils.isBlank(builder.bucket) && StringUtils.isBlank(builder.uid)) {
            throw new IllegalArgumentException("both bucket and uid not set");
        }
        this.bucket = builder.bucket;
        this.uid = builder.uid;
        this.stats = builder.stats;
    }

    private static <T> Function<Object, T> getter(Function<GetBucketInfoRequest, T> g) {
        return obj -> g.apply((GetBucketInfoRequest) obj);
    }

    private static <T> BiConsumer<Object, T> setter(BiConsumer<Builder, T> s) {
        return (obj, val) -> s.accept((Builder) obj, val);
    }

    @Override
    public Builder toBuilder() {
        return new Builder(this);
    }

    public String getBucket() {
        return bucket;
    }

    public String getUid() {
        return uid;
    }

    public Boolean isStats() {
        return stats;
    }

    @Override
    public List<SdkField<?>> sdkFields() {
        return SDK_FIELDS;
    }

    @Override
    public String toString() {
        return "GetBucketInfoRequest{" +
                "bucket='" + bucket + '\'' +
                ", uid='" + uid + '\'' +
                ", stats=" + stats +
                "} " + super.toString();
    }

    public static class Builder extends AdminRequestBuilder<Builder, GetBucketInfoRequest, GetBucketInfoResponse> {
        private String bucket;
        private String uid;
        private Boolean stats;

        public Builder(AdminClient client) {
            super(client);
        }

        private Builder(GetBucketInfoRequest request) {
            withBucket(request.bucket);
            withUid(request.uid);
            withStats(request.stats);
        }

        public Builder withBucket(String bucket) {
            this.bucket = bucket;
            return self();
        }

        public Builder withUid(String uid) {
            this.uid = uid;
            return self();
        }

        public Builder withStats(Boolean stats) {
            this.stats = stats;
            return self();
        }

        @Override
        public GetBucketInfoRequest build() {
            return new GetBucketInfoRequest(this);
        }

        @Override
        public GetBucketInfoResponse run() {
            return client.getBucketInfo(build());
        }

        @Override
        public ActionFuture<GetBucketInfoResponse> execute() {
            return client.getBucketInfoAsync(build());
        }

        @Override
        public void execute(ActionListener<GetBucketInfoResponse> listener) {
            client.getBucketInfoAsync(build(), listener);
        }
    }
}
