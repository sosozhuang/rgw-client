package io.ceph.rgw.client.model;

import io.ceph.rgw.client.ObjectClient;
import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;

import java.io.InputStream;

/**
 * Created by zhuangshuo on 2020/3/1.
 */
public class PutInputStreamRequest extends GenericPutObjectRequest<InputStream> {
    public PutInputStreamRequest(String bucketName, String key, InputStream value, Metadata metadata, Tagging tagging, ACL acl, CannedACL cannedACL) {
        super(bucketName, key, value, metadata, tagging, acl, cannedACL);
    }

    @Override
    public String toString() {
        return "PutInputStreamRequest{} " + super.toString();
    }

    public static final class Builder extends GenericPutObjectRequest.Builder<Builder, InputStream> {
        public Builder(ObjectClient client) {
            super(client);
        }

        @Override
        public PutInputStreamRequest build() {
            return new PutInputStreamRequest(bucketName, key, value, metadata, tagging, acl, cannedACL);
        }

        @Override
        public PutObjectResponse run() {
            return client.putInputStream(build());
        }

        @Override
        public ActionFuture<PutObjectResponse> execute() {
            return client.putInputStreamAsync(build());
        }

        @Override
        public void execute(ActionListener<PutObjectResponse> listener) {
            client.putInputStreamAsync(build(), listener);
        }
    }
}