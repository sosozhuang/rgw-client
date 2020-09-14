package io.ceph.rgw.client.model;

import io.ceph.rgw.client.ObjectClient;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;

import java.util.Arrays;

/**
 * Created by zhuangshuo on 2020/7/8.
 */
public abstract class BaseGetObjectRequest extends BaseObjectRequest {
    private final long[] range;

    protected BaseGetObjectRequest(String bucketName, String key, String versionId, long[] range) {
        super(bucketName, key, versionId);
        if (ArrayUtils.isNotEmpty(range)) {
            Validate.isTrue(range.length == 2);
            Validate.isTrue(0 <= range[0] && range[0] <= range[1]);
        }
        this.range = range;
    }

    public long[] getRange() {
        return range;
    }

    @Override
    public String toString() {
        return "BaseGetObjectRequest{" +
                "range=" + Arrays.toString(range) +
                "} " + super.toString();
    }

    public static abstract class Builder<T extends Builder<T, REQ, RESP>, REQ extends BaseGetObjectRequest, RESP extends BaseGetObjectResponse> extends BaseObjectRequest.Builder<T, REQ, RESP> {
        protected long[] range;

        protected Builder(ObjectClient client) {
            super(client);
        }

        public T withRange(long start) {
            return withRange(start, Long.MAX_VALUE - 1);
        }

        public T withRange(long start, long end) {
            this.range = new long[]{start, end};
            return self();
        }

        public T withRange(long[] range) {
            this.range = range;
            return self();
        }
    }
}
