package io.ceph.rgw.client.model;

import io.ceph.rgw.client.ObjectClient;
import org.apache.commons.lang3.Validate;

import java.util.Objects;

/**
 * Created by zhuangshuo on 2020/2/29.
 */
public abstract class GenericPutObjectRequest<V> extends PutObjectRequest {
    private final V value;
    private final Tagging tagging;

    GenericPutObjectRequest(String bucketName, String key, V value, Metadata metadata, Tagging tagging, ACL acl, CannedACL cannedACL) {
        super(bucketName, key, metadata, acl, cannedACL);
        this.value = Validate.notNull(value, "value cannot be null");
        this.tagging = tagging;
    }

    public V getValue() {
        return value;
    }

    public Tagging getTagging() {
        return tagging;
    }


    @Override
    public String toString() {
        return "GenericPutObjectRequest{" +
                "value=" + value +
                ", tagging=" + tagging +
                "} " + super.toString();
    }

    public static abstract class Builder<T extends Builder<T, V>, V> extends PutObjectRequest.Builder<T, GenericPutObjectRequest<V>> implements RequestBuilder<GenericPutObjectRequest<V>, PutObjectResponse> {
        ObjectClient client;
        //        String bucketName;
//        String key;
        V value;
        //        Metadata metadata;
        Tagging tagging;
//        ACL acl;
//        CannedACL cannedACL;

        Builder(ObjectClient client) {
            this.client = Objects.requireNonNull(client);
        }

//        public T withBucketName(String bucketName) {
//            this.bucketName = bucketName;
//            return self();
//        }

//        public T withKey(String key) {
//            this.key = key;
//            return self();
//        }

        public T withValue(V value) {
            this.value = value;
            return self();
        }

//        public T withMetadata(Metadata metadata) {
//            this.metadata = metadata;
//            return self();
//        }

//        public MetadataBuilder<T, V> addMetadata() {
//            return new MetadataBuilder<>(this);
//        }

        public T withTagging(Tagging tagging) {
            this.tagging = tagging;
            return self();
        }

        public TaggingBuilder<T, V> addTagging() {
            return new TaggingBuilder<>(this);
        }

//        public ACLBuilder<T, V> withACL() {
//            return new ACLBuilder<>(this);
//        }

//        public T withACL(ACL acl) {
//            this.acl = acl;
//            return self();
//        }

//        public T withCannedACL(CannedACL cannedACL) {
//            this.cannedACL = cannedACL;
//            return self();
//        }

        @Override
        public abstract GenericPutObjectRequest<V> build();
    }

//    public static class MetadataBuilder<T extends Builder<T, V>, V> extends Metadata.NestedBuilder<MetadataBuilder<T, V>> {
//        final Builder<T, V> builder;
//        MetadataBuilder(Builder<T, V> builder) {
//            this.builder = builder;
//        }
//
//        public T endMetadata() {
//            return builder.withMetadata(build());
//        }
//    }

    public static class TaggingBuilder<T extends Builder<T, V>, V> extends Tagging.NestedBuilder<TaggingBuilder<T, V>> {
        final Builder<T, V> builder;

        TaggingBuilder(Builder<T, V> builder) {
            this.builder = builder;
        }

        public T endTagging() {
            return builder.withTagging(build());
        }
    }

//    public static class ACLBuilder<T extends Builder<T, V>, V> extends ACL.NestedBuilder<ACLBuilder<T, V>> {
//        final Builder<T, V> builder;
//        ACLBuilder(Builder<T, V> builder) {
//            this.builder = builder;
//        }
//
//        public T endACL() {
//            return builder.withACL(build());
//        }
//    }
}
