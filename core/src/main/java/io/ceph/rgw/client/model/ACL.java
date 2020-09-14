package io.ceph.rgw.client.model;

import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Created by zhuangshuo on 2020/3/23.
 */
public class ACL {
    private final List<Grant> grants;
    private final Owner owner;

    public ACL(List<Grant> grants, Owner owner) {
        if (grants == null || grants.size() == 0) {
            this.grants = Collections.emptyList();
        } else {
            this.grants = Collections.unmodifiableList(Validate.noNullElements(grants));
        }
        this.owner = Objects.requireNonNull(owner);
    }

    public List<Grant> getGrants() {
        return grants;
    }

    public Owner getOwner() {
        return owner;
    }

    @Override
    public String toString() {
        return "ACL{" +
                "grants=" + grants +
                ", owner=" + owner +
                '}';
    }

    static class NestedBuilder<T extends NestedBuilder<T>> extends NestedGenericBuilder<T, ACL> {
        private List<Grant> grants;
        private Owner owner;

        NestedBuilder() {
            this.grants = new ArrayList<>();
        }

        public GrantBuilder<T> addGrant() {
            return new GrantBuilder<>(this);
        }

        public T addGrant(Grant grant) {
            this.grants.add(grant);
            return self();
        }

        public T addGrant(Grantee grantee, Permission permission) {
            this.grants.add(new Grant(grantee, permission));
            return self();
        }

        public T withOwner(Owner owner) {
            this.owner = owner;
            return self();
        }

        public T withOwner(String id, String displayName) {
            this.owner = new Owner(id, displayName);
            return self();
        }

        @Override
        protected ACL build() {
            return new ACL(grants, owner);
        }
    }

    public static class Builder extends NestedBuilder<Builder> {
        public ACL build() {
            return super.build();
        }
    }

    public static class GrantBuilder<T extends NestedBuilder<T>> extends Grant.NestedBuilder<GrantBuilder<T>> {
        final NestedBuilder<T> builder;

        GrantBuilder(NestedBuilder<T> builder) {
            this.builder = builder;
        }

        public T endGrant() {
            return builder.addGrant(build());
        }
    }
}
