package io.ceph.rgw.client.model;

/**
 * Created by zhuangshuo on 2020/4/28.
 */
public enum Event {
    OBJECT_CREATED("s3:ObjectCreated:*"),
    OBJECT_CREATED_BY_PUT("s3:ObjectCreated:Put"),
    OBJECT_CREATED_BY_POST("s3:ObjectCreated:Post"),
    OBJECT_CREATED_BY_COPY("s3:ObjectCreated:Copy"),
    OBJECT_CREATED_BY_COMPLETE_MULTIPART_UPLOAD("s3:ObjectCreated:CompleteMultipartUpload"),
    OBJECT_REMOVED("s3:ObjectRemoved:*"),
    OBJECT_REMOVED_DELETE("s3:ObjectRemoved:Delete"),
    OBJECT_REMOVED_DELETE_MARKER_CREATED("s3:ObjectRemoved:DeleteMarkerCreated");

    private final String event;

    Event(String event) {
        this.event = event;
    }

    @Override
    public String toString() {
        return event;
    }
}
