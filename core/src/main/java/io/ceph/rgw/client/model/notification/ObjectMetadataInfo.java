package io.ceph.rgw.client.model.notification;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.ceph.rgw.client.model.Metadata;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/5/22.
 */
@JsonSerialize(using = ObjectMetadataInfoSerializer.class)
@JsonDeserialize(using = ObjectMetadataInfoDeserializer.class)
public class ObjectMetadataInfo {
    private final ObjectInfo info;
    private final Metadata metadata;

    public ObjectMetadataInfo(ObjectInfo info, Metadata metadata) {
        this.info = info;
        this.metadata = metadata;
    }

    public ObjectInfo getInfo() {
        return info;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    @Override
    public String toString() {
        return "ObjectMetadataInfo{" +
                "info=" + info +
                ", metadata=" + metadata +
                '}';
    }
}
