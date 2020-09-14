package io.ceph.rgw.client.util;

import org.apache.commons.lang3.Validate;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/3/4.
 */
public class StringPairCollection implements Iterable<Map.Entry<String, String>> {
    private final Map<String, String> map;

    public StringPairCollection(Map<String, String> map) {
        if (map == null || map.size() == 0) {
            this.map = Collections.emptyMap();
        } else {
            Validate.isTrue(!map.containsKey(null), "cannot contain null key");
            Validate.isTrue(!map.containsValue(null), "cannot contain null value");
            this.map = Collections.unmodifiableMap(map);
        }
    }

    public String get(String key) {
        return map.get(key);
    }

    public Map<String, String> all() {
        return map;
    }

    @Override
    public Iterator<Map.Entry<String, String>> iterator() {
        return map.entrySet().iterator();
    }

    @Override
    public String toString() {
        return "StringPairCollection{" +
                "map=" + map +
                '}';
    }
}
