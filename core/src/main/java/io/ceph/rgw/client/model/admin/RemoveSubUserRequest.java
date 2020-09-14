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
public class RemoveSubUserRequest extends AdminRequest {
    private static final SdkField<String> UID_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(RemoveSubUserRequest::getUid))
            .setter(setter(Builder::withUid))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("uid")
                    .unmarshallLocationName("uid").build()).build();
    private static final SdkField<String> SUB_USER_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(RemoveSubUserRequest::getSubUser))
            .setter(setter(Builder::withSubUser))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("subuser")
                    .unmarshallLocationName("subuser").build()).build();
    private static final SdkField<Boolean> PURGE_KEYS_FIELD = SdkField
            .builder(MarshallingType.BOOLEAN)
            .getter(getter(RemoveSubUserRequest::getPurgeKeys))
            .setter(setter(Builder::withPurgeKeys))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("purge-keys")
                    .unmarshallLocationName("purge-keys").build()).build();
    private static final List<SdkField<?>> SDK_FIELDS = Collections.unmodifiableList(Arrays.asList(UID_FIELD, SUB_USER_FIELD, PURGE_KEYS_FIELD));

    private String uid;
    private String subUser;
    private Boolean purgeKeys;

    public RemoveSubUserRequest(Builder builder) {
        super(builder);
        this.uid = Validate.notBlank(builder.uid, "uid cannot be empty string");
        this.subUser = Validate.notBlank(builder.subUser, "subUser cannot be empty string");
        this.purgeKeys = builder.purgeKeys;
    }

    private static <T> Function<Object, T> getter(Function<RemoveSubUserRequest, T> g) {
        return obj -> g.apply((RemoveSubUserRequest) obj);
    }

    private static <T> BiConsumer<Object, T> setter(BiConsumer<Builder, T> s) {
        return (obj, val) -> s.accept((Builder) obj, val);
    }

    public String getUid() {
        return uid;
    }

    public String getSubUser() {
        return subUser;
    }

    public Boolean getPurgeKeys() {
        return purgeKeys;
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
        return "RemoveSubUserRequest{" +
                "uid='" + uid + '\'' +
                ", subUser='" + subUser + '\'' +
                ", purgeKeys=" + purgeKeys +
                "} " + super.toString();
    }

    public static class Builder extends AdminRequestBuilder<Builder, RemoveSubUserRequest, RemoveSubUserResponse> {
        private String uid;
        private String subUser;
        private Boolean purgeKeys;

        public Builder(AdminClient client) {
            super(client);
        }

        private Builder(RemoveSubUserRequest request) {
            withUid(request.uid);
            withSubUser(request.subUser);
            withPurgeKeys(request.purgeKeys);
        }

        public Builder withUid(String uid) {
            this.uid = uid;
            return self();
        }

        public Builder withSubUser(String subUser) {
            this.subUser = subUser;
            return self();
        }

        public Builder withPurgeKeys(Boolean purgeKeys) {
            this.purgeKeys = purgeKeys;
            return self();
        }

        @Override
        public RemoveSubUserRequest build() {
            return new RemoveSubUserRequest(this);
        }

        @Override
        public RemoveSubUserResponse run() {
            return client.removeSubUser(build());
        }

        @Override
        public ActionFuture<RemoveSubUserResponse> execute() {
            return client.removeSubUserAsync(build());
        }

        @Override
        public void execute(ActionListener<RemoveSubUserResponse> listener) {
            client.removeSubUserAsync(build(), listener);
        }
    }
}
