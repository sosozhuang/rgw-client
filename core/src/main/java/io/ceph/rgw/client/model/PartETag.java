package io.ceph.rgw.client.model;

import org.apache.commons.lang3.Validate;

/**
 * Created by zhuangshuo on 2020/3/26.
 */
public class PartETag {
    private final int part;
    private final String eTag;

    public PartETag(int part, String eTag) {
        Validate.isTrue(part > 0, "part cannot be non positive");
        this.part = part;
        this.eTag = Validate.notBlank(eTag, "eTag cannot be empty string");
    }

    public int getPart() {
        return part;
    }

    public String getETag() {
        return eTag;
    }

    @Override
    public String toString() {
        return "PartETag{" +
                "part=" + part +
                ", eTag='" + eTag + '\'' +
                '}';
    }
}
