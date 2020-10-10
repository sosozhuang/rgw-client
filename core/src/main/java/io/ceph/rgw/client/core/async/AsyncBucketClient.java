package io.ceph.rgw.client.core.async;

import io.ceph.rgw.client.BucketClient;
import io.ceph.rgw.client.action.ActionExecutor;
import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;
import io.ceph.rgw.client.converter.AsyncRequestConverter;
import io.ceph.rgw.client.converter.AsyncResponseConverter;
import io.ceph.rgw.client.core.Connectors;
import io.ceph.rgw.client.model.*;
import software.amazon.awssdk.services.s3.S3AsyncClient;

import java.util.concurrent.CompletableFuture;


/**
 * An Async implementation of BucketClient.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/3/16.
 */
public class AsyncBucketClient extends AsyncConnectorAware<S3AsyncClient> implements BucketClient {
    public AsyncBucketClient(Connectors<S3AsyncClient> connectors, ActionExecutor executor) {
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
        return createBucketAsync(request).actionGet();
    }

    private CompletableFuture<CreateBucketResponse> doPutBucket(CreateBucketRequest request) {
        return doAction(s3AsyncClient -> s3AsyncClient.createBucket(AsyncRequestConverter.createBucket(request)), AsyncResponseConverter::createBucket);
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
        return deleteBucketAsync(request).actionGet();
    }

    private CompletableFuture<DeleteBucketResponse> doRemoveBucket(DeleteBucketRequest request) {
        return doAction(s3AsyncClient -> s3AsyncClient.deleteBucket(AsyncRequestConverter.deleteBucket(request)), AsyncResponseConverter::deleteBucket);
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
        return getBucketAsync(request).actionGet();
    }

    private CompletableFuture<GetBucketResponse> doGetBucket(GetBucketRequest request) {
        return doAction(s3AsyncClient -> s3AsyncClient.headBucket(AsyncRequestConverter.getBucket(request)).handle(AsyncResponseConverter::handleGetBucket), AsyncResponseConverter::getBucket);
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
        return listBucketsAsync(request).actionGet();
    }

    private CompletableFuture<ListBucketsResponse> doListBuckets(ListBucketsRequest request) {
        return doAction(s3AsyncClient -> s3AsyncClient.listBuckets(AsyncRequestConverter.listBuckets(request)), AsyncResponseConverter::listBuckets);
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
        return listObjectsAsync(request).actionGet();
    }

    private CompletableFuture<ListObjectsResponse> doListObjects(ListObjectsRequest request) {
        return doAction(s3AsyncClient -> s3AsyncClient.listObjectsV2(AsyncRequestConverter.listObjects(request)), AsyncResponseConverter::listObjects);
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
        return getBucketLocationAsync(request).actionGet();
    }

    private CompletableFuture<GetBucketLocationResponse> doGetBucketLocation(GetBucketLocationRequest request) {
        return doAction(s3AsyncClient -> s3AsyncClient.getBucketLocation(AsyncRequestConverter.getBucketLocation(request)), AsyncResponseConverter::getBucketLocation);
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
        return getBucketACLAsync(request).actionGet();
    }

    private CompletableFuture<GetBucketACLResponse> doGetBucketACL(GetBucketACLRequest request) {
        return doAction(s3AsyncClient -> s3AsyncClient.getBucketAcl(AsyncRequestConverter.getBucketACL(request)), AsyncResponseConverter::getBucketACL);
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
        return putBucketACLAsync(request).actionGet();
    }

    private CompletableFuture<PutBucketACLResponse> doPutBucketACL(PutBucketACLRequest request) {
        return doAction(s3AsyncClient -> s3AsyncClient.putBucketAcl(AsyncRequestConverter.putBucketACL(request)), AsyncResponseConverter::putBucketACL);
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
        return listBucketMultipartUploadsAsync(request).actionGet();
    }

    private CompletableFuture<ListBucketMultipartUploadsResponse> doListBucketMultipartUploads(ListMultipartUploadPartsRequest request) {
        return doAction(s3AsyncClient -> s3AsyncClient.listMultipartUploads(AsyncRequestConverter.listBucketMultipartUploads(request)), AsyncResponseConverter::listBucketMultipartUploads);
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
        return putBucketVersioningAsync(request).actionGet();
    }

    private CompletableFuture<PutBucketVersioningResponse> doSetBucketVersioning(PutBucketVersioningRequest request) {
        return doAction(s3AsyncClient -> s3AsyncClient.putBucketVersioning(AsyncRequestConverter.putBucketVersioning(request)), AsyncResponseConverter::putBucketVersioning);
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
        return getBucketVersioningAsync(request).actionGet();
    }

    private CompletableFuture<GetBucketVersioningResponse> doGetBucketVersioning(GetBucketVersioningRequest request) {
        return doAction(s3AsyncClient -> s3AsyncClient.getBucketVersioning(AsyncRequestConverter.getBucketVersioning(request)), AsyncResponseConverter::getBucketVersioning);
    }

    @Override
    public ActionFuture<PutNotificationResponse> putNotificationAsync(PutBucketNotificationRequest request) {
        return executor.execute(() -> doSetNotification(request));
    }

    @Override
    public void putNotificationAsync(PutBucketNotificationRequest request, ActionListener<PutNotificationResponse> listener) {
        executor.execute(() -> doSetNotification(request), listener);
    }

    @Override
    public PutNotificationResponse putNotification(PutBucketNotificationRequest request) {
        return putNotificationAsync(request).actionGet();
    }

    private CompletableFuture<PutNotificationResponse> doSetNotification(PutBucketNotificationRequest request) {
        return doAction(s3AsyncClient -> s3AsyncClient.putBucketNotificationConfiguration(AsyncRequestConverter.putNotification(request)), AsyncResponseConverter::setNotification);
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
        return getNotificationAsync(request).actionGet();
    }

    private CompletableFuture<GetNotificationResponse> doGetNotification(GetBucketNotificationRequest request) {
        return doAction(s3AsyncClient -> s3AsyncClient.getBucketNotificationConfiguration(AsyncRequestConverter.getNotification(request)), AsyncResponseConverter::getNotification);
    }
}
