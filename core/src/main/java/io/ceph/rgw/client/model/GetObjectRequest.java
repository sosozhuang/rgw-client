package io.ceph.rgw.client.model;

import io.ceph.rgw.client.ObjectClient;
import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;

/**
 * Created by zhuangshuo on 2020/3/13.
 */
public class GetObjectRequest extends BaseGetObjectRequest {
    public GetObjectRequest(String bucketName, String key, String versionId, long[] range) {
        super(bucketName, key, versionId, range);
    }

    @Override
    public String toString() {
        return "GetObjectRequest{} " + super.toString();
    }

    public static class Builder extends BaseGetObjectRequest.Builder<Builder, GetObjectRequest, GetObjectResponse> {
        public Builder(ObjectClient client) {
            super(client);
        }

        @Override
        public GetObjectRequest build() {
            return new GetObjectRequest(bucketName, key, versionId, range);
        }

        @Override
        public GetObjectResponse run() {
            return client.getObject(build());
        }

        @Override
        public ActionFuture<GetObjectResponse> execute() {
            return client.getObjectAsync(build());
        }

        @Override
        public void execute(ActionListener<GetObjectResponse> listener) {
            client.getObjectAsync(build(), listener);
        }
    }
}
