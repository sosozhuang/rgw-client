package io.ceph.rgw.client.model;

import io.ceph.rgw.client.ObjectClient;
import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;

import java.io.File;

/**
 * Created by zhuangshuo on 2020/3/1.
 */
public class PutFileRequest extends GenericPutObjectRequest<File> {
    public PutFileRequest(String bucketName, String key, File value, Metadata metadata, Tagging tagging, ACL acl, CannedACL cannedACL) {
        super(bucketName, key, value, metadata, tagging, acl, cannedACL);
    }

    @Override
    public String toString() {
        return "PutFileRequest{} " + super.toString();
    }

    public static final class Builder extends GenericPutObjectRequest.Builder<Builder, File> {
        public Builder(ObjectClient client) {
            super(client);
        }

        @Override
        public PutFileRequest build() {
            return new PutFileRequest(bucketName, key, value, metadata, tagging, acl, cannedACL);
        }

        @Override
        public PutObjectResponse run() {
            return client.putFile(build());
        }

        @Override
        public ActionFuture<PutObjectResponse> execute() {
            return client.putFileAsync(build());
        }

        @Override
        public void execute(ActionListener<PutObjectResponse> listener) {
            client.putFileAsync(build(), listener);
        }
    }
}
