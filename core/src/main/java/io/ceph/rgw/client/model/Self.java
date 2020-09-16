package io.ceph.rgw.client.model;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/3/16.
 */
abstract class Self<T extends Self> {
    /**
     * cast to the object type
     *
     * @return the object type reference
     */
    @SuppressWarnings("unchecked")
    protected T self() {
        return (T) this;
    }
}
