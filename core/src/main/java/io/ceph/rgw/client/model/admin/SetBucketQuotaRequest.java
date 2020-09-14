package io.ceph.rgw.client.model.admin;

import io.ceph.rgw.client.AdminClient;
import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;
import org.apache.commons.lang3.Validate;
import software.amazon.awssdk.core.SdkField;
import software.amazon.awssdk.core.protocol.MarshallLocation;
import software.amazon.awssdk.core.protocol.MarshallingType;
import software.amazon.awssdk.core.traits.LocationTrait;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Created by zhuangshuo on 2020/7/28.
 */
public class SetBucketQuotaRequest extends AdminRequest {
    private static final SdkField<String> UID_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(SetBucketQuotaRequest::getUid))
            .setter(setter(Builder::withUid))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("uid")
                    .unmarshallLocationName("uid").build()).build();
    private static final SdkField<Quota> QUOTA_FIELD = SdkField
            .<Quota>builder(MarshallingType.SDK_POJO)
            .getter(getter(SetBucketQuotaRequest::getQuota))
            .setter(setter(Builder::withQuota))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("bucket_quota")
                    .unmarshallLocationName("bucket_quota").build()).build();

    private static final List<SdkField<?>> SDK_FIELDS = Collections.unmodifiableList(Collections.singletonList(UID_FIELD));

    private final String uid;
    private final Quota quota;

    public SetBucketQuotaRequest(Builder builder) {
        super(builder);
        this.uid = Validate.notBlank(builder.uid, "uid cannot be empty string");
        this.quota = Objects.requireNonNull(builder.quota);
    }

    private static <T> Function<Object, T> getter(Function<SetBucketQuotaRequest, T> g) {
        return obj -> g.apply((SetBucketQuotaRequest) obj);
    }

    private static <T> BiConsumer<Object, T> setter(BiConsumer<Builder, T> s) {
        return (obj, val) -> s.accept((Builder) obj, val);
    }

    public String getUid() {
        return uid;
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
        return "SetBucketQuotaRequest{" +
                "uid='" + uid + '\'' +
                "quota='" + quota + '\'' +
                "} " + super.toString();
    }

    public static class Builder extends AdminRequestBuilder<Builder, SetBucketQuotaRequest, SetBucketQuotaResponse> {
        private String uid;
        private Quota quota;

        public Builder(AdminClient client) {
            super(client);
            this.quota = new Quota();
        }

        private Builder(SetBucketQuotaRequest request) {
            this.quota = new Quota();
            withUid(request.uid);
            withEnabled(request.quota.isEnabled());
            withCheckOnRaw(request.quota.isCheckOnRaw());
            withMaxSize(request.quota.getMaxSize());
            withMaxSizeKb(request.quota.getMaxSizeKb());
            withMaxObjects(request.quota.getMaxObjects());
        }

        public Builder withUid(String uid) {
            this.uid = uid;
            return self();
        }

        private Builder withQuota(Quota quota) {
            this.quota = quota;
            return self();
        }

        public Builder withEnabled(Boolean enabled) {
            this.quota.setEnabled(enabled);
            return self();
        }

        public Builder withCheckOnRaw(Boolean checkOnRaw) {
            this.quota.setCheckOnRaw(checkOnRaw);
            return self();
        }

        public Builder withMaxSize(Integer maxSize) {
            this.quota.setMaxSize(maxSize);
            return self();
        }

        public Builder withMaxSizeKb(Integer maxSizeKb) {
            this.quota.setMaxSizeKb(maxSizeKb);
            return self();
        }

        public Builder withMaxObjects(Integer maxObjects) {
            this.quota.setMaxObjects(maxObjects);
            return self();
        }

        @Override
        public SetBucketQuotaRequest build() {
            return new SetBucketQuotaRequest(this);
        }

        @Override
        public SetBucketQuotaResponse run() {
            return client.setBucketQuota(build());
        }

        @Override
        public ActionFuture<SetBucketQuotaResponse> execute() {
            return client.setBucketQuotaAsync(build());
        }

        @Override
        public void execute(ActionListener<SetBucketQuotaResponse> listener) {
            client.setBucketQuotaAsync(build(), listener);
        }
    }
}
