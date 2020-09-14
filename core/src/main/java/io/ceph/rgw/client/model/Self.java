package io.ceph.rgw.client.model;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/3/16.
 */
abstract class Self<T> {
    @SuppressWarnings("unchecked")
    protected T self() {
        return (T) this;
    }
}
