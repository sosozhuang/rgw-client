package io.ceph.rgw.client.spring.autoconfigure;

import brave.Tracer;
import com.netflix.loadbalancer.ServerList;
import io.ceph.rgw.client.*;
import io.ceph.rgw.client.action.ActionExecutorDecorator;
import io.ceph.rgw.client.core.Connectors;
import io.ceph.rgw.client.spring.loadbalancer.LoadBalancedConnectors;
import io.ceph.rgw.client.spring.trace.TraceActionExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.ServiceInstanceChooser;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.List;
import java.util.Map;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/2/27.
 */
@Configuration
@EnableConfigurationProperties(RGWClientAutoProperties.class)
@RibbonClients({@RibbonClient(name = "storage", configuration = StorageEndpointsConfiguration.class),
        @RibbonClient(name = "subscribe", configuration = SubscribeEndpointsConfiguration.class)})
public class RGWClientConfiguration {
    @Bean("s3Connectors")
    @ConditionalOnClass(value = {ServiceInstanceChooser.class, ServerList.class})
    @Conditional(StorageEndpointsCondition.class)
    Connectors<?> s3Connectors1(RGWClientAutoProperties properties, ServiceInstanceChooser chooser) {
        return LoadBalancedConnectors.createS3(properties.getConnector(), chooser);
    }

    @Bean("adminConnectors")
    @ConditionalOnClass(value = {ServiceInstanceChooser.class, ServerList.class})
    @ConditionalOnProperty(value = {"enableAdmin"}, prefix = RGWClientAutoProperties.NAME)
    @Conditional(StorageEndpointsCondition.class)
    Connectors<?> adminConnectors1(RGWClientAutoProperties properties, ServiceInstanceChooser chooser) {
        return LoadBalancedConnectors.createAdmin(properties.getConnector(), chooser);
    }

    @Bean("subscribeConnectors")
    @ConditionalOnClass(value = {ServiceInstanceChooser.class, ServerList.class})
    @Conditional(MultiSubscribeEndpointsCondition.class)
    Connectors<?> subscribeConnectors1(RGWClientAutoProperties properties, ServiceInstanceChooser chooser) {
        return LoadBalancedConnectors.createSubscribe(properties.getConnector(), chooser);
    }

    @Bean(name = "s3Connectors")
    @ConditionalOnMissingBean(name = "s3Connectors")
    public Connectors<?> s3Connectors2(RGWClientAutoProperties properties) {
        return Connectors.createS3(properties.getConnector());
    }

    @Bean("adminConnectors")
    @ConditionalOnProperty(value = {"enableAdmin"}, prefix = RGWClientAutoProperties.NAME)
    @ConditionalOnMissingBean(name = "adminConnectors")
    Connectors<?> adminConnectors2(RGWClientAutoProperties properties) {
        return Connectors.createAdmin(properties.getConnector());
    }

    @Bean("subscribeConnectors")
    @Conditional(SingleSubscribeEndpointsCondition.class)
    @ConditionalOnMissingBean(name = "subscribeConnectors")
    Connectors<?> subscribeConnectors2(RGWClientAutoProperties properties, ServiceInstanceChooser chooser) {
        return Connectors.createSubscribe(properties.getConnector());
    }

    @Bean
    @ConditionalOnProperty(value = {"enableTrace"}, prefix = RGWClientAutoProperties.NAME)
    @ConditionalOnBean(Tracer.class)
    ActionExecutorDecorator traceActionExecutorDecorator() {
        return TraceActionExecutor::new;
    }

    @Bean
    public Clients clients(RGWClientAutoProperties properties,
                           Map<String, Connectors> connectorsMap,
                           @Autowired(required = false) List<ActionExecutorDecorator> decorators) {
        return Clients.create(properties, connectorsMap, decorators);
    }

    @Bean
    @Lazy
    @ConditionalOnProperty(value = {"enableAdmin"}, prefix = RGWClientAutoProperties.NAME)
    public AdminClient adminClient(Clients clients) {
        return clients.getAdmin();
    }

    @Bean
    @ConditionalOnProperty(value = {"enableBucket"}, matchIfMissing = true, prefix = RGWClientAutoProperties.NAME)
    public BucketClient bucketClient(Clients clients) {
        return clients.getBucket();
    }

    @Bean
    @ConditionalOnProperty(value = {"enableObject"}, matchIfMissing = true, prefix = RGWClientAutoProperties.NAME)
    public ObjectClient objectClient(Clients clients) {
        return clients.getObject();
    }

    @Bean
    @Lazy
    @ConditionalOnBean(name = "subscribeConnectors")
    public SubscribeClient subscribeClient(Clients clients) {
        return clients.getSubscribe();
    }
}
