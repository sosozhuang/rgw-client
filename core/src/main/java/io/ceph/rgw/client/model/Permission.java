package io.ceph.rgw.client.model;

/**
 * Created by zhuangshuo on 2020/3/23.
 */
public enum Permission {
    FULL_CONTROL("FULL_CONTROL", "x-amz-grant-full-control"),
    READ("READ", "x-amz-grant-read"),
    WRITE("WRITE", "x-amz-grant-write"),
    READ_ACP("READ_ACP", "x-amz-grant-read-acp"),
    WRITE_ACP("WRITE_ACP", "x-amz-grant-write-acp");

    private final String permission;
    private final String header;

    Permission(String permission, String header) {
        this.permission = permission;
        this.header = header;
    }

    public String getPermission() {
        return permission;
    }

    public String getHeader() {
        return header;
    }
}
