package io.ceph.rgw.client.model.admin;

import org.apache.commons.lang3.StringUtils;
import software.amazon.awssdk.core.SdkField;
import software.amazon.awssdk.core.SdkPojo;
import software.amazon.awssdk.core.protocol.MarshallLocation;
import software.amazon.awssdk.core.protocol.MarshallingType;
import software.amazon.awssdk.core.traits.LocationTrait;
import software.amazon.awssdk.utils.builder.Buildable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Created by zhuangshuo on 2020/8/3.
 */
public class SubUser implements SdkPojo, Buildable {
    private static final SdkField<String> ID_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(SubUser::getId))
            .setter(setter(SubUser::setId))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("id")
                    .unmarshallLocationName("id").build()).build();
    private static final SdkField<String> PERMISSION_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(SubUser::getPermissionAsString))
            .setter(setter(SubUser::setPermission))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("permissions")
                    .unmarshallLocationName("permissions").build()).build();
    private static final List<SdkField<?>> SDK_FIELDS = Collections.unmodifiableList(Arrays.asList(ID_FIELD, PERMISSION_FIELD));

    private String id;
    private Permission permission;

    SubUser() {
    }

    private static <T> Function<Object, T> getter(Function<SubUser, T> g) {
        return obj -> g.apply((SubUser) obj);
    }

    private static <T> BiConsumer<Object, T> setter(BiConsumer<SubUser, T> s) {
        return (obj, val) -> s.accept((SubUser) obj, val);
    }

    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id;
    }

    public Permission getPermission() {
        return permission;
    }

    private void setPermission(String permission) {
        this.permission = Permission.fromString(permission);
    }

    private String getPermissionAsString() {
        return String.valueOf(permission);
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
        return "SubUser{" +
                "id='" + id + '\'' +
                ", permission=" + permission +
                '}';
    }

    public enum Permission {
        NONE("<none>"),
        READ("read"),
        WRITE("write"),
        READ_WRITE("read-write"),
        FULL_CONTROL("full-control");
        private final String perm;

        Permission(String perm) {
            this.perm = perm;
        }

        public static Permission fromString(String perm) {
            if (StringUtils.isBlank(perm)) {
                return null;
            }
            for (Permission value : values()) {
                if (value.perm.equals(perm)) {
                    return value;
                }
            }
            if ("full".equals(perm)) {
                return FULL_CONTROL;
            }
            return null;
        }

        @Override
        public String toString() {
            return perm;
        }
    }
}
