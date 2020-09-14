package io.ceph.rgw.client.converter;

import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.model.BucketNotificationConfiguration;
import com.amazonaws.services.s3.model.CanonicalGrantee;
import com.amazonaws.services.s3.model.Filter;
import com.amazonaws.services.s3.model.FilterRule;
import com.amazonaws.services.s3.model.Grantee;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.NotificationConfiguration;
import com.amazonaws.services.s3.model.Owner;
import com.amazonaws.services.s3.model.QueueConfiguration;
import com.amazonaws.services.s3.model.TopicConfiguration;
import io.ceph.rgw.client.model.*;
import io.ceph.rgw.client.model.Bucket;
import io.ceph.rgw.client.model.EmailAddressGrantee;
import io.ceph.rgw.client.model.GetObjectRequest;
import io.ceph.rgw.client.model.Grant;
import io.ceph.rgw.client.model.MultipartUpload;
import io.ceph.rgw.client.model.Permission;
import io.ceph.rgw.client.util.IOUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Accepts s3 response model and converts to {@link BucketResponse}/{@link ObjectResponse}.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/3/1.
 * @see io.ceph.rgw.client.core.async.AsyncBucketClient
 * @see io.ceph.rgw.client.core.async.AsyncObjectClient
 * @see SyncRequestConverter
 */
public final class SyncResponseConverter {

    private SyncResponseConverter() {
        throw new RuntimeException();
    }

    public static PutObjectResponse putObject(PutObjectResult result) {
        return new PutObjectResponse(result.getContentMd5(),
                result.getETag(),
                result.getExpirationTime(),
                result.getExpirationTimeRuleId(),
                getMetadata(result),
                result.getVersionId());
    }

    private static Metadata getMetadata(PutObjectResult result) {
        return Optional.ofNullable(result.getMetadata()).map(SyncResponseConverter::convert).orElse(null);
    }

    private static Metadata convert(ObjectMetadata metadata) {
        Metadata.Builder builder = new Metadata.Builder();
        Optional.ofNullable(metadata.getCacheControl()).ifPresent(builder::withCacheControl);
        Optional.ofNullable(metadata.getContentDisposition()).ifPresent(builder::withContentDisposition);
        Optional.ofNullable(metadata.getContentEncoding()).ifPresent(builder::withContentEncoding);
        Optional.ofNullable(metadata.getContentLanguage()).ifPresent(builder::withContentLanguage);
        Optional.ofNullable(metadata.getContentLength()).ifPresent(builder::withContentLength);
        Optional.ofNullable(metadata.getContentMD5()).ifPresent(builder::withContentMD5);
        Optional.ofNullable(metadata.getContentType()).ifPresent(builder::withContentType);
        Optional.ofNullable(metadata.getHttpExpiresDate()).ifPresent(builder::withHttpExpiresDate);
        Optional.ofNullable(metadata.getUserMetadata()).ifPresent(kv -> kv.forEach(builder::add));
        return builder.build();
    }

    @FunctionalInterface
    public interface GetObjectResponseConverter<REQ extends BaseGetObjectRequest, RESP extends BaseGetObjectResponse> {
        RESP convert(REQ request, S3Object object);
    }

    public static GetObjectResponse getObject(GetObjectRequest request, S3Object object) {
        return new GetObjectResponse(object.getObjectMetadata().getVersionId(), object.getTaggingCount(), convert(object.getObjectMetadata()), object.getObjectContent());
    }

    public static GetFileResponse getFile(GetFileRequest request, S3Object object) {
        IOUtil.copyToFile(object.getObjectContent(), request.getPath().toFile());
        return new GetFileResponse(object.getObjectMetadata().getVersionId(), object.getTaggingCount(), convert(object.getObjectMetadata()));
    }

    public static GetInputStreamResponse getInputStream(GetInputStreamRequest request, S3Object object) {
        return new GetInputStreamResponse(object.getObjectMetadata().getVersionId(), object.getTaggingCount(), convert(object.getObjectMetadata()), object.getObjectContent());
    }

    public static GetStringResponse getString(GetStringRequest request, S3Object object) {
        return new GetStringResponse(object.getObjectMetadata().getVersionId(), object.getTaggingCount(), convert(object.getObjectMetadata()), IOUtil.toString(object.getObjectContent()));
    }

    public static CopyObjectResponse copyObject(CopyObjectResult result) {
        return new CopyObjectResponse(result.getETag(), result.getExpirationTime(), result.getExpirationTimeRuleId(), result.getLastModifiedDate(), result.getVersionId());
    }

    public static DeleteObjectResponse deleteObject() {
        return DeleteObjectResponse.INSTANCE;
    }

    public static GetObjectInfoResponse getObjectInfo(ObjectMetadata metadata) {
        return new GetObjectInfoResponse(Optional.ofNullable(metadata).map(SyncResponseConverter::convert).orElse(null));
    }

    public static GetObjectACLResponse getObjectACL(AccessControlList acl) {
        return new GetObjectACLResponse(convert(acl));
    }

