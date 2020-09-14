package io.ceph.rgw.client.converter;

import com.amazonaws.services.s3.model.AbortMultipartUploadRequest;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.model.BucketNotificationConfiguration;
import com.amazonaws.services.s3.model.CanonicalGrantee;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.DeleteBucketRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.EmailAddressGrantee;
import com.amazonaws.services.s3.model.Filter;
import com.amazonaws.services.s3.model.FilterRule;
import com.amazonaws.services.s3.model.GetBucketLocationRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.Grant;
import com.amazonaws.services.s3.model.Grantee;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.ListBucketsRequest;
import com.amazonaws.services.s3.model.NotificationConfiguration;
import com.amazonaws.services.s3.model.Owner;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.QueueConfiguration;
import com.amazonaws.services.s3.model.TopicConfiguration;
import com.amazonaws.services.s3.model.UploadPartRequest;
import com.amazonaws.util.StringUtils;
import io.ceph.rgw.client.core.sync.ByteBufferInputStream;
import io.ceph.rgw.client.model.*;
import org.apache.commons.lang3.ArrayUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Accepts {@link BucketRequest}/{@link ObjectRequest} and converts to s3 request model.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/2/29.
 * @see io.ceph.rgw.client.core.async.AsyncBucketClient
 * @see io.ceph.rgw.client.core.async.AsyncObjectClient
 * @see SyncResponseConverter
 */
public final class SyncRequestConverter {
    private SyncRequestConverter() {
        throw new RuntimeException();
    }

    @FunctionalInterface
    public interface PutObjectRequestConverter<REQ extends GenericPutObjectRequest<?>> {
        PutObjectRequest convert(REQ src);
    }

    public static PutObjectRequest putObject(GenericPutObjectRequest<?> request) {
        if (request instanceof PutFileRequest) {
            return putFile((PutFileRequest) request);
        } else if (request instanceof PutInputStreamRequest) {
            return putInputStream((PutInputStreamRequest) request);
        } else if (request instanceof PutStringRequest) {
            return putString((PutStringRequest) request);
        }
        throw new UnsupportedOperationException("unknown put request");
    }

    public static PutObjectRequest putByteBuffer(PutByteBufferRequest src) {
        PutObjectRequest dest = new PutObjectRequest(src.getBucketName(), src.getKey(), new ByteBufferInputStream(src.getValue()), null);
        dest.setMetadata(convert(src.getMetadata()));
        setTagging(dest, src.getTagging());
        setACL(dest, src.getACL());
        setCannedACL(dest, src.getCannedACL());
        return dest;
    }

    public static PutObjectRequest putFile(PutFileRequest src) {
        PutObjectRequest dest = new PutObjectRequest(src.getBucketName(), src.getKey(), src.getValue());
        dest.setMetadata(convert(src.getMetadata()));
        setTagging(dest, src.getTagging());
        setACL(dest, src.getACL());
        setCannedACL(dest, src.getCannedACL());
        return dest;
    }

    public static PutObjectRequest putInputStream(PutInputStreamRequest src) {
        PutObjectRequest dest = new PutObjectRequest(src.getBucketName(), src.getKey(), src.getValue(), null);
        dest.setMetadata(convert(src.getMetadata()));
        setTagging(dest, src.getTagging());
        setACL(dest, src.getACL());
        setCannedACL(dest, src.getCannedACL());
        return dest;
    }

    public static PutObjectRequest putString(PutStringRequest src) {
        byte[] bytes = src.getValue().getBytes(StringUtils.UTF8);
        InputStream is = new ByteArrayInputStream(bytes);
        PutObjectRequest dest = new PutObjectRequest(src.getBucketName(), src.getKey(), is, new ObjectMetadata());
        dest.setMetadata(convert(src.getMetadata()));
        dest.getMetadata().setContentType("text/plain");
        dest.getMetadata().setContentLength(bytes.length);
        setTagging(dest, src.getTagging());
        setACL(dest, src.getACL());
        setCannedACL(dest, src.getCannedACL());
        return dest;
    }

    private static void setTagging(PutObjectRequest request, Tagging tagging) {
        Optional.ofNullable(tagging).map(SyncRequestConverter::convert).ifPresent(request::setTagging);
    }

    private static void setACL(PutObjectRequest request, ACL acl) {
        Optional.ofNullable(acl).map(SyncRequestConverter::convert).ifPresent(request::setAccessControlList);
    }

    private static void setCannedACL(PutObjectRequest request, CannedACL cannedACL) {
        Optional.ofNullable(cannedACL).map(SyncRequestConverter::convert).ifPresent(request::setCannedAcl);
    }

