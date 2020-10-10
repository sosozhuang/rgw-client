package io.ceph.rgw.client.converter;

import io.ceph.rgw.client.core.async.ByteBufferAsyncRequestBody;
import io.ceph.rgw.client.core.async.InputStreamAsyncRequestBody;
import io.ceph.rgw.client.model.*;
import io.ceph.rgw.client.model.ListObjectsRequest;
import org.apache.commons.lang3.ArrayUtils;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.model.AbortMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.model.CompleteMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetBucketLocationRequest;
import software.amazon.awssdk.services.s3.model.GetBucketVersioningRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.Grant;
import software.amazon.awssdk.services.s3.model.Grantee;
import software.amazon.awssdk.services.s3.model.ListBucketsRequest;
import software.amazon.awssdk.services.s3.model.NotificationConfiguration;
import software.amazon.awssdk.services.s3.model.Owner;
import software.amazon.awssdk.services.s3.model.Permission;
import software.amazon.awssdk.services.s3.model.PutBucketVersioningRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.Tagging;
import software.amazon.awssdk.services.s3.model.UploadPartRequest;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Accepts {@link BucketRequest}/{@link ObjectRequest} and converts to s3 request model.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/6/11.
 * @see io.ceph.rgw.client.core.async.AsyncBucketClient
 * @see io.ceph.rgw.client.core.async.AsyncObjectClient
 * @see AsyncResponseConverter
 */
public final class AsyncRequestConverter {
    static final DateTimeFormatter RFC822_DATE_FORMAT = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'").withLocale(Locale.CHINA).withZone(ZoneOffset.ofHours(8));

    private AsyncRequestConverter() {
        throw new RuntimeException();
    }

    public static PutObjectRequest putObject(GenericPutObjectRequest<?> src, Map<String, String> internalMetadata) {
        PutObjectRequest.Builder builder = PutObjectRequest.builder()
                .bucket(src.getBucketName())
                .key(src.getKey());
        Map<String, String> merged = new HashMap<>(internalMetadata);
        Optional.ofNullable(src.getMetadata()).ifPresent(m -> merged.putAll(m.all()));
        setMetadata(builder, src.getMetadata(), merged);
        if (src instanceof PutStringRequest) {
            builder.contentType("text/plain");
        }
        setTagging(builder, src.getTagging());
        setACL(builder, src.getACL());
        setCannedACL(builder, src.getCannedACL());
        return builder.build();
    }

    public static AsyncRequestBody putObjectContent(GenericPutObjectRequest<?> request) {
        if (request instanceof PutByteBufferRequest) {
            return new ByteBufferAsyncRequestBody(((PutByteBufferRequest) request).getValue());
        } else if (request instanceof PutFileRequest) {
            return AsyncRequestBody.fromFile(((PutFileRequest) request).getValue().toPath());
        } else if (request instanceof PutInputStreamRequest) {
            return new InputStreamAsyncRequestBody(((PutInputStreamRequest) request).getValue());
        } else if (request instanceof PutStringRequest) {
            return AsyncRequestBody.fromString(((PutStringRequest) request).getValue());
        }
        throw new UnsupportedOperationException("unknown put request");
    }

    private static void setMetadata(PutObjectRequest.Builder builder, Metadata metadata, Map<String, String> merged) {
        builder.metadata(merged);
        Optional.ofNullable(metadata).ifPresent(m -> builder.cacheControl(m.getCacheControl())
                .contentDisposition(m.getContentDisposition())
                .contentEncoding(m.getContentEncoding())
                .contentLanguage(m.getContentLanguage())
                .contentMD5(m.getContentMD5())
                .contentType(m.getContentType())
                .expires(Optional.ofNullable(m.getHttpExpiresDate()).map(Date::toInstant).orElse(null)));
    }

    private static void setTagging(PutObjectRequest.Builder builder, io.ceph.rgw.client.model.Tagging tagging) {
        if (tagging != null && tagging.all().size() > 0) {
            builder.tagging(Tagging.builder()
                    .tagSet(tagging.all().entrySet().stream()
                            .map(e -> Tag.builder().key(e.getKey()).value(e.getValue()).build())
                            .collect(Collectors.toList()))
                    .build());
        }
    }

