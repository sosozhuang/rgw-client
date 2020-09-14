package io.ceph.rgw.client.model;

import io.ceph.rgw.client.ObjectClient;
import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * Created by zhuangshuo on 2020/3/13.
 */
public class GetFileRequest extends BaseGetObjectRequest {
    private final Path path;

    public GetFileRequest(String bucketName, String key, String versionId, long[] range, Path path) {
        super(bucketName, key, versionId, range);
        this.path = Objects.requireNonNull(path);
    }

    public Path getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "GetFileRequest{" +
                "path=" + path +
                "} " + super.toString();
    }

    public static class Builder extends BaseGetObjectRequest.Builder<Builder, GetFileRequest, GetFileResponse> {
        private Path path;

        public Builder(ObjectClient client) {
            super(client);
        }

        public Builder withPath(Path path) {
            this.path = path;
            return self();
        }

        public Builder withPath(String path) {
            this.path = Paths.get(path);
            return self();
        }

        @Override
        public GetFileRequest build() {
            return new GetFileRequest(bucketName, key, versionId, range, path);
        }

        @Override
        public GetFileResponse run() {
            return client.getFile(build());
        }

        @Override
        public ActionFuture<GetFileResponse> execute() {
            return client.getFileAsync(build());
        }

        @Override
        public void execute(ActionListener<GetFileResponse> listener) {
            client.getFileAsync(build(), listener);
        }
    }
}
