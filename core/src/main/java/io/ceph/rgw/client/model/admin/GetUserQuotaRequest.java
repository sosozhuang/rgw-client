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
public class GetUserQuotaRequest extends AdminRequest {
    private static final SdkField<String> UID_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(GetUserQuotaRequest::getUid))
            .setter(setter(Builder::withUid))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("uid")
                    .unmarshallLocationName("uid").build()).build();
    private static final List<SdkField<?>> SDK_FIELDS = Collections.unmodifiableList(Collections.singletonList(UID_FIELD));

    private final String uid;

    public GetUserQuotaRequest(Builder builder) {
        super(builder);
        this.uid = Validate.notBlank(builder.uid, "uid cannot be empty string");
    }

    private static <T> Function<Object, T> getter(Function<GetUserQuotaRequest, T> g) {
        return obj -> g.apply((GetUserQuotaRequest) obj);
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

    public static class Builder extends AdminRequestBuilder<Builder, GetUserQuotaRequest, GetUserQuotaResponse> {
        public String uid;

        public Builder(AdminClient client) {
            super(client);
        }

        private Builder(GetUserQuotaRequest request) {
            withUid(request.uid);
        }

        public Builder withUid(String uid) {
            this.uid = uid;
            return self();
        }

        @Override
        public GetUserQuotaRequest build() {
            return new GetUserQuotaRequest(this);
        }

        @Override
        public GetUserQuotaResponse run() {
            return client.getUserQuota(build());
        }

        @Override
        public ActionFuture<GetUserQuotaResponse> execute() {
            return client.getUserQuotaAsync(build());
        }

        @Override
        public void execute(ActionListener<GetUserQuotaResponse> listener) {
            client.getUserQuotaAsync(build(), listener);
        }
    }
}
