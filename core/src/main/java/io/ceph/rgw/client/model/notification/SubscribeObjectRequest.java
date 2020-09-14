package io.ceph.rgw.client.model.notification;

import io.ceph.rgw.client.SubscribeClient;
import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;
import io.ceph.rgw.client.model.GenericBuilder;
import org.apache.commons.lang3.Validate;

import java.util.Objects;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/6/3.
 */
public class SubscribeObjectRequest implements SubscribeRequest {
    private final String condition;

    public SubscribeObjectRequest(String condition) {
        this.condition = Validate.notBlank(condition, "condition cannot be empty string");
    }

    public String getCondition() {
        return condition;
    }

    @Override
    public String toString() {
        return "SubscribeObjectRequest{" +
                "condition='" + condition + '\'' +
                '}';
    }

    public static class Builder extends GenericBuilder<Builder, SubscribeObjectRequest> {
        private String condition;
        private SubscribeClient client;

        public Builder(SubscribeClient client) {
            this.client = Objects.requireNonNull(client);
        }

        //TODO: provide bool, regex and other condition builders
        public Builder withCondition(String condition) {
            this.condition = condition;
            return self();
        }

        @Override
        public SubscribeObjectRequest build() {
            return new SubscribeObjectRequest(condition);
        }

        public ActionFuture<Void> execute(ActionListener<SubscribeObjectResponse> listener) {
            return client.subscribeObjectAsync(build(), listener);
        }
    }
}
