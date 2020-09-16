package io.ceph.rgw.client.spring.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/4/27.
 */
public class SingleSubscribeEndpointsCondition extends EndpointsCondition {
    @Override
    String getKeyPrefix() {
        return "rgwclient.connector.subscribes";
    }

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return new ConditionOutcome(getEndpoints(context.getEnvironment()).size() == 1, "Subscribe endpoints");
    }
}
