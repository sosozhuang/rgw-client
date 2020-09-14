package io.ceph.rgw.client.model;

import io.ceph.rgw.client.exception.RGWClientException;

import java.io.InputStream;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by zhuangshuo on 2020/3/8.
 */
public abstract class BaseGetObjectResponse implements ObjectResponse {
    private final String versionId;
    private final Integer taggingCount;
    private final Metadata metadata;
    private final AtomicReference<InputStream> content;

    BaseGetObjectResponse(String versionId, Integer taggingCount, Metadata metadata) {
        this(versionId, taggingCount, metadata, null);
    }

    BaseGetObjectResponse(String versionId, Integer taggingCount, Metadata metadata, InputStream content) {
        this.versionId = versionId;
        this.taggingCount = taggingCount;
        this.metadata = metadata;
        this.content = new AtomicReference<>(content);
    }

    public String getVersionId() {
        return versionId;
    }

    public int getTaggingCount() {
        return taggingCount;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    InputStream doGetContent() {
        InputStream is = content.getAndSet(null);
        if (is == null) {
            throw new RGWClientException("cannot get object again");
        }
        return is;
    }

    @Override
    public String toString() {
        return "BaseGetObjectResponse{" +
                "versionId='" + versionId + '\'' +
                ", taggingCount=" + taggingCount +
                ", metadata=" + metadata +
                ", content=" + content +
                '}';
    }
}