    private static ObjectMetadata convert(Metadata src) {
        ObjectMetadata dest = new ObjectMetadata();
        if (src == null) {
            return dest;
        }
        src.forEach(e -> dest.addUserMetadata(e.getKey(), e.getValue()));
        Optional.ofNullable(src.getCacheControl()).ifPresent(dest::setCacheControl);
        Optional.ofNullable(src.getContentDisposition()).ifPresent(dest::setContentDisposition);
        Optional.ofNullable(src.getContentEncoding()).ifPresent(dest::setContentEncoding);
        Optional.ofNullable(src.getContentLanguage()).ifPresent(dest::setContentLanguage);
        Optional.ofNullable(src.getContentLength()).ifPresent(dest::setContentLength);
        Optional.ofNullable(src.getContentMD5()).ifPresent(dest::setContentMD5);
        Optional.ofNullable(src.getContentType()).ifPresent(dest::setContentType);
        Optional.ofNullable(src.getHttpExpiresDate()).ifPresent(dest::setHttpExpiresDate);
        return dest;
    }

    private static ObjectTagging convert(Tagging src) {
        return new ObjectTagging(StreamSupport.stream(src.spliterator(), false)
                .map(e -> new Tag(e.getKey(), e.getValue())).collect(Collectors.toList()));
    }

    public static GetObjectRequest getObject(io.ceph.rgw.client.model.BaseGetObjectRequest src) {
        GetObjectRequest dest = new GetObjectRequest(src.getBucketName(), src.getKey(), src.getVersionId());
        long[] range = src.getRange();
        if (ArrayUtils.isNotEmpty(range)) {
            dest.setRange(range[0], range[1]);
        }
        return dest;
    }

//    public static GetObjectRequest getFile(GetFileRequest src) {
//        return new GetObjectRequest(src.getBucketName(), src.getKey(), src.getVersionId());
//    }
//
//    public static GetObjectRequest getInputStream(GetInputStreamRequest src) {
//        return new GetObjectRequest(src.getBucketName(), src.getKey(), src.getVersionId());
//    }
//
//    public static GetObjectRequest getString(GetStringRequest src) {
//        return new GetObjectRequest(src.getBucketName(), src.getKey(), src.getVersionId());
//    }

    public static CopyObjectRequest copyObject(io.ceph.rgw.client.model.CopyObjectRequest src) {
        return new CopyObjectRequest(src.getSrcBucketName(), src.getSrcKey(), src.getSrcVersionId(), src.getDestBucketName(), src.getDestKey());
    }

    public static DeleteObjectRequest deleteObject(io.ceph.rgw.client.model.DeleteObjectRequest src) {
        return new DeleteObjectRequest(src.getBucketName(), src.getKey());
    }

    public static GetObjectMetadataRequest getObjectInfo(GetObjectInfoRequest src) {
        return new GetObjectMetadataRequest(src.getBucketName(), src.getKey(), src.getVersionId());
    }

    public static GetObjectAclRequest getObjectACL(GetObjectACLRequest src) {
        return new GetObjectAclRequest(src.getBucketName(), src.getKey(), src.getVersionId());
    }

    public static SetObjectAclRequest putObjectACL(PutObjectACLRequest src) {
        if (src.getACL() != null) {
            return new SetObjectAclRequest(src.getBucketName(), src.getKey(), src.getVersionId(), convert(src.getACL()));
        }
        return new SetObjectAclRequest(src.getBucketName(), src.getKey(), src.getVersionId(), convert(src.getCannedACL()));
    }

    public static InitiateMultipartUploadRequest initiateMultipartUpload(io.ceph.rgw.client.model.InitiateMultipartUploadRequest src) {
        InitiateMultipartUploadRequest dest = new InitiateMultipartUploadRequest(src.getBucketName(), src.getKey());
        dest.setObjectMetadata(convert(src.getMetadata()));
        setACL(dest, src.getACL());
        setCannedACL(dest, src.getCannedACL());
        return dest;
    }

    private static void setMetadata(InitiateMultipartUploadRequest request, Metadata metadata) {
        Optional.ofNullable(metadata).map(SyncRequestConverter::convert).ifPresent(request::setObjectMetadata);
    }

    private static void setACL(InitiateMultipartUploadRequest request, ACL acl) {
        Optional.ofNullable(acl).map(SyncRequestConverter::convert).ifPresent(request::setAccessControlList);
    }

    private static void setCannedACL(InitiateMultipartUploadRequest request, CannedACL cannedACL) {
        Optional.ofNullable(cannedACL).map(SyncRequestConverter::convert).ifPresent(request::setCannedACL);
    }