    private static void setACL(PutObjectRequest.Builder builder, ACL acl) {
        Optional.ofNullable(acl).ifPresent(src -> {
            Map<io.ceph.rgw.client.model.Permission, Collection<io.ceph.rgw.client.model.Grantee>> map = new HashMap<>();
            for (io.ceph.rgw.client.model.Grant grant : acl.getGrants()) {
                map.computeIfAbsent(grant.getPermission(), k -> new LinkedList<>()).add(grant.getGrantee());
            }
            for (Map.Entry<io.ceph.rgw.client.model.Permission, Collection<io.ceph.rgw.client.model.Grantee>> entry : map.entrySet()) {
                switch (entry.getKey()) {
                    case FULL_CONTROL:
                        builder.grantFullControl(convertGrantees(entry.getValue()));
                        break;
                    case READ:
                        builder.grantRead(convertGrantees(entry.getValue()));
                        break;
                    case READ_ACP:
                        builder.grantReadACP(convertGrantees(entry.getValue()));
                        break;
                    case WRITE_ACP:
                        builder.grantWriteACP(convertGrantees(entry.getValue()));
                        break;
                }
            }
        });
    }

    private static String convertGrantees(Collection<io.ceph.rgw.client.model.Grantee> grantees) {
        return grantees.stream().map(grantee -> grantee.getType() + "=\"" + grantee.getId() + "\"").collect(Collectors.joining(", "));
    }

    private static void setCannedACL(PutObjectRequest.Builder builder, CannedACL cannedACL) {
        Optional.ofNullable(cannedACL).ifPresent(src -> builder.acl(convertObjectACL(src)));
    }

    private static ObjectCannedACL convertObjectACL(CannedACL src) {
        if (src == null) {
            return null;
        }
        switch (src) {
            case PRIVATE:
                return ObjectCannedACL.PRIVATE;
            case PUBLIC_READ:
                return ObjectCannedACL.PUBLIC_READ;
            case PUBLIC_READ_WRITE:
                return ObjectCannedACL.PUBLIC_READ_WRITE;
            case AUTHENTICATED_READ:
                return ObjectCannedACL.AUTHENTICATED_READ;
        }
        throw new IllegalArgumentException("cannot convert CannedACL");
    }

    public static GetObjectRequest getObject(io.ceph.rgw.client.model.BaseGetObjectRequest src) {
        GetObjectRequest.Builder builder = GetObjectRequest.builder().bucket(src.getBucketName()).key(src.getKey()).versionId(src.getVersionId());
        if (ArrayUtils.isNotEmpty(src.getRange())) {
            builder.range("bytes=" + src.getRange()[0] + "-" + src.getRange()[1]);
        }
        return builder.build();
    }

    public static CopyObjectRequest copyObject(io.ceph.rgw.client.model.CopyObjectRequest src) {
        return CopyObjectRequest.builder()
                .copySource(src.getSrcBucketName() + "/" + src.getSrcKey())
                .destinationBucket(src.getDestBucketName())
                .destinationKey(src.getDestKey())
                .build();
    }

    public static DeleteObjectRequest deleteObject(io.ceph.rgw.client.model.DeleteObjectRequest src) {
        return DeleteObjectRequest.builder().bucket(src.getBucketName()).key(src.getKey()).build();
    }

    public static HeadObjectRequest getObjectInfo(GetObjectInfoRequest src) {
        return HeadObjectRequest.builder().bucket(src.getBucketName()).key(src.getKey()).versionId(src.getVersionId()).build();
    }

    public static GetObjectAclRequest getObjectACL(GetObjectACLRequest src) {
        return GetObjectAclRequest.builder().bucket(src.getBucketName()).key(src.getKey()).versionId(src.getVersionId()).build();
    }

