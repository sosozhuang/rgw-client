package io.ceph.rgw.client.model;

import io.ceph.rgw.client.BucketClient;
import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;
import org.apache.commons.lang3.Validate;

/**
 * Created by zhuangshuo on 2020/3/17.
 */
public class PutBucketACLRequest extends BaseBucketRequest {
    private final ACL acl;
    private final CannedACL cannedACL;

    public PutBucketACLRequest(String bucketName, ACL acl, CannedACL cannedACL) {
        super(bucketName);
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
        return "SetBucketACLRequest{" +
                "acl=" + acl +
                ", cannedACL=" + cannedACL +
                "} " + super.toString();
    }

    public static class Builder extends BaseBucketRequest.Builder<Builder, PutBucketACLRequest, PutBucketACLResponse> {
        private ACL acl;
        private CannedACL cannedACL;

        public Builder(BucketClient client) {
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
        public PutBucketACLRequest build() {
            return new PutBucketACLRequest(bucketName, acl, cannedACL);
        }

        @Override
        public PutBucketACLResponse run() {
            return client.putBucketACL(build());
        }

        @Override
        public ActionFuture<PutBucketACLResponse> execute() {
            return client.putBucketACLAsync(build());
        }

        @Override
        public void execute(ActionListener<PutBucketACLResponse> listener) {
            client.putBucketACLAsync(build(), listener);
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
