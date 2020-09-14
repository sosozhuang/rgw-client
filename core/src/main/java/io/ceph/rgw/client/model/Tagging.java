package io.ceph.rgw.client.model;

import io.ceph.rgw.client.util.StringPairCollection;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhuangshuo on 2020/3/1.
 */
public class Tagging extends StringPairCollection {
    public Tagging(Map<String, String> map) {
        super(map);
    }

    @Override
    public String toString() {
        return "Tagging{} " + super.toString();
    }

    static class NestedBuilder<T extends NestedBuilder<T>> extends NestedGenericBuilder<T, Tagging> {
        private final Map<String, String> map;

        NestedBuilder() {
            this.map = new HashMap<>();
        }

        public T add(String key, String value) {
            map.put(key, value);
            return self();
        }

        @Override
        protected Tagging build() {
            return new Tagging(map);
        }
    }

    public static class Builder extends NestedBuilder<Builder> {
        public Tagging build() {
            return super.build();
        }
    }
}
