package io.ceph.rgw.client.core.subscribe;

import io.ceph.rgw.client.concurrent.SimpleThreadFactory;
import io.ceph.rgw.client.config.RGWClientProperties;
import io.ceph.rgw.client.core.GenericConnectors;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/3/18.
 */
public class SubscribeConnectors extends GenericConnectors<SubscribeConnector> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubscribeConnectors.class);
    final Bootstrap bootstrap;
    final EventLoopGroup eventLoopGroup;
    final ChannelPoolHandler poolHandler;
    final HttpHeaders headers;
    final RequestInterceptor interceptor;
    private final Supplier<SubscribeConnector> supplier;
    protected final Map<RGWClientProperties.EndpointProperties, SubscribeConnector> connectors;

    public SubscribeConnectors(RGWClientProperties.ConnectorProperties properties) {
        this(properties, RequestInterceptor.NOOP);
    }

    public SubscribeConnectors(RGWClientProperties.ConnectorProperties properties, RequestInterceptor interceptor) {
        super(properties);
        this.interceptor = Objects.requireNonNull(interceptor);
        this.bootstrap = new Bootstrap();
        this.eventLoopGroup = new NioEventLoopGroup(0, new SimpleThreadFactory("rgw-client-subscribe"));
        this.bootstrap.group(this.eventLoopGroup).channel(NioSocketChannel.class)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .option(ChannelOption.SO_KEEPALIVE, properties.isEnableKeepAlive())
                .option(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, properties.getConnectionTimeout())
                .option(ChannelOption.SO_TIMEOUT, properties.getSocketTimeout());
        this.poolHandler = new SubscribeChannelPoolHandler();
        this.headers = new DefaultHttpHeaders();
        this.headers.add(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE)
                .add(HttpHeaderNames.ACCEPT, HttpHeaderValues.APPLICATION_JSON)
                .add(HttpHeaderNames.ACCEPT, HttpHeaderValues.TEXT_PLAIN)
                .add(HttpHeaderNames.ACCEPT_ENCODING, HttpHeaderValues.GZIP_DEFLATE)
                .add(HttpHeaderNames.USER_AGENT, getUserAgent());
        List<RGWClientProperties.EndpointProperties> subscribes = this.properties.getSubscribes();
        if (subscribes.size() == 1) {
            RGWClientProperties.EndpointProperties subscribe = subscribes.get(0);
            this.connectors = Collections.singletonMap(subscribe, create(subscribe));
            this.supplier = () -> connectors.get(subscribe);
        } else {
            AtomicInteger counter = new AtomicInteger(-1);
            Supplier<RGWClientProperties.EndpointProperties> endpointSupplier = () -> subscribes.get(getNext(counter, subscribes.size()));
            this.connectors = new ConcurrentHashMap<>(subscribes.size());
            this.supplier = () -> connectors.computeIfAbsent(endpointSupplier.get(), this::create);
        }
    }

    private static String getUserAgent() {
        return "rgw_client/1.0.0 " + StringUtils.replace(SystemUtils.OS_NAME, " ", "_") +
                '/' + StringUtils.replace(SystemUtils.OS_VERSION, " ", "_") +
                ' ' + StringUtils.replace(SystemUtils.JAVA_VM_NAME, " ", "_") +
                '/' + StringUtils.replace(SystemUtils.JAVA_VM_VERSION, " ", "_") +
                '/' + StringUtils.replace(SystemUtils.JAVA_VERSION, " ", "_");
    }

    protected SubscribeConnector create(RGWClientProperties.EndpointProperties endpointProperties) {
        String[] hostPort = endpointProperties.getEndpoint().split(":");
        Bootstrap bootstrap = this.bootstrap.clone().remoteAddress(hostPort[0], Integer.valueOf(hostPort[1]));
        return new SubscribeConnector(bootstrap, poolHandler, properties.getMaxConnections(), this::interceptHttpRequest);
    }

    protected void interceptHttpRequest(HttpRequest request) {
        request.headers().add(headers);
        if (!request.headers().contains(HttpHeaderNames.CONTENT_TYPE)) {
            request.headers().add(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_OCTET_STREAM);
        }
        interceptor.intercept(request);
    }

    @Override
    protected SubscribeConnector doGet() {
        return supplier.get();
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @Override
    protected void doClose() {
        for (Map.Entry<RGWClientProperties.EndpointProperties, SubscribeConnector> entry : connectors.entrySet()) {
            try {
                entry.getValue().close();
                getLogger().info("Finished to close subscribe connector [{}].", entry.getKey().getEndpoint());
            } catch (Throwable cause) {
                getLogger().error("Failed to close subscribe connector [{}].", entry.getKey().getEndpoint(), cause);
            }
        }
        if (eventLoopGroup != null) {
            try {
                eventLoopGroup.shutdownGracefully().sync();
                getLogger().info("Finished to close eventLoopGroup.");
            } catch (Throwable cause) {
                getLogger().error("Failed to close eventLoopGroup.", cause);
            }
        }
    }
}
