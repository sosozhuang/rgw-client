package io.ceph.rgw.client.circuitbreaker;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import io.ceph.rgw.client.action.*;
import io.ceph.rgw.client.config.Configuration;
import io.ceph.rgw.client.config.RGWClientProperties;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * An {@link ActionExecutor} that wraps {@link Action} and executes as {@link HystrixCommand}
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/3/10.
 * @see io.ceph.rgw.client.config.RGWClientProperties#enableHystrix
 * @see RGWClientProperties#getHystrixConfig()
 */
public class HystrixActionExecutor implements ActionExecutor {
    private final ActionExecutor delegate;
    private final HystrixCommandProperties.Setter setter;

    public HystrixActionExecutor(ActionExecutor delegate, Configuration hystrixConfig) {
        this.delegate = Objects.requireNonNull(delegate);
        this.setter = initializeSetter(hystrixConfig);
    }

    private interface PropertySetter {
        void set(String key);
    }

    private static HystrixCommandProperties.Setter initializeSetter(Configuration hystrixConfig) {
        HystrixCommandProperties.Setter setter = HystrixCommandProperties.Setter()
                .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)
                .withRequestCacheEnabled(false)
                .withFallbackEnabled(false);
        List<Map.Entry<String, PropertySetter>> list = new LinkedList<>();
        list.add(new SimpleImmutableEntry<>("execution.isolation.semaphore.maxConcurrentRequests", k -> Optional.ofNullable(hystrixConfig.getInteger(k)).ifPresent(setter::withExecutionIsolationSemaphoreMaxConcurrentRequests)));
        list.add(new SimpleImmutableEntry<>("execution.isolation.thread.interruptOnTimeout", k -> Optional.ofNullable(hystrixConfig.getBoolean(k)).ifPresent(setter::withExecutionIsolationThreadInterruptOnTimeout)));
        list.add(new SimpleImmutableEntry<>("execution.timeoutInMilliseconds", k -> Optional.ofNullable(hystrixConfig.getInteger(k)).ifPresent(setter::withExecutionTimeoutInMilliseconds)));
        list.add(new SimpleImmutableEntry<>("circuitBreaker.enabled", k -> Optional.ofNullable(hystrixConfig.getBoolean(k)).ifPresent(setter::withCircuitBreakerEnabled)));
        list.add(new SimpleImmutableEntry<>("circuitBreaker.requestVolumeThreshold", k -> Optional.ofNullable(hystrixConfig.getInteger(k)).ifPresent(setter::withCircuitBreakerRequestVolumeThreshold)));
        list.add(new SimpleImmutableEntry<>("circuitBreaker.sleepWindowInMilliseconds", k -> Optional.ofNullable(hystrixConfig.getInteger(k)).ifPresent(setter::withCircuitBreakerSleepWindowInMilliseconds)));
        list.add(new SimpleImmutableEntry<>("circuitBreaker.errorThresholdPercentage", k -> Optional.ofNullable(hystrixConfig.getInteger(k)).ifPresent(setter::withCircuitBreakerErrorThresholdPercentage)));
        list.add(new SimpleImmutableEntry<>("circuitBreaker.forceOpen", k -> Optional.ofNullable(hystrixConfig.getBoolean(k)).ifPresent(setter::withCircuitBreakerForceOpen)));
        list.add(new SimpleImmutableEntry<>("circuitBreaker.forceClosed", k -> Optional.ofNullable(hystrixConfig.getBoolean(k)).ifPresent(setter::withCircuitBreakerForceClosed)));
        list.add(new SimpleImmutableEntry<>("metrics.rollingPercentile.enabled", k -> Optional.ofNullable(hystrixConfig.getBoolean(k)).ifPresent(setter::withMetricsRollingPercentileEnabled)));
        list.add(new SimpleImmutableEntry<>("metrics.rollingPercentile.timeInMilliseconds", k -> Optional.ofNullable(hystrixConfig.getInteger(k)).ifPresent(setter::withMetricsRollingPercentileWindowInMilliseconds)));
        list.add(new SimpleImmutableEntry<>("metrics.rollingPercentile.numBuckets", k -> Optional.ofNullable(hystrixConfig.getInteger(k)).ifPresent(setter::withMetricsRollingPercentileWindowBuckets)));
        list.add(new SimpleImmutableEntry<>("metrics.rollingPercentile.bucketSize", k -> Optional.ofNullable(hystrixConfig.getInteger(k)).ifPresent(setter::withMetricsRollingPercentileBucketSize)));
        list.add(new SimpleImmutableEntry<>("metrics.healthSnapshot.intervalInMilliseconds", k -> Optional.ofNullable(hystrixConfig.getInteger(k)).ifPresent(setter::withMetricsHealthSnapshotIntervalInMilliseconds)));
        list.add(new SimpleImmutableEntry<>("requestLog.enabled", k -> Optional.ofNullable(hystrixConfig.getBoolean(k)).ifPresent(setter::withRequestLogEnabled)));
        list.forEach(e -> e.getValue().set(e.getKey()));
        return setter;
    }

    @Override
    public <R> ActionFuture<R> execute(SyncAction<R> action) {
        return delegate.execute(wrap(action, setter));
    }

    @Override
    public <R> void execute(SyncAction<R> action, ActionListener<R> listener) {
        delegate.execute(wrap(action, setter), listener);
    }

    @Override
    public <R> R run(SyncAction<R> action) {
        return delegate.run(wrap(action, setter));
    }

    @Override
    public <R> void execute(AsyncAction<R> action, ActionListener<R> listener) {
        delegate.execute(wrap(action, setter), listener);
    }

    @Override
    public <R> SubscribeActionFuture<R> execute(SubscribeAction<R> action, ActionListener<R> listener) {
        return delegate.execute(action, listener);
    }

    private static <R> SyncAction<R> wrap(SyncAction<R> action, HystrixCommandProperties.Setter setter) {
        if (action == null || action instanceof HystrixCommandSyncAction || Boolean.FALSE.equals(setter.getCircuitBreakerEnabled())) {
            return action;
        }
        return new HystrixCommandSyncAction<>(action, setter);
    }

    private static <R> AsyncAction<R> wrap(AsyncAction<R> action, HystrixCommandProperties.Setter setter) {
        if (action == null || action instanceof HystrixCommandAsyncAction || Boolean.FALSE.equals(setter.getCircuitBreakerEnabled())) {
            return action;
        }
        return new HystrixCommandAsyncAction<>(action, setter);
    }

    private static class HystrixCommandSyncAction<R> extends HystrixCommand<R> implements SyncAction<R> {
        private final SyncAction<R> action;

        private HystrixCommandSyncAction(SyncAction<R> action, HystrixCommandProperties.Setter setter) {
            super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("rgw-client"))
                    .andCommandKey(HystrixCommandKey.Factory.asKey(action.name()))
                    .andCommandPropertiesDefaults(setter));
            this.action = action;
        }

        @Override
        public R run() throws Exception {
            return action.call();
        }

        @Override
        public R call() {
            return execute();
        }
    }

    private static class HystrixCommandAsyncAction<R> extends HystrixCommand<R> implements AsyncAction<R> {
        private final AsyncAction<R> action;

        private HystrixCommandAsyncAction(AsyncAction<R> action, HystrixCommandProperties.Setter setter) {
            super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("rgw-client"))
                    .andCommandKey(HystrixCommandKey.Factory.asKey(action.name()))
                    .andCommandPropertiesDefaults(setter));
            this.action = action;
        }

        @Override
        public R run() throws Exception {
            return action.call().get();
        }

        @Override
        public CompletableFuture<R> call() {
            return CompletableFuture.supplyAsync(this::execute);
        }
    }
}
