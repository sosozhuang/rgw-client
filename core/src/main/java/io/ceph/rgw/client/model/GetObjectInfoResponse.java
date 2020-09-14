package io.ceph.rgw.client.model;

/**
 * Created by zhuangshuo on 2020/3/16.
 */
public class GetObjectInfoResponse implements ObjectResponse {
    private final Metadata metadata;

    public GetObjectInfoResponse(Metadata metadata) {
        this.metadata = metadata;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    @Override
    public String toString() {
        return "GetObjectInfoResponse{" +
                "metadata=" + metadata +
                '}';
    }
}
