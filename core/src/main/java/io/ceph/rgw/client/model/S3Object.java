package io.ceph.rgw.client.model;

import java.util.Date;

/**
 * Created by zhuangshuo on 2020/10/10.
 */
public class S3Object {
    private final String key;
    private final String eTag;
    private final long size;
    private final Date lastModified;
    private final String storageClass;
    private final Owner owner;

    public S3Object(String key, String eTag, long size, Date lastModified, String storageClass, Owner owner) {
        this.key = key;
        this.eTag = eTag;
        this.size = size;
        this.lastModified = lastModified;
        this.storageClass = storageClass;
        this.owner = owner;
    }

    public String getKey() {
        return key;
    }

    public String geteTag() {
        return eTag;
    }

    public long getSize() {
        return size;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public String getStorageClass() {
        return storageClass;
    }

    public Owner getOwner() {
        return owner;
    }

    @Override
    public String toString() {
        return "S3Object{" +
                "key='" + key + '\'' +
                ", eTag='" + eTag + '\'' +
                ", size=" + size +
                ", lastModified=" + lastModified +
                ", storageClass='" + storageClass + '\'' +
                ", owner=" + owner +
                '}';
    }
}
