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
public class RemoveUserRequest extends AdminRequest {
    private static final SdkField<String> UID_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(RemoveUserRequest::getUid))
            .setter(setter(Builder::withUid))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("uid")
                    .unmarshallLocationName("uid").build()).build();
    private static final SdkField<Boolean> PURGE_DATA_FIELD = SdkField
            .builder(MarshallingType.BOOLEAN)
            .getter(getter(RemoveUserRequest::isPurgeData))
            .setter(setter(Builder::withPurgeData))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("purge-data")
                    .unmarshallLocationName("purge-data").build()).build();
    private static final List<SdkField<?>> SDK_FIELDS = Collections.unmodifiableList(Arrays.asList(UID_FIELD, PURGE_DATA_FIELD));

    private final String uid;
    private final Boolean purgeData;

    public RemoveUserRequest(Builder builder) {
        super(builder);
        this.uid = Validate.notBlank(builder.uid, "uid cannot be empty string");
        this.purgeData = builder.purgeData;
    }

    private static <T> Function<Object, T> getter(Function<RemoveUserRequest, T> g) {
        return obj -> g.apply((RemoveUserRequest) obj);
    }

    private static <T> BiConsumer<Object, T> setter(BiConsumer<Builder, T> s) {
        return (obj, val) -> s.accept((Builder) obj, val);
    }

    public String getUid() {
        return uid;
    }

    public Boolean isPurgeData() {
        return purgeData;
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
        return "RemoveUserRequest{" +
                "uid='" + uid + '\'' +
                ", purgeData=" + purgeData +
                "} " + super.toString();
    }

    public static class Builder extends AdminRequestBuilder<Builder, RemoveUserRequest, RemoveUserResponse> {
        private String uid;
        private Boolean purgeData;

        public Builder(AdminClient client) {
            super(client);
        }

        private Builder(RemoveUserRequest request) {
            withUid(request.uid);
            withPurgeData(request.purgeData);
        }

        public Builder withUid(String uid) {
            this.uid = uid;
            return self();
        }

        public Builder withPurgeData(Boolean purgeData) {
            this.purgeData = purgeData;
            return self();
        }

        @Override
        public RemoveUserRequest build() {
            return new RemoveUserRequest(this);
        }

        @Override
        public RemoveUserResponse run() {
            return client.removeUser(build());
        }

        @Override
        public ActionFuture<RemoveUserResponse> execute() {
            return client.removeUserAsync(build());
        }

        @Override
        public void execute(ActionListener<RemoveUserResponse> listener) {
            client.removeUserAsync(build(), listener);
        }
    }
}
