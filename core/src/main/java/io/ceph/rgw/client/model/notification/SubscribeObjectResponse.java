package io.ceph.rgw.client.model.notification;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/6/4.
 */
public class SubscribeObjectResponse implements SubscribeResponse {
    private final ObjectMetadataInfo objectMetadataInfo;

    public SubscribeObjectResponse(ObjectMetadataInfo objectMetadataInfo) {
        this.objectMetadataInfo = objectMetadataInfo;
    }

    public ObjectMetadataInfo getObjectMetadataInfo() {
        return objectMetadataInfo;
    }

    @Override
    public String toString() {
        return "SubscribeObjectResponse{" +
                "objectMetadataInfo=" + objectMetadataInfo +
                '}';
    }
}
