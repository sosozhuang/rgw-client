package io.ceph.rgw.client.model;

import io.ceph.rgw.client.ObjectClient;
import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;

/**
 * Created by zhuangshuo on 2020/3/13.
 */
public class GetStringRequest extends BaseGetObjectRequest {
    public GetStringRequest(String bucketName, String key, String versionId, long[] range) {
        super(bucketName, key, versionId, range);
    }

    @Override
    public String toString() {
        return "GetStringRequest{} " + super.toString();
    }

    public static class Builder extends BaseGetObjectRequest.Builder<Builder, GetStringRequest, GetStringResponse> {
        public Builder(ObjectClient client) {
            super(client);
        }

        @Override
        public GetStringRequest build() {
            return new GetStringRequest(bucketName, key, versionId, range);
        }

        @Override
        public GetStringResponse run() {
            return client.getString(build());
        }

        @Override
        public ActionFuture<GetStringResponse> execute() {
            return client.getStringAsync(build());
        }

        @Override
        public void execute(ActionListener<GetStringResponse> listener) {
            client.getStringAsync(build(), listener);
        }
    }
}
