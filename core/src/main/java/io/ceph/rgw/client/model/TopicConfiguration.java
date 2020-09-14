package io.ceph.rgw.client.model;

import org.apache.commons.lang3.Validate;

import java.util.Set;

/**
 * Created by zhuangshuo on 2020/4/28.
 */
public class TopicConfiguration extends NotificationConfiguration {
    private final String topicARN;

    public TopicConfiguration(String topicARN, Set<Event> events, Filter filter) {
        super(events, filter);
        this.topicARN = Validate.notBlank(topicARN, "topicARN cannot be empty string");
    }

    public String getTopicARN() {
        return topicARN;
    }

    @Override
    public String toString() {
        return "TopicConfiguration{" +
                "topicARN='" + topicARN + '\'' +
                "} " + super.toString();
    }

    static class NestedBuilder<T extends NestedBuilder<T>> extends NotificationConfiguration.NestedBuilder<T, TopicConfiguration> {
        private String topicARN;

        public T withTopicARN(String topicARN) {
            this.topicARN = topicARN;
            return self();
        }

        @Override
        protected TopicConfiguration build() {
            return new TopicConfiguration(topicARN, events, filter);
        }
    }

    public static class Builder extends NestedBuilder<Builder> {
        @Override
        public TopicConfiguration build() {
            return super.build();
        }
    }
}
