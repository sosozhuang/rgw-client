package io.ceph.rgw.client.spring.loadbalancer;

import io.ceph.rgw.client.config.RGWClientProperties;
import io.ceph.rgw.client.core.subscribe.RequestInterceptor;
import io.ceph.rgw.client.core.subscribe.SubscribeConnector;
import io.ceph.rgw.client.core.subscribe.SubscribeConnectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.loadbalancer.ServiceInstanceChooser;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/8/24.
 */
public class LoadBalancedSubscribeConnectors extends SubscribeConnectors {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoadBalancedSubscribeConnectors.class);
    private final ServiceInstanceChooser chooser;
    private final Map<String, RGWClientProperties.EndpointProperties> endpoints;

    public LoadBalancedSubscribeConnectors(RGWClientProperties.ConnectorProperties properties, ServiceInstanceChooser chooser) {
        this(properties, RequestInterceptor.NOOP, chooser);
    }

    public LoadBalancedSubscribeConnectors(RGWClientProperties.ConnectorProperties properties, RequestInterceptor interceptor, ServiceInstanceChooser chooser) {
        super(properties, interceptor);
        this.chooser = Objects.requireNonNull(chooser);
        this.endpoints = this.properties.getSubscribes().stream().collect(Collectors.toMap(RGWClientProperties.EndpointProperties::getEndpoint, Function.identity()));
    }

    @Override
    protected SubscribeConnector doGet() {
        String endpoint = LoadBalancedConnectors.getEndpoint("subscribe", chooser);
        if (StringUtils.isBlank(endpoint)) {
            LOGGER.warn("ServiceInstanceChooser choose return null, pick a default connector.");
            return LoadBalancedConnectors.getDefaultConnector(connectors);
        }
        return connectors.computeIfAbsent(endpoints.get(endpoint), this::create);
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}
