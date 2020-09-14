package io.ceph.rgw.client.model;

/**
 * A builder contains required parameters, and is used to create a {@link Request}.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/3/14.
 */
public interface Builder<T extends Request> {
    /**
     * build a {@link Request}
     *
     * @return built request
     */
    T build();
}
