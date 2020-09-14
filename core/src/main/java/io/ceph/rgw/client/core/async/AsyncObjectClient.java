package io.ceph.rgw.client.core.async;

import io.ceph.rgw.client.ObjectClient;
import io.ceph.rgw.client.action.ActionExecutor;
import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;
import io.ceph.rgw.client.converter.AsyncRequestConverter;
import io.ceph.rgw.client.converter.AsyncResponseConverter;
import io.ceph.rgw.client.core.Connectors;
import io.ceph.rgw.client.model.*;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.services.s3.S3AsyncClient;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * An async implementation of ObjectClient.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/3/16.
 */
public class AsyncObjectClient extends AsyncConnectorAware<S3AsyncClient> implements ObjectClient {
    private final Map<String, String> internalMetadata;

    public AsyncObjectClient(Connectors<S3AsyncClient> connectors, ActionExecutor executor, Map<String, String> internalMetadata) {
        super(connectors, executor);
        if (internalMetadata == null || internalMetadata.size() == 0) {
            this.internalMetadata = Collections.emptyMap();
        } else {
            this.internalMetadata = Collections.unmodifiableMap(new HashMap<>(internalMetadata));
        }
    }

    private <T extends GenericPutObjectRequest<?>> CompletableFuture<PutObjectResponse> doPutObject(T request) {
        return doAction(s3AsyncClient -> s3AsyncClient.putObject(AsyncRequestConverter.putObject(request, internalMetadata), AsyncRequestConverter.putObjectContent(request)), AsyncResponseConverter::putObject);
    }

    @Override
    public ActionFuture<PutObjectResponse> putByteBufferAsync(PutByteBufferRequest request) {
        return executor.execute(() -> doPutByteBuffer(request));
    }

    @Override
    public void putByteBufferAsync(PutByteBufferRequest request, ActionListener<PutObjectResponse> listener) {
        executor.execute(() -> doPutByteBuffer(request), listener);
    }

    @Override
    public PutObjectResponse putByteBuffer(PutByteBufferRequest request) {
        return putByteBufferAsync(request).actionGet();
    }

    private CompletableFuture<PutObjectResponse> doPutByteBuffer(PutByteBufferRequest request) {
        return doPutObject(request);
    }

    @Override
    public ActionFuture<PutObjectResponse> putFileAsync(PutFileRequest request) {
        return executor.execute(() -> doPutFile(request));
    }

    @Override
    public void putFileAsync(PutFileRequest request, ActionListener<PutObjectResponse> listener) {
        executor.execute(() -> doPutFile(request), listener);
    }

    @Override
    public PutObjectResponse putFile(PutFileRequest request) {
        return putFileAsync(request).actionGet();
    }

    private CompletableFuture<PutObjectResponse> doPutFile(PutFileRequest request) {
        return doPutObject(request);
    }

    @Override
    public ActionFuture<PutObjectResponse> putInputStreamAsync(PutInputStreamRequest request) {
        return executor.execute(() -> doPutInputStream(request));
    }

    @Override
    public void putInputStreamAsync(PutInputStreamRequest request, ActionListener<PutObjectResponse> listener) {
        executor.execute(() -> doPutInputStream(request), listener);
    }

    @Override
    public PutObjectResponse putInputStream(PutInputStreamRequest request) {
        return putInputStreamAsync(request).actionGet();
    }

    private CompletableFuture<PutObjectResponse> doPutInputStream(PutInputStreamRequest request) {
        return doPutObject(request);
    }

    @Override
    public ActionFuture<PutObjectResponse> putStringAsync(PutStringRequest request) {
        return executor.execute(() -> doPutString(request));
    }

    @Override
    public void putStringAsync(PutStringRequest request, ActionListener<PutObjectResponse> listener) {
        executor.execute(() -> doPutString(request), listener);
    }

    @Override
    public PutObjectResponse putString(PutStringRequest request) {
        return putStringAsync(request).actionGet();
    }

    private CompletableFuture<PutObjectResponse> doPutString(PutStringRequest request) {
        return doPutObject(request);
    }

    @Override
    public ActionFuture<GetObjectResponse> getObjectAsync(GetObjectRequest request) {
        return executor.execute(() -> doGetObject(request));
    }

    @Override
    public void getObjectAsync(GetObjectRequest request, ActionListener<GetObjectResponse> listener) {
        executor.execute(() -> doGetObject(request), listener);
    }

    @Override
    public GetObjectResponse getObject(GetObjectRequest request) {
        return getObjectAsync(request).actionGet();
    }

