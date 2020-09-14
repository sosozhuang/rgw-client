package io.ceph.rgw.client.model;

/**
 * Created by zhuangshuo on 2020/3/23.
 */
public enum CannedACL {
    PRIVATE("private"),
    PUBLIC_READ("public-read"),
    PUBLIC_READ_WRITE("public-read-write"),
    AUTHENTICATED_READ("authenticated-read");

    private final String acl;

    CannedACL(String acl) {
        this.acl = acl;
    }
}
