package io.ceph.rgw.client.action;

import java.util.concurrent.ExecutorService;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/6/16.
 */
public class SyncListenerActionExecutor extends ThreadedListenerActionExecutor {
    public SyncListenerActionExecutor(ActionExecutor delegate, ExecutorService executor) {
        super(delegate, executor);
    }

    @Override
    protected <R> ListenableActionFuture<R> createActionFuture() {
        return new SyncActionFuture<>();
    }
}
