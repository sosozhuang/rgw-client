package io.ceph.rgw.client.model;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by zhuangshuo on 2020/7/9.
 */
public enum GroupGrantee implements Grantee {
    ALL_USERS("http://acs.amazonaws.com/groups/global/AllUsers"),
    AUTHENTICATED_USERS("http://acs.amazonaws.com/groups/global/AuthenticatedUsers"),
    LOG_DELIVERY("http://acs.amazonaws.com/groups/s3/LogDelivery");

    private final String uri;

    GroupGrantee(String uri) {
        this.uri = uri;
    }

    @Override
    public String getType() {
        return "uri";
    }

    @Override
    public String getId() {
        return uri;
    }

    public static GroupGrantee fromString(String uri) {
        if (StringUtils.isBlank(uri)) {
            return null;
        }
        for (GroupGrantee grantee : values()) {
            if (grantee.uri.equals(uri)) {
                return grantee;
            }
        }
        return null;
    }
}
