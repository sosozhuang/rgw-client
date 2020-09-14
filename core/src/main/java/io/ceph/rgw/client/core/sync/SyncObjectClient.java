package io.ceph.rgw.client.core.sync;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import io.ceph.rgw.client.ObjectClient;
import io.ceph.rgw.client.action.ActionExecutor;
import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;
import io.ceph.rgw.client.converter.SyncRequestConverter;
import io.ceph.rgw.client.converter.SyncResponseConverter;
import io.ceph.rgw.client.core.Connectors;
import io.ceph.rgw.client.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A sync implementation of ObjectClient.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/2/29.
 */
public final class SyncObjectClient extends SyncConnectorAware implements ObjectClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(SyncObjectClient.class);
    private final Map<String, String> internalMetadata;

    public SyncObjectClient(Connectors<AmazonS3> connectors, ActionExecutor executor, Map<String, String> internalMetadata) {
        super(connectors, executor);
        if (internalMetadata == null || internalMetadata.size() == 0) {
            this.internalMetadata = Collections.emptyMap();
        } else {
            this.internalMetadata = Collections.unmodifiableMap(new HashMap<>(internalMetadata));
        }
    }

    @Override
    public ActionFuture<PutObjectResponse> putByteBufferAsync(PutByteBufferRequest request) {
        return putObjectAsync(request, SyncRequestConverter::putByteBuffer);
    }

    @Override
    public void putByteBufferAsync(PutByteBufferRequest request, ActionListener<PutObjectResponse> listener) {
        putObjectAsync(request, SyncRequestConverter::putByteBuffer, listener);
    }

    @Override
    public PutObjectResponse putByteBuffer(PutByteBufferRequest request) {
        return executor.run(() -> doPutObject(request, SyncRequestConverter::putByteBuffer));
    }

    @Override
    public ActionFuture<PutObjectResponse> putFileAsync(PutFileRequest request) {
        return putObjectAsync(request, SyncRequestConverter::putFile);
    }

    @Override
    public void putFileAsync(PutFileRequest request, ActionListener<PutObjectResponse> listener) {
        putObjectAsync(request, SyncRequestConverter::putFile, listener);
    }

    @Override
    public PutObjectResponse putFile(PutFileRequest request) {
        return executor.run(() -> doPutObject(request, SyncRequestConverter::putFile));
    }

    @Override
    public ActionFuture<PutObjectResponse> putInputStreamAsync(PutInputStreamRequest request) {
        return putObjectAsync(request, SyncRequestConverter::putInputStream);
    }

    @Override
    public void putInputStreamAsync(PutInputStreamRequest request, ActionListener<PutObjectResponse> listener) {
        putObjectAsync(request, SyncRequestConverter::putInputStream, listener);
    }

    @Override
    public PutObjectResponse putInputStream(PutInputStreamRequest request) {
        return executor.run(() -> doPutObject(request, SyncRequestConverter::putInputStream));
    }

    @Override
    public ActionFuture<PutObjectResponse> putStringAsync(PutStringRequest request) {
        return putObjectAsync(request, SyncRequestConverter::putString);
    }

    @Override
    public void putStringAsync(PutStringRequest request, ActionListener<PutObjectResponse> listener) {
        putObjectAsync(request, SyncRequestConverter::putString, listener);
    }

    @Override
    public PutObjectResponse putString(PutStringRequest request) {
        return executor.run(() -> doPutObject(request, SyncRequestConverter::putString));
    }

    private <T extends GenericPutObjectRequest<?>> ActionFuture<PutObjectResponse> putObjectAsync(T request, SyncRequestConverter.PutObjectRequestConverter<T> converter) {
        return executor.execute(() -> doPutObject(request, converter));
    }

    private <T extends GenericPutObjectRequest<?>> void putObjectAsync(T request, SyncRequestConverter.PutObjectRequestConverter<T> converter, ActionListener<PutObjectResponse> listener) {
        executor.execute(() -> doPutObject(request, converter), listener);
    }

    private <T extends GenericPutObjectRequest<?>> PutObjectResponse doPutObject(T request, SyncRequestConverter.PutObjectRequestConverter<T> converter) {
        PutObjectRequest putObjectRequest = converter.convert(request).withGeneralProgressListener(LoggingProgressListener.create(LOGGER, request));
        internalMetadata.forEach((k, v) -> putObjectRequest.getMetadata().addUserMetadata(k, v));
        return doAction(amazonS3 -> SyncResponseConverter.putObject(amazonS3.putObject(putObjectRequest)));
    }

    @Override
    public ActionFuture<GetObjectResponse> getObjectAsync(GetObjectRequest request) {
        return getObjectAsync(request, SyncResponseConverter::getObject);
    }

    @Override
    public void getObjectAsync(GetObjectRequest request, ActionListener<GetObjectResponse> listener) {
        getObjectAsync(request, SyncResponseConverter::getObject, listener);
    }

    @Override
    public GetObjectResponse getObject(GetObjectRequest request) {
        return doGetObject(request, SyncResponseConverter::getObject);
    }

    @Override
    public ActionFuture<GetFileResponse> getFileAsync(GetFileRequest request) {
        return getObjectAsync(request, SyncResponseConverter::getFile);
    }

    @Override
    public void getFileAsync(GetFileRequest request, ActionListener<GetFileResponse> listener) {
        getObjectAsync(request, SyncResponseConverter::getFile, listener);
    }

    @Override
    public GetFileResponse getFile(GetFileRequest request) {
        return doGetObject(request, SyncResponseConverter::getFile);
    }

    @Override
    public ActionFuture<GetInputStreamResponse> getInputStreamAsync(GetInputStreamRequest request) {
        return getObjectAsync(request, SyncResponseConverter::getInputStream);
    }

    @Override
    public void getInputStreamAsync(GetInputStreamRequest request, ActionListener<GetInputStreamResponse> listener) {
        getObjectAsync(request, SyncResponseConverter::getInputStream, listener);
    }

    @Override
    public GetInputStreamResponse getInputStream(GetInputStreamRequest request) {
        return doGetObject(request, SyncResponseConverter::getInputStream);
    }

    @Override
    public ActionFuture<GetStringResponse> getStringAsync(GetStringRequest request) {
        return getObjectAsync(request, SyncResponseConverter::getString);
    }

    @Override
    public void getStringAsync(GetStringRequest request, ActionListener<GetStringResponse> listener) {
        getObjectAsync(request, SyncResponseConverter::getString, listener);
    }

    @Override
    public GetStringResponse getString(GetStringRequest request) {
        return doGetObject(request, SyncResponseConverter::getString);
    }

    private <REQ extends BaseGetObjectRequest, RESP extends BaseGetObjectResponse> ActionFuture<RESP> getObjectAsync(REQ request, SyncResponseConverter.GetObjectResponseConverter<REQ, RESP> respConverter) {
        return executor.execute(() -> doGetObject(request, respConverter));
    }

    private <REQ extends BaseGetObjectRequest, RESP extends BaseGetObjectResponse> void getObjectAsync(REQ request, SyncResponseConverter.GetObjectResponseConverter<REQ, RESP> respConverter, ActionListener<RESP> listener) {
        executor.execute(() -> doGetObject(request, respConverter), listener);
    }

    private <REQ extends BaseGetObjectRequest, RESP extends BaseGetObjectResponse> RESP doGetObject(REQ request, SyncResponseConverter.GetObjectResponseConverter<REQ, RESP> respConverter) {
        return doAction(amazonS3 -> respConverter.convert(request, amazonS3.getObject(SyncRequestConverter.getObject(request).withGeneralProgressListener(LoggingProgressListener.create(LOGGER, request)))));
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
        return executor.run(() -> doCopyObject(request));
    }

    private CopyObjectResponse doCopyObject(CopyObjectRequest request) {
        return doAction(amazonS3 -> SyncResponseConverter.copyObject(amazonS3.copyObject(SyncRequestConverter.copyObject(request).withGeneralProgressListener(LoggingProgressListener.create(LOGGER, request)))));
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
        return executor.run(() -> doRemoveObject(request));
    }

    private DeleteObjectResponse doRemoveObject(DeleteObjectRequest request) {
        return doAction(amazonS3 -> {
            amazonS3.deleteObject(SyncRequestConverter.deleteObject(request).withGeneralProgressListener(LoggingProgressListener.create(LOGGER, request)));
            return SyncResponseConverter.deleteObject();
        });
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
        return executor.run(() -> doGetObjectInfo(request));
    }

    private GetObjectInfoResponse doGetObjectInfo(GetObjectInfoRequest request) {
        return doAction(amazonS3 -> SyncResponseConverter.getObjectInfo(amazonS3.getObjectMetadata(SyncRequestConverter.getObjectInfo(request).withGeneralProgressListener(LoggingProgressListener.create(LOGGER, request)))));
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
        return executor.run(() -> doGetObjectACL(request));
    }

    private GetObjectACLResponse doGetObjectACL(GetObjectACLRequest request) {
        return doAction(amazonS3 -> SyncResponseConverter.getObjectACL(amazonS3.getObjectAcl(SyncRequestConverter.getObjectACL(request).withGeneralProgressListener(LoggingProgressListener.create(LOGGER, request)))));
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
        return executor.run(() -> doSetObjectACL(request));
    }

    private PutObjectACLResponse doSetObjectACL(PutObjectACLRequest request) {
        return doAction((amazonS3 -> {
            amazonS3.setObjectAcl(SyncRequestConverter.putObjectACL(request).withGeneralProgressListener(LoggingProgressListener.create(LOGGER, request)));
            return SyncResponseConverter.putObjectACL();
        }));
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
        return executor.run(() -> doInitiateMultipartUpload(request));
    }

    private InitiateMultipartUploadResponse doInitiateMultipartUpload(InitiateMultipartUploadRequest request) {
        return doAction(amazonS3 -> {
            com.amazonaws.services.s3.model.InitiateMultipartUploadRequest uploadRequest = SyncRequestConverter.initiateMultipartUpload(request).withGeneralProgressListener(LoggingProgressListener.create(LOGGER, request));
            internalMetadata.forEach((k, v) -> uploadRequest.getObjectMetadata().addUserMetadata(k, v));
            return SyncResponseConverter.initiateMultipartUpload(amazonS3.initiateMultipartUpload(uploadRequest));
        });
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
        return executor.run(() -> doUploadByteBuffer(request));
    }

    private MultipartUploadPartResponse doUploadByteBuffer(UploadByteBufferRequest request) {
        return doAction(amazonS3 -> SyncResponseConverter.multipartUploadPart(amazonS3.uploadPart(SyncRequestConverter.uploadByteBuffer(request).withGeneralProgressListener(LoggingProgressListener.create(LOGGER, request)))));
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
        return executor.run(() -> doUploadFile(request));
    }

    private MultipartUploadPartResponse doUploadFile(UploadFileRequest request) {
        return doAction(amazonS3 -> SyncResponseConverter.multipartUploadPart(amazonS3.uploadPart(SyncRequestConverter.uploadFile(request).withGeneralProgressListener(LoggingProgressListener.create(LOGGER, request)))));
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
        return executor.run(() -> doUploadInputStream(request));
    }

    private MultipartUploadPartResponse doUploadInputStream(UploadInputStreamRequest request) {
        return doAction(amazonS3 -> SyncResponseConverter.multipartUploadPart(amazonS3.uploadPart(SyncRequestConverter.uploadInputStream(request).withGeneralProgressListener(LoggingProgressListener.create(LOGGER, request)))));
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
        return executor.run(() -> doUploadString(request));
    }

    private MultipartUploadPartResponse doUploadString(UploadStringRequest request) {
        return doAction(amazonS3 -> SyncResponseConverter.multipartUploadPart(amazonS3.uploadPart(SyncRequestConverter.uploadString(request).withGeneralProgressListener(LoggingProgressListener.create(LOGGER, request)))));
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
        return executor.run(() -> doCompleteMultipartUpload(request));
    }

    private CompleteMultipartUploadResponse doCompleteMultipartUpload(CompleteMultipartUploadRequest request) {
        return doAction(amazonS3 -> SyncResponseConverter.completeMultipartUpload(amazonS3.completeMultipartUpload(SyncRequestConverter.completeMultipartUpload(request))));
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
        return executor.run(() -> doAbortMultipartUpload(request));
    }

    private AbortMultipartUploadResponse doAbortMultipartUpload(AbortMultipartUploadRequest request) {
        return doAction(amazonS3 -> {
            amazonS3.abortMultipartUpload(SyncRequestConverter.abortMultipartUpload(request).withGeneralProgressListener(LoggingProgressListener.create(LOGGER, request)));
            return SyncResponseConverter.abortMultipartUpload();
        });
    }
}