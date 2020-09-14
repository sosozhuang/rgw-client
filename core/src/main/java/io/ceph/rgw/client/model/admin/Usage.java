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
 * Created by zhuangshuo on 2020/7/30.
 */
public class Usage implements SdkPojo, Buildable {
    private static final SdkField<String> CATEGORY_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(Usage::getCategory))
            .setter(setter(Usage::setCategory))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("category")
                    .unmarshallLocationName("category").build()).build();
    private static final SdkField<Long> BYTE_SENT_FIELD = SdkField
            .builder(MarshallingType.LONG)
            .getter(getter(Usage::getByteSent))
            .setter(setter(Usage::setByteSent))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("byte_sent")
                    .unmarshallLocationName("byte_sent").build()).build();
    private static final SdkField<Long> BYTE_RECEIVED_FIELD = SdkField
            .builder(MarshallingType.LONG)
            .getter(getter(Usage::getByteReceived))
            .setter(setter(Usage::setByteReceived))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("byte_received")
                    .unmarshallLocationName("byte_received").build()).build();
    private static final SdkField<Long> OPS_FIELD = SdkField
            .builder(MarshallingType.LONG)
            .getter(getter(Usage::getOps))
            .setter(setter(Usage::setOps))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("ops")
                    .unmarshallLocationName("ops").build()).build();
    private static final SdkField<Long> SUCCESSFUL_OPS_FIELD = SdkField
            .builder(MarshallingType.LONG)
            .getter(getter(Usage::getSuccessfulOps))
            .setter(setter(Usage::setSuccessfulOps))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("successful_ops")
                    .unmarshallLocationName("successful_ops").build()).build();
    private static final List<SdkField<?>> SDK_FIELDS = Collections.unmodifiableList(Arrays.asList(CATEGORY_FIELD, BYTE_SENT_FIELD, BYTE_RECEIVED_FIELD, OPS_FIELD, SUCCESSFUL_OPS_FIELD));

    private String category;
    private long byteSent;
    private long byteReceived;
    private long ops;
    private long successfulOps;

    Usage() {
    }

    private static <T> Function<Object, T> getter(Function<Usage, T> g) {
        return obj -> g.apply((Usage) obj);
    }

    private static <T> BiConsumer<Object, T> setter(BiConsumer<Usage, T> s) {
        return (obj, val) -> s.accept((Usage) obj, val);
    }

    public String getCategory() {
        return category;
    }

    private void setCategory(String category) {
        this.category = category;
    }

    public long getByteSent() {
        return byteSent;
    }

    private void setByteSent(long byteSent) {
        this.byteSent = byteSent;
    }

    public long getByteReceived() {
        return byteReceived;
    }

    private void setByteReceived(long byteReceived) {
        this.byteReceived = byteReceived;
    }

    public long getOps() {
        return ops;
    }

    private void setOps(long ops) {
        this.ops = ops;
    }

    public long getSuccessfulOps() {
        return successfulOps;
    }

    private void setSuccessfulOps(long successfulOps) {
        this.successfulOps = successfulOps;
    }

    @Override
    public String toString() {
        return "Usage{" +
                "category='" + category + '\'' +
                ", byteSent=" + byteSent +
                ", byteReceived=" + byteReceived +
                ", ops=" + ops +
                ", successfulOps=" + successfulOps +
                '}';
    }

    @Override
    public List<SdkField<?>> sdkFields() {
        return SDK_FIELDS;
    }

    @Override
    public Object build() {
        return this;
    }
}
