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
public class Summary implements SdkPojo, Buildable {
    private static final SdkField<String> USER_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(Summary::getUser))
            .setter(setter(Summary::setUser))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("user")
                    .unmarshallLocationName("user").build()).build();
    private static final SdkField<Usage> TOTAL_FIELD = SdkField
            .<Usage>builder(MarshallingType.SDK_POJO)
            .getter(getter(Summary::getTotal))
            .setter(setter(Summary::setTotal))
            .constructor(Usage::new)
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("total")
                    .unmarshallLocationName("total").build()).build();
    private static final SdkField<List<Usage>> CATEGORIES_FIELD = SdkField
            .<List<Usage>>builder(MarshallingType.LIST)
            .getter(getter(Summary::getCategories))
            .setter(setter(Summary::setCategories))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("categories")
                            .unmarshallLocationName("categories").build(),
                    ListTrait
                            .builder()
                            .memberLocationName("categories")
                            .memberFieldInfo(
                                    SdkField.<Usage>builder(MarshallingType.SDK_POJO)
                                            .constructor(Usage::new)
                                            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD)
                                                    .locationName("category").unmarshallLocationName("category").build()).build())
                            .build()).build();
    private static final List<SdkField<?>> SDK_FIELDS = Collections.unmodifiableList(Arrays.asList(USER_FIELD, TOTAL_FIELD, CATEGORIES_FIELD));
    private String user;
    private Usage total;
    private List<Usage> categories;

    Summary() {
    }

    private static <T> Function<Object, T> getter(Function<Summary, T> g) {
        return obj -> g.apply((Summary) obj);
    }

    private static <T> BiConsumer<Object, T> setter(BiConsumer<Summary, T> s) {
        return (obj, val) -> s.accept((Summary) obj, val);
    }

    public String getUser() {
        return user;
    }

    private void setUser(String user) {
        this.user = user;
    }

    public Usage getTotal() {
        return total;
    }

    private void setTotal(Usage total) {
        this.total = total;
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
        return "Summary{" +
                "user='" + user + '\'' +
                ", total=" + total +
                ", categories=" + categories +
                '}';
    }
}
