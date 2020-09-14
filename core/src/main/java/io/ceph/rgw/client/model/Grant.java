package io.ceph.rgw.client.model;

import java.util.Objects;

/**
 * Created by zhuangshuo on 2020/3/23.
 */
public class Grant {
    private final Grantee grantee;
    private final Permission permission;

    public Grant(Grantee grantee, Permission permission) {
        this.grantee = Objects.requireNonNull(grantee);
        this.permission = Objects.requireNonNull(permission);
    }

    public Grantee getGrantee() {
        return grantee;
    }

    public Permission getPermission() {
        return permission;
    }

    @Override
    public String toString() {
        return "Grant{" +
                "grantee=" + grantee +
                ", permission=" + permission +
                '}';
    }

    static class NestedBuilder<T extends NestedBuilder<T>> extends NestedGenericBuilder<T, Grant> {
        private Grantee grantee;
        private Permission permission;

        public T withGrantee(String id, String displayName) {
            this.grantee = new CanonicalGrantee(id, displayName);
            return self();
        }

        public T withGrantee(String email) {
            this.grantee = new EmailAddressGrantee(email);
            return self();
        }

        public T withGrantee(GroupGrantee grantee) {
            this.grantee = grantee;
            return self();
        }

        public T withPermission(Permission permission) {
            this.permission = permission;
            return self();
        }

        @Override
        Grant build() {
            return new Grant(grantee, permission);
        }
    }

    public static class Builder extends NestedBuilder<Builder> {
        @Override
        public Grant build() {
            return super.build();
        }
    }
}