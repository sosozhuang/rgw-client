package io.ceph.rgw.client.concurrent;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * A handle that tries best effort to execute the runnable task.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/3/6.
 */
public class ForceQueuePolicy implements RejectedExecutionHandler {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        if (executor.getQueue() instanceof ExecutorScalingQueue) {
            try {
                executor.getQueue().put(r);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                RejectedExecutionException ex = new RejectedExecutionException("task " + r.toString() +
                        " rejected");
                ex.addSuppressed(e);
                throw ex;
            }
        } else {
            throw new RejectedExecutionException("task " + r + " rejected on " + executor);
        }
    }
}
