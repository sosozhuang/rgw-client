package io.ceph.rgw.client.model;

import java.util.Date;

/**
 * Created by zhuangshuo on 2020/3/26.
 */
public class MultipartUpload {
    private final String key;
    private final String uploadId;
    private final Owner owner;
    private final Owner initiator;
    private final Date initiated;

    public MultipartUpload(String key, String uploadId, Owner owner, Owner initiator, Date initiated) {
        this.key = key;
        this.uploadId = uploadId;
        this.owner = owner;
        this.initiator = initiator;
        this.initiated = initiated;
    }

    public String getKey() {
        return key;
    }

    public String getUploadId() {
        return uploadId;
    }

    public Owner getOwner() {
        return owner;
    }

    public Owner getInitiator() {
        return initiator;
    }

    public Date getInitiated() {
        return initiated;
    }

    @Override
    public String toString() {
        return "MultipartUpload{" +
                "key='" + key + '\'' +
                ", uploadId='" + uploadId + '\'' +
                ", owner=" + owner +
                ", initiator=" + initiator +
                ", initiated=" + initiated +
                '}';
    }
}
