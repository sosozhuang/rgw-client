package io.ceph.rgw.client.model;

import io.ceph.rgw.client.ObjectClient;
import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by zhuangshuo on 2020/3/16.
 */
public class CompleteMultipartUploadRequest extends BaseObjectRequest {
    private final String uploadId;
    private final List<PartETag> partETags;

    public CompleteMultipartUploadRequest(String bucketName, String key, String uploadId, List<PartETag> partETags) {
        super(bucketName, key, null);
        this.uploadId = Validate.notBlank(uploadId);
        this.partETags = Collections.unmodifiableList(Validate.notEmpty(partETags));
    }

    public String getUploadId() {
        return uploadId;
    }

    public List<PartETag> getPartETags() {
        return partETags;
    }

    @Override
    public String toString() {
        return "CompleteMultipartUploadRequest{" +
                "uploadId='" + uploadId + '\'' +
                ", partETags=" + partETags +
                "} " + super.toString();
    }

    public static class Builder extends BaseObjectRequest.Builder<Builder, CompleteMultipartUploadRequest, CompleteMultipartUploadResponse> {
        private String uploadId;
        private List<PartETag> partETags;

        public Builder(ObjectClient client) {
            super(client);
            this.partETags = new ArrayList<>();
        }

        public Builder withUploadId(String uploadId) {
            this.uploadId = uploadId;
            return self();
        }

        public Builder addPartETag(int part, String eTag) {
            this.partETags.add(new PartETag(part, eTag));
            return self();
        }

        public Builder addPartETag(PartETag partETag) {
            this.partETags.add(partETag);
            return self();
        }

        public Builder withPartETags(Collection<PartETag> partETags) {
            this.partETags.clear();
            this.partETags.addAll(partETags);
            return self();
        }

        @Override
        public CompleteMultipartUploadRequest build() {
            return new CompleteMultipartUploadRequest(bucketName, key, uploadId, partETags);
        }

        @Override
        public CompleteMultipartUploadResponse run() {
            return client.completeMultipartUpload(build());
        }

        @Override
        public ActionFuture<CompleteMultipartUploadResponse> execute() {
            return client.completeMultipartUploadAsync(build());
        }

        @Override
        public void execute(ActionListener<CompleteMultipartUploadResponse> listener) {
            client.completeMultipartUploadAsync(build(), listener);
        }
    }
}
