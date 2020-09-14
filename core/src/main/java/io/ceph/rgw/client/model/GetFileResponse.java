package io.ceph.rgw.client.model;

/**
 * Created by zhuangshuo on 2020/3/13.
 */
public class GetFileResponse extends BaseGetObjectResponse {

    public GetFileResponse(String versionId, Integer taggingCount, Metadata metadata) {
        super(versionId, taggingCount, metadata);
    }

    @Override
    public String toString() {
        return "GetFileResponse{} " + super.toString();
    }
}
