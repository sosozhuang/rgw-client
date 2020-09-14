package io.ceph.rgw.client.model.admin;

import software.amazon.awssdk.core.SdkField;
import software.amazon.awssdk.core.SdkPojo;
import software.amazon.awssdk.core.protocol.MarshallLocation;
import software.amazon.awssdk.core.protocol.MarshallingType;
import software.amazon.awssdk.core.traits.LocationTrait;
import software.amazon.awssdk.utils.builder.Buildable;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Created by zhuangshuo on 2020/8/4.
 */
public class BucketInfo implements SdkPojo, Buildable {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS'Z'")
            .withLocale(Locale.CHINA).withZone(ZoneOffset.ofHours(8));
    private static final SdkField<String> BUCKET_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(BucketInfo::getBucket))
            .setter(setter(BucketInfo::setBucket))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("bucket")
                    .unmarshallLocationName("bucket").build()).build();
    private static final SdkField<Integer> NUM_SHARDS_FIELD = SdkField
            .builder(MarshallingType.INTEGER)
            .getter(getter(BucketInfo::getNumShards))
            .setter(setter(BucketInfo::setNumShards))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("num_shards")
                    .unmarshallLocationName("num_shards").build()).build();
    private static final SdkField<String> TENANT_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(BucketInfo::getTenant))
            .setter(setter(BucketInfo::setTenant))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("tenant")
                    .unmarshallLocationName("tenant").build()).build();
    private static final SdkField<String> ZONE_GROUP_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(BucketInfo::getZoneGroup))
            .setter(setter(BucketInfo::setZoneGroup))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("zonegroup")
                    .unmarshallLocationName("zonegroup").build()).build();
    private static final SdkField<String> PLACEMENT_RULE_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(BucketInfo::getPlacementRule))
            .setter(setter(BucketInfo::setPlacementRule))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("placement_rule")
                    .unmarshallLocationName("placement_rule").build()).build();
    private static final SdkField<String> DATA_POOL_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(BucketInfo::getDataPool))
            .setter(setter(BucketInfo::setDataPool))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("explicit_placement")
                            .unmarshallLocationName("explicit_placement").build(),
                    LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("data_pool")
                            .unmarshallLocationName("data_pool").build()).build();
    private static final SdkField<String> DATA_EXTRA_POOL_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(BucketInfo::getDataExtraPool))
            .setter(setter(BucketInfo::setDataExtraPool))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("explicit_placement")
                            .unmarshallLocationName("explicit_placement").build(),
                    LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("data_extra_pool")
                            .unmarshallLocationName("data_extra_pool").build()).build();
    private static final SdkField<String> INDEX_POOL_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(BucketInfo::getIndexPool))
            .setter(setter(BucketInfo::setIndexPool))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("explicit_placement")
                            .unmarshallLocationName("explicit_placement").build(),
                    LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("index_pool")
                            .unmarshallLocationName("index_pool").build()).build();
    private static final SdkField<String> ID_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(BucketInfo::getId))
            .setter(setter(BucketInfo::setId))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("id")
                    .unmarshallLocationName("id").build()).build();
    private static final SdkField<String> MARKER_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(BucketInfo::getMarker))
            .setter(setter(BucketInfo::setMarker))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("marker")
                    .unmarshallLocationName("marker").build()).build();
    private static final SdkField<String> INDEX_TYPE_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(BucketInfo::getIndexType))
            .setter(setter(BucketInfo::setIndexType))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("index_type")
                    .unmarshallLocationName("index_type").build()).build();
    private static final SdkField<String> OWNER_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(BucketInfo::getOwner))
            .setter(setter(BucketInfo::setOwner))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("owner")
                    .unmarshallLocationName("owner").build()).build();
    private static final SdkField<String> VER_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(BucketInfo::getVer))
            .setter(setter(BucketInfo::setVer))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("ver")
                    .unmarshallLocationName("ver").build()).build();
    private static final SdkField<String> MASTER_VER_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(BucketInfo::getMasterVer))
            .setter(setter(BucketInfo::setMasterVer))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("master_ver")
                    .unmarshallLocationName("master_ver").build()).build();
    private static final SdkField<String> MTIME_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(BucketInfo::getMtimeAsString))
            .setter(setter(BucketInfo::setMtime))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("mtime")
                    .unmarshallLocationName("mtime").build()).build();
    private static final SdkField<String> MAX_MARKER_FIELD = SdkField
            .builder(MarshallingType.STRING)
            .getter(getter(BucketInfo::getMaxMarker))
            .setter(setter(BucketInfo::setMaxMarker))
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("max_marker")
                    .unmarshallLocationName("max_marker").build()).build();
    private static final SdkField<Usage> USAGE_FIELD = SdkField
            .<Usage>builder(MarshallingType.SDK_POJO)
            .getter(getter(BucketInfo::getUsage))
            .setter(setter(BucketInfo::setUsage))
            .constructor(Usage::new)
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("usage")
                    .unmarshallLocationName("usage").build()).build();
    private static final SdkField<Quota> BUCKET_QUOTA_FIELD = SdkField
            .<Quota>builder(MarshallingType.SDK_POJO)
            .getter(getter(BucketInfo::getBucketQuota))
            .setter(setter(BucketInfo::setBucketQuota))
            .constructor(Quota::new)
            .traits(LocationTrait.builder().location(MarshallLocation.PAYLOAD).locationName("bucket_quota")
                    .unmarshallLocationName("bucket_quota").build()).build();
    private static final List<SdkField<?>> SDK_FIELDS = Collections.unmodifiableList(Arrays.asList(BUCKET_FIELD, NUM_SHARDS_FIELD, TENANT_FIELD, ZONE_GROUP_FIELD,
            PLACEMENT_RULE_FIELD, DATA_POOL_FIELD, DATA_EXTRA_POOL_FIELD, INDEX_POOL_FIELD, ID_FIELD, MARKER_FIELD, INDEX_TYPE_FIELD, OWNER_FIELD, VER_FIELD, MASTER_VER_FIELD, MTIME_FIELD, MAX_MARKER_FIELD, USAGE_FIELD, BUCKET_QUOTA_FIELD));
    private String bucket;
    private Integer numShards;
    private String tenant;
    private String zoneGroup;
    private String placementRule;
    private String dataPool;
    private String dataExtraPool;
    private String indexPool;
    private String id;
    private String marker;
    private String indexType;
    private String owner;
    private String ver;
    private String masterVer;
    private Date mtime;
    private String maxMarker;
    private Usage usage;
    private Quota bucketQuota;

    BucketInfo() {
    }

    private static <T> Function<Object, T> getter(Function<BucketInfo, T> g) {
        return obj -> g.apply((BucketInfo) obj);
    }

    private static <T> BiConsumer<Object, T> setter(BiConsumer<BucketInfo, T> s) {
        return (obj, val) -> s.accept((BucketInfo) obj, val);
    }

    public String getBucket() {
        return bucket;
    }

    BucketInfo setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public Integer getNumShards() {
        return numShards;
    }

    private void setNumShards(Integer numShards) {
        this.numShards = numShards;
    }

    public String getTenant() {
        return tenant;
    }

    private void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public String getZoneGroup() {
        return zoneGroup;
    }

    private void setZoneGroup(String zoneGroup) {
        this.zoneGroup = zoneGroup;
    }

    public String getPlacementRule() {
        return placementRule;
    }

    private void setPlacementRule(String placementRule) {
        this.placementRule = placementRule;
    }

    public String getDataPool() {
        return dataPool;
    }

    private void setDataPool(String dataPool) {
        this.dataPool = dataPool;
    }

    public String getDataExtraPool() {
        return dataExtraPool;
    }

    private void setDataExtraPool(String dataExtraPool) {
        this.dataExtraPool = dataExtraPool;
    }

    public String getIndexPool() {
        return indexPool;
    }

    private void setIndexPool(String indexPool) {
        this.indexPool = indexPool;
    }

    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id;
    }

    public String getMarker() {
        return marker;
    }

    private void setMarker(String marker) {
        this.marker = marker;
    }

    public String getIndexType() {
        return indexType;
    }

    private void setIndexType(String indexType) {
        this.indexType = indexType;
    }

    public String getOwner() {
        return owner;
    }

    private void setOwner(String owner) {
        this.owner = owner;
    }

    public String getVer() {
        return ver;
    }

    private void setVer(String ver) {
        this.ver = ver;
    }

    public String getMasterVer() {
        return masterVer;
    }

    private void setMasterVer(String masterVer) {
        this.masterVer = masterVer;
    }

    public Date getMtime() {
        return mtime;
    }

    private String getMtimeAsString() {
        return DATE_FORMATTER.format(mtime.toInstant());
    }

    private void setMtime(String mtime) {
        this.mtime = Date.from(Instant.from(DATE_FORMATTER.parse(mtime)));
    }

    public String getMaxMarker() {
        return maxMarker;
    }

    private void setMaxMarker(String maxMarker) {
        this.maxMarker = maxMarker;
    }

    public Usage getUsage() {
        return usage;
    }

    private void setUsage(Usage usage) {
        this.usage = usage;
    }

    public Quota getBucketQuota() {
        return bucketQuota;
    }

    private void setBucketQuota(Quota bucketQuota) {
        this.bucketQuota = bucketQuota;
    }

    @Override
    public String toString() {
        return "BucketInfo{" +
                "bucket='" + bucket + '\'' +
                ", numShards=" + numShards +
                ", tenant='" + tenant + '\'' +
                ", zoneGroup='" + zoneGroup + '\'' +
                ", placementRule='" + placementRule + '\'' +
                ", dataPool='" + dataPool + '\'' +
                ", dataExtraPool='" + dataExtraPool + '\'' +
                ", indexPool='" + indexPool + '\'' +
                ", id='" + id + '\'' +
                ", marker='" + marker + '\'' +
                ", indexType='" + indexType + '\'' +
                ", owner='" + owner + '\'' +
                ", ver='" + ver + '\'' +
                ", masterVer='" + masterVer + '\'' +
                ", mtime='" + mtime + '\'' +
                ", maxMarker='" + maxMarker + '\'' +
                ", usage=" + usage +
                ", bucketQuota=" + bucketQuota +
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
