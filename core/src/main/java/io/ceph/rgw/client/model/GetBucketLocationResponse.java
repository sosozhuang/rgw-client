package io.ceph.rgw.client.model;

/**
 * Created by zhuangshuo on 2020/3/17.
 */
public class GetBucketLocationResponse implements BucketResponse {
    private final String location;

    public GetBucketLocationResponse(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "GetBucketLocationResponse{" +
                "location='" + location + '\'' +
                '}';
    }
}
