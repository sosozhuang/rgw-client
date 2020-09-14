package io.ceph.rgw.client.model.admin;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by zhuangshuo on 2020/8/3.
 */
public enum KeyType {
    S3,
    SWIFT;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }

    public static KeyType fromString(String type) {
        if (StringUtils.isBlank(type)) {
            return null;
        }
        for (KeyType value : values()) {
            if (value.toString().equals(type.toLowerCase())) {
                return value;
            }
        }
        return null;
    }
}