    public static PutObjectAclRequest putObjectACL(PutObjectACLRequest src) {
        PutObjectAclRequest.Builder builder = PutObjectAclRequest.builder().bucket(src.getBucketName()).key(src.getKey()).versionId(src.getVersionId());
        if (src.getACL() != null) {
//            setACL(builder, src.getACL());
            builder.accessControlPolicy(convert(src.getACL()));
        } else {
            builder.acl(convertObjectACL(src.getCannedACL()));
        }
        return builder.build();
    }

//    private static void setACL(PutObjectAclRequest.Builder builder, ACL acl) {
//        Optional.ofNullable(acl).ifPresent(src -> {
//            Map<Permission, Collection<Grantee>> map = new HashMap<>();
//            for (Grant grant : acl.getGrants()) {
//                map.computeIfAbsent(grant.getPermission(), k -> new LinkedList<>()).add(grant.getGrantee());
//            }
//            for (Map.Entry<Permission, Collection<Grantee>> entry : map.entrySet()) {
//                switch (entry.getKey()) {
//                    case FULL_CONTROL:
//                        builder.grantFullControl(convertGrantees(entry.getValue()));
//                        break;
//                    case READ:
//                        builder.grantRead(convertGrantees(entry.getValue()));
//                        break;
//                    case WRITE:
//                        builder.grantWrite(convertGrantees(entry.getValue()));
//                        break;
//                    case READ_ACP:
//                        builder.grantReadACP(convertGrantees(entry.getValue()));
//                        break;
//                    case WRITE_ACP:
//                        builder.grantWriteACP(convertGrantees(entry.getValue()));
//                        break;
//                }
//            }
//        });
//    }

    public static CreateMultipartUploadRequest initiateMultipartUpload(InitiateMultipartUploadRequest src, Map<String, String> internalMetadata) {
        CreateMultipartUploadRequest.Builder builder = CreateMultipartUploadRequest.builder()
                .bucket(src.getBucketName())
                .key(src.getKey());
        Map<String, String> merged = new HashMap<>(internalMetadata);
        Optional.ofNullable(src.getMetadata()).ifPresent(m -> merged.putAll(m.all()));
        setMetadata(builder, src.getMetadata(), merged);
        if (src.getACL() != null) {
            setACL(builder, src.getACL());
        } else {
            builder.acl(convertObjectACL(src.getCannedACL()));
        }
        return builder.build();
    }

    private static void setMetadata(CreateMultipartUploadRequest.Builder builder, Metadata metadata, Map<String, String> merged) {
        builder.metadata(merged);
        Optional.ofNullable(metadata).ifPresent(m -> builder.cacheControl(m.getCacheControl())
                .contentDisposition(m.getContentDisposition())
                .contentEncoding(m.getContentEncoding())
                .contentLanguage(m.getContentLanguage())
//                .contentMD5(m.getContentMd5())
                .contentType(m.getContentType())
                .expires(Optional.ofNullable(m.getHttpExpiresDate()).map(Date::toInstant).orElse(null)));
    }

    private static void setACL(CreateMultipartUploadRequest.Builder builder, ACL acl) {
        Optional.ofNullable(acl).ifPresent(src -> {
            Map<io.ceph.rgw.client.model.Permission, Collection<io.ceph.rgw.client.model.Grantee>> map = new HashMap<>();
            for (io.ceph.rgw.client.model.Grant grant : acl.getGrants()) {
                map.computeIfAbsent(grant.getPermission(), k -> new LinkedList<>()).add(grant.getGrantee());
            }
            for (Map.Entry<io.ceph.rgw.client.model.Permission, Collection<io.ceph.rgw.client.model.Grantee>> entry : map.entrySet()) {
                switch (entry.getKey()) {
                    case FULL_CONTROL:
                        builder.grantFullControl(convertGrantees(entry.getValue()));
                        break;
                    case READ:
                        builder.grantRead(convertGrantees(entry.getValue()));
                        break;
                    case READ_ACP:
                        builder.grantReadACP(convertGrantees(entry.getValue()));
                        break;
                    case WRITE_ACP:
                        builder.grantWriteACP(convertGrantees(entry.getValue()));
                        break;
                }
            }
        });
    }

    public static UploadPartRequest uploadPart(io.ceph.rgw.client.model.UploadPartRequest src) {
        return UploadPartRequest.builder()
                .bucket(src.getBucketName())
                .key(src.getKey())
                .contentMD5(src.getMD5())
                .partNumber(src.getPartNumber())
                .uploadId(src.getUploadId())
                .build();
    }

    public static CompleteMultipartUploadRequest completeMultipartUpload(io.ceph.rgw.client.model.CompleteMultipartUploadRequest src) {
        return CompleteMultipartUploadRequest.builder()
                .bucket(src.getBucketName())
                .key(src.getKey())
                .uploadId(src.getUploadId())
                .multipartUpload(CompletedMultipartUpload.builder().parts(AsyncRequestConverter.convertPartETags(src.getPartETags())).build())
                .build();
    }

    private static List<CompletedPart> convertPartETags(List<PartETag> src) {
        return src.stream().map(AsyncRequestConverter::convert).collect(Collectors.toList());
    }

