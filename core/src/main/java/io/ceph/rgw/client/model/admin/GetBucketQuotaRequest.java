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
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Created by zhuangshuo on 2020/7/28.
 */
public class GetBucketQuotaRequest extends AdminRequest {
    private static final SdkField<String> UID_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(GetBucketQuotaRequest::getUid))
            .setter(setter(Builder::withUid))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("uid")
                    .unmarshallLocationName("uid").build()).build();
    private static final List<SdkField<?>> SDK_FIELDS = Collections.unmodifiableList(Collections.singletonList(UID_FIELD));

    private final String uid;

    public GetBucketQuotaRequest(Builder builder) {
        super(builder);
        this.uid = Validate.notBlank(builder.uid, "uid cannot be empty string");
    }

    private static <T> Function<Object, T> getter(Function<GetBucketQuotaRequest, T> g) {
        return obj -> g.apply((GetBucketQuotaRequest) obj);
    }

    private static <T> BiConsumer<Object, T> setter(BiConsumer<Builder, T> s) {
        return (obj, val) -> s.accept((Builder) obj, val);
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
        return "GetBucketQuotaRequest{" +
                "uid='" + uid + '\'' +
                "} " + super.toString();
    }

    public static class Builder extends AdminRequestBuilder<Builder, GetBucketQuotaRequest, GetBucketQuotaResponse> {
        private String uid;

        public Builder(AdminClient client) {
            super(client);
        }

        private Builder(GetBucketQuotaRequest request) {
            withUid(request.uid);
        }

        public Builder withUid(String uid) {
            this.uid = uid;
            return self();
        }

        @Override
        public GetBucketQuotaRequest build() {
            return new GetBucketQuotaRequest(this);
        }

        @Override
        public GetBucketQuotaResponse run() {
            return client.getBucketQuota(build());
        }

        @Override
        public ActionFuture<GetBucketQuotaResponse> execute() {
            return client.getBucketQuotaAsync(build());
        }

        @Override
        public void execute(ActionListener<GetBucketQuotaResponse> listener) {
            client.getBucketQuotaAsync(build(), listener);
        }
    }
}
