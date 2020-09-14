package io.ceph.rgw.client.model;

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

/**
 * Created by zhuangshuo on 2020/3/24.
 */
public enum BucketVersioning {
    OFF("Off"),
    SUSPENDED("Suspended"),
    ENABLED("Enabled");
    private final String status;

    BucketVersioning(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static BucketVersioning fromString(String status) {
        if (StringUtils.isBlank(status)) {
            return null;
        }
        return valueOf(status.toUpperCase(Locale.ROOT));
    }
}
