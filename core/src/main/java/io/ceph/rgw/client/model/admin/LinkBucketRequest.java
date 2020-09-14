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
public class LinkBucketRequest extends AdminRequest {
    private static final SdkField<String> BUCKET_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(LinkBucketRequest::getBucket))
            .setter(setter(Builder::withBucket))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("bucket")
                    .unmarshallLocationName("bucket").build()).build();
    private static final SdkField<String> BUCKET_ID_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(LinkBucketRequest::getBucketId))
            .setter(setter(Builder::withBucketId))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("bucket-id")
                    .unmarshallLocationName("bucket-id").build()).build();
    private static final SdkField<String> UID_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(LinkBucketRequest::getUid))
            .setter(setter(Builder::withUid))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("uid")
                    .unmarshallLocationName("uid").build()).build();
    private static final List<SdkField<?>> SDK_FIELDS = Collections.unmodifiableList(Arrays.asList(BUCKET_FIELD, BUCKET_ID_FIELD, UID_FIELD));

    private final String bucket;
    private final String bucketId;
    private final String uid;

    public LinkBucketRequest(Builder builder) {
        super(builder);
        this.bucket = Validate.notBlank(builder.bucket, "bucket cannot be empty string");
        this.bucketId = Validate.notBlank(builder.bucketId, "bucketId cannot be empty string");
        this.uid = Validate.notBlank(builder.uid, "uid cannot be empty string");
    }

    private static <T> Function<Object, T> getter(Function<LinkBucketRequest, T> g) {
        return obj -> g.apply((LinkBucketRequest) obj);
    }

    private static <T> BiConsumer<Object, T> setter(BiConsumer<Builder, T> s) {
        return (obj, val) -> s.accept((Builder) obj, val);
    }

    public String getBucket() {
        return bucket;
    }

    public String getBucketId() {
        return bucketId;
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
        return "LinkBucketRequest{" +
                "bucket='" + bucket + '\'' +
                ", bucketId='" + bucketId + '\'' +
                ", uid='" + uid + '\'' +
                "} " + super.toString();
    }

    public static class Builder extends AdminRequestBuilder<Builder, LinkBucketRequest, LinkBucketResponse> {
        private String bucket;
        private String bucketId;
        private String uid;

        public Builder(AdminClient client) {
            super(client);
        }

        private Builder(LinkBucketRequest request) {
            withBucket(request.bucket);
            withBucketId(request.bucketId);
            withUid(request.uid);
        }

        public Builder withBucket(String bucket) {
            this.bucket = bucket;
            return self();
        }

        public Builder withBucketId(String bucketId) {
            this.bucketId = bucketId;
            return self();
        }

        public Builder withUid(String uid) {
            this.uid = uid;
            return self();
        }

        @Override
        public LinkBucketRequest build() {
            return new LinkBucketRequest(this);
        }

        @Override
        public LinkBucketResponse run() {
            return client.linkBucket(build());
        }

        @Override
        public ActionFuture<LinkBucketResponse> execute() {
            return client.linkBucketAsync(build());
        }

        @Override
        public void execute(ActionListener<LinkBucketResponse> listener) {
            client.linkBucketAsync(build(), listener);
        }
    }
}