    private static CompletedPart convert(PartETag src) {
        return CompletedPart.builder().eTag(src.getETag()).partNumber(src.getPart()).build();
    }

    public static AbortMultipartUploadRequest abortMultipartUpload(io.ceph.rgw.client.model.AbortMultipartUploadRequest src) {
        return AbortMultipartUploadRequest.builder()
                .bucket(src.getBucketName())
                .key(src.getKey())
                .uploadId(src.getUploadId())
                .build();
    }

    public static CreateBucketRequest createBucket(io.ceph.rgw.client.model.CreateBucketRequest src) {
        return CreateBucketRequest.builder()
                .bucket(src.getBucketName())
                .objectLockEnabledForBucket(src.isEnableObjectLock())
                .build();
    }

    public static DeleteBucketRequest deleteBucket(io.ceph.rgw.client.model.DeleteBucketRequest src) {
        return DeleteBucketRequest.builder()
                .bucket(src.getBucketName())
                .build();
    }

    public static HeadBucketRequest getBucket(GetBucketRequest src) {
        return HeadBucketRequest.builder()
                .bucket(src.getBucketName())
                .build();
    }

    public static ListBucketsRequest listBuckets(io.ceph.rgw.client.model.ListBucketsRequest src) {
        return ListBucketsRequest.builder().build();
    }

    public static ListObjectsV2Request listObjects(ListObjectsRequest src) {
        return ListObjectsV2Request.builder()
                .bucket(src.getBucketName())
                .prefix(src.getPrefix())
                .delimiter(src.getDelimiter())
                .maxKeys(src.getMaxKeys())
                .encodingType(src.getEncodingType())
                .continuationToken(src.getContinuationToken())
                .fetchOwner(src.isFetchOwner())
                .startAfter(src.getStartAfter())
                .build();
    }

    public static GetBucketLocationRequest getBucketLocation(io.ceph.rgw.client.model.GetBucketLocationRequest src) {
        return GetBucketLocationRequest.builder()
                .bucket(src.getBucketName())
                .build();
    }

    public static GetBucketAclRequest getBucketACL(GetBucketACLRequest src) {
        return GetBucketAclRequest.builder()
                .bucket(src.getBucketName())
                .build();
    }

    public static PutBucketAclRequest putBucketACL(PutBucketACLRequest src) {
        PutBucketAclRequest.Builder builder = PutBucketAclRequest.builder()
                .bucket(src.getBucketName());
        if (src.getACL() != null) {
            builder.accessControlPolicy(convert(src.getACL()));
        } else {
            builder.acl(convertBucketACL(src.getCannedACL()));
        }
        return builder.build();
    }

//    private static void setACL(PutBucketAclRequest.Builder builder, ACL acl) {
//        Optional.ofNullable(acl).ifPresent(src -> {
//            Map<Permission, Collection<Grantee>> map = new HashMap<>();
//            for (Grant grant : acl.getGrants()) {
//                map.computeIfAbsent(grant.getPermission(), k -> new LinkedList<>()).add(grant.getGrantee());
//            }
//            for (Map.Entry<Permission, Collection<Grantee>> entry : map.entrySet()) {
//                switch (entry.getKey()) {
//                    case FULL_CONTROL:
//                        builder.grantFullControl(convertGrantees(entry.getValue()));
//                        break;
//                    case READ:
//                        builder.grantRead(convertGrantees(entry.getValue()));
//                        break;
//                    case WRITE:
//                        builder.grantWrite(convertGrantees(entry.getValue()));
//                        break;
//                    case READ_ACP:
//                        builder.grantReadACP(convertGrantees(entry.getValue()));
//                        break;
//                    case WRITE_ACP:
//                        builder.grantWriteACP(convertGrantees(entry.getValue()));
//                        break;
//                }
//            }
//        });
//    }

    private static AccessControlPolicy convert(ACL src) {
        AccessControlPolicy.Builder builder = AccessControlPolicy.builder().owner(convert(src.getOwner()));
        builder.grants(convertGrants(src.getGrants()));
        return builder.build();
    }

    private static Owner convert(io.ceph.rgw.client.model.Owner src) {
        return Optional.ofNullable(src).map(o -> Owner.builder().id(src.getId()).displayName(src.getDisplayName()).build()).orElse(null);
    }

