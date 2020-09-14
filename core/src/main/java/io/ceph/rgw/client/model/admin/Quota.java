package io.ceph.rgw.client.model.admin;

import software.amazon.awssdk.core.SdkField;
import software.amazon.awssdk.core.SdkPojo;
import software.amazon.awssdk.core.protocol.MarshallLocation;
import software.amazon.awssdk.core.protocol.MarshallingType;
import software.amazon.awssdk.core.traits.LocationTrait;
import software.amazon.awssdk.utils.builder.Buildable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Created by zhuangshuo on 2020/8/4.
 */
public class Quota implements SdkPojo, Buildable {
    private static final SdkField<Boolean> ENABLED_FIELD = SdkField
            .builder(MarshallingType.BOOLEAN)
            .getter(getter(Quota::isEnabled))
            .setter(setter(Quota::setEnabled))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("enabled")
                    .unmarshallLocationName("enabled").build()).build();
    private static final SdkField<Boolean> CHECK_ON_RAW_FIELD = SdkField
            .builder(MarshallingType.BOOLEAN)
            .getter(getter(Quota::isCheckOnRaw))
            .setter(setter(Quota::setCheckOnRaw))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("check_on_raw")
                    .unmarshallLocationName("check_on_raw").build()).build();
    private static final SdkField<Integer> MAX_SIZE_FIELD = SdkField
            .builder(MarshallingType.INTEGER)
            .getter(getter(Quota::getMaxSize))
            .setter(setter(Quota::setMaxSize))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("max_size")
                    .unmarshallLocationName("max_size").build()).build();
    private static final SdkField<Integer> MAX_SIZE_KB_FIELD = SdkField
            .builder(MarshallingType.INTEGER)
            .getter(getter(Quota::getMaxSizeKb))
            .setter(setter(Quota::setMaxSizeKb))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("max_size_kb")
                    .unmarshallLocationName("max_size_kb").build()).build();
    private static final SdkField<Integer> MAX_OBJECTS_FIELD = SdkField
            .builder(MarshallingType.INTEGER)
            .getter(getter(Quota::getMaxObjects))
            .setter(setter(Quota::setMaxObjects))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("max_objects")
                    .unmarshallLocationName("max_objects").build()).build();
    private static final List<SdkField<?>> SDK_FIELDS = Collections.unmodifiableList(Arrays.asList(ENABLED_FIELD, CHECK_ON_RAW_FIELD, MAX_SIZE_FIELD, MAX_SIZE_KB_FIELD, MAX_OBJECTS_FIELD));
    private Boolean enabled;
    private Boolean checkOnRaw;
    private Integer maxSize;
    private Integer maxSizeKb;
    private Integer maxObjects;

    Quota() {
    }

    private static <T> Function<Object, T> getter(Function<Quota, T> g) {
        return obj -> g.apply((Quota) obj);
    }

    private static <T> BiConsumer<Object, T> setter(BiConsumer<Quota, T> s) {
        return (obj, val) -> s.accept((Quota) obj, val);
    }

    public Boolean isEnabled() {
        return enabled;
    }

    void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    //    @JsonProperty("check_on_raw")
    public Boolean isCheckOnRaw() {
        return checkOnRaw;
    }

    void setCheckOnRaw(Boolean checkOnRaw) {
        this.checkOnRaw = checkOnRaw;
    }

    //    @JsonProperty("max_size")
    public Integer getMaxSize() {
        return maxSize;
    }

    void setMaxSize(Integer maxSize) {
        this.maxSize = maxSize;
    }

    //    @JsonProperty("max_size_kb")
    public Integer getMaxSizeKb() {
        return maxSizeKb;
    }

    void setMaxSizeKb(Integer maxSizeKb) {
        this.maxSizeKb = maxSizeKb;
    }

    //    @JsonProperty("max_objects")
    public Integer getMaxObjects() {
        return maxObjects;
    }

    void setMaxObjects(Integer maxObjects) {
        this.maxObjects = maxObjects;
    }

    @Override
    public List<SdkField<?>> sdkFields() {
        return SDK_FIELDS;
    }

    @Override
    public String toString() {
        return "Quota{" +
                "enabled=" + enabled +
                ", checkOnRaw=" + checkOnRaw +
                ", maxSize=" + maxSize +
                ", maxSizeKb=" + maxSizeKb +
                ", maxObjects=" + maxObjects +
                '}';
    }

    @Override
    public Object build() {
        return this;
    }
}
