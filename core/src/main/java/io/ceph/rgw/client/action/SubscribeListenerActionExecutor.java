package io.ceph.rgw.client.action;

import java.util.concurrent.ExecutorService;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/6/16.
 */
public class SubscribeListenerActionExecutor extends ThreadedListenerActionExecutor {
    public SubscribeListenerActionExecutor(ActionExecutor delegate, ExecutorService executor) {
        super(delegate, executor);
    }

    @Override
    protected <R> ListenableActionFuture<R> createActionFuture() {
        throw new UnsupportedOperationException();
    }
}
