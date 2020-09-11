package io.ceph.rgw.client.action;

/**
 * Accepts an {@link ActionExecutor} and, may return a new one or the same value given.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/4/26.
 */
public interface ActionExecutorDecorator {
    ActionExecutor decorate(ActionExecutor actionExecutor);
}
