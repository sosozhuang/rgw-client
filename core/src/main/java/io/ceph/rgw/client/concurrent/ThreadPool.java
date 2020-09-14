package io.ceph.rgw.client.concurrent;

import io.ceph.rgw.client.config.RGWClientProperties;
import io.ceph.rgw.client.util.AbstractClosable;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * A Pool of Thread. Creates ExecutorService from ThreadPoolProperties.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/3/6.
 * @see io.ceph.rgw.client.config.RGWClientProperties.ThreadPoolProperties
 * @see io.ceph.rgw.client.action.ActionExecutor
 */
public class ThreadPool extends AbstractClosable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadPool.class);
    private final Map<String, ExecutorService> executors;
    private final ScheduledExecutorService scheduler;

    public ThreadPool(Map<String, RGWClientProperties.ThreadPoolProperties> props) {
        Validate.notEmpty(props, "ThreadPoolProperties cannot be empty");
        this.executors = new HashMap<>(props.size());
        for (Map.Entry<String, RGWClientProperties.ThreadPoolProperties> entry : props.entrySet()) {
            executors.put(entry.getKey(), newExecutorService(entry.getKey(), entry.getValue()));
        }
        this.scheduler = newScheduler();
    }

    private static ExecutorService newExecutorService(String name, RGWClientProperties.ThreadPoolProperties properties) {
        if (Objects.equals(properties.getCoreSize(), properties.getMaxSize())) {
            return newFixed(name, properties.getCoreSize(), properties.getQueueSize());
        }
        return newScaling(name, properties.getCoreSize(), properties.getMaxSize(), properties.getKeepAlive(), properties.getQueueSize());
    }

    private static ExecutorService newFixed(String name, Integer size, Integer queueSize) {
        BlockingQueue<Runnable> queue = new LinkedTransferQueue<>();
        if (queueSize > 0) {
            queue = new SizeBlockingQueue<>(queue, queueSize);
        }
        return new ThreadPoolExecutor(size, size, 0, TimeUnit.MILLISECONDS, queue, new SimpleThreadFactory("rgw-client-" + name), new ThreadPoolExecutor.AbortPolicy());
    }

    private static ExecutorService newScaling(String name, Integer coreSize, Integer maxSize, Integer keepAlive, Integer queueSize) {
        ExecutorScalingQueue<Runnable> queue = new ExecutorScalingQueue<>();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(coreSize, maxSize, keepAlive, TimeUnit.MILLISECONDS,
                queue, new SimpleThreadFactory("rgw-client-" + name), new ForceQueuePolicy());
        queue.executor = executor;
        return executor;
    }

    private static ScheduledExecutorService newScheduler() {
        ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(1,
                new SimpleThreadFactory("rgw-client-scheduler"), new ThreadPoolExecutor.AbortPolicy());
        scheduler.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
        scheduler.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
        scheduler.setRemoveOnCancelPolicy(true);
        return scheduler;
    }

    public ExecutorService get(String name) {
        return executors.get(name);
    }

    public ScheduledExecutorService getScheduler() {
        return scheduler;
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @Override
    public void doClose() {
        try {
            scheduler.shutdown();
            scheduler.awaitTermination(30, TimeUnit.SECONDS);
            LOGGER.info("Finished to shutdown scheduler.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.error("Interrupted while waiting.", e);
        } catch (Throwable e) {
            LOGGER.error("Failed to shutdown scheduler.", e);
        }
        for (Map.Entry<String, ExecutorService> entry : executors.entrySet()) {
            try {
                entry.getValue().shutdown();
                entry.getValue().awaitTermination(30, TimeUnit.SECONDS);
                LOGGER.info("Finished to shutdown ExecutorService[{}].", entry.getKey());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LOGGER.error("Interrupted while waiting.", e);
            } catch (Throwable e) {
                LOGGER.error("Failed to shutdown ExecutorService[{}].", entry.getKey(), e);
            }
        }
    }
}
