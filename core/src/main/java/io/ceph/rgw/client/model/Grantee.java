package io.ceph.rgw.client.model;

/**
 * Created by zhuangshuo on 2020/3/23.
 */
public interface Grantee {
    String getType();

    String getId();
}
