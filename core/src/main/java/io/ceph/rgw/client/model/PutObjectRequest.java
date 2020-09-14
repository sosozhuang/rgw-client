package io.ceph.rgw.client.model;

import org.apache.commons.lang3.Validate;

/**
 * Created by zhuangshuo on 2020/7/20.
 */
public abstract class PutObjectRequest implements ObjectRequest {
    private final String bucketName;
    private final String key;
    private final Metadata metadata;
    private final ACL acl;
    private final CannedACL cannedACL;

    protected PutObjectRequest(String bucketName, String key, Metadata metadata, ACL acl, CannedACL cannedACL) {
        this.bucketName = Validate.notBlank(bucketName, "bucketName cannot be empty string");
        this.key = Validate.notBlank(key, "key cannot be empty string");
        this.metadata = metadata;
        Validate.isTrue(acl == null || cannedACL == null, "one acl type only");
        this.acl = acl;
        this.cannedACL = cannedACL;
    }

    public String getBucketName() {
        return bucketName;
    }

    public String getKey() {
        return key;
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
        return "PutObjectRequest{" +
                "bucketName='" + bucketName + '\'' +
                ", key='" + key + '\'' +
                ", metadata=" + metadata +
                ", acl=" + acl +
                ", cannedACL=" + cannedACL +
                '}';
    }

    public static abstract class Builder<T extends Builder<T, R>, R extends ObjectRequest> extends GenericBuilder<T, R> {
        protected String bucketName;
        protected String key;
        protected Metadata metadata;
        protected ACL acl;
        protected CannedACL cannedACL;

        public T withBucketName(String bucketName) {
            this.bucketName = bucketName;
            return self();
        }

        public T withKey(String key) {
            this.key = key;
            return self();
        }

        public T withMetadata(Metadata metadata) {
            this.metadata = metadata;
            return self();
        }

        public MetadataBuilder<T, R> withMetadata() {
            return new MetadataBuilder<>(this);
        }

        public ACLBuilder<T, R> withACL() {
            return new ACLBuilder<>(this);
        }

        public T withACL(ACL acl) {
            this.acl = acl;
            return self();
        }

        public T withCannedACL(CannedACL cannedACL) {
            this.cannedACL = cannedACL;
            return self();
        }
    }

    public static class MetadataBuilder<T extends Builder<T, R>, R extends ObjectRequest> extends Metadata.NestedBuilder<MetadataBuilder<T, R>> {
        final Builder<T, R> builder;

        MetadataBuilder(Builder<T, R> builder) {
            this.builder = builder;
        }

        public T endMetadata() {
            return builder.withMetadata(build());
        }
    }

    public static class ACLBuilder<T extends Builder<T, R>, R extends ObjectRequest> extends ACL.NestedBuilder<ACLBuilder<T, R>> {
        final Builder<T, R> builder;

        ACLBuilder(Builder<T, R> builder) {
            this.builder = builder;
        }

        public T endACL() {
            return builder.withACL(build());
        }
    }
}