package io.ceph.rgw.client.model;

import io.ceph.rgw.client.ObjectClient;
import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;
import org.apache.commons.lang3.Validate;

/**
 * Created by zhuangshuo on 2020/3/16.
 */
public class InitiateMultipartUploadRequest extends BaseObjectRequest {
    private final Metadata metadata;
    private final ACL acl;
    private final CannedACL cannedACL;

    public InitiateMultipartUploadRequest(String bucketName, String key, Metadata metadata, ACL acl, CannedACL cannedACL) {
        super(bucketName, key, null);
        this.metadata = metadata;
        Validate.isTrue(acl == null || cannedACL == null, "one acl type only");
        this.acl = acl;
        this.cannedACL = cannedACL;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public ACL getACL() {
        return acl;
    }

    public CannedACL getCannedACL() {
        return cannedACL;
    }

    @Override
    public String toString() {
        return "InitiateMultipartUploadRequest{" +
                "metadata=" + metadata +
                ", acl=" + acl +
                ", cannedACL=" + cannedACL +
                "} " + super.toString();
    }

    public static class Builder extends BaseObjectRequest.Builder<Builder, InitiateMultipartUploadRequest, InitiateMultipartUploadResponse> {
        private Metadata metadata;
        private ACL acl;
        private CannedACL cannedACL;

        public Builder(ObjectClient client) {
            super(client);
        }

        public MetadataBuilder withMetadata() {
            return new MetadataBuilder(this);
        }

        public Builder withMetadata(Metadata metadata) {
            this.metadata = metadata;
            return self();
        }

        public ACLBuilder withACL() {
            return new ACLBuilder(this);
        }

        public Builder withACL(ACL acl) {
            this.acl = acl;
            return self();
        }

        public Builder withCannedACL(CannedACL cannedACL) {
            this.cannedACL = cannedACL;
            return self();
        }

        @Override
        public InitiateMultipartUploadRequest build() {
            return new InitiateMultipartUploadRequest(bucketName, key, metadata, acl, cannedACL);
        }

        @Override
        public InitiateMultipartUploadResponse run() {
            return client.initiateMultipartUpload(build());
        }

        @Override
        public ActionFuture<InitiateMultipartUploadResponse> execute() {
            return client.initiateMultipartUploadAsync(build());
        }

        @Override
        public void execute(ActionListener<InitiateMultipartUploadResponse> listener) {
            client.initiateMultipartUploadAsync(build(), listener);
        }
    }

    public static class MetadataBuilder extends Metadata.NestedBuilder<MetadataBuilder> {
        private final Builder builder;

        MetadataBuilder(Builder builder) {
            this.builder = builder;
        }

        public Builder endMetadata() {
            return builder.withMetadata(build());
        }
    }

    public static class ACLBuilder extends ACL.NestedBuilder<ACLBuilder> {
        private final Builder builder;

        ACLBuilder(Builder builder) {
            this.builder = builder;
        }

        public Builder endACL() {
            return builder.withACL(build());
        }
    }
}
