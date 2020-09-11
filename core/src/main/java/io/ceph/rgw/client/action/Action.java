package io.ceph.rgw.client.action;

/**
 * An Action designed for a task which are executed by {@link ActionExecutor}.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/8/31.
 * @see SyncAction
 * @see AsyncAction
 * @see SubscribeAction
 */
public interface Action {
    /**
     * return this action's name
     *
     * @return action name
     */
    default String name() {
        return getClass().getSimpleName();
    }
}
