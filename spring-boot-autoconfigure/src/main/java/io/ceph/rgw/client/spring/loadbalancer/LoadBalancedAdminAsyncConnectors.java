package io.ceph.rgw.client.spring.loadbalancer;

import io.ceph.rgw.client.config.RGWClientProperties;
import io.ceph.rgw.client.core.async.AdminAsyncConnector;
import io.ceph.rgw.client.core.async.AdminAsyncConnectors;
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
 * Created by zhuangshuo on 2020/7/30.
 */
public class LoadBalancedAdminAsyncConnectors extends AdminAsyncConnectors {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoadBalancedAdminAsyncConnectors.class);
    private final ServiceInstanceChooser chooser;
    private final Map<String, RGWClientProperties.EndpointProperties> endpoints;

    public LoadBalancedAdminAsyncConnectors(RGWClientProperties.ConnectorProperties properties, ServiceInstanceChooser chooser) {
        super(properties);
        this.chooser = Objects.requireNonNull(chooser);
        this.endpoints = this.properties.getStorages().stream().collect(Collectors.toMap(RGWClientProperties.EndpointProperties::getEndpoint, Function.identity()));
    }

    @Override
    protected AdminAsyncConnector doGet() {
        String endpoint = LoadBalancedConnectors.getEndpoint("storage", chooser);
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
