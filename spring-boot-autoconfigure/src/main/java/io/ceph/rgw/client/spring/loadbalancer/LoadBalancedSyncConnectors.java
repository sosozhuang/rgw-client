package io.ceph.rgw.client.spring.loadbalancer;

import com.amazonaws.services.s3.AmazonS3;
import io.ceph.rgw.client.config.RGWClientProperties;
import io.ceph.rgw.client.core.sync.SyncConnectors;
import io.ceph.rgw.client.exception.RGWClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.loadbalancer.ServiceInstanceChooser;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/3/5.
 */
public class LoadBalancedSyncConnectors extends SyncConnectors {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoadBalancedSyncConnectors.class);
    private final ServiceInstanceChooser chooser;
    private final Map<String, RGWClientProperties.EndpointProperties> endpoints;

    LoadBalancedSyncConnectors(RGWClientProperties.ConnectorProperties properties, ServiceInstanceChooser chooser) {
        super(properties);
        this.chooser = Objects.requireNonNull(chooser);
        this.endpoints = this.properties.getStorages().stream().collect(Collectors.toMap(RGWClientProperties.EndpointProperties::getEndpoint, Function.identity()));
    }

    @Override
    public AmazonS3 doGet() {
        String endpoint = LoadBalancedConnectors.getEndpoint("storage", chooser);
        if (endpoint == null) {
            LOGGER.warn("ServiceInstanceChooser choose return null, pick a default connector.");
            return getDefaultConnector();
        }
        return connectors.computeIfAbsent(endpoints.get(endpoint), this::create);
    }

    private AmazonS3 getDefaultConnector() {
        try {
            return connectors.entrySet().iterator().next().getValue();
        } catch (NoSuchElementException e) {
            throw new RGWClientException("no connector available", e);
        }
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}
