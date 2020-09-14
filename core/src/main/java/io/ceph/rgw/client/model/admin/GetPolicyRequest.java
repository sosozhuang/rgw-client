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
public class GetPolicyRequest extends AdminRequest {
    private static final SdkField<String> BUCKET_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(GetPolicyRequest::getBucket))
            .setter(setter(Builder::withBucket))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("bucket")
                    .unmarshallLocationName("bucket").build()).build();
    private static final SdkField<String> OBJECT_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(GetPolicyRequest::getObject))
            .setter(setter(Builder::withObject))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("object")
                    .unmarshallLocationName("object").build()).build();
    private static final List<SdkField<?>> SDK_FIELDS = Collections.unmodifiableList(Arrays.asList(BUCKET_FIELD, OBJECT_FIELD));

    private final String bucket;
    private final String object;

    public GetPolicyRequest(Builder builder) {
        super(builder);
        this.bucket = Validate.notBlank(builder.bucket, "bucket cannot be empty string");
        this.object = builder.object;
    }

    private static <T> Function<Object, T> getter(Function<GetPolicyRequest, T> g) {
        return obj -> g.apply((GetPolicyRequest) obj);
    }

    private static <T> BiConsumer<Object, T> setter(BiConsumer<Builder, T> s) {
        return (obj, val) -> s.accept((Builder) obj, val);
    }

    public String getBucket() {
        return bucket;
    }

    public String getObject() {
        return object;
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
        return "GetPolicyRequest{" +
                "bucket='" + bucket + '\'' +
                ", object='" + object + '\'' +
                "} " + super.toString();
    }

    public static class Builder extends AdminRequestBuilder<Builder, GetPolicyRequest, GetPolicyResponse> {
        private String bucket;
        private String object;

        public Builder(AdminClient client) {
            super(client);
        }

        private Builder(GetPolicyRequest request) {
            withBucket(request.bucket);
            withObject(request.object);
        }

        public Builder withBucket(String bucket) {
            this.bucket = bucket;
            return self();
        }

        public Builder withObject(String object) {
            this.object = object;
            return self();
        }

        @Override
        public GetPolicyRequest build() {
            return new GetPolicyRequest(this);
        }

        @Override
        public GetPolicyResponse run() {
            return client.getPolicy(build());
        }

        @Override
        public ActionFuture<GetPolicyResponse> execute() {
            return client.getPolicyAsync(build());
        }

        @Override
        public void execute(ActionListener<GetPolicyResponse> listener) {
            client.getPolicyAsync(build(), listener);
        }
    }
}
