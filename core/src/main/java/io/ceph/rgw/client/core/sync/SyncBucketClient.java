package io.ceph.rgw.client.core.sync;

import com.amazonaws.services.s3.AmazonS3;
import io.ceph.rgw.client.BucketClient;
import io.ceph.rgw.client.action.ActionExecutor;
import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;
import io.ceph.rgw.client.converter.SyncRequestConverter;
import io.ceph.rgw.client.converter.SyncResponseConverter;
import io.ceph.rgw.client.core.Connectors;
import io.ceph.rgw.client.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A sync implementation of BucketClient.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/3/16.
 */
public class SyncBucketClient extends SyncConnectorAware implements BucketClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(SyncBucketClient.class);

    public SyncBucketClient(Connectors<AmazonS3> connectors, ActionExecutor executor) {
        super(connectors, executor);
    }

    @Override
    public ActionFuture<CreateBucketResponse> createBucketAsync(CreateBucketRequest request) {
        return executor.execute(() -> doPutBucket(request));
    }

    @Override
    public void createBucketAsync(CreateBucketRequest request, ActionListener<CreateBucketResponse> listener) {
        executor.execute(() -> doPutBucket(request), listener);
    }

    @Override
    public CreateBucketResponse createBucket(CreateBucketRequest request) {
        return executor.run(() -> doPutBucket(request));
    }

    private CreateBucketResponse doPutBucket(CreateBucketRequest request) {
        return doAction(amazonS3 -> SyncResponseConverter.createBucket(amazonS3.createBucket(SyncRequestConverter.createBucket(request).withGeneralProgressListener(LoggingProgressListener.create(LOGGER, request)))));
    }

    @Override
    public ActionFuture<DeleteBucketResponse> deleteBucketAsync(DeleteBucketRequest request) {
        return executor.execute(() -> doRemoveBucket(request));
    }

    @Override
    public void deleteBucketAsync(DeleteBucketRequest request, ActionListener<DeleteBucketResponse> listener) {
        executor.execute(() -> doRemoveBucket(request), listener);
    }

    @Override
    public DeleteBucketResponse deleteBucket(DeleteBucketRequest request) {
        return executor.run(() -> doRemoveBucket(request));
    }

    private DeleteBucketResponse doRemoveBucket(DeleteBucketRequest request) {
        return doAction(amazonS3 -> {
            amazonS3.deleteBucket(SyncRequestConverter.deleteBucket(request).withGeneralProgressListener(LoggingProgressListener.create(LOGGER, request)));
            return SyncResponseConverter.deleteBucket();
        });
    }

    @Override
    public ActionFuture<GetBucketResponse> getBucketAsync(GetBucketRequest request) {
        return executor.execute(() -> doGetBucket(request));
    }

    @Override
    public void getBucketAsync(GetBucketRequest request, ActionListener<GetBucketResponse> listener) {
        executor.execute(() -> doGetBucket(request), listener);
    }

    @Override
    public GetBucketResponse getBucket(GetBucketRequest request) {
        return executor.run(() -> doGetBucket(request));
    }

    private GetBucketResponse doGetBucket(GetBucketRequest request) {
        return doAction(amazonS3 -> SyncResponseConverter.getBucket(amazonS3.doesBucketExist(SyncRequestConverter.getBucket(request))));
    }

    @Override
    public ActionFuture<ListBucketsResponse> listBucketsAsync(ListBucketsRequest request) {
        return executor.execute(() -> doListBuckets(request));
    }

    @Override
    public void listBucketsAsync(ListBucketsRequest request, ActionListener<ListBucketsResponse> listener) {
        executor.execute(() -> doListBuckets(request), listener);
    }

    @Override
    public ListBucketsResponse listBuckets(ListBucketsRequest request) {
        return executor.run(() -> doListBuckets(request));
    }

    private ListBucketsResponse doListBuckets(ListBucketsRequest request) {
        return doAction(amazonS3 -> SyncResponseConverter.listBuckets(amazonS3.listBuckets(SyncRequestConverter.listBuckets(request).withGeneralProgressListener(LoggingProgressListener.create(LOGGER, request)))));
    }

    @Override
    public ActionFuture<ListObjectsResponse> listObjectsAsync(ListObjectsRequest request) {
        return executor.execute(() -> doListObjects(request));
    }

    @Override
    public void listObjectsAsync(ListObjectsRequest request, ActionListener<ListObjectsResponse> listener) {
        executor.execute(() -> doListObjects(request), listener);
    }

    @Override
    public ListObjectsResponse listObjects(ListObjectsRequest request) {
        return executor.run(() -> doListObjects(request));
    }

    private ListObjectsResponse doListObjects(ListObjectsRequest request) {
        return doAction(amazonS3 -> SyncResponseConverter.listObjects(amazonS3.listObjectsV2(SyncRequestConverter.listObjects(request).withGeneralProgressListener(LoggingProgressListener.create(LOGGER, request)))));
    }

    @Override
    public ActionFuture<GetBucketLocationResponse> getBucketLocationAsync(GetBucketLocationRequest request) {
        return executor.execute(() -> doGetBucketLocation(request));
    }

    @Override
    public void getBucketLocationAsync(GetBucketLocationRequest request, ActionListener<GetBucketLocationResponse> listener) {
        executor.execute(() -> doGetBucketLocation(request), listener);
    }

    @Override
    public GetBucketLocationResponse getBucketLocation(GetBucketLocationRequest request) {
        return executor.run(() -> doGetBucketLocation(request));
    }

    private GetBucketLocationResponse doGetBucketLocation(GetBucketLocationRequest request) {
        return doAction(amazonS3 -> SyncResponseConverter.getBucketLocation(amazonS3.getBucketLocation(SyncRequestConverter.getBucketLocation(request).withGeneralProgressListener(LoggingProgressListener.create(LOGGER, request)))));
    }

    @Override
    public ActionFuture<GetBucketACLResponse> getBucketACLAsync(GetBucketACLRequest request) {
        return executor.execute(() -> doGetBucketACL(request));
    }

    @Override
    public void getBucketACLAsync(GetBucketACLRequest request, ActionListener<GetBucketACLResponse> listener) {
        executor.execute(() -> doGetBucketACL(request), listener);
    }

    @Override
    public GetBucketACLResponse getBucketACL(GetBucketACLRequest request) {
        return executor.run(() -> doGetBucketACL(request));
    }

    private GetBucketACLResponse doGetBucketACL(GetBucketACLRequest request) {
        return doAction(amazonS3 -> SyncResponseConverter.getBucketACL(amazonS3.getBucketAcl(SyncRequestConverter.getBucketACL(request).withGeneralProgressListener(LoggingProgressListener.create(LOGGER, request)))));
    }

    @Override
    public ActionFuture<PutBucketACLResponse> putBucketACLAsync(PutBucketACLRequest request) {
        return executor.execute(() -> doPutBucketACL(request));
    }

    @Override
    public void putBucketACLAsync(PutBucketACLRequest request, ActionListener<PutBucketACLResponse> listener) {
        executor.execute(() -> doPutBucketACL(request), listener);
    }

    @Override
    public PutBucketACLResponse putBucketACL(PutBucketACLRequest request) {
        return executor.run(() -> doPutBucketACL(request));
    }

    private PutBucketACLResponse doPutBucketACL(PutBucketACLRequest request) {
        return doAction(amazonS3 -> {
            amazonS3.setBucketAcl(SyncRequestConverter.putBucketACL(request).withGeneralProgressListener(LoggingProgressListener.create(LOGGER, request)));
            return SyncResponseConverter.putBucketACL();
        });
    }

    @Override
    public ActionFuture<ListBucketMultipartUploadsResponse> listBucketMultipartUploadsAsync(ListMultipartUploadPartsRequest request) {
        return executor.execute(() -> doListBucketMultipartUploads(request));
    }

    @Override
    public void listBucketMultipartUploadsAsync(ListMultipartUploadPartsRequest request, ActionListener<ListBucketMultipartUploadsResponse> listener) {
        executor.execute(() -> doListBucketMultipartUploads(request), listener);
    }

    @Override
    public ListBucketMultipartUploadsResponse listBucketMultipartUploads(ListMultipartUploadPartsRequest request) {
        return executor.run(() -> doListBucketMultipartUploads(request));
    }

    private ListBucketMultipartUploadsResponse doListBucketMultipartUploads(ListMultipartUploadPartsRequest request) {
        return doAction(amazonS3 -> SyncResponseConverter.listMultipartUploadParts(amazonS3.listMultipartUploads(SyncRequestConverter.listMultipartUploadParts(request).withGeneralProgressListener(LoggingProgressListener.create(LOGGER, request)))));
    }

    @Override
    public ActionFuture<PutBucketVersioningResponse> putBucketVersioningAsync(PutBucketVersioningRequest request) {
        return executor.execute(() -> doSetBucketVersioning(request));
    }

    @Override
    public void putBucketVersioningAsync(PutBucketVersioningRequest request, ActionListener<PutBucketVersioningResponse> listener) {
        executor.execute(() -> doSetBucketVersioning(request), listener);
    }

    @Override
    public PutBucketVersioningResponse putBucketVersioning(PutBucketVersioningRequest request) {
        return executor.run(() -> doSetBucketVersioning(request));
    }

    private PutBucketVersioningResponse doSetBucketVersioning(PutBucketVersioningRequest request) {
        return doAction(amazonS3 -> {
            amazonS3.setBucketVersioningConfiguration(SyncRequestConverter.putBucketVersioning(request).withGeneralProgressListener(LoggingProgressListener.create(LOGGER, request)));
            return SyncResponseConverter.putBucketVersioning();
        });
    }

    @Override
    public ActionFuture<GetBucketVersioningResponse> getBucketVersioningAsync(GetBucketVersioningRequest request) {
        return executor.execute(() -> doGetBucketVersioning(request));
    }

    @Override
    public void getBucketVersioningAsync(GetBucketVersioningRequest request, ActionListener<GetBucketVersioningResponse> listener) {
        executor.execute(() -> doGetBucketVersioning(request), listener);
    }

    @Override
    public GetBucketVersioningResponse getBucketVersioning(GetBucketVersioningRequest request) {
        return executor.run(() -> doGetBucketVersioning(request));
    }

    private GetBucketVersioningResponse doGetBucketVersioning(GetBucketVersioningRequest request) {
        return doAction(amazonS3 -> SyncResponseConverter.getBucketVersioning(amazonS3.getBucketVersioningConfiguration(SyncRequestConverter.getBucketVersioning(request).withGeneralProgressListener(LoggingProgressListener.create(LOGGER, request)))));
    }

    @Override
    public ActionFuture<PutNotificationResponse> putNotificationAsync(PutBucketNotificationRequest request) {
        return executor.execute(() -> doPutNotification(request));
    }

    @Override
    public void putNotificationAsync(PutBucketNotificationRequest request, ActionListener<PutNotificationResponse> listener) {
        executor.execute(() -> doPutNotification(request), listener);
    }

    @Override
    public PutNotificationResponse putNotification(PutBucketNotificationRequest request) {
        return executor.run(() -> doPutNotification(request));
    }

    private PutNotificationResponse doPutNotification(PutBucketNotificationRequest request) {
        return doAction(amazonS3 -> {
            amazonS3.setBucketNotificationConfiguration(SyncRequestConverter.putNotification(request).withGeneralProgressListener(LoggingProgressListener.create(LOGGER, request)));
            return SyncResponseConverter.putNotification();
        });
    }

    @Override
    public ActionFuture<GetNotificationResponse> getNotificationAsync(GetBucketNotificationRequest request) {
        return executor.execute(() -> doGetNotification(request));
    }

    @Override
    public void getNotificationAsync(GetBucketNotificationRequest request, ActionListener<GetNotificationResponse> listener) {
        executor.execute(() -> doGetNotification(request), listener);
    }

    @Override
    public GetNotificationResponse getNotification(GetBucketNotificationRequest request) {
        return executor.run(() -> doGetNotification(request));
    }

    private GetNotificationResponse doGetNotification(GetBucketNotificationRequest request) {
        return doAction(amazonS3 -> SyncResponseConverter.getNotification(amazonS3.getBucketNotificationConfiguration(SyncRequestConverter.getNotification(request).withGeneralProgressListener(LoggingProgressListener.create(LOGGER, request)))));
    }
}
