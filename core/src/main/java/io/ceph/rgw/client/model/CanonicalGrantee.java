package io.ceph.rgw.client.model;

import org.apache.commons.lang3.Validate;

/**
 * Created by zhuangshuo on 2020/7/9.
 */
public class CanonicalGrantee implements Grantee {
    private final String id;
    private final String displayName;

    public CanonicalGrantee(String id, String displayName) {
        this.id = Validate.notBlank(id);
        this.displayName = Validate.notBlank(displayName);
    }

    @Override
    public String getType() {
        return "id";
    }

    @Override
    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return "CanonicalGrantee{" +
                "id='" + id + '\'' +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
