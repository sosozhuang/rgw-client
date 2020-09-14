package io.ceph.rgw.client.model;

import java.io.InputStream;

/**
 * Created by zhuangshuo on 2020/3/13.
 */
public class GetInputStreamResponse extends BaseGetObjectResponse {
    public GetInputStreamResponse(String versionId, Integer taggingCount, Metadata metadata, InputStream content) {
        super(versionId, taggingCount, metadata, content);
    }

    public InputStream getContent() {
        return super.doGetContent();
    }

    @Override
    public String toString() {
        return "GetInputStreamResponse{} " + super.toString();
    }
}
