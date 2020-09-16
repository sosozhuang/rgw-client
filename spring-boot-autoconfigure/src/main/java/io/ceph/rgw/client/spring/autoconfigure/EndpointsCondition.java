package io.ceph.rgw.client.spring.autoconfigure;

import io.ceph.rgw.client.config.RGWClientProperties;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;

import java.util.Collections;
import java.util.List;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/4/27.
 */
abstract class EndpointsCondition extends SpringBootCondition {
    abstract String getKeyPrefix();

    List<RGWClientProperties.EndpointProperties> getEndpoints(Environment environment) {
        return Binder.get(environment)
                .bind(getKeyPrefix(), Bindable.listOf(RGWClientProperties.EndpointProperties.class))
                .orElse(Collections.emptyList());
    }
}