    private CompletableFuture<GetObjectResponse> doGetObject(GetObjectRequest request) {
        return doAction(s3AsyncClient -> s3AsyncClient.getObject(AsyncRequestConverter.getObject(request), AsyncResponseTransformer.toBytes()), AsyncResponseConverter::getObject);
    }

    @Override
    public ActionFuture<GetFileResponse> getFileAsync(GetFileRequest request) {
        return executor.execute(() -> doGetFile(request));
    }

    @Override
    public void getFileAsync(GetFileRequest request, ActionListener<GetFileResponse> listener) {
        executor.execute(() -> doGetFile(request), listener);
    }

    @Override
    public GetFileResponse getFile(GetFileRequest request) {
        return getFileAsync(request).actionGet();
    }

    private CompletableFuture<GetFileResponse> doGetFile(GetFileRequest request) {
        return doAction(s3AsyncClient -> s3AsyncClient.getObject(AsyncRequestConverter.getObject(request), request.getPath()), AsyncResponseConverter::getFile);
    }

    @Override
    public ActionFuture<GetInputStreamResponse> getInputStreamAsync(GetInputStreamRequest request) {
        return executor.execute(() -> doGetInputStream(request));
    }

    @Override
    public void getInputStreamAsync(GetInputStreamRequest request, ActionListener<GetInputStreamResponse> listener) {
        executor.execute(() -> doGetInputStream(request), listener);
    }

    @Override
    public GetInputStreamResponse getInputStream(GetInputStreamRequest request) {
        return getInputStreamAsync(request).actionGet();
    }

    private CompletableFuture<GetInputStreamResponse> doGetInputStream(GetInputStreamRequest request) {
        return doAction(s3AsyncClient -> s3AsyncClient.getObject(AsyncRequestConverter.getObject(request), new InputStreamAsyncResponseTransformer()), AsyncResponseConverter::getInputStream);
    }

    @Override
    public ActionFuture<GetStringResponse> getStringAsync(GetStringRequest request) {
        return executor.execute(() -> doGetString(request));
    }

    @Override
    public void getStringAsync(GetStringRequest request, ActionListener<GetStringResponse> listener) {
        executor.execute(() -> doGetString(request), listener);
    }

    @Override
    public GetStringResponse getString(GetStringRequest request) {
        return getStringAsync(request).actionGet();
    }

    private CompletableFuture<GetStringResponse> doGetString(GetStringRequest request) {
        return doAction(s3AsyncClient -> s3AsyncClient.getObject(AsyncRequestConverter.getObject(request), AsyncResponseTransformer.toBytes()), AsyncResponseConverter::getString);
    }

    @Override
    public ActionFuture<CopyObjectResponse> copyObjectAsync(CopyObjectRequest request) {
        return executor.execute(() -> doCopyObject(request));
    }

    @Override
    public void copyObjectAsync(CopyObjectRequest request, ActionListener<CopyObjectResponse> listener) {
        executor.execute(() -> doCopyObject(request), listener);
    }

    @Override
    public CopyObjectResponse copyObject(CopyObjectRequest request) {
        return copyObjectAsync(request).actionGet();
    }

    private CompletableFuture<CopyObjectResponse> doCopyObject(CopyObjectRequest request) {
        return doAction(s3AsyncClient -> s3AsyncClient.copyObject(AsyncRequestConverter.copyObject(request)), AsyncResponseConverter::copyObject);
    }

    @Override
    public ActionFuture<DeleteObjectResponse> deleteObjectAsync(DeleteObjectRequest request) {
        return executor.execute(() -> doRemoveObject(request));
    }

    @Override
    public void deleteObjectAsync(DeleteObjectRequest request, ActionListener<DeleteObjectResponse> listener) {
        executor.execute(() -> doRemoveObject(request), listener);
    }

    @Override
    public DeleteObjectResponse deleteObject(DeleteObjectRequest request) {
        return deleteObjectAsync(request).actionGet();
    }

    private CompletableFuture<DeleteObjectResponse> doRemoveObject(DeleteObjectRequest request) {
        return doAction(s3AsyncClient -> s3AsyncClient.deleteObject(AsyncRequestConverter.deleteObject(request)), AsyncResponseConverter::deleteObject);
    }

    @Override
    public ActionFuture<GetObjectInfoResponse> getObjectInfoAsync(GetObjectInfoRequest request) {
        return executor.execute(() -> doGetObjectInfo(request));
    }

    @Override
    public void getObjectInfoAsync(GetObjectInfoRequest request, ActionListener<GetObjectInfoResponse> listener) {
        executor.execute(() -> doGetObjectInfo(request), listener);
    }

    @Override
    public GetObjectInfoResponse getObjectInfo(GetObjectInfoRequest request) {
        return getObjectInfoAsync(request).actionGet();
    }

    private CompletableFuture<GetObjectInfoResponse> doGetObjectInfo(GetObjectInfoRequest request) {
        return doAction(s3AsyncClient -> s3AsyncClient.headObject(AsyncRequestConverter.getObjectInfo(request)), AsyncResponseConverter::getObjectInfo);
    }

    @Override
    public ActionFuture<GetObjectACLResponse> getObjectACLAsync(GetObjectACLRequest request) {
        return executor.execute(() -> doGetObjectACL(request));
    }

    @Override
    public void getObjectACLAsync(GetObjectACLRequest request, ActionListener<GetObjectACLResponse> listener) {
        executor.execute(() -> doGetObjectACL(request), listener);
    }

    @Override
    public GetObjectACLResponse getObjectACL(GetObjectACLRequest request) {
        return getObjectACLAsync(request).actionGet();
    }

    private CompletableFuture<GetObjectACLResponse> doGetObjectACL(GetObjectACLRequest request) {
        return doAction(s3AsyncClient -> s3AsyncClient.getObjectAcl(AsyncRequestConverter.getObjectACL(request)), AsyncResponseConverter::getObjectACL);
    }

    @Override
    public ActionFuture<PutObjectACLResponse> putObjectACLAsync(PutObjectACLRequest request) {
        return executor.execute(() -> doSetObjectACL(request));
    }

    @Override
    public void putObjectACLAsync(PutObjectACLRequest request, ActionListener<PutObjectACLResponse> listener) {
        executor.execute(() -> doSetObjectACL(request), listener);
    }

    @Override
    public PutObjectACLResponse putObjectACL(PutObjectACLRequest request) {
        return putObjectACLAsync(request).actionGet();
    }

    private CompletableFuture<PutObjectACLResponse> doSetObjectACL(PutObjectACLRequest request) {
        return doAction(s3AsyncClient -> s3AsyncClient.putObjectAcl(AsyncRequestConverter.putObjectACL(request)), AsyncResponseConverter::putObjectACL);
    }

    @Override
    public ActionFuture<InitiateMultipartUploadResponse> initiateMultipartUploadAsync(InitiateMultipartUploadRequest request) {
        return executor.execute(() -> doInitiateMultipartUpload(request));
    }

    @Override
    public void initiateMultipartUploadAsync(InitiateMultipartUploadRequest request, ActionListener<InitiateMultipartUploadResponse> listener) {
        executor.execute(() -> doInitiateMultipartUpload(request), listener);
    }

    @Override
    public InitiateMultipartUploadResponse initiateMultipartUpload(InitiateMultipartUploadRequest request) {
        return initiateMultipartUploadAsync(request).actionGet();
    }

    private CompletableFuture<InitiateMultipartUploadResponse> doInitiateMultipartUpload(InitiateMultipartUploadRequest request) {
        return doAction(s3AsyncClient -> s3AsyncClient.createMultipartUpload(AsyncRequestConverter.initiateMultipartUpload(request, internalMetadata)), AsyncResponseConverter::initiateMultipartUpload);
    }

    @Override
    public ActionFuture<MultipartUploadPartResponse> uploadByteBufferAsync(UploadByteBufferRequest request) {
        return executor.execute(() -> doUploadByteBuffer(request));
    }

    @Override
    public void uploadByteBufferAsync(UploadByteBufferRequest request, ActionListener<MultipartUploadPartResponse> listener) {
        executor.execute(() -> doUploadByteBuffer(request), listener);
    }

    @Override
    public MultipartUploadPartResponse uploadByteBuffer(UploadByteBufferRequest request) {
        return uploadByteBufferAsync(request).actionGet();
    }

    private CompletableFuture<MultipartUploadPartResponse> doUploadByteBuffer(UploadByteBufferRequest request) {
        return doAction(s3AsyncClient -> s3AsyncClient.uploadPart(AsyncRequestConverter.uploadPart(request), new ByteBufferAsyncRequestBody(request.getUpload())), AsyncResponseConverter::uploadPart);
    }

    @Override
    public ActionFuture<MultipartUploadPartResponse> uploadFileAsync(UploadFileRequest request) {
        return executor.execute(() -> doUploadFile(request));
    }

    @Override
    public void uploadFileAsync(UploadFileRequest request, ActionListener<MultipartUploadPartResponse> listener) {
        executor.execute(() -> doUploadFile(request), listener);
    }

    @Override
    public MultipartUploadPartResponse uploadFile(UploadFileRequest request) {
        return uploadFileAsync(request).actionGet();
    }

    private CompletableFuture<MultipartUploadPartResponse> doUploadFile(UploadFileRequest request) {
        return doAction(s3AsyncClient -> s3AsyncClient.uploadPart(AsyncRequestConverter.uploadPart(request), request.getUpload().toPath()), AsyncResponseConverter::uploadPart);
    }

    @Override
    public ActionFuture<MultipartUploadPartResponse> uploadInputStreamAsync(UploadInputStreamRequest request) {
        return executor.execute(() -> doUploadInputStream(request));
    }

    @Override
    public void uploadInputStreamAsync(UploadInputStreamRequest request, ActionListener<MultipartUploadPartResponse> listener) {
        executor.execute(() -> doUploadInputStream(request), listener);
    }

    @Override
    public MultipartUploadPartResponse uploadInputStream(UploadInputStreamRequest request) {
        return uploadInputStreamAsync(request).actionGet();
    }

    private CompletableFuture<MultipartUploadPartResponse> doUploadInputStream(UploadInputStreamRequest request) {
        return doAction(s3AsyncClient -> s3AsyncClient.uploadPart(AsyncRequestConverter.uploadPart(request), new InputStreamAsyncRequestBody(request.getUpload())), AsyncResponseConverter::uploadPart);
    }

    @Override
    public ActionFuture<MultipartUploadPartResponse> UploadStringAsync(UploadStringRequest request) {
        return executor.execute(() -> doUploadString(request));
    }

    @Override
    public void UploadStringAsync(UploadStringRequest request, ActionListener<MultipartUploadPartResponse> listener) {
        executor.execute(() -> doUploadString(request), listener);
    }

    @Override
    public MultipartUploadPartResponse UploadString(UploadStringRequest request) {
        return UploadStringAsync(request).actionGet();
    }

    private CompletableFuture<MultipartUploadPartResponse> doUploadString(UploadStringRequest request) {
        return doAction(s3AsyncClient -> s3AsyncClient.uploadPart(AsyncRequestConverter.uploadPart(request), new InputStreamAsyncRequestBody(request.getUpload(), request.getCharset())), AsyncResponseConverter::uploadPart);
    }

    @Override
    public ActionFuture<CompleteMultipartUploadResponse> completeMultipartUploadAsync(CompleteMultipartUploadRequest request) {
        return executor.execute(() -> doCompleteMultipartUpload(request));
    }

    @Override
    public void completeMultipartUploadAsync(CompleteMultipartUploadRequest request, ActionListener<CompleteMultipartUploadResponse> listener) {
        executor.execute(() -> doCompleteMultipartUpload(request), listener);
    }

    @Override
    public CompleteMultipartUploadResponse completeMultipartUpload(CompleteMultipartUploadRequest request) {
        return completeMultipartUploadAsync(request).actionGet();
    }

    private CompletableFuture<CompleteMultipartUploadResponse> doCompleteMultipartUpload(CompleteMultipartUploadRequest request) {
        return doAction(s3AsyncClient -> s3AsyncClient.completeMultipartUpload(AsyncRequestConverter.completeMultipartUpload(request)), AsyncResponseConverter::completeMultipartUpload);
    }

    @Override
    public ActionFuture<AbortMultipartUploadResponse> abortMultipartUploadAsync(AbortMultipartUploadRequest request) {
        return executor.execute(() -> doAbortMultipartUpload(request));
    }

    @Override
    public void abortMultipartUploadAsync(AbortMultipartUploadRequest request, ActionListener<AbortMultipartUploadResponse> listener) {
        executor.execute(() -> doAbortMultipartUpload(request), listener);
    }

    @Override
    public AbortMultipartUploadResponse abortMultipartUpload(AbortMultipartUploadRequest request) {
        return abortMultipartUploadAsync(request).actionGet();
    }

    private CompletableFuture<AbortMultipartUploadResponse> doAbortMultipartUpload(AbortMultipartUploadRequest request) {
        return doAction(s3AsyncClient -> s3AsyncClient.abortMultipartUpload(AsyncRequestConverter.abortMultipartUpload(request)), AsyncResponseConverter::abortMultipartUpload);
    }
}
