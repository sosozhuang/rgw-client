package io.ceph.rgw.client.model;

import io.ceph.rgw.client.BucketClient;
import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;
import org.apache.commons.lang3.Validate;

/**
 * Created by zhuangshuo on 2020/3/16.
 */
public class CreateBucketRequest extends BaseBucketRequest {
    private final ACL acl;
    private final CannedACL cannedACL;
    private final boolean enableObjectLock;

    public CreateBucketRequest(String bucketName, ACL acl, CannedACL cannedACL, boolean enableObjectLock) {
        super(bucketName);
        Validate.isTrue(acl == null || cannedACL == null, "one acl type only");
        this.acl = acl;
        this.cannedACL = cannedACL;
        this.enableObjectLock = enableObjectLock;
    }

    public ACL getACL() {
        return acl;
    }

    public CannedACL getCannedACL() {
        return cannedACL;
    }

    public boolean isEnableObjectLock() {
        return enableObjectLock;
    }

    @Override
    public String toString() {
        return "PutBucketRequest{" +
                "acl=" + acl +
                ", cannedACL=" + cannedACL +
                ", enableObjectLock=" + enableObjectLock +
                "} " + super.toString();
    }

    public static class Builder extends BaseBucketRequest.Builder<Builder, CreateBucketRequest, CreateBucketResponse> {
        private ACL acl;
        private CannedACL cannedACL;
        private boolean enableObjectLock;

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

        public Builder withEnableObjectLock(boolean enableObjectLock) {
            this.enableObjectLock = enableObjectLock;
            return self();
        }

        @Override
        public CreateBucketRequest build() {
            return new CreateBucketRequest(bucketName, acl, cannedACL, enableObjectLock);
        }

        @Override
        public CreateBucketResponse run() {
            return client.createBucket(build());
        }

        @Override
        public ActionFuture<CreateBucketResponse> execute() {
            return client.createBucketAsync(build());
        }

        @Override
        public void execute(ActionListener<CreateBucketResponse> listener) {
            client.createBucketAsync(build(), listener);
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
