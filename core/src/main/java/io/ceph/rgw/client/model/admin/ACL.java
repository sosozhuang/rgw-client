package io.ceph.rgw.client.model.admin;

import software.amazon.awssdk.core.SdkField;
import software.amazon.awssdk.core.SdkPojo;
import software.amazon.awssdk.core.protocol.MarshallLocation;
import software.amazon.awssdk.core.protocol.MarshallingType;
import software.amazon.awssdk.core.traits.ListTrait;
import software.amazon.awssdk.core.traits.LocationTrait;
import software.amazon.awssdk.utils.builder.Buildable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Created by zhuangshuo on 2020/8/7.
 */
public class ACL implements SdkPojo, Buildable {
    private static final SdkField<List<UserACL>> USER_ACLS_FIELD = SdkField
            .<List<UserACL>>builder(MarshallingType.LIST)
            .getter(getter(ACL::getUserACLs))
            .setter(setter(ACL::setUserACLs))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("acl_user_map")
                            .unmarshallLocationName("acl_user_map").build(),
                    ListTrait
                            .builder()
                            .memberLocationName("entry")
                            .memberFieldInfo(
                                    SdkField.builder(MarshallingType.SDK_POJO)
                                            .constructor(UserACL::new)
                                            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD)
                                                    .locationName("entry").unmarshallLocationName("entry").build()).build())
                            .build()).build();
    private static final SdkField<List<GroupACL>> GROUP_ACLS_FIELD = SdkField
            .<List<GroupACL>>builder(MarshallingType.LIST)
            .getter(getter(ACL::getGroupACLs))
            .setter(setter(ACL::setGroupACLs))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("acl_group_map")
                            .unmarshallLocationName("acl_group_map").build(),
                    ListTrait
                            .builder()
                            .memberLocationName("entry")
                            .memberFieldInfo(
                                    SdkField.builder(MarshallingType.SDK_POJO)
                                            .constructor(GroupACL::new)
                                            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD)
                                                    .locationName("entry").unmarshallLocationName("entry").build()).build())
                            .build()).build();
    private static final List<SdkField<?>> SDK_FIELDS = Collections.unmodifiableList(Arrays.asList(USER_ACLS_FIELD, GROUP_ACLS_FIELD));
    private List<UserACL> userACLs;
    private List<GroupACL> groupACLs;

    ACL() {
    }

    private static <T> Function<Object, T> getter(Function<ACL, T> g) {
        return obj -> g.apply((ACL) obj);
    }

    private static <T> BiConsumer<Object, T> setter(BiConsumer<ACL, T> s) {
        return (obj, val) -> s.accept((ACL) obj, val);
    }

    public List<UserACL> getUserACLs() {
        return userACLs;
    }

    private void setUserACLs(List<UserACL> userACLs) {
        this.userACLs = AdminRequest.unmodifiableList(userACLs);
    }

    public List<GroupACL> getGroupACLs() {
        return groupACLs;
    }

    private void setGroupACLs(List<GroupACL> groupACLs) {
        this.groupACLs = AdminRequest.unmodifiableList(groupACLs);
    }

    @Override
    public List<SdkField<?>> sdkFields() {
        return SDK_FIELDS;
    }

    @Override
    public Object build() {
        return this;
    }

    @Override
    public String toString() {
        return "ACL{" +
                "userACLs=" + userACLs +
                ", groupACLs=" + groupACLs +
                '}';
    }

    public static class UserACL implements SdkPojo, Buildable {
        private static final SdkField<String> USER_FIELD = SdkField
                .builder(MarshallingType.STRING)
                .getter(getter(UserACL::getUser))
                .setter(setter(UserACL::setUser))
                .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("user")
                        .unmarshallLocationName("user").build()).build();
        private static final SdkField<Integer> ACL_FIELD = SdkField
                .builder(MarshallingType.INTEGER)
                .getter(getter(UserACL::getAcl))
                .setter(setter(UserACL::setAcl))
                .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("acl")
                        .unmarshallLocationName("acl").build()).build();
        private static final List<SdkField<?>> SDK_FIELDS = Collections.unmodifiableList(Arrays.asList(USER_FIELD, ACL_FIELD));
        private String user;
        private Integer acl;

        UserACL() {
        }

        private static <T> Function<Object, T> getter(Function<UserACL, T> g) {
            return obj -> g.apply((UserACL) obj);
        }

        private static <T> BiConsumer<Object, T> setter(BiConsumer<UserACL, T> s) {
            return (obj, val) -> s.accept((UserACL) obj, val);
        }

        public String getUser() {
            return user;
        }

        private void setUser(String user) {
            this.user = user;
        }

        public Integer getAcl() {
            return acl;
        }

        private void setAcl(Integer acl) {
            this.acl = acl;
        }

        @Override
        public List<SdkField<?>> sdkFields() {
            return SDK_FIELDS;
        }

        @Override
        public Object build() {
            return this;
        }

        @Override
        public String toString() {
            return "UserACL{" +
                    "user='" + user + '\'' +
                    ", acl=" + acl +
                    '}';
        }
    }

    public static class GroupACL implements SdkPojo, Buildable {
        private static final SdkField<Integer> GROUP_FIELD = SdkField
                .builder(MarshallingType.INTEGER)
                .getter(getter(GroupACL::getGroup))
                .setter(setter(GroupACL::setGroup))
                .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("group")
                        .unmarshallLocationName("group").build()).build();
        private static final SdkField<Integer> ACL_FIELD = SdkField
                .builder(MarshallingType.INTEGER)
                .getter(getter(GroupACL::getAcl))
                .setter(setter(GroupACL::setAcl))
                .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("acl")
                        .unmarshallLocationName("acl").build()).build();
        private static final List<SdkField<?>> SDK_FIELDS = Collections.unmodifiableList(Arrays.asList(GROUP_FIELD, ACL_FIELD));
        private Integer group;
        private Integer acl;

        GroupACL() {
        }

        private static <T> Function<Object, T> getter(Function<GroupACL, T> g) {
            return obj -> g.apply((GroupACL) obj);
        }

        private static <T> BiConsumer<Object, T> setter(BiConsumer<GroupACL, T> s) {
            return (obj, val) -> s.accept((GroupACL) obj, val);
        }

        public Integer getGroup() {
            return group;
        }

        private void setGroup(Integer group) {
            this.group = group;
        }

        public Integer getAcl() {
            return acl;
        }

        private void setAcl(Integer acl) {
            this.acl = acl;
        }

        @Override
        public List<SdkField<?>> sdkFields() {
            return SDK_FIELDS;
        }

        @Override
        public Object build() {
            return this;
        }

        @Override
        public String toString() {
            return "GroupACL{" +
                    "group=" + group +
                    ", acl=" + acl +
                    '}';
        }
    }
}
