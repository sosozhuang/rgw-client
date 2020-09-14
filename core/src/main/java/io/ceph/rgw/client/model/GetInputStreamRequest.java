package io.ceph.rgw.client.model;

import io.ceph.rgw.client.ObjectClient;
import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;

/**
 * Created by zhuangshuo on 2020/3/13.
 */
public class GetInputStreamRequest extends BaseGetObjectRequest {
    public GetInputStreamRequest(String bucketName, String key, String versionId, long[] range) {
        super(bucketName, key, versionId, range);
    }

    @Override
    public String toString() {
        return "GetInputStreamRequest{} " + super.toString();
    }

    public static class Builder extends BaseGetObjectRequest.Builder<Builder, GetInputStreamRequest, GetInputStreamResponse> {
        public Builder(ObjectClient client) {
            super(client);
        }

        @Override
        public GetInputStreamRequest build() {
            return new GetInputStreamRequest(bucketName, key, versionId, range);
        }

        @Override
        public GetInputStreamResponse run() {
            return client.getInputStream(build());
        }

        @Override
        public ActionFuture<GetInputStreamResponse> execute() {
            return client.getInputStreamAsync(build());
        }

        @Override
        public void execute(ActionListener<GetInputStreamResponse> listener) {
            client.getInputStreamAsync(build(), listener);
        }
    }

}
