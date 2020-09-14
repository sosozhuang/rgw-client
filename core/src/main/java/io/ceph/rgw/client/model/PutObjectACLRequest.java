package io.ceph.rgw.client.model;

import io.ceph.rgw.client.ObjectClient;
import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;
import org.apache.commons.lang3.Validate;

/**
 * Created by zhuangshuo on 2020/3/16.
 */
public class PutObjectACLRequest extends BaseObjectRequest {
    private final ACL acl;
    private final CannedACL cannedACL;

    public PutObjectACLRequest(String bucketName, String key, String versionId, ACL acl, CannedACL cannedACL) {
        super(bucketName, key, versionId);
        Validate.isTrue(acl == null || cannedACL == null, "one acl type only");
        this.acl = acl;
        this.cannedACL = cannedACL;
    }

    public ACL getACL() {
        return acl;
    }

    public CannedACL getCannedACL() {
        return cannedACL;
    }

    @Override
    public String toString() {
        return "SetObjectACLRequest{" +
                "acl=" + acl +
                ", cannedACL=" + cannedACL +
                "} " + super.toString();
    }

    public static class Builder extends BaseObjectRequest.Builder<Builder, PutObjectACLRequest, PutObjectACLResponse> {
        private ACL acl;
        private CannedACL cannedACL;

        public Builder(ObjectClient client) {
            super(client);
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
        public PutObjectACLRequest build() {
            return new PutObjectACLRequest(bucketName, key, versionId, acl, cannedACL);
        }

        @Override
        public PutObjectACLResponse run() {
            return client.putObjectACL(build());
        }

        @Override
        public ActionFuture<PutObjectACLResponse> execute() {
            return client.putObjectACLAsync(build());
        }

        @Override
        public void execute(ActionListener<PutObjectACLResponse> listener) {
            client.putObjectACLAsync(build(), listener);
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
