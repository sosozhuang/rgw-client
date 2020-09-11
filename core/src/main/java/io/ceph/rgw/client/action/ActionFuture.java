package io.ceph.rgw.client.action;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * An extension to {@link Future} allowing for simplified get operations.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/3/2.
 */
public interface ActionFuture<R> extends Future<R> {
    R actionGet();

    R actionGet(long timeout, TimeUnit unit);
}
