package io.ceph.rgw.client.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zhuangshuo on 2020/4/28.
 */
public class KeyFilter {
    private final List<FilterRule> filterRules;

    public KeyFilter(List<FilterRule> filterRules) {
        if (filterRules != null && filterRules.size() > 0) {
            this.filterRules = Collections.unmodifiableList(filterRules);
        } else {
            this.filterRules = Collections.emptyList();
        }
    }

    public List<FilterRule> getFilterRules() {
        return filterRules;
    }

    @Override
    public String toString() {
        return "KeyFilter{" +
                "filterRules=" + filterRules +
                '}';
    }

    static class NestedBuilder<T extends NestedBuilder<T>> extends NestedGenericBuilder<T, KeyFilter> {
        private List<FilterRule> filterRules;

        NestedBuilder() {
            this.filterRules = new ArrayList<>();
        }

        public T addFilterRule(String name, String value) {
            this.filterRules.add(new FilterRule(name, value));
            return self();
        }

        public T addFilterRule(FilterRule filterRule) {
            this.filterRules.add(filterRule);
            return self();
        }

        public T withFilterRules(List<FilterRule> filterRules) {
            this.filterRules.clear();
            this.filterRules.addAll(filterRules);
            return self();
        }

        @Override
        protected KeyFilter build() {
            return new KeyFilter(filterRules);
        }
    }

    public static class Builder extends NestedBuilder<Builder> {
        @Override
        public KeyFilter build() {
            return super.build();
        }
    }
}
