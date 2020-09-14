package io.ceph.rgw.client.concurrent;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/3/6.
 */
public class ExecutorScalingQueue<E> extends LinkedTransferQueue<E> {

    private static final long serialVersionUID = 6373441897358835983L;
    ThreadPoolExecutor executor;

    public ExecutorScalingQueue() {
    }

    @Override
    public boolean offer(E e) {
        if (!tryTransfer(e)) {
            int left = executor.getMaximumPoolSize() - executor.getCorePoolSize();
            return left <= 0 && super.offer(e);
        }
        return true;
    }
}