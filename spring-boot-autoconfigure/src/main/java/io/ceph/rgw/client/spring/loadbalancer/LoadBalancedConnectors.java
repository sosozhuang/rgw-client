package io.ceph.rgw.client.spring.loadbalancer;

import io.ceph.rgw.client.config.RGWClientProperties;
import io.ceph.rgw.client.core.Connectors;
import io.ceph.rgw.client.exception.RGWClientException;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.ServiceInstanceChooser;

import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/3/16.
 */
public final class LoadBalancedConnectors {

    static String getEndpoint(String name, ServiceInstanceChooser chooser) {
        ServiceInstance server = chooser.choose(name);
        if (server == null) {
            return null;
        }
        return String.format("%s:%d", server.getHost(), server.getPort());
    }

    static <C> C getDefaultConnector(Map<RGWClientProperties.EndpointProperties, C> connectors) {
        try {
            return connectors.entrySet().iterator().next().getValue();
        } catch (NoSuchElementException e) {
            throw new RGWClientException("no connector available", e);
        }
    }

    public static Connectors<?> createS3(RGWClientProperties.ConnectorProperties properties, ServiceInstanceChooser chooser) {
        Connectors<?> connectors;
        if (Connectors.isAsync()) {
            connectors = new LoadBalancedS3AsyncConnectors(properties, chooser);
        } else {
            connectors = new LoadBalancedSyncConnectors(properties, chooser);
        }
        return connectors;
    }

    public static Connectors<?> createAdmin(RGWClientProperties.ConnectorProperties properties, ServiceInstanceChooser chooser) {
        if (Connectors.isAsync()) {
            return new LoadBalancedAdminAsyncConnectors(properties, chooser);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public static Connectors<?> createSubscribe(RGWClientProperties.ConnectorProperties properties, ServiceInstanceChooser chooser) {
        return new LoadBalancedSubscribeConnectors(properties, chooser);
    }
}