    private static ACL convert(AccessControlList src) {
        return new ACL(convertGrants(src.getGrantsAsList()), convert(src.getOwner()));
    }

    public static PutObjectACLResponse putObjectACL() {
        return PutObjectACLResponse.INSTANCE;
    }

    public static InitiateMultipartUploadResponse initiateMultipartUpload(InitiateMultipartUploadResult result) {
        return new InitiateMultipartUploadResponse(result.getBucketName(), result.getKey(), result.getUploadId(), result.getAbortDate());
    }

    public static MultipartUploadPartResponse multipartUploadPart(UploadPartResult result) {
        return new MultipartUploadPartResponse(result.getETag());
    }

    public static CompleteMultipartUploadResponse completeMultipartUpload(CompleteMultipartUploadResult result) {
        return new CompleteMultipartUploadResponse(result.getBucketName(), result.getKey(), result.getVersionId(), result.getETag(), result.getLocation(), result.getExpirationTime(), result.getExpirationTimeRuleId());
    }

    public static AbortMultipartUploadResponse abortMultipartUpload() {
        return AbortMultipartUploadResponse.INSTANCE;
    }

    public static CreateBucketResponse createBucket(com.amazonaws.services.s3.model.Bucket bucket) {
        return CreateBucketResponse.INSTANCE;
    }

    public static DeleteBucketResponse deleteBucket() {
        return DeleteBucketResponse.INSTANCE;
    }

    public static GetBucketResponse getBucket(boolean exist) {
        return new GetBucketResponse(exist);
    }

    public static ListBucketsResponse listBuckets(List<com.amazonaws.services.s3.model.Bucket> buckets) {
        return new ListBucketsResponse(convertBuckets(buckets));
    }

    private static List<Bucket> convertBuckets(List<com.amazonaws.services.s3.model.Bucket> src) {
        if (src == null || src.size() == 0) {
            return Collections.emptyList();
        }
        return src.stream().map(SyncResponseConverter::convert)
                .collect(Collectors.toList());
    }

    private static Bucket convert(com.amazonaws.services.s3.model.Bucket src) {
        return new Bucket(src.getName(), convert(src.getOwner()), src.getCreationDate());
    }

    private static io.ceph.rgw.client.model.Owner convert(Owner src) {
        return Optional.ofNullable(src).map(owner -> new io.ceph.rgw.client.model.Owner(src.getId(), src.getDisplayName())).orElse(null);
    }

    public static GetBucketLocationResponse getBucketLocation(String location) {
        return new GetBucketLocationResponse(location);
    }

    public static GetBucketACLResponse getBucketACL(AccessControlList acl) {
        return new GetBucketACLResponse(convert(acl));
    }

    private static List<Grant> convertGrants(List<com.amazonaws.services.s3.model.Grant> src) {
        if (src == null || src.size() == 0) {
            return Collections.emptyList();
        }
        return src.stream().map(SyncResponseConverter::convert).collect(Collectors.toList());
    }

    private static Grant convert(com.amazonaws.services.s3.model.Grant src) {
        return new Grant(convert(src.getGrantee()), convert(src.getPermission()));
    }

    private static io.ceph.rgw.client.model.Grantee convert(Grantee src) {
        return Optional.ofNullable(src)
                .map(grantee -> {
                    if (grantee instanceof CanonicalGrantee) {
                        return new io.ceph.rgw.client.model.CanonicalGrantee(grantee.getIdentifier(), ((CanonicalGrantee) grantee).getDisplayName());
                    } else if (grantee instanceof GroupGrantee) {
                        switch ((GroupGrantee) grantee) {
                            case AllUsers:
                                return io.ceph.rgw.client.model.GroupGrantee.ALL_USERS;
                            case AuthenticatedUsers:
                                return io.ceph.rgw.client.model.GroupGrantee.AUTHENTICATED_USERS;
                            case LogDelivery:
                                return io.ceph.rgw.client.model.GroupGrantee.LOG_DELIVERY;
                        }
                    } else if (grantee instanceof EmailAddressGrantee) {
                        return new io.ceph.rgw.client.model.EmailAddressGrantee(((EmailAddressGrantee) grantee).getId());
                    }
                    return null;
                })
                .orElse(null);
    }

    private static Permission convert(com.amazonaws.services.s3.model.Permission permission) {
        if (permission == null) {
            return null;
        }
        switch (permission) {
            case FullControl:
                return Permission.FULL_CONTROL;
            case Read:
                return Permission.READ;
            case Write:
                return Permission.WRITE;
            case ReadAcp:
                return Permission.READ_ACP;
            case WriteAcp:
                return Permission.WRITE_ACP;
        }
        return null;
    }

    public static PutBucketACLResponse putBucketACL() {
        return PutBucketACLResponse.INSTANCE;
    }

