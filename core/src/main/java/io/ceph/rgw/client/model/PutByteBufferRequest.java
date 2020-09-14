package io.ceph.rgw.client.model;

import io.ceph.rgw.client.ObjectClient;
import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;

import java.nio.ByteBuffer;

/**
 * Created by zhuangshuo on 2020/7/20.
 */
public class PutByteBufferRequest extends GenericPutObjectRequest<ByteBuffer> {
    public PutByteBufferRequest(String bucketName, String key, ByteBuffer value, Metadata metadata, Tagging tagging, ACL acl, CannedACL cannedACL) {
        super(bucketName, key, value, metadata, tagging, acl, cannedACL);
    }

    @Override
    public String toString() {
        return "PutByteBufferRequest{" +
                "} " + super.toString();
    }

    public static final class Builder extends GenericPutObjectRequest.Builder<Builder, ByteBuffer> {
        public Builder(ObjectClient client) {
            super(client);
        }

        @Override
        public PutByteBufferRequest build() {
            return new PutByteBufferRequest(bucketName, key, value, metadata, tagging, acl, cannedACL);
        }

        @Override
        public PutObjectResponse run() {
            return client.putByteBuffer(build());
        }

        @Override
        public ActionFuture<PutObjectResponse> execute() {
            return client.putByteBufferAsync(build());
        }

        @Override
        public void execute(ActionListener<PutObjectResponse> listener) {
            client.putByteBufferAsync(build(), listener);
        }
    }
}
