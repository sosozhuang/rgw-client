package io.ceph.rgw.client.spring.loadbalancer;

import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;
import io.ceph.rgw.client.config.RGWClientProperties;
import org.apache.commons.lang3.Validate;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/3/5.
 */
public class StaticServerList implements ServerList<Server> {
    private final List<Server> servers;

    public StaticServerList(List<RGWClientProperties.EndpointProperties> endpoints) {
        Validate.notEmpty(endpoints, "endpoints cannot be empty");
        this.servers = Collections.unmodifiableList(endpoints.stream()
                .map(e -> new Server(e.getEndpoint())).collect(Collectors.toList()));
    }

    @Override
    public List<Server> getInitialListOfServers() {
        return servers;
    }

    @Override
    public List<Server> getUpdatedListOfServers() {
        return servers;
    }

}
