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
public class UnlinkBucketRequest extends AdminRequest {
    private static final SdkField<String> BUCKET_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(UnlinkBucketRequest::getBucket))
            .setter(setter(Builder::withBucket))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("bucket")
                    .unmarshallLocationName("bucket").build()).build();
    private static final SdkField<String> UID_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(UnlinkBucketRequest::getUid))
            .setter(setter(Builder::withUid))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("uid")
                    .unmarshallLocationName("uid").build()).build();
    private static final List<SdkField<?>> SDK_FIELDS = Collections.unmodifiableList(Arrays.asList(BUCKET_FIELD, UID_FIELD));

    private final String bucket;
    private final String uid;

    public UnlinkBucketRequest(Builder builder) {
        super(builder);
        this.bucket = Validate.notBlank(builder.bucket, "bucket cannot be empty string");
        this.uid = Validate.notBlank(builder.uid, "uid cannot be empty string");
    }

    private static <T> Function<Object, T> getter(Function<UnlinkBucketRequest, T> g) {
        return obj -> g.apply((UnlinkBucketRequest) obj);
    }

    private static <T> BiConsumer<Object, T> setter(BiConsumer<Builder, T> s) {
        return (obj, val) -> s.accept((Builder) obj, val);
    }

    public String getBucket() {
        return bucket;
    }

    public String getUid() {
        return uid;
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
        return "UnlinkBucketRequest{" +
                "bucket='" + bucket + '\'' +
                ", uid='" + uid + '\'' +
                "} " + super.toString();
    }

    public static class Builder extends AdminRequestBuilder<Builder, UnlinkBucketRequest, UnlinkBucketResponse> {
        private String bucket;
        private String uid;

        public Builder(AdminClient client) {
            super(client);
        }

        private Builder(UnlinkBucketRequest request) {
            withBucket(request.bucket);
            withUid(request.uid);
        }

        public Builder withBucket(String bucket) {
            this.bucket = bucket;
            return self();
        }

        public Builder withUid(String uid) {
            this.uid = uid;
            return self();
        }

        @Override
        public UnlinkBucketRequest build() {
            return new UnlinkBucketRequest(this);
        }

        @Override
        public UnlinkBucketResponse run() {
            return client.unlinkBucket(build());
        }

        @Override
        public ActionFuture<UnlinkBucketResponse> execute() {
            return client.unlinkBucketAsync(build());
        }

        @Override
        public void execute(ActionListener<UnlinkBucketResponse> listener) {
            client.unlinkBucketAsync(build(), listener);
        }
    }
}
