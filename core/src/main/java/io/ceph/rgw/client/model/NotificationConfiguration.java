package io.ceph.rgw.client.model;

import org.apache.commons.lang3.Validate;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Created by zhuangshuo on 2020/4/28.
 */
public abstract class NotificationConfiguration {
    private final Set<Event> events;
    private final Filter filter;

    protected NotificationConfiguration(Set<Event> events, Filter filter) {
        if (events != null && events.size() != 0) {
            Validate.noNullElements(events, "events cannot contains null elements");
            this.events = Collections.unmodifiableSet(Objects.requireNonNull(events));
        } else {
            this.events = Collections.emptySet();
        }
        this.filter = filter;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public Filter getFilter() {
        return filter;
    }

    @Override
    public String toString() {
        return "NotificationConfiguration{" +
                "events=" + events +
                ", filter=" + filter +
                '}';
    }

    static abstract class NestedBuilder<T extends NestedBuilder<T, N>, N extends NotificationConfiguration> extends NestedGenericBuilder<T, N> {
        Set<Event> events;
        Filter filter;

        NestedBuilder() {
            this.events = new HashSet<>();
        }

        public T addEvent(Event event) {
            this.events.add(event);
            return self();
        }

        public T withEvents(Set<Event> events) {
            this.events.clear();
            this.events.addAll(events);
            return self();
        }

        public FilterBuilder<T, N> withFilter() {
            return new FilterBuilder<>(this);
        }

        public T withFilter(Filter filter) {
            this.filter = filter;
            return self();
        }
    }

    public static class FilterBuilder<T extends NestedBuilder<T, N>, N extends NotificationConfiguration> extends Filter.NestedBuilder<FilterBuilder<T, N>> {
        private final NestedBuilder<T, N> builder;

        FilterBuilder(NestedBuilder<T, N> builder) {
            this.builder = builder;
        }

        public T endFilter() {
            return builder.withFilter(build());
        }
    }
}
