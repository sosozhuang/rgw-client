package io.ceph.rgw.client.model;

import java.util.Collections;
import java.util.List;

/**
 * Created by zhuangshuo on 2020/3/17.
 */
public class ListBucketsResponse implements BucketResponse {
    private final List<Bucket> buckets;

    public ListBucketsResponse(List<Bucket> buckets) {
        this.buckets = Collections.unmodifiableList(buckets);
    }

    public List<Bucket> getBuckets() {
        return buckets;
    }

    @Override
    public String toString() {
        return "ListBucketsResponse{" +
                "buckets=" + buckets +
                '}';
    }
}
