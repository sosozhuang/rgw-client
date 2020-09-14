package io.ceph.rgw.client.model;

import org.apache.commons.lang3.Validate;

/**
 * Created by zhuangshuo on 2020/4/28.
 */
public class FilterRule {
    private final String name;
    private final String value;

    public FilterRule(String name, String value) {
        this.name = Validate.notBlank(name, "name cannot be empty string");
        this.value = Validate.notBlank(value, "value cannot be empty string");
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "FilterRule{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
