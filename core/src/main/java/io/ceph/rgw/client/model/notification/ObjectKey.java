package io.ceph.rgw.client.model.notification;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/5/13.
 */
public class ObjectKey {
    private final String instance;
    private final String name;

    public ObjectKey(String instance, String name) {
        this.instance = instance;
        this.name = name;
    }

    public String getInstance() {
        return instance;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Key{" +
                "instance='" + instance + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
