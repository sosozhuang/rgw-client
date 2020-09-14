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
 * Created by zhuangshuo on 2020/8/7.
 */
public class Owner implements SdkPojo, Buildable {
    private static final SdkField<String> ID_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(Owner::getId))
            .setter(setter(Owner::setId))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("id")
                    .unmarshallLocationName("id").build()).build();
    private static final SdkField<String> DISPLAY_NAME_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(Owner::getDisplayName))
            .setter(setter(Owner::setDisplayName))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("display_name")
                    .unmarshallLocationName("display_name").build()).build();
    private static final List<SdkField<?>> SDK_FIELDS = Collections.unmodifiableList(Arrays.asList(ID_FIELD, DISPLAY_NAME_FIELD));

    private String id;
    private String displayName;

    Owner() {
    }

    private static <T> Function<Object, T> getter(Function<Owner, T> g) {
        return obj -> g.apply((Owner) obj);
    }

    private static <T> BiConsumer<Object, T> setter(BiConsumer<Owner, T> s) {
        return (obj, val) -> s.accept((Owner) obj, val);
    }

    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    private void setDisplayName(String displayName) {
        this.displayName = displayName;
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
        return "Owner{" +
                "id='" + id + '\'' +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
