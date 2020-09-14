package io.ceph.rgw.client;

import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;
import io.ceph.rgw.client.model.*;

/**
 * A client that provides <a href="https://docs.ceph.com/docs/master/radosgw/s3/bucketops/">bucket operations</a>, sends BucketRequest and receives BucketResponse.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/2/27.
 * @see Clients#getBucket()
 * @see BucketRequest
 * @see BucketResponse
 */
public interface BucketClient {
    /**
     * Create a bucket asynchronously.
     *
     * @param request the create bucket request
     * @return an ActionFuture containing the create bucket response
     */
    ActionFuture<CreateBucketResponse> createBucketAsync(CreateBucketRequest request);

    /**
     * Create a bucket asynchronously.
     *
     * @param request  the create bucket request
     * @param listener the callback listener after action is done
     */
    void createBucketAsync(CreateBucketRequest request, ActionListener<CreateBucketResponse> listener);

    /**
     * Create a bucket synchronously.
     *
     * @param request the create bucket request
     * @return the create bucket response
     */
    CreateBucketResponse createBucket(CreateBucketRequest request);

    /**
     * Fluent api to create a bucket.
     *
     * @return the request builder
     */
    default CreateBucketRequest.Builder prepareCreateBucket() {
        return new CreateBucketRequest.Builder(this);
    }

    /**
     * Delete bucket asynchronously.
     *
     * @param request the delete bucket request
     * @return An ActionFuture containing the delete bucket response
     */
    ActionFuture<DeleteBucketResponse> deleteBucketAsync(DeleteBucketRequest request);

    /**
     * Delete bucket asynchronously.
     *
     * @param request  the delete bucket request
     * @param listener the callback listener after action is done
     */
    void deleteBucketAsync(DeleteBucketRequest request, ActionListener<DeleteBucketResponse> listener);

    /**
     * Delete bucket synchronously.
     *
     * @param request the delete bucket request
     * @return the delete bucket response
     */
    DeleteBucketResponse deleteBucket(DeleteBucketRequest request);

    /**
     * Fluent api to delete bucket.
     *
     * @return the request builder
     */
    default DeleteBucketRequest.Builder prepareDeleteBucket() {
        return new DeleteBucketRequest.Builder(this);
    }

    /**
     * Get bucket asynchronously.
     *
     * @param request the get bucket request
     * @return An ActionFuture containing the get bucket response
     */
    ActionFuture<GetBucketResponse> getBucketAsync(GetBucketRequest request);

    /**
     * Get bucket asynchronously.
     *
     * @param request  the get bucket request
     * @param listener the callback listener after action is done
     */
    void getBucketAsync(GetBucketRequest request, ActionListener<GetBucketResponse> listener);

    /**
     * Get bucket synchronously.
     *
     * @param request the get bucket request
     * @return the get bucket response
     */
    GetBucketResponse getBucket(GetBucketRequest request);

    /**
     * Fluent api to get bucket.
     *
     * @return the request builder
     */
    default GetBucketRequest.Builder prepareGetBucket() {
        return new GetBucketRequest.Builder(this);
    }

    /**
     * List buckets asynchronously.
     *
     * @param request the list buckets request
     * @return An ActionFuture containing the list buckets response
     */
    ActionFuture<ListBucketsResponse> listBucketsAsync(ListBucketsRequest request);

    /**
     * List buckets asynchronously.
     *
     * @param request  the list buckets request
     * @param listener the callback listener after action is done
     */
    void listBucketsAsync(ListBucketsRequest request, ActionListener<ListBucketsResponse> listener);

    /**
     * List buckets synchronously.
     *
     * @param request the list buckets request
     * @return the list buckets response
     */
    ListBucketsResponse listBuckets(ListBucketsRequest request);

    /**
     * Fluent api to list buckets.
     *
     * @return the request builder
     */
    default ListBucketsRequest.Builder prepareListBuckets() {
        return new ListBucketsRequest.Builder(this);
    }

    /**
     * Get bucket location asynchronously.
     *
     * @param request the get bucket location request
     * @return An ActionFuture containing the get bucket location response
     */
    ActionFuture<GetBucketLocationResponse> getBucketLocationAsync(GetBucketLocationRequest request);

    /**
     * Get bucket location asynchronously.
     *
     * @param request  the get bucket location request
     * @param listener the callback listener after action is done
     */
    void getBucketLocationAsync(GetBucketLocationRequest request, ActionListener<GetBucketLocationResponse> listener);

    /**
     * Get bucket location synchronously.
     *
     * @param request the get bucket location request
     * @return the get bucket location response
     */
    GetBucketLocationResponse getBucketLocation(GetBucketLocationRequest request);

    /**
     * Fluent api to get bucket location.
     *
     * @return the request builder
     */
    default GetBucketLocationRequest.Builder prepareGetBucketLocation() {
        return new GetBucketLocationRequest.Builder(this);
    }

    /**
     * Get bucket ACL asynchronously.
     *
     * @param request the get bucket ACL request
     * @return An ActionFuture containing the get bucket ACL response
     */
    ActionFuture<GetBucketACLResponse> getBucketACLAsync(GetBucketACLRequest request);

    /**
     * Get bucket ACL asynchronously.
     *
     * @param request  the get bucket ACL request
     * @param listener the callback listener after action is done
     */
    void getBucketACLAsync(GetBucketACLRequest request, ActionListener<GetBucketACLResponse> listener);

    /**
     * Get bucket ACL synchronously.
     *
     * @param request the get bucket ACL request
     * @return the get bucket ACL response
     */
    GetBucketACLResponse getBucketACL(GetBucketACLRequest request);

    /**
     * Fluent api to get bucket ACL.
     *
     * @return the request builder
     */
    default GetBucketACLRequest.Builder prepareGetBucketACL() {
        return new GetBucketACLRequest.Builder(this);
    }

    /**
     * Put bucket ACL asynchronously.
     *
     * @param request the put bucket ACL request
     * @return An ActionFuture containing the put bucket ACL response
     */
    ActionFuture<PutBucketACLResponse> putBucketACLAsync(PutBucketACLRequest request);

    /**
     * Put bucket ACL asynchronously.
     *
     * @param request  the put bucket ACL request
     * @param listener the callback listener after action is done
     */
    void putBucketACLAsync(PutBucketACLRequest request, ActionListener<PutBucketACLResponse> listener);

    /**
     * Put bucket ACL synchronously.
     *
     * @param request the put bucket ACL request
     * @return the put bucket ACL response
     */
    PutBucketACLResponse putBucketACL(PutBucketACLRequest request);

    /**
     * Fluent api to put bucket ACL.
     *
     * @return the request builder
     */
    default PutBucketACLRequest.Builder preparePutBucketACL() {
        return new PutBucketACLRequest.Builder(this);
    }

    /**
     * List bucket multipart uploads asynchronously.
     *
     * @param request the list bucket multipart uploads request
     * @return An ActionFuture containing the list bucket multipart uploads response
     */
    ActionFuture<ListBucketMultipartUploadsResponse> listBucketMultipartUploadsAsync(ListMultipartUploadPartsRequest request);

    /**
     * List bucket multipart uploads asynchronously.
     *
     * @param request  the list bucket multipart uploads request
     * @param listener the callback listener after action is done
     */
    void listBucketMultipartUploadsAsync(ListMultipartUploadPartsRequest request, ActionListener<ListBucketMultipartUploadsResponse> listener);

    /**
     * List bucket multipart uploads synchronously.
     *
     * @param request the list bucket multipart uploads request
     * @return the list bucket multipart uploads response
     */
    ListBucketMultipartUploadsResponse listBucketMultipartUploads(ListMultipartUploadPartsRequest request);

    /**
     * Fluent api to list bucket multipart uploads.
     *
     * @return the request builder
     */
    default ListMultipartUploadPartsRequest.Builder prepareListBucketMultipartUploads() {
        return new ListMultipartUploadPartsRequest.Builder(this);
    }

    /**
     * Put bucket versioning asynchronously.
     *
     * @param request the put bucket versioning request
     * @return An ActionFuture containing the put bucket versioning response
     */
    ActionFuture<PutBucketVersioningResponse> putBucketVersioningAsync(PutBucketVersioningRequest request);

    /**
     * Put bucket versioning asynchronously.
     *
     * @param request  the put bucket versioning request
     * @param listener the callback listener after action is done
     */
    void putBucketVersioningAsync(PutBucketVersioningRequest request, ActionListener<PutBucketVersioningResponse> listener);

    /**
     * Put bucket versioning asynchronously.
     *
     * @param request the put bucket versioning request
     * @return the put bucket versioning response
     */
    PutBucketVersioningResponse putBucketVersioning(PutBucketVersioningRequest request);

    /**
     * Fluent api to put bucket versioning.
     *
     * @return the request builder
     */
    default PutBucketVersioningRequest.Builder preparePutBucketVersioning() {
        return new PutBucketVersioningRequest.Builder(this);
    }

    /**
     * Get bucket versioning asynchronously.
     *
     * @param request the get bucket versioning request
     * @return An ActionFuture containing the get bucket versioning response
     */
    ActionFuture<GetBucketVersioningResponse> getBucketVersioningAsync(GetBucketVersioningRequest request);

    /**
     * Get bucket versioning asynchronously.
     *
     * @param request  the get bucket versioning request
     * @param listener the callback listener after action is done
     */
    void getBucketVersioningAsync(GetBucketVersioningRequest request, ActionListener<GetBucketVersioningResponse> listener);

    /**
     * Get bucket versioning synchronously.
     *
     * @param request the get bucket versioning request
     * @return the get bucket versioning response
     */
    GetBucketVersioningResponse getBucketVersioning(GetBucketVersioningRequest request);

    /**
     * Fluent api to get bucket versioning.
     *
     * @return the request builder
     */
    default GetBucketVersioningRequest.Builder prepareGetBucketVersioning() {
        return new GetBucketVersioningRequest.Builder(this);
    }

//    void putBucketObjectLock();
//
//    void getBucketObjectLock();

    /**
     * Put notification asynchronously.
     *
     * @param request the put notification request
     * @return An ActionFuture containing the put notification response
     */
    ActionFuture<PutNotificationResponse> putNotificationAsync(PutBucketNotificationRequest request);

    /**
     * Put notification asynchronously.
     *
     * @param request  the put notification request
     * @param listener the callback listener after action is done
     */
    void putNotificationAsync(PutBucketNotificationRequest request, ActionListener<PutNotificationResponse> listener);

    /**
     * Put notification synchronously.
     *
     * @param request the put notification request
     * @return the put notification response
     */
    PutNotificationResponse putNotification(PutBucketNotificationRequest request);

    /**
     * Fluent api to put notification.
     *
     * @return the request builder
     */
    default PutBucketNotificationRequest.Builder preparePutNotification() {
        return new PutBucketNotificationRequest.Builder(this);
    }

    /**
     * Get notification asynchronously.
     *
     * @param request the get notification request
     * @return An ActionFuture containing the get notification response
     */
    ActionFuture<GetNotificationResponse> getNotificationAsync(GetBucketNotificationRequest request);

    /**
     * Get notification asynchronously.
     *
     * @param request  the get notification request
     * @param listener the callback listener after action is done
     */
    void getNotificationAsync(GetBucketNotificationRequest request, ActionListener<GetNotificationResponse> listener);

    /**
     * Get notification synchronously.
     *
     * @param request the get notification request
     * @return the get notification response
     */
    GetNotificationResponse getNotification(GetBucketNotificationRequest request);

    /**
     * Fluent api to get notification.
     *
     * @return the request builder
     */
    default GetBucketNotificationRequest.Builder prepareGetNotification() {
        return new GetBucketNotificationRequest.Builder(this);
    }
}
