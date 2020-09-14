package io.ceph.rgw.client.model.admin;

import io.ceph.rgw.client.AdminClient;
import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;
import org.apache.commons.lang3.Validate;
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
public class CheckBucketIndexRequest extends AdminRequest {
    private static final SdkField<String> BUCKET_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(CheckBucketIndexRequest::getBucket))
            .setter(setter(Builder::withBucket))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("bucket")
                    .unmarshallLocationName("bucket").build()).build();
    private static final SdkField<Boolean> CHECK_OBJECTS_FIELD = SdkField
            .builder(MarshallingType.BOOLEAN)
            .getter(getter(CheckBucketIndexRequest::isCheckObjects))
            .setter(setter(Builder::withCheckObjects))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("check-objects")
                    .unmarshallLocationName("check-objects").build()).build();
    private static final SdkField<Boolean> FIX_FIELD = SdkField
            .builder(MarshallingType.BOOLEAN)
            .getter(getter(CheckBucketIndexRequest::isFix))
            .setter(setter(Builder::withFix))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("fix")
                    .unmarshallLocationName("fix").build()).build();
    private static final List<SdkField<?>> SDK_FIELDS = Collections.unmodifiableList(Arrays.asList(BUCKET_FIELD, CHECK_OBJECTS_FIELD, FIX_FIELD));
    private final String bucket;
    private final Boolean checkObjects;
    private final Boolean fix;

    public CheckBucketIndexRequest(Builder builder) {
        super(builder);
        this.bucket = Validate.notBlank(builder.bucket, "bucket cannot be empty string");
        this.checkObjects = builder.checkObjects;
        this.fix = builder.fix;
    }

    private static <T> Function<Object, T> getter(Function<CheckBucketIndexRequest, T> g) {
        return obj -> g.apply((CheckBucketIndexRequest) obj);
    }

    private static <T> BiConsumer<Object, T> setter(BiConsumer<Builder, T> s) {
        return (obj, val) -> s.accept((Builder) obj, val);
    }

    public String getBucket() {
        return bucket;
    }

    public Boolean isCheckObjects() {
        return checkObjects;
    }

    public Boolean isFix() {
        return fix;
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
        return "CheckBucketIndexRequest{" +
                "bucket='" + bucket + '\'' +
                ", checkObjects=" + checkObjects +
                ", fix=" + fix +
                "} " + super.toString();
    }

    public static class Builder extends AdminRequestBuilder<Builder, CheckBucketIndexRequest, CheckBucketIndexResponse> {
        private String bucket;
        private Boolean checkObjects;
        private Boolean fix;

        public Builder(AdminClient client) {
            super(client);
        }

        private Builder(CheckBucketIndexRequest request) {
            withBucket(request.bucket);
            withCheckObjects(request.checkObjects);
            withFix(request.fix);
        }

        public Builder withBucket(String bucket) {
            this.bucket = bucket;
            return self();
        }

        public Builder withCheckObjects(Boolean checkObjects) {
            this.checkObjects = checkObjects;
            return self();
        }

        public Builder withFix(Boolean fix) {
            this.fix = fix;
            return self();
        }

        @Override
        public CheckBucketIndexRequest build() {
            return new CheckBucketIndexRequest(this);
        }

        @Override
        public CheckBucketIndexResponse run() {
            return client.checkBucketIndex(build());
        }

        @Override
        public ActionFuture<CheckBucketIndexResponse> execute() {
            return client.checkBucketIndexAsync(build());
        }

        @Override
        public void execute(ActionListener<CheckBucketIndexResponse> listener) {
            client.checkBucketIndexAsync(build(), listener);
        }
    }
}
