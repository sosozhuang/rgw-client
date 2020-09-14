package io.ceph.rgw.client.model;

import io.ceph.rgw.client.ObjectClient;
import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;

/**
 * Created by zhuangshuo on 2020/3/16.
 */
public class GetObjectACLRequest extends BaseObjectRequest {
    public GetObjectACLRequest(String bucketName, String key, String versionId) {
        super(bucketName, key, versionId);
    }

    @Override
    public String toString() {
        return "GetObjectACLRequest{} " + super.toString();
    }

    public static class Builder extends BaseObjectRequest.Builder<Builder, GetObjectACLRequest, GetObjectACLResponse> {

        public Builder(ObjectClient client) {
            super(client);
        }

        @Override
        public GetObjectACLRequest build() {
            return new GetObjectACLRequest(bucketName, key, versionId);
        }

        @Override
        public GetObjectACLResponse run() {
            return client.getObjectACL(build());
        }

        @Override
        public ActionFuture<GetObjectACLResponse> execute() {
            return client.getObjectACLAsync(build());
        }

        @Override
        public void execute(ActionListener<GetObjectACLResponse> listener) {
            client.getObjectACLAsync(build(), listener);
        }
    }
}
