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
public class RemoveBucketRequest extends AdminRequest {
    private static final SdkField<String> BUCKET_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(RemoveBucketRequest::getBucket))
            .setter(setter(Builder::withBucket))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("bucket")
                    .unmarshallLocationName("bucket").build()).build();
    private static final SdkField<Boolean> PURGE_OBJECTS_FIELD = SdkField
            .builder(MarshallingType.BOOLEAN)
            .getter(getter(RemoveBucketRequest::getPurgeObjects))
            .setter(setter(Builder::withPurgeObjects))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("purge-objects")
                    .unmarshallLocationName("purge-objects").build()).build();
    private static final List<SdkField<?>> SDK_FIELDS = Collections.unmodifiableList(Arrays.asList(BUCKET_FIELD, PURGE_OBJECTS_FIELD));
    private final String bucket;
    private final Boolean purgeObjects;

    public RemoveBucketRequest(Builder builder) {
        super(builder);
        this.bucket = Validate.notBlank(builder.bucket, "bucket cannot be empty string");
        this.purgeObjects = builder.purgeObjects;
    }

    private static <T> Function<Object, T> getter(Function<RemoveBucketRequest, T> g) {
        return obj -> g.apply((RemoveBucketRequest) obj);
    }

    private static <T> BiConsumer<Object, T> setter(BiConsumer<Builder, T> s) {
        return (obj, val) -> s.accept((Builder) obj, val);
    }

    public String getBucket() {
        return bucket;
    }

    public Boolean getPurgeObjects() {
        return purgeObjects;
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
        return "RemoveBucketRequest{" +
                "bucket='" + bucket + '\'' +
                ", purgeObjects=" + purgeObjects +
                "} " + super.toString();
    }

    public static class Builder extends AdminRequestBuilder<Builder, RemoveBucketRequest, RemoveBucketResponse> {
        private String bucket;
        private Boolean purgeObjects;

        public Builder(AdminClient client) {
            super(client);
        }

        public Builder(RemoveBucketRequest request) {
            withBucket(request.bucket);
            withPurgeObjects(request.purgeObjects);
        }

        public Builder withBucket(String bucket) {
            this.bucket = bucket;
            return self();
        }

        public Builder withPurgeObjects(Boolean purgeObjects) {
            this.purgeObjects = purgeObjects;
            return self();
        }

        @Override
        public RemoveBucketRequest build() {
            return new RemoveBucketRequest(this);
        }

        @Override
        public RemoveBucketResponse run() {
            return client.removeBucket(build());
        }

        @Override
        public ActionFuture<RemoveBucketResponse> execute() {
            return client.removeBucketAsync(build());
        }

        @Override
        public void execute(ActionListener<RemoveBucketResponse> listener) {
            client.removeBucketAsync(build(), listener);
        }
    }
}
