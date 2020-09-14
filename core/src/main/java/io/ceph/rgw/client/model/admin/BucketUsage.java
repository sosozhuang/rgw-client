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
public class BucketUsage implements SdkPojo, Buildable {
    private static final SdkField<String> BUCKET_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(BucketUsage::getBucket))
            .setter(setter(BucketUsage::setBucket))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("bucket")
                    .unmarshallLocationName("bucket").build()).build();
    private static final SdkField<String> TIME_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(BucketUsage::getTime))
            .setter(setter(BucketUsage::setTime))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("time")
                    .unmarshallLocationName("time").build()).build();
    private static final SdkField<Long> EPOCH_FIELD = SdkField
            .builder(MarshallingType.LONG)
            .getter(getter(BucketUsage::getEpoch))
            .setter(setter(BucketUsage::setEpoch))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("epoch")
                    .unmarshallLocationName("epoch").build()).build();
    private static final SdkField<String> OWNER_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(BucketUsage::getOwner))
            .setter(setter(BucketUsage::setOwner))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("owner")
                    .unmarshallLocationName("owner").build()).build();
    private static final SdkField<List<Usage>> CATEGORIES_FIELD = SdkField
            .<List<Usage>>builder(MarshallingType.LIST)
            .getter(getter(BucketUsage::getCategories))
            .setter(setter(BucketUsage::setCategories))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("categories")
                            .unmarshallLocationName("owner").build(),
                    ListTrait
                            .builder()
                            .memberLocationName("category")
                            .memberFieldInfo(
                                    SdkField.<Usage>builder(MarshallingType.SDK_POJO)
                                            .constructor(Usage::new)
                                            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD)
                                                    .locationName("category").unmarshallLocationName("category").build()).build())
                            .build()).build();
    private static final List<SdkField<?>> SDK_FIELDS = Collections.unmodifiableList(Arrays.asList(BUCKET_FIELD, TIME_FIELD, EPOCH_FIELD, OWNER_FIELD, CATEGORIES_FIELD));

    private String bucket;
    private String time;
    private long epoch;
    private String owner;
    private List<Usage> categories;

    BucketUsage() {
    }

    private static <T> Function<Object, T> getter(Function<BucketUsage, T> g) {
        return obj -> g.apply((BucketUsage) obj);
    }

    private static <T> BiConsumer<Object, T> setter(BiConsumer<BucketUsage, T> s) {
        return (obj, val) -> s.accept((BucketUsage) obj, val);
    }

    public String getBucket() {
        return bucket;
    }

    private void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getTime() {
        return time;
    }

    private void setTime(String time) {
        this.time = time;
    }

    public long getEpoch() {
        return epoch;
    }

    private void setEpoch(long epoch) {
        this.epoch = epoch;
    }

    public String getOwner() {
        return owner;
    }

    private void setOwner(String owner) {
        this.owner = owner;
    }

    public List<Usage> getCategories() {
        return categories;
    }

    private void setCategories(List<Usage> categories) {
        this.categories = AdminRequest.unmodifiableList(categories);
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
        return "BucketUsage{" +
                "bucket='" + bucket + '\'' +
                ", time='" + time + '\'' +
                ", epoch=" + epoch +
                ", owner='" + owner + '\'' +
                ", categories=" + categories +
                '}';
    }
}
