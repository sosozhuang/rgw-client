package io.ceph.rgw.client.core.async;

import io.ceph.rgw.client.concurrent.SimpleThreadFactory;
import io.ceph.rgw.client.config.RGWClientProperties;
import io.ceph.rgw.client.core.GenericConnectors;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelOption;
import software.amazon.awssdk.core.SdkClient;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.core.client.config.SdkAdvancedClientOption;
import software.amazon.awssdk.core.retry.RetryPolicy;
import software.amazon.awssdk.core.retry.backoff.EqualJitterBackoffStrategy;
import software.amazon.awssdk.core.retry.conditions.RetryCondition;
import software.amazon.awssdk.http.Protocol;
import software.amazon.awssdk.http.async.SdkAsyncHttpClient;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.http.nio.netty.SdkEventLoopGroup;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/6/29.
 */
public abstract class AsyncConnectors<C extends SdkClient> extends GenericConnectors<C> {
    final SdkAsyncHttpClient httpClient;
    final ClientOverrideConfiguration overrideConf;
    private final Supplier<C> supplier;
    protected final Map<RGWClientProperties.EndpointProperties, C> connectors;

    protected AsyncConnectors(RGWClientProperties.ConnectorProperties properties) {
        super(properties);
        this.httpClient = NettyNioAsyncHttpClient.builder()
                .protocol(Protocol.HTTP1_1)
                .maxConcurrency(properties.getMaxConnections())
                .connectionAcquisitionTimeout(Duration.ofMillis(properties.getConnectionTimeout() * properties.getMaxConnections()))
                .connectionTimeout(Duration.ofMillis(properties.getConnectionTimeout()))
                .readTimeout(Duration.ofMillis(properties.getSocketTimeout()))
                .writeTimeout(Duration.ofMillis(properties.getSocketTimeout()))
                .connectionMaxIdleTime(Duration.ofMillis(properties.getConnectionMaxIdle()))
                .putChannelOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .putChannelOption(ChannelOption.SO_KEEPALIVE, properties.isEnableKeepAlive())
                .putChannelOption(ChannelOption.TCP_NODELAY, true)
                .eventLoopGroup(SdkEventLoopGroup.builder().threadFactory(new SimpleThreadFactory("rgw-client-async")).build())
                .build();
        this.overrideConf = ClientOverrideConfiguration.builder()
                .retryPolicy(retryPolicy())
                .addExecutionInterceptor(new PutObjectInterceptor())
                .putAdvancedOption(SdkAdvancedClientOption.USER_AGENT_PREFIX, "rgw_client/1.0.0")
                .build();
        List<RGWClientProperties.EndpointProperties> storages = this.properties.getStorages();
        if (storages.size() == 1) {
            RGWClientProperties.EndpointProperties storage = storages.get(0);
            this.connectors = Collections.singletonMap(storage, create(storage));
            this.supplier = () -> connectors.get(storage);
        } else {
            AtomicInteger counter = new AtomicInteger(-1);
            Supplier<RGWClientProperties.EndpointProperties> storageSupplier = () -> storages.get(getNext(counter, storages.size()));
            this.connectors = new ConcurrentHashMap<>(storages.size());
            this.supplier = () -> connectors.computeIfAbsent(storageSupplier.get(), this::create);
        }
    }

    private RetryPolicy retryPolicy() {
        return properties.isEnableRetry() && properties.getStorages().size() == 1 ? RetryPolicy.builder()
                .retryCondition(RetryCondition.defaultRetryCondition())
                .backoffStrategy(EqualJitterBackoffStrategy.builder()
                        .baseDelay(Duration.ofMillis(properties.getBaseDelayTime()))
                        .maxBackoffTime(Duration.ofMillis(properties.getMaxBackoffTime()))
                        .build())
                .build() : RetryPolicy.none();
    }

    protected abstract C create(RGWClientProperties.EndpointProperties properties);

    @Override
    protected C doGet() {
        return supplier.get();
    }

    @Override
    protected void doClose() {
        for (Map.Entry<RGWClientProperties.EndpointProperties, C> entry : connectors.entrySet()) {
            try {
                entry.getValue().close();
                getLogger().info("Finished to close async connector [{}].", entry.getKey().getEndpoint());
            } catch (Throwable cause) {
                getLogger().error("Failed to close async connector [{}].", entry.getKey().getEndpoint(), cause);
            }
        }
    }
}
