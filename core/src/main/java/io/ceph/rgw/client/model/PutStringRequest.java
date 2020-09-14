package io.ceph.rgw.client.model;

import io.ceph.rgw.client.ObjectClient;
import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;

import java.nio.charset.Charset;

/**
 * Created by zhuangshuo on 2020/3/1.
 */
public class PutStringRequest extends GenericPutObjectRequest<String> {
    private final Charset charset;

    public PutStringRequest(String bucketName, String key, String value, Metadata metadata, Tagging tagging, ACL acl, CannedACL cannedACL, Charset charset) {
        super(bucketName, key, value, metadata, tagging, acl, cannedACL);
        this.charset = charset;
    }

    public Charset getCharset() {
        return charset;
    }

    @Override
    public String toString() {
        return "PutStringRequest{} " + super.toString();
    }

    public static final class Builder extends GenericPutObjectRequest.Builder<Builder, String> {
        private Charset charset;

        public Builder(ObjectClient client) {
            super(client);
        }

        public Builder withCharset(Charset charset) {
            this.charset = charset;
            return self();
        }

        @Override
        public PutStringRequest build() {
            return new PutStringRequest(bucketName, key, value, metadata, tagging, acl, cannedACL, charset);
        }

        @Override
        public PutObjectResponse run() {
            return client.putString(build());
        }

        @Override
        public ActionFuture<PutObjectResponse> execute() {
            return client.putStringAsync(build());
        }

        @Override
        public void execute(ActionListener<PutObjectResponse> listener) {
            client.putStringAsync(build(), listener);
        }
    }
}
