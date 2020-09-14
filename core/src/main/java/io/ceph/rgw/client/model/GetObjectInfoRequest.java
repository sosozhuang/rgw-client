package io.ceph.rgw.client.model;

import io.ceph.rgw.client.ObjectClient;
import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;

/**
 * Created by zhuangshuo on 2020/3/16.
 */
public class GetObjectInfoRequest extends BaseObjectRequest {

    public GetObjectInfoRequest(String bucketName, String key, String versionId) {
        super(bucketName, key, versionId);
    }

    @Override
    public String toString() {
        return "GetObjectInfoRequest{} " + super.toString();
    }

    public static class Builder extends BaseObjectRequest.Builder<Builder, GetObjectInfoRequest, GetObjectInfoResponse> {
        public Builder(ObjectClient client) {
            super(client);
        }

        @Override
        public GetObjectInfoRequest build() {
            return new GetObjectInfoRequest(bucketName, key, versionId);
        }

        @Override
        public GetObjectInfoResponse run() {
            return client.getObjectInfo(build());
        }

        @Override
        public ActionFuture<GetObjectInfoResponse> execute() {
            return client.getObjectInfoAsync(build());
        }

        @Override
        public void execute(ActionListener<GetObjectInfoResponse> listener) {
            client.getObjectInfoAsync(build(), listener);
        }
    }
}
