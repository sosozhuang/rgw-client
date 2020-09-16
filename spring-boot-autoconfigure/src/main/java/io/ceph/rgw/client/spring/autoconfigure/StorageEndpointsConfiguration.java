package io.ceph.rgw.client.spring.autoconfigure;

import com.netflix.loadbalancer.DummyPing;
import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;
import io.ceph.rgw.client.spring.loadbalancer.StaticServerList;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;
import org.springframework.context.annotation.Bean;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/3/5.
 */
@ConditionalOnClass(RibbonLoadBalancerClient.class)
public class StorageEndpointsConfiguration {
    @Bean
    public IPing ribbonPing() {
        return new DummyPing();
    }

    @Bean
    public ServerList<Server> ribbonServerList(RGWClientAutoProperties properties) {
        return new StaticServerList(properties.getConnector().getStorages());
    }
}
