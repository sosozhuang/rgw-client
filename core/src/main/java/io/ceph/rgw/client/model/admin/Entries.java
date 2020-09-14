package io.ceph.rgw.client.model.admin;

import software.amazon.awssdk.core.SdkField;
import software.amazon.awssdk.core.SdkPojo;
import software.amazon.awssdk.core.protocol.MarshallLocation;
import software.amazon.awssdk.core.protocol.MarshallingType;
import software.amazon.awssdk.core.traits.ListTrait;
import software.amazon.awssdk.core.traits.LocationTrait;
import software.amazon.awssdk.utils.builder.Buildable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Created by zhuangshuo on 2020/7/30.
 */
public class Entries implements SdkPojo, Buildable {
    private static final SdkField<String> USER_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(Entries::getUser))
            .setter(setter(Entries::setUser))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("user")
                    .unmarshallLocationName("user").build()).build();
    private static final SdkField<List<BucketUsage>> BUCKET_USAGES_FIELD = SdkField
            .<List<BucketUsage>>builder(MarshallingType.LIST)
            .getter(getter(Entries::getBucketUsages))
            .setter(setter(Entries::setBucketUsages))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("buckets")
                            .unmarshallLocationName("buckets").build(),
                    ListTrait
                            .builder()
                            .memberLocationName("buckets")
                            .memberFieldInfo(
                                    SdkField.builder(MarshallingType.SDK_POJO)
                                            .constructor(BucketUsage::new)
                                            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD)
                                                    .locationName("bucket").unmarshallLocationName("bucket").build()).build())
                            .build()).build();
    private static final List<SdkField<?>> SDK_FIELDS = Collections.unmodifiableList(Arrays.asList(USER_FIELD, BUCKET_USAGES_FIELD));
    private String user;
    private List<BucketUsage> bucketUsages;

    Entries() {
    }

    private static <T> Function<Object, T> getter(Function<Entries, T> g) {
        return obj -> g.apply((Entries) obj);
    }

    private static <T> BiConsumer<Object, T> setter(BiConsumer<Entries, T> s) {
        return (obj, val) -> s.accept((Entries) obj, val);
    }

    public String getUser() {
        return user;
    }

    private void setUser(String user) {
        this.user = user;
    }

    public List<BucketUsage> getBucketUsages() {
        return bucketUsages;
    }

    private void setBucketUsages(List<BucketUsage> bucketUsages) {
        this.bucketUsages = AdminRequest.unmodifiableList(bucketUsages);
    }

    @Override
    public List<SdkField<?>> sdkFields() {
        return SDK_FIELDS;
    }

    @Override
    public Object build() {
        return this;
    }

    @Override
    public String toString() {
        return "Entries{" +
                "user='" + user + '\'' +
                ", bucketUsages=" + bucketUsages +
                '}';
    }
}
