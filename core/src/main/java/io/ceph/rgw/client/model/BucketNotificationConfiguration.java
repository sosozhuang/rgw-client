package io.ceph.rgw.client.model;

import org.apache.commons.lang3.Validate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhuangshuo on 2020/4/28.
 */
public class BucketNotificationConfiguration {
    private final Map<String, NotificationConfiguration> configurations;

    public BucketNotificationConfiguration(Map<String, NotificationConfiguration> configurations) {
        if (configurations != null && configurations.size() > 0) {
            for (Map.Entry<String, NotificationConfiguration> entry : configurations.entrySet()) {
                Validate.notBlank(entry.getKey(), "name cannot be empty string");
                Validate.notNull(entry.getValue(), "configuration cannot be null");
            }
            this.configurations = Collections.unmodifiableMap(configurations);
        } else {
            this.configurations = Collections.emptyMap();
        }
    }

    public Map<String, NotificationConfiguration> getNotificationConfigurations() {
        return configurations;
    }

    @Override
    public String toString() {
        return "BucketNotificationConfiguration{" +
                "notificationConfigurations=" + configurations +
                '}';
    }

    static class NestedBuilder<T extends NestedBuilder<T>> extends NestedGenericBuilder<T, BucketNotificationConfiguration> {
        private final Map<String, NotificationConfiguration> configurations;

        NestedBuilder() {
            this.configurations = new HashMap<>();
        }

        public T withNotificationConfigurations(Map<String, NotificationConfiguration> configurations) {
            this.configurations.clear();
            this.configurations.putAll(configurations);
            return self();
        }

        public T addNotificationConfiguration(String name, NotificationConfiguration value) {
            configurations.put(name, value);
            return self();
        }

        public QueueConfigurationBuilder<T> addQueueConfiguration() {
            return new QueueConfigurationBuilder<>(this);
        }

        public TopicConfigurationBuilder<T> addTopicConfiguration() {
            return new TopicConfigurationBuilder<>(this);
        }

        @Override
        protected BucketNotificationConfiguration build() {
            return new BucketNotificationConfiguration(configurations);
        }
    }

    public static class QueueConfigurationBuilder<T extends NestedBuilder<T>> extends QueueConfiguration.NestedBuilder<QueueConfigurationBuilder<T>> {
        private final NestedBuilder<T> builder;
        private String name;

        QueueConfigurationBuilder(NestedBuilder<T> builder) {
            this.builder = builder;
        }

        public QueueConfigurationBuilder<T> withName(String name) {
            this.name = name;
            return self();
        }

        public T endQueueConfiguration() {
            return builder.addNotificationConfiguration(name, build());
        }
    }

    public static class TopicConfigurationBuilder<T extends NestedBuilder<T>> extends TopicConfiguration.NestedBuilder<TopicConfigurationBuilder<T>> {
        private final NestedBuilder<T> builder;
        private String name;

        TopicConfigurationBuilder(NestedBuilder<T> builder) {
            this.builder = builder;
        }

        public TopicConfigurationBuilder<T> withName(String name) {
            this.name = name;
            return self();
        }

        public T endTopicConfiguration() {
            return builder.addNotificationConfiguration(name, build());
        }
    }

    public static class Builder extends NestedBuilder<Builder> {
        @Override
        public BucketNotificationConfiguration build() {
            return super.build();
        }
    }
}