    private static List<Grant> convertGrants(List<io.ceph.rgw.client.model.Grant> src) {
        if (src == null || src.size() == 0) {
            return Collections.emptyList();
        }
        return src.stream().map(AsyncRequestConverter::convert).collect(Collectors.toList());
    }

    private static Grant convert(io.ceph.rgw.client.model.Grant src) {
        return Grant.builder().grantee(convert(src.getGrantee())).permission(convert(src.getPermission())).build();
    }

    private static Grantee convert(io.ceph.rgw.client.model.Grantee src) {
        Grantee.Builder builder = Grantee.builder();
        if (src instanceof CanonicalGrantee) {
            return builder.type(Type.CANONICAL_USER).id(src.getId()).displayName(((CanonicalGrantee) src).getDisplayName()).build();
        } else if (src instanceof GroupGrantee) {
            return builder.type(Type.GROUP).uri(src.getId()).build();
        } else if (src instanceof EmailAddressGrantee) {
            return builder.type(Type.AMAZON_CUSTOMER_BY_EMAIL).emailAddress(src.getId()).build();
        }
        throw new IllegalArgumentException("cannot convert Grantee");
    }

    private static Permission convert(io.ceph.rgw.client.model.Permission src) {
        if (src == null) {
            return null;
        }
        switch (src) {
            case FULL_CONTROL:
                return Permission.FULL_CONTROL;
            case READ:
                return Permission.READ;
            case WRITE:
                return Permission.WRITE;
            case READ_ACP:
                return Permission.READ_ACP;
            case WRITE_ACP:
                return Permission.WRITE_ACP;
        }
        throw new IllegalArgumentException("cannot convert Permission");
    }

    private static BucketCannedACL convertBucketACL(CannedACL src) {
        if (src == null) {
            return null;
        }
        switch (src) {
            case PRIVATE:
                return BucketCannedACL.PRIVATE;
            case PUBLIC_READ:
                return BucketCannedACL.PUBLIC_READ;
            case PUBLIC_READ_WRITE:
                return BucketCannedACL.PUBLIC_READ_WRITE;
            case AUTHENTICATED_READ:
                return BucketCannedACL.AUTHENTICATED_READ;
        }
        throw new IllegalArgumentException("cannot convert CannedACL");
    }

    public static ListMultipartUploadsRequest listBucketMultipartUploads(ListMultipartUploadPartsRequest src) {
        return ListMultipartUploadsRequest.builder()
                .bucket(src.getBucketName())
                .encodingType(src.getEncodingType())
                .keyMarker(src.getKeyMarker())
                .maxUploads(src.getMaxUploads())
                .prefix(src.getPrefix())
                .uploadIdMarker(src.getUploadIdMarker())
                .build();
    }

    public static PutBucketVersioningRequest putBucketVersioning(io.ceph.rgw.client.model.PutBucketVersioningRequest src) {
        return PutBucketVersioningRequest.builder()
                .bucket(src.getBucketName())
                .versioningConfiguration(VersioningConfiguration.builder().status(convert(src.getBucketVersioning())).build())
                .build();
    }

    private static BucketVersioningStatus convert(BucketVersioning src) {
        if (src == null) {
            return BucketVersioningStatus.UNKNOWN_TO_SDK_VERSION;
        }
        switch (src) {
            case OFF:
                return BucketVersioningStatus.UNKNOWN_TO_SDK_VERSION;
            case SUSPENDED:
                return BucketVersioningStatus.SUSPENDED;
            case ENABLED:
                return BucketVersioningStatus.ENABLED;
        }
        throw new IllegalArgumentException("cannot convert BucketVersioning");
    }

    public static GetBucketVersioningRequest getBucketVersioning(io.ceph.rgw.client.model.GetBucketVersioningRequest src) {
        return GetBucketVersioningRequest.builder()
                .bucket(src.getBucketName())
                .build();
    }

    public static PutBucketNotificationConfigurationRequest putNotification(PutBucketNotificationRequest src) {
        return PutBucketNotificationConfigurationRequest.builder()
                .bucket(src.getBucketName())
                .notificationConfiguration(NotificationConfiguration.builder().build())
                .build();
    }

    public static GetBucketNotificationConfigurationRequest getNotification(GetBucketNotificationRequest src) {
        return GetBucketNotificationConfigurationRequest.builder()
                .bucket(src.getBucketName())
                .build();
    }
}