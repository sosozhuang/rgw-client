package io.ceph.rgw.client.action;

import java.util.function.Function;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/8/28.
 */
@FunctionalInterface
public interface SubscribeAction<R> extends Action, Function<ActionListener<R>, SubscribeActionFuture<R>> {
    @Override
    default String name() {
        return "SubscribeAction";
    }
}
