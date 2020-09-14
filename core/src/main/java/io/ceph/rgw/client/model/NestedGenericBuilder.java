package io.ceph.rgw.client.model;

/**
 * Created by zhuangshuo on 2020/3/14.
 */
abstract class NestedGenericBuilder<B extends NestedGenericBuilder<B, T>, T> extends Self<B> {
    abstract T build();
}
