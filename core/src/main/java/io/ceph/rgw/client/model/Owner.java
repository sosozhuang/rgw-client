package io.ceph.rgw.client.model;

import org.apache.commons.lang3.Validate;

/**
 * Created by zhuangshuo on 2020/3/23.
 */
public class Owner {
    private final String id;
    private final String displayName;

    public Owner(String id, String displayName) {
        this.id = Validate.notBlank(id);
        this.displayName = Validate.notBlank(displayName);
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return "Owner{" +
                "id='" + id + '\'' +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
