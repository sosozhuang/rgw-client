package io.ceph.rgw.client.model.notification;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/5/13.
 */
public class ObjectInfo {
    private final ObjectAttrs attrs;
    private final Bucket bucket;
    private final ObjectKey key;

    public ObjectInfo(ObjectAttrs attrs, Bucket bucket, ObjectKey key) {
        this.attrs = attrs;
        this.bucket = bucket;
        this.key = key;
    }

    public ObjectAttrs getAttrs() {
        return attrs;
    }

    public Bucket getBucket() {
        return bucket;
    }

    public ObjectKey getKey() {
        return key;
    }

    @Override
    public String toString() {
        return "Info{" +
                "attrs='" + attrs + '\'' +
                ", bucket='" + bucket + '\'' +
                ", key=" + key +
                '}';
    }
}
