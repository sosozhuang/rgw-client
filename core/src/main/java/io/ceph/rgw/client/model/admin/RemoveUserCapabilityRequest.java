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
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Created by zhuangshuo on 2020/7/28.
 */
public class RemoveUserCapabilityRequest extends AdminRequest {
    private static final SdkField<String> UID_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(RemoveUserCapabilityRequest::getUid))
            .setter(setter(Builder::withUid))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("uid")
                    .unmarshallLocationName("uid").build()).build();
    private static final SdkField<String> USER_CAPS_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(RemoveUserCapabilityRequest::getUserCapsAsString))
            .setter(setter(Builder::withUserCaps))
            .traits(LocationTrait.builder().location(MarshallLocation.QUERY_PARAM).locationName("user-caps")
                    .unmarshallLocationName("user-caps").build()).build();
    private static final List<SdkField<?>> SDK_FIELDS = Collections.unmodifiableList(Arrays.asList(UID_FIELD, USER_CAPS_FIELD));

    private final String uid;
    private final List<UserCap> userCaps;

    public RemoveUserCapabilityRequest(Builder builder) {
        super(builder);
        this.uid = Validate.notBlank(builder.uid, "uid cannot be empty string");
        this.userCaps = unmodifiableList(builder.userCaps);
        for (UserCap cap : this.userCaps) {
            Validate.notNull(cap.getType(), "type cannot be null");
            Validate.notNull(cap.getPerm(), "perm cannot be null");
        }
    }

    private static <T> Function<Object, T> getter(Function<RemoveUserCapabilityRequest, T> g) {
        return obj -> g.apply((RemoveUserCapabilityRequest) obj);
    }

    private static <T> BiConsumer<Object, T> setter(BiConsumer<Builder, T> s) {
        return (obj, val) -> s.accept((Builder) obj, val);
    }

    public String getUid() {
        return uid;
    }

    public List<UserCap> getUserCaps() {
        return userCaps;
    }

    private String getUserCapsAsString() {
        return UserCap.format(userCaps);
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
        return "RemoveUserCapabilityRequest{" +
                "uid='" + uid + '\'' +
                ", userCaps=" + userCaps +
                "} " + super.toString();
    }

    public static class Builder extends AdminRequestBuilder<Builder, RemoveUserCapabilityRequest, RemoveUserCapabilityResponse> {
        private String uid;
        private List<UserCap> userCaps;

        public Builder(AdminClient client) {
            super(client);
            this.userCaps = new LinkedList<>();
        }

        private Builder(RemoveUserCapabilityRequest request) {
            this.userCaps = new LinkedList<>();
            withUid(request.uid);
            withUserCaps(request.userCaps);
        }

        public Builder withUid(String uid) {
            this.uid = uid;
            return self();
        }

        public Builder withUserCaps(List<UserCap> userCaps) {
            this.userCaps.clear();
            this.userCaps.addAll(userCaps);
            return self();
        }

        private Builder withUserCaps(String userCaps) {
            return withUserCaps(UserCap.parse(userCaps));
        }

        public Builder addUserCap(UserCap.Type type, UserCap.Perm perm) {
            this.userCaps.add(new UserCap(type, perm));
            return self();
        }

        @Override
        public RemoveUserCapabilityRequest build() {
            return new RemoveUserCapabilityRequest(this);
        }

        @Override
        public RemoveUserCapabilityResponse run() {
            return client.removeUserCapability(build());
        }

        @Override
        public ActionFuture<RemoveUserCapabilityResponse> execute() {
            return client.removeUserCapabilityAsync(build());
        }

        @Override
        public void execute(ActionListener<RemoveUserCapabilityResponse> listener) {
            client.removeUserCapabilityAsync(build(), listener);
        }
    }
}