    public static UploadPartRequest uploadByteBuffer(UploadByteBufferRequest src) {
        UploadPartRequest dest = new UploadPartRequest();
        dest.setBucketName(src.getBucketName());
        dest.setKey(src.getKey());
        dest.setInputStream(new ByteBufferInputStream(src.getUpload()));
        dest.setLastPart(src.isLastPart());
        dest.setMd5Digest(src.getMD5());
        dest.setPartNumber(src.getPartNumber());
        dest.setPartSize(src.getPartSize() == 0 ? src.getUpload().remaining() : src.getPartSize());
        dest.setUploadId(src.getUploadId());
        return dest;
    }

    public static UploadPartRequest uploadFile(UploadFileRequest src) {
        UploadPartRequest dest = new UploadPartRequest();
        dest.setBucketName(src.getBucketName());
        dest.setKey(src.getKey());
        dest.setFile(src.getUpload());
        dest.setFileOffset(src.getFileOffset());
        dest.setLastPart(src.isLastPart());
        dest.setMd5Digest(src.getMD5());
        dest.setPartNumber(src.getPartNumber());
        dest.setPartSize(src.getPartSize() == 0 ? src.getUpload().length() - src.getFileOffset() : src.getPartSize());
        dest.setUploadId(src.getUploadId());
        return dest;
    }

    public static UploadPartRequest uploadInputStream(UploadInputStreamRequest src) {
        UploadPartRequest dest = new UploadPartRequest();
        dest.setBucketName(src.getBucketName());
        dest.setKey(src.getKey());
        dest.setInputStream(src.getUpload());
        dest.setLastPart(src.isLastPart());
        dest.setMd5Digest(src.getMD5());
        dest.setPartNumber(src.getPartNumber());
        dest.setPartSize(src.getPartSize());
        dest.setUploadId(src.getUploadId());
        return dest;
    }

    public static UploadPartRequest uploadString(UploadStringRequest src) {
        UploadPartRequest dest = new UploadPartRequest();
        dest.setBucketName(src.getBucketName());
        dest.setKey(src.getKey());
        Charset charset = src.getCharset();
        dest.setInputStream(new ByteArrayInputStream(charset == null ? src.getUpload().getBytes() : src.getUpload().getBytes(charset)));
        dest.setLastPart(src.isLastPart());
        dest.setMd5Digest(src.getMD5());
        dest.setPartNumber(src.getPartNumber());
        dest.setPartSize(src.getPartSize() == 0 ? src.getUpload().length() : src.getPartSize());
        dest.setUploadId(src.getUploadId());
        return dest;
    }

    public static CompleteMultipartUploadRequest completeMultipartUpload(io.ceph.rgw.client.model.CompleteMultipartUploadRequest src) {
        return new CompleteMultipartUploadRequest(src.getBucketName(), src.getKey(), src.getUploadId(), convert(src.getPartETags()));
    }

    private static List<PartETag> convert(List<io.ceph.rgw.client.model.PartETag> src) {
        if (src == null || src.size() == 0) {
            return Collections.emptyList();
        }
        return src.stream().map(SyncRequestConverter::convert).collect(Collectors.toList());
    }

    private static PartETag convert(io.ceph.rgw.client.model.PartETag src) {
        return new PartETag(src.getPart(), src.getETag());
    }

    public static AbortMultipartUploadRequest abortMultipartUpload(io.ceph.rgw.client.model.AbortMultipartUploadRequest src) {
        return new AbortMultipartUploadRequest(src.getBucketName(), src.getKey(), src.getUploadId());
    }

    public static CreateBucketRequest createBucket(io.ceph.rgw.client.model.CreateBucketRequest src) {
        CreateBucketRequest dest = new CreateBucketRequest(src.getBucketName());
        setACL(dest, src.getACL());
        setCannedACL(dest, src.getCannedACL());
        dest.putCustomRequestHeader("x-amz-bucket-client-lock-enabled", String.valueOf(src.isEnableObjectLock()));
        return dest;
    }

    private static void setACL(CreateBucketRequest request, ACL acl) {
        Optional.ofNullable(acl).map(SyncRequestConverter::convert).ifPresent(request::setAccessControlList);
    }

    private static void setCannedACL(CreateBucketRequest request, CannedACL cannedACL) {
        Optional.ofNullable(cannedACL).map(SyncRequestConverter::convert).ifPresent(request::setCannedAcl);
    }

    private static CannedAccessControlList convert(CannedACL src) {
        if (src == null) {
            return null;
        }
        switch (src) {
            case PRIVATE:
                return CannedAccessControlList.Private;
            case PUBLIC_READ:
                return CannedAccessControlList.PublicRead;
            case PUBLIC_READ_WRITE:
                return CannedAccessControlList.PublicReadWrite;
            case AUTHENTICATED_READ:
                return CannedAccessControlList.AuthenticatedRead;
        }
        throw new IllegalArgumentException("cannot convert CannedACL");
    }

    public static DeleteBucketRequest deleteBucket(io.ceph.rgw.client.model.DeleteBucketRequest src) {
        return new DeleteBucketRequest(src.getBucketName());
    }

    public static String getBucket(GetBucketRequest request) {
        return request.getBucketName();
    }

    public static ListBucketsRequest listBuckets(io.ceph.rgw.client.model.ListBucketsRequest src) {
        return new ListBucketsRequest();
    }

    public static GetBucketLocationRequest getBucketLocation(io.ceph.rgw.client.model.GetBucketLocationRequest src) {
        return new GetBucketLocationRequest(src.getBucketName());
    }

    public static GetBucketAclRequest getBucketACL(GetBucketACLRequest src) {
        return new GetBucketAclRequest(src.getBucketName());
    }

    public static SetBucketAclRequest putBucketACL(PutBucketACLRequest src) {
        if (src.getACL() != null) {
            return new SetBucketAclRequest(src.getBucketName(), convert(src.getACL()));
        }
        return new SetBucketAclRequest(src.getBucketName(), convert(src.getCannedACL()));
    }

    private static AccessControlList convert(ACL src) {
        AccessControlList dest = new AccessControlList();
        setOwner(dest, src.getOwner());
        setGrants(dest, src.getGrants());
        return dest;
    }

    private static void setOwner(AccessControlList acl, io.ceph.rgw.client.model.Owner owner) {
        Optional.ofNullable(owner).map(SyncRequestConverter::convert).ifPresent(acl::setOwner);
    }

    private static Owner convert(io.ceph.rgw.client.model.Owner src) {
        return new Owner(src.getId(), src.getDisplayName());
    }

    private static void setGrants(AccessControlList acl, List<io.ceph.rgw.client.model.Grant> grants) {
        if (grants == null || grants.size() == 0) {
            return;
        }
        acl.grantAllPermissions(grants.stream().map(SyncRequestConverter::convert).toArray(Grant[]::new));
    }

    private static Grant convert(io.ceph.rgw.client.model.Grant src) {
        return new Grant(convert(src.getGrantee()), convert(src.getPermission()));
    }

    private static Grantee convert(io.ceph.rgw.client.model.Grantee src) {
        return Optional.ofNullable(src).map(g -> {
            if (g instanceof io.ceph.rgw.client.model.CanonicalGrantee) {
                CanonicalGrantee dest = new CanonicalGrantee(g.getId());
                dest.setDisplayName(((io.ceph.rgw.client.model.CanonicalGrantee) g).getDisplayName());
                return dest;
            } else if (g instanceof io.ceph.rgw.client.model.GroupGrantee) {
                switch ((io.ceph.rgw.client.model.GroupGrantee) g) {
                    case ALL_USERS:
                        return GroupGrantee.AllUsers;
                    case AUTHENTICATED_USERS:
                        return GroupGrantee.AuthenticatedUsers;
                    case LOG_DELIVERY:
                        return GroupGrantee.LogDelivery;
                }
            } else if (g instanceof io.ceph.rgw.client.model.EmailAddressGrantee) {
                return new EmailAddressGrantee(g.getId());
            }
            throw new IllegalArgumentException("cannot convert Grantee");
        }).orElse(null);
    }

    private static Permission convert(io.ceph.rgw.client.model.Permission src) {
        if (src == null) {
            return null;
        }
        switch (src) {
            case FULL_CONTROL:
                return Permission.FullControl;
            case READ:
                return Permission.Read;
            case WRITE:
                return Permission.Write;
            case READ_ACP:
                return Permission.ReadAcp;
            case WRITE_ACP:
                return Permission.WriteAcp;
        }
        throw new IllegalArgumentException("cannot convert Permission");
    }

    public static ListMultipartUploadsRequest listMultipartUploadParts(ListMultipartUploadPartsRequest src) {
        ListMultipartUploadsRequest dest = new ListMultipartUploadsRequest(src.getBucketName());
        dest.setDelimiter(src.getDelimiter());
        dest.setEncodingType(src.getEncodingType());
        dest.setKeyMarker(src.getKeyMarker());
        dest.setMaxUploads(src.getMaxUploads());
        dest.setPrefix(src.getPrefix());
        dest.setUploadIdMarker(src.getUploadIdMarker());
        return dest;
    }

    public static SetBucketVersioningConfigurationRequest putBucketVersioning(PutBucketVersioningRequest src) {
        return new SetBucketVersioningConfigurationRequest(src.getBucketName(), new BucketVersioningConfiguration(src.getBucketVersioning().getStatus()));
    }

    public static GetBucketVersioningConfigurationRequest getBucketVersioning(GetBucketVersioningRequest src) {
        return new GetBucketVersioningConfigurationRequest(src.getBucketName());
    }

    public static SetBucketNotificationConfigurationRequest putNotification(PutBucketNotificationRequest src) {
        return new SetBucketNotificationConfigurationRequest(src.getBucketName(), convert(src.getBucketNotificationConfiguration()));
    }

    private static BucketNotificationConfiguration convert(io.ceph.rgw.client.model.BucketNotificationConfiguration src) {
        return Optional.ofNullable(src).map(c -> new BucketNotificationConfiguration().withNotificationConfiguration(c.getNotificationConfigurations().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> SyncRequestConverter.convert(e.getValue()))))).orElse(null);
    }

    private static NotificationConfiguration convert(io.ceph.rgw.client.model.NotificationConfiguration src) {
        if (src == null) {
            return null;
        }
        if (src instanceof io.ceph.rgw.client.model.QueueConfiguration) {
            return convert((io.ceph.rgw.client.model.QueueConfiguration) src);
        } else if (src instanceof io.ceph.rgw.client.model.TopicConfiguration) {
            return convert((io.ceph.rgw.client.model.TopicConfiguration) src);
        }
        throw new IllegalArgumentException("cannot convert NotificationConfiguration");
    }

    private static NotificationConfiguration convert(io.ceph.rgw.client.model.QueueConfiguration src) {
        return Optional.ofNullable(src).map(c -> new QueueConfiguration(c.getQueueARN(), EnumSet.copyOf(c.getEvents().stream().map(SyncRequestConverter::convert).collect(Collectors.toList()))).withFilter(convert(c.getFilter()))).orElse(null);
    }

    private static NotificationConfiguration convert(io.ceph.rgw.client.model.TopicConfiguration src) {
        return Optional.ofNullable(src).map(c -> new TopicConfiguration(c.getTopicARN(), EnumSet.copyOf(c.getEvents().stream().map(SyncRequestConverter::convert).collect(Collectors.toList()))).withFilter(convert(c.getFilter()))).orElse(null);
    }

    private static S3Event convert(Event src) {
        if (src == null) {
            return null;
        }
        switch (src) {
            case OBJECT_CREATED:
                return S3Event.ObjectCreated;
            case OBJECT_CREATED_BY_PUT:
                return S3Event.ObjectCreatedByPut;
            case OBJECT_CREATED_BY_POST:
                return S3Event.ObjectCreatedByPost;
            case OBJECT_CREATED_BY_COPY:
                return S3Event.ObjectCreatedByCopy;
            case OBJECT_CREATED_BY_COMPLETE_MULTIPART_UPLOAD:
                return S3Event.ObjectCreatedByCompleteMultipartUpload;
            case OBJECT_REMOVED:
                return S3Event.ObjectRemoved;
            case OBJECT_REMOVED_DELETE:
                return S3Event.ObjectRemovedDelete;
            case OBJECT_REMOVED_DELETE_MARKER_CREATED:
                return S3Event.ObjectRemovedDeleteMarkerCreated;
        }
        throw new IllegalArgumentException("cannot convert Event");
    }

    private static Filter convert(io.ceph.rgw.client.model.Filter src) {
        return Optional.ofNullable(src).map(f -> new Filter().withS3KeyFilter(convert(f.getKeyFilter()))).orElse(null);
    }

    private static S3KeyFilter convert(KeyFilter src) {
        return Optional.ofNullable(src)
                .map(f -> new S3KeyFilter().withFilterRules(f.getFilterRules().stream().map(SyncRequestConverter::convert).collect(Collectors.toList())))
                .orElse(null);
    }

    private static FilterRule convert(io.ceph.rgw.client.model.FilterRule src) {
        return Optional.ofNullable(src).map(r -> new FilterRule().withName(r.getName()).withValue(r.getValue())).orElse(null);
    }

    public static GetBucketNotificationConfigurationRequest getNotification(GetBucketNotificationRequest src) {
        return new GetBucketNotificationConfigurationRequest(src.getBucketName());
    }
}