package io.ceph.rgw.client.converter;

import io.ceph.rgw.client.model.*;
import org.apache.commons.lang3.StringUtils;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.http.SdkHttpResponse;
import software.amazon.awssdk.services.s3.model.CopyObjectResult;
import software.amazon.awssdk.services.s3.model.HeadBucketResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;

import java.io.InputStream;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.ceph.rgw.client.converter.AsyncRequestConverter.RFC822_DATE_FORMAT;

/**
 * Accepts s3 response model and converts to {@link BucketResponse}/{@link ObjectResponse}.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/6/11.
 * @see io.ceph.rgw.client.core.async.AsyncBucketClient
 * @see io.ceph.rgw.client.core.async.AsyncObjectClient
 * @see AsyncRequestConverter
 */
public final class AsyncResponseConverter {
    private static final Pattern DATE_PATTERN = Pattern.compile("expiry-date=\"(.*?)\"");
    private static final Pattern RULE_PATTERN = Pattern.compile("rule-id=\"(.*?)\"");

    private AsyncResponseConverter() {
        throw new RuntimeException();
    }

    public static PutObjectResponse putObject(software.amazon.awssdk.services.s3.model.PutObjectResponse src) {
        return new PutObjectResponse("", src.eTag(), parseDate(src.expiration()), parseRuleId(src.expiration()), new Metadata.Builder().build(), src.versionId());
    }

    private static Date parseDate(String expiration) {
        if (StringUtils.isBlank(expiration)) {
            return null;
        }
        Matcher matcher = DATE_PATTERN.matcher(expiration);
        if (matcher.find()) {
            String date = matcher.group(1);
            try {
                return Date.from(Instant.from(RFC822_DATE_FORMAT.parse(date)));
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    private static String parseRuleId(String expiration) {
        if (StringUtils.isBlank(expiration)) {
            return null;
        }
        Matcher matcher = RULE_PATTERN.matcher(expiration);
        return matcher.find() ? matcher.group(1) : null;
    }

    public static GetObjectResponse getObject(ResponseBytes<software.amazon.awssdk.services.s3.model.GetObjectResponse> src) {
        return new GetObjectResponse(src.response().versionId(), src.response().tagCount(), getMetadata(src.response()), src.asInputStream());
    }

    private static Metadata getMetadata(software.amazon.awssdk.services.s3.model.GetObjectResponse response) {
        Metadata.Builder builder = new Metadata.Builder();
        Optional.ofNullable(response.contentDisposition()).ifPresent(builder::withCacheControl);
        Optional.ofNullable(response.contentDisposition()).ifPresent(builder::withContentDisposition);
        Optional.ofNullable(response.contentEncoding()).ifPresent(builder::withContentEncoding);
        Optional.ofNullable(response.contentLanguage()).ifPresent(builder::withContentLanguage);
        Optional.ofNullable(response.contentLength()).ifPresent(builder::withContentLength);
        Optional.ofNullable(response.contentType()).ifPresent(builder::withContentType);
        Optional.ofNullable(response.expires()).map(Date::from).ifPresent(builder::withHttpExpiresDate);
        Optional.ofNullable(response.metadata()).ifPresent(kv -> kv.forEach(builder::add));
        return builder.build();
    }

    public static GetFileResponse getFile(software.amazon.awssdk.services.s3.model.GetObjectResponse src) {
        return new GetFileResponse(src.versionId(), src.tagCount(), getMetadata(src));
    }

    public static GetInputStreamResponse getInputStream(Map.Entry<software.amazon.awssdk.services.s3.model.GetObjectResponse, InputStream> src) {
        return new GetInputStreamResponse(src.getKey().versionId(), src.getKey().tagCount(), getMetadata(src.getKey()), src.getValue());
    }

    public static GetStringResponse getString(ResponseBytes<software.amazon.awssdk.services.s3.model.GetObjectResponse> src) {
        return new GetStringResponse(src.response().versionId(), src.response().tagCount(), getMetadata(src.response()), src.asUtf8String());
    }

    public static CopyObjectResponse copyObject(software.amazon.awssdk.services.s3.model.CopyObjectResponse src) {
        CopyObjectResult result = src.copyObjectResult();
        return new CopyObjectResponse(result.eTag(), parseDate(src.expiration()), parseRuleId(src.expiration()), Optional.ofNullable(result.lastModified()).map(Date::from).orElse(null), src.versionId());
    }

    public static DeleteObjectResponse deleteObject(software.amazon.awssdk.services.s3.model.DeleteObjectResponse src) {
        return DeleteObjectResponse.INSTANCE;
    }

    public static GetObjectInfoResponse getObjectInfo(software.amazon.awssdk.services.s3.model.HeadObjectResponse src) {
        Metadata.Builder builder = new Metadata.Builder();
        Optional.ofNullable(src.cacheControl()).ifPresent(builder::withCacheControl);
        Optional.ofNullable(src.contentDisposition()).ifPresent(builder::withContentDisposition);
        Optional.ofNullable(src.contentEncoding()).ifPresent(builder::withContentEncoding);
        Optional.ofNullable(src.contentLanguage()).ifPresent(builder::withContentLanguage);
        Optional.ofNullable(src.contentLength()).ifPresent(builder::withContentLength);
        Optional.ofNullable(src.contentType()).ifPresent(builder::withContentType);
        Optional.ofNullable(src.expires()).map(Date::from).ifPresent(builder::withHttpExpiresDate);
        Optional.ofNullable(src.metadata()).ifPresent(kv -> kv.forEach(builder::add));
        return new GetObjectInfoResponse(builder.build());
    }

    public static GetObjectACLResponse getObjectACL(software.amazon.awssdk.services.s3.model.GetObjectAclResponse src) {
        return new GetObjectACLResponse(convertACL(src.hasGrants() ? src.grants() : Collections.emptyList(), src.owner()));
    }

    private static ACL convertACL(List<software.amazon.awssdk.services.s3.model.Grant> grants, software.amazon.awssdk.services.s3.model.Owner owner) {
        return new ACL(grants.stream().map(AsyncResponseConverter::convert).collect(Collectors.toList()), convert(owner));
    }

    private static Grant convert(software.amazon.awssdk.services.s3.model.Grant src) {
        return new Grant(convert(src.grantee()), convert(src.permission()));
    }

    private static Grantee convert(software.amazon.awssdk.services.s3.model.Grantee src) {
        switch (src.type()) {
            case CANONICAL_USER:
                return new CanonicalGrantee(src.id(), src.displayName());
            case GROUP:
                return GroupGrantee.fromString(src.uri());
            case AMAZON_CUSTOMER_BY_EMAIL:
                return new EmailAddressGrantee(src.emailAddress());
        }
        throw new UnsupportedOperationException("unknown grantee");
    }

    private static Permission convert(software.amazon.awssdk.services.s3.model.Permission src) {
        if (src == null) {
            return null;
        }
        switch (src) {
            case FULL_CONTROL:
                return Permission.FULL_CONTROL;
            case WRITE:
                return Permission.WRITE;
            case WRITE_ACP:
                return Permission.WRITE_ACP;
            case READ:
                return Permission.READ;
            case READ_ACP:
                return Permission.READ_ACP;
        }
        throw new UnsupportedOperationException("unknown permission");
    }

    private static Owner convert(software.amazon.awssdk.services.s3.model.Owner src) {
        return new Owner(src.id(), src.displayName());
    }

    public static PutObjectACLResponse putObjectACL(software.amazon.awssdk.services.s3.model.PutObjectAclResponse src) {
        return PutObjectACLResponse.INSTANCE;
    }

    public static InitiateMultipartUploadResponse initiateMultipartUpload(software.amazon.awssdk.services.s3.model.CreateMultipartUploadResponse src) {
        return new InitiateMultipartUploadResponse(src.bucket(), src.key(), src.uploadId(), Optional.ofNullable(src.abortDate()).map(Date::from).orElse(null));
    }

    public static MultipartUploadPartResponse uploadPart(software.amazon.awssdk.services.s3.model.UploadPartResponse src) {
        return new MultipartUploadPartResponse(src.eTag());
    }

    public static CompleteMultipartUploadResponse completeMultipartUpload(software.amazon.awssdk.services.s3.model.CompleteMultipartUploadResponse src) {
        return new CompleteMultipartUploadResponse(src.bucket(), src.key(), src.versionId(), src.eTag(), src.location(), parseDate(src.expiration()), parseRuleId(src.expiration()));
    }

    public static AbortMultipartUploadResponse abortMultipartUpload(software.amazon.awssdk.services.s3.model.AbortMultipartUploadResponse src) {
        return AbortMultipartUploadResponse.INSTANCE;
    }

    public static CreateBucketResponse createBucket(software.amazon.awssdk.services.s3.model.CreateBucketResponse src) {
        return CreateBucketResponse.INSTANCE;
    }

    public static DeleteBucketResponse deleteBucket(software.amazon.awssdk.services.s3.model.DeleteBucketResponse src) {
        return DeleteBucketResponse.INSTANCE;
    }

    public static HeadBucketResponse handleGetBucket(software.amazon.awssdk.services.s3.model.HeadBucketResponse r, Throwable t) {
        if (r != null) {
            return r;
        }
        Throwable cause = t.getCause();
        if (cause instanceof NoSuchBucketException) {
            HeadBucketResponse.Builder builder = HeadBucketResponse.builder();
            builder.sdkHttpResponse(SdkHttpResponse.builder().statusCode(((NoSuchBucketException) cause).statusCode()).build());
            return builder.build();
        }
        throw (CompletionException) t;
    }

    public static GetBucketResponse getBucket(HeadBucketResponse src) {
        return new GetBucketResponse(src.sdkHttpResponse().isSuccessful());
    }

    public static ListBucketsResponse listBuckets(software.amazon.awssdk.services.s3.model.ListBucketsResponse src) {
        return new ListBucketsResponse(convertBuckets(src.buckets(), src.owner()));
    }

    private static List<Bucket> convertBuckets(List<software.amazon.awssdk.services.s3.model.Bucket> src, software.amazon.awssdk.services.s3.model.Owner owner) {
        if (src == null || src.size() == 0) {
            return Collections.emptyList();
        }
        return IntStream.range(0, src.size()).mapToObj(i -> convert(src.get(i), owner)).collect(Collectors.toList());
    }

    private static Bucket convert(software.amazon.awssdk.services.s3.model.Bucket src, software.amazon.awssdk.services.s3.model.Owner owner) {
        return new Bucket(src.name(), new Owner(owner.id(), owner.displayName()), Optional.ofNullable(src.creationDate()).map(Date::from).orElse(null));
    }

    public static ListObjectsResponse listObjects(ListObjectsV2Response src) {
        return new ListObjectsResponse(src.name(), src.isTruncated(), src.keyCount(),
                src.nextContinuationToken(), src.prefix(), src.delimiter(),
                src.maxKeys(), src.encodingTypeAsString(), src.continuationToken(),
                src.startAfter(), convertObjects(src.contents()));
    }

    private static List<S3Object> convertObjects(List<software.amazon.awssdk.services.s3.model.S3Object> src) {
        if (src == null || src.size() == 0) {
            return Collections.emptyList();
        }
        return src.stream().map(AsyncResponseConverter::convert).collect(Collectors.toList());
    }

    private static S3Object convert(software.amazon.awssdk.services.s3.model.S3Object src) {
        return new S3Object(src.key(), src.eTag(),
                src.size(), Optional.ofNullable(src.lastModified()).map(Date::from).orElse(null),
                src.storageClassAsString(), convert(src.owner()));
    }

    public static GetBucketLocationResponse getBucketLocation(software.amazon.awssdk.services.s3.model.GetBucketLocationResponse src) {
        return new GetBucketLocationResponse(src.locationConstraintAsString());
    }

    public static GetBucketACLResponse getBucketACL(software.amazon.awssdk.services.s3.model.GetBucketAclResponse src) {
        return new GetBucketACLResponse(convertACL(src.hasGrants() ? src.grants() : Collections.emptyList(), src.owner()));
    }

    public static PutBucketACLResponse putBucketACL(software.amazon.awssdk.services.s3.model.PutBucketAclResponse src) {
        return PutBucketACLResponse.INSTANCE;
    }

    public static ListBucketMultipartUploadsResponse listBucketMultipartUploads(software.amazon.awssdk.services.s3.model.ListMultipartUploadsResponse src) {
        return new ListBucketMultipartUploadsResponse(src.bucket(), src.keyMarker(), src.delimiter(), src.prefix(),
                src.uploadIdMarker(), src.maxUploads(), src.encodingTypeAsString(), src.isTruncated(), src.nextKeyMarker(),
                src.nextUploadIdMarker(), convertMultipartUploads(src.uploads()));
    }

    private static List<MultipartUpload> convertMultipartUploads(List<software.amazon.awssdk.services.s3.model.MultipartUpload> src) {
        if (src == null || src.size() == 0) {
            return Collections.emptyList();
        }
        return src.stream().map(AsyncResponseConverter::convert).collect(Collectors.toList());
    }

    private static MultipartUpload convert(software.amazon.awssdk.services.s3.model.MultipartUpload src) {
        return new MultipartUpload(src.key(), src.uploadId(), convert(src.owner()), convert(src.initiator()), Optional.ofNullable(src.initiated()).map(Date::from).orElse(null));
    }

    private static Owner convert(software.amazon.awssdk.services.s3.model.Initiator src) {
        return new Owner(src.id(), src.displayName());
    }

    public static PutBucketVersioningResponse putBucketVersioning(software.amazon.awssdk.services.s3.model.PutBucketVersioningResponse src) {
        return PutBucketVersioningResponse.INSTANCE;
    }

    public static GetBucketVersioningResponse getBucketVersioning(software.amazon.awssdk.services.s3.model.GetBucketVersioningResponse src) {
        return new GetBucketVersioningResponse(BucketVersioning.fromString(src.statusAsString()));
    }

    public static PutNotificationResponse setNotification(software.amazon.awssdk.services.s3.model.PutBucketNotificationConfigurationResponse src) {
        return PutNotificationResponse.INSTANCE;
    }

    public static GetNotificationResponse getNotification(software.amazon.awssdk.services.s3.model.GetBucketNotificationConfigurationResponse src) {
        return null;
    }
}
