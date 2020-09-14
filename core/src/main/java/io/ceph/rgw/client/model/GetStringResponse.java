package io.ceph.rgw.client.model;

/**
 * Created by zhuangshuo on 2020/3/13.
 */
public class GetStringResponse extends BaseGetObjectResponse {
    private final String content;

    public GetStringResponse(String versionId, Integer taggingCount, Metadata metadata, String content) {
        super(versionId, taggingCount, metadata);
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "GetStringResponse{} " + super.toString();
    }
}