    public static ListBucketMultipartUploadsResponse listMultipartUploadParts(MultipartUploadListing listing) {
        return new ListBucketMultipartUploadsResponse(listing.getBucketName(), listing.getKeyMarker(), listing.getDelimiter(), listing.getPrefix(), listing.getUploadIdMarker(), listing.getMaxUploads(), listing.getEncodingType(), listing.isTruncated(), listing.getNextKeyMarker(), listing.getNextUploadIdMarker(), convert(listing.getMultipartUploads()));
    }

    private static List<MultipartUpload> convert(List<com.amazonaws.services.s3.model.MultipartUpload> src) {
        if (src == null || src.size() == 0) {
            return Collections.emptyList();
        }
        return src.stream().map(SyncResponseConverter::convert).collect(Collectors.toList());
    }

    private static MultipartUpload convert(com.amazonaws.services.s3.model.MultipartUpload src) {
        return new MultipartUpload(src.getKey(), src.getUploadId(), convert(src.getOwner()), convert(src.getInitiator()), src.getInitiated());
    }

    public static PutBucketVersioningResponse putBucketVersioning() {
        return PutBucketVersioningResponse.INSTANCE;
    }

    public static GetBucketVersioningResponse getBucketVersioning(BucketVersioningConfiguration configuration) {
        return new GetBucketVersioningResponse(BucketVersioning.fromString(configuration.getStatus()));
    }

    public static PutNotificationResponse putNotification() {
        return PutNotificationResponse.INSTANCE;
    }

    public static GetNotificationResponse getNotification(BucketNotificationConfiguration configuration) {
        return new GetNotificationResponse(convert(configuration));
    }

    private static io.ceph.rgw.client.model.BucketNotificationConfiguration convert(BucketNotificationConfiguration src) {
        return Optional.ofNullable(src)
                .map(c -> new io.ceph.rgw.client.model.BucketNotificationConfiguration.Builder()
                        .withNotificationConfigurations(c.getConfigurations().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> SyncResponseConverter.convert(e.getValue())))).build())
                .orElse(null);
    }

    private static io.ceph.rgw.client.model.NotificationConfiguration convert(NotificationConfiguration src) {
        if (src == null) {
            return null;
        }
        if (src instanceof QueueConfiguration) {
            return convert((QueueConfiguration) src);
        } else if (src instanceof TopicConfiguration) {
            return convert((TopicConfiguration) src);
        }
        throw new UnsupportedOperationException("cannot convert NotificationConfiguration");
    }

    private static io.ceph.rgw.client.model.QueueConfiguration convert(QueueConfiguration src) {
        return new io.ceph.rgw.client.model.QueueConfiguration.Builder()
                .withQueueARN(src.getQueueARN())
                .withEvents(src.getEvents().stream().map(SyncResponseConverter::convert).collect(Collectors.toSet()))
                .withFilter(convert(src.getFilter()))
                .build();
    }

    private static io.ceph.rgw.client.model.TopicConfiguration convert(TopicConfiguration src) {
        return new io.ceph.rgw.client.model.TopicConfiguration.Builder()
                .withTopicARN(src.getTopicARN())
                .withEvents(src.getEvents().stream().map(SyncResponseConverter::convert).collect(Collectors.toSet()))
                .withFilter(convert(src.getFilter()))
                .build();
    }

    private static Event convert(String src) {
        if (StringUtils.isBlank(src)) {
            return null;
        }
        switch (src) {
            case "ObjectCreated":
                return Event.OBJECT_CREATED;
            case "ObjectCreatedByPut":
                return Event.OBJECT_CREATED_BY_PUT;
            case "ObjectCreatedByPost":
                return Event.OBJECT_CREATED_BY_POST;
            case "ObjectCreatedByCopy":
                return Event.OBJECT_CREATED_BY_COPY;
            case "ObjectCreatedByCompleteMultipartUpload":
                return Event.OBJECT_CREATED_BY_COMPLETE_MULTIPART_UPLOAD;
            case "ObjectRemoved":
                return Event.OBJECT_REMOVED;
            case "ObjectRemovedDelete":
                return Event.OBJECT_REMOVED_DELETE;
            case "ObjectRemovedDeleteMarkerCreated":
                return Event.OBJECT_REMOVED_DELETE_MARKER_CREATED;
        }
        throw new UnsupportedOperationException("cannot convert S3Event");
    }

    private static io.ceph.rgw.client.model.Filter convert(Filter src) {
        return Optional.ofNullable(src).map(f -> new io.ceph.rgw.client.model.Filter.Builder().withKeyFilter(convert(f.getS3KeyFilter())).build()).orElse(null);
    }

    private static KeyFilter convert(S3KeyFilter src) {
        return Optional.ofNullable(src).map(f -> new KeyFilter.Builder().withFilterRules(f.getFilterRules().stream().map(SyncResponseConverter::convert).collect(Collectors.toList())).build()).orElse(null);
    }

    private static io.ceph.rgw.client.model.FilterRule convert(FilterRule src) {
        return Optional.ofNullable(src).map(r -> new io.ceph.rgw.client.model.FilterRule(r.getName(), r.getValue())).orElse(null);
    }
}