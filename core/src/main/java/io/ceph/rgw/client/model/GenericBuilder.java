package io.ceph.rgw.client.model;

/**
 * Created by zhuangshuo on 2020/3/16.
 */
public abstract class GenericBuilder<B extends GenericBuilder<B, T>, T extends Request> extends Self<B> implements Builder<T> {
}
