package io.ceph.rgw.client.model;

import org.apache.commons.lang3.Validate;

import java.util.Set;

/**
 * Created by zhuangshuo on 2020/4/28.
 */
public class QueueConfiguration extends NotificationConfiguration {
    private final String queueARN;

    public QueueConfiguration(String queueARN, Set<Event> events, Filter filter) {
        super(events, filter);
        this.queueARN = Validate.notBlank(queueARN, "queueARN cannot be empty string");
    }

    public String getQueueARN() {
        return queueARN;
    }

    @Override
    public String toString() {
        return "QueueConfiguration{" +
                "queueARN='" + queueARN + '\'' +
                "} " + super.toString();
    }

    static class NestedBuilder<T extends NestedBuilder<T>> extends NotificationConfiguration.NestedBuilder<T, QueueConfiguration> {
        private String queueARN;

        public T withQueueARN(String queueARN) {
            this.queueARN = queueARN;
            return self();
        }

        @Override
        protected QueueConfiguration build() {
            return new QueueConfiguration(queueARN, events, filter);
        }
    }

    public static class Builder extends NestedBuilder<Builder> {
        @Override
        public QueueConfiguration build() {
            return super.build();
        }
    }
}
