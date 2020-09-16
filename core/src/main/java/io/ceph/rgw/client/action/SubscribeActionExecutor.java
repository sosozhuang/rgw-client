package io.ceph.rgw.client.action;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/9/2.
 */
public class SubscribeActionExecutor implements ActionExecutor {
    @Override
    public <R> SubscribeActionFuture<R> execute(SubscribeAction<R> action, ActionListener<R> listener) {
        return action.apply(listener);
    }
}
