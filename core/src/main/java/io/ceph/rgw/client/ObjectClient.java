package io.ceph.rgw.client;

import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;
import io.ceph.rgw.client.core.io.ObjectWriter;
import io.ceph.rgw.client.model.*;

/**
 * A client that provides <a href="https://docs.ceph.com/docs/master/radosgw/s3/objectops/">object operations</a>, sends ObjectRequest and receives ObjectResponse.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/2/27.
 * @see Clients#getObject()
 * @see ObjectRequest
 * @see ObjectResponse
 */
public interface ObjectClient {
    /**
     * Fluent api to write an object
     *
     * @return the write object builder
     */
    default ObjectWriter.Builder preparePutObject() {
        return new ObjectWriter.Builder(this);
    }

    /**
     * Put byte buffer asynchronously.
     *
     * @param request the put byte buffer request
     * @return An ActionFuture containing the put byte buffer response
     */
    ActionFuture<PutObjectResponse> putByteBufferAsync(PutByteBufferRequest request);

    /**
     * Put byte buffer asynchronously.
     *
     * @param request  the put byte buffer request
     * @param listener the callback listener after action is done
     */
    void putByteBufferAsync(PutByteBufferRequest request, ActionListener<PutObjectResponse> listener);

    /**
     * Put byte buffer synchronously.
     *
     * @param request the put byte buffer request
     * @return the put byte buffer response
     */
    PutObjectResponse putByteBuffer(PutByteBufferRequest request);

    /**
     * Fluent api to put byte buffer.
     *
     * @return the request builder
     */
    default PutByteBufferRequest.Builder preparePutByteBuffer() {
        return new PutByteBufferRequest.Builder(this);
    }

    /**
     * Put file asynchronously.
     *
     * @param request the put file request
     * @return An ActionFuture containing the put file response
     */
    ActionFuture<PutObjectResponse> putFileAsync(PutFileRequest request);

    /**
     * Put file asynchronously.
     *
     * @param request  the put file request
     * @param listener the callback listener after action is done
     */
    void putFileAsync(PutFileRequest request, ActionListener<PutObjectResponse> listener);

    /**
     * Put file synchronously.
     *
     * @param request the put file request
     * @return the put file response
     */
    PutObjectResponse putFile(PutFileRequest request);

    /**
     * Fluent api to put file.
     *
     * @return the request builder
     */
    default PutFileRequest.Builder preparePutFile() {
        return new PutFileRequest.Builder(this);
    }

    /**
     * Put input stream asynchronously.
     *
     * @param request the put input stream request
     * @return An ActionFuture containing the put input stream response
     */
    ActionFuture<PutObjectResponse> putInputStreamAsync(PutInputStreamRequest request);

    /**
     * Put input stream asynchronously.
     *
     * @param request  the put input stream request
     * @param listener the callback listener after action is done
     */
    void putInputStreamAsync(PutInputStreamRequest request, ActionListener<PutObjectResponse> listener);

    /**
     * Put input stream synchronously.
     *
     * @param request the put input stream request
     * @return the put input stream response
     */
    PutObjectResponse putInputStream(PutInputStreamRequest request);

    /**
     * Fluent api to put input stream.
     *
     * @return the request builder
     */
    default PutInputStreamRequest.Builder preparePutInputStream() {
        return new PutInputStreamRequest.Builder(this);
    }

    /**
     * Put string asynchronously.
     *
     * @param request the put string request
     * @return An ActionFuture containing the put string response
     */
    ActionFuture<PutObjectResponse> putStringAsync(PutStringRequest request);

    /**
     * Put string asynchronously.
     *
     * @param request  the put string request
     * @param listener the callback listener after action is done
     */
    void putStringAsync(PutStringRequest request, ActionListener<PutObjectResponse> listener);

    /**
     * Put string synchronously.
     *
     * @param request the put string request
     * @return the put string response
     */
    PutObjectResponse putString(PutStringRequest request);

    /**
     * Fluent api to put string.
     *
     * @return the request builder
     */
    default PutStringRequest.Builder preparePutString() {
        return new PutStringRequest.Builder(this);
    }

    /**
     * Get object asynchronously.
     *
     * @param request the get object request
     * @return An ActionFuture containing the get object response
     */
    ActionFuture<GetObjectResponse> getObjectAsync(GetObjectRequest request);

    /**
     * Get object asynchronously.
     *
     * @param request  the get object request
     * @param listener the callback listener after action is done
     */
    void getObjectAsync(GetObjectRequest request, ActionListener<GetObjectResponse> listener);

    /**
     * Get object synchronously.
     *
     * @param request the get object request
     * @return the get object response
     */
    GetObjectResponse getObject(GetObjectRequest request);

    /**
     * Fluent api to get object.
     *
     * @return the request builder
     */
    default GetObjectRequest.Builder prepareGetObject() {
        return new GetObjectRequest.Builder(this);
    }

    /**
     * Get file asynchronously.
     *
     * @param request the get file request
     * @return An ActionFuture containing the get file response
     */
    ActionFuture<GetFileResponse> getFileAsync(GetFileRequest request);

    /**
     * Get file asynchronously.
     *
     * @param request  the get file request
     * @param listener the callback listener after action is done
     */
    void getFileAsync(GetFileRequest request, ActionListener<GetFileResponse> listener);

    /**
     * Get file synchronously.
     *
     * @param request the get file request
     * @return the get file response
     */
    GetFileResponse getFile(GetFileRequest request);

    /**
     * Fluent api to get file.
     *
     * @return the request builder
     */
    default GetFileRequest.Builder prepareGetFile() {
        return new GetFileRequest.Builder(this);
    }

    /**
     * Get input stream asynchronously.
     *
     * @param request the get input stream request
     * @return An ActionFuture containing the get input stream response
     */
    ActionFuture<GetInputStreamResponse> getInputStreamAsync(GetInputStreamRequest request);

    /**
     * Get input stream asynchronously.
     *
     * @param request  the get input stream request
     * @param listener the callback listener after action is done
     */
    void getInputStreamAsync(GetInputStreamRequest request, ActionListener<GetInputStreamResponse> listener);

    /**
     * Get input stream synchronously.
     *
     * @param request the get input stream request
     * @return the get input stream response
     */
    GetInputStreamResponse getInputStream(GetInputStreamRequest request);

    /**
     * Fluent api to get input stream.
     *
     * @return the request builder
     */
    default GetInputStreamRequest.Builder prepareGetInputStream() {
        return new GetInputStreamRequest.Builder(this);
    }

    /**
     * Get string asynchronously.
     *
     * @param request the get string request
     * @return An ActionFuture containing get get string response
     */
    ActionFuture<GetStringResponse> getStringAsync(GetStringRequest request);

    /**
     * Get string asynchronously.
     *
     * @param request  the get string request
     * @param listener the callback listener after action is done
     */
    void getStringAsync(GetStringRequest request, ActionListener<GetStringResponse> listener);

    /**
     * Get string synchronously.
     *
     * @param request the get string request
     * @return the get string response
     */
    GetStringResponse getString(GetStringRequest request);

    /**
     * Fluent api to get string.
     *
     * @return the request builder
     */
    default GetStringRequest.Builder prepareGetString() {
        return new GetStringRequest.Builder(this);
    }

    /**
     * Copy object asynchronously.
     *
     * @param request the copy object request
     * @return An ActionFuture containing the copy object response
     */
    ActionFuture<CopyObjectResponse> copyObjectAsync(CopyObjectRequest request);

    /**
     * Copy object asynchronously.
     *
     * @param request  the copy object request
     * @param listener the callback listener after action is done
     */
    void copyObjectAsync(CopyObjectRequest request, ActionListener<CopyObjectResponse> listener);

    /**
     * Copy object synchronously.
     *
     * @param request the copy object request
     * @return the copy object response
     */
    CopyObjectResponse copyObject(CopyObjectRequest request);

    /**
     * Fluent api to copy object.
     *
     * @return the request builder
     */
    default CopyObjectRequest.Builder prepareCopyObject() {
        return new CopyObjectRequest.Builder(this);
    }

    /**
     * Delete object asynchronously.
     *
     * @param request the delete object request
     * @return An ActionFuture containing the delete object response
     */
    ActionFuture<DeleteObjectResponse> deleteObjectAsync(DeleteObjectRequest request);

    /**
     * Delete object asynchronously.
     *
     * @param request  the delete object request
     * @param listener the callback listener after action is done
     */
    void deleteObjectAsync(DeleteObjectRequest request, ActionListener<DeleteObjectResponse> listener);

    /**
     * Delete object synchronously.
     *
     * @param request the delete object request
     * @return the delete object response
     */
    DeleteObjectResponse deleteObject(DeleteObjectRequest request);

    /**
     * Fluent api to delete object.
     *
     * @return the request builder
     */
    default DeleteObjectRequest.Builder prepareDeleteObject() {
        return new DeleteObjectRequest.Builder(this);
    }

    /**
     * Get object info asynchronously.
     *
     * @param request the get object info request
     * @return An ActionFuture containing the get object info response
     */
    ActionFuture<GetObjectInfoResponse> getObjectInfoAsync(GetObjectInfoRequest request);

    /**
     * Get object info asynchronously.
     *
     * @param request  the get object info request
     * @param listener the callback listener after action is done
     */
    void getObjectInfoAsync(GetObjectInfoRequest request, ActionListener<GetObjectInfoResponse> listener);

    /**
     * Get object info synchronously.
     *
     * @param request the get object info request
     * @return the get object info response
     */
    GetObjectInfoResponse getObjectInfo(GetObjectInfoRequest request);

    /**
     * Fluent api to get object info.
     *
     * @return the request builder
     */
    default GetObjectInfoRequest.Builder prepareGetObjectInfo() {
        return new GetObjectInfoRequest.Builder(this);
    }

    /**
     * Get object ACL asynchronously.
     *
     * @param request the get object ACL request
     * @return An ActionFuture containing the get object ACL response
     */
    ActionFuture<GetObjectACLResponse> getObjectACLAsync(GetObjectACLRequest request);

    /**
     * Get object ACL asynchronously.
     *
     * @param request  the get object ACL request
     * @param listener the callback listener after action is done
     */
    void getObjectACLAsync(GetObjectACLRequest request, ActionListener<GetObjectACLResponse> listener);

    /**
     * Get object ACL synchronously.
     *
     * @param request the get object ACL request
     * @return the get object ACL response
     */
    GetObjectACLResponse getObjectACL(GetObjectACLRequest request);

    /**
     * Fluent api to get object ACL.
     *
     * @return the request builder
     */
    default GetObjectACLRequest.Builder prepareGetObjectACL() {
        return new GetObjectACLRequest.Builder(this);
    }

    /**
     * Put object ACL asynchronously.
     *
     * @param request the put object ACL request
     * @return An ActionFuture containing the put object ACL response
     */
    ActionFuture<PutObjectACLResponse> putObjectACLAsync(PutObjectACLRequest request);

    /**
     * Put object ACL asynchronously.
     *
     * @param request  the put object ACL request
     * @param listener the callback listener after action is done
     */
    void putObjectACLAsync(PutObjectACLRequest request, ActionListener<PutObjectACLResponse> listener);

    /**
     * Put object ACL synchronously.
     *
     * @param request the put object ACL request
     * @return the put object ACL response
     */
    PutObjectACLResponse putObjectACL(PutObjectACLRequest request);

    /**
     * Fluent api to get object ACL.
     *
     * @return the request builder
     */
    default PutObjectACLRequest.Builder preparePutObjectACL() {
        return new PutObjectACLRequest.Builder(this);
    }

    /**
     * Initiate multipart upload asynchronously.
     *
     * @param request the initiate multipart upload request
     * @return An ActionFuture containing the initiate multipart uplaod response
     */
    ActionFuture<InitiateMultipartUploadResponse> initiateMultipartUploadAsync(InitiateMultipartUploadRequest request);

    /**
     * Initiate multipart upload asynchronously.
     *
     * @param request  the initiate multipart upload request
     * @param listener the callback listener after action is done
     */
    void initiateMultipartUploadAsync(InitiateMultipartUploadRequest request, ActionListener<InitiateMultipartUploadResponse> listener);

    /**
     * Initiate multipart upload synchronously.
     *
     * @param request the initiate multipart upload request
     * @return the initiate multipart upload response
     */
    InitiateMultipartUploadResponse initiateMultipartUpload(InitiateMultipartUploadRequest request);

    /**
     * Fluent api to initiate multipart upload.
     *
     * @return the request builder
     */
    default InitiateMultipartUploadRequest.Builder prepareInitiateMultipartUpload() {
        return new InitiateMultipartUploadRequest.Builder(this);
    }

    /**
     * Upload byte buffer asynchronously.
     *
     * @param request the upload byte buffer request
     * @return An ActionFuture containing the upload byte buffer response
     */
    ActionFuture<MultipartUploadPartResponse> uploadByteBufferAsync(UploadByteBufferRequest request);

    /**
     * Upload byte buffer asynchronously.
     *
     * @param request  the upload byte buffer request
     * @param listener the callback listener after action is done
     */
    void uploadByteBufferAsync(UploadByteBufferRequest request, ActionListener<MultipartUploadPartResponse> listener);

    /**
     * Upload byte buffer synchronously.
     *
     * @param request the upload byte buffer request
     * @return the upload byte buffer response
     */
    MultipartUploadPartResponse uploadByteBuffer(UploadByteBufferRequest request);

    /**
     * Fluent api to upload byte buffer.
     *
     * @return the request builder
     */
    default UploadByteBufferRequest.Builder prepareUploadByteBuffer() {
        return new UploadByteBufferRequest.Builder(this);
    }

    /**
     * Upload file asynchronously.
     *
     * @param request the upload file request
     * @return An ActionFuture containing the upload file response
     */
    ActionFuture<MultipartUploadPartResponse> uploadFileAsync(UploadFileRequest request);

    /**
     * Upload file asynchronously.
     *
     * @param request  the upload file request
     * @param listener the callback listener after action is done
     */
    void uploadFileAsync(UploadFileRequest request, ActionListener<MultipartUploadPartResponse> listener);

    /**
     * Upload file synchronously.
     *
     * @param request the upload file request
     * @return the upload file response
     */
    MultipartUploadPartResponse uploadFile(UploadFileRequest request);

    /**
     * Fluent api to upload file.
     *
     * @return the request builder
     */
    default UploadFileRequest.Builder prepareUploadFile() {
        return new UploadFileRequest.Builder(this);
    }

    /**
     * Upload input stream asynchronously.
     *
     * @param request the upload input stream request
     * @return An ActionFuture containing the upload input stream response
     */
    ActionFuture<MultipartUploadPartResponse> uploadInputStreamAsync(UploadInputStreamRequest request);

    /**
     * Upload input stream asynchronously.
     *
     * @param request  the upload input stream request
     * @param listener the callback listener after action is done
     */
    void uploadInputStreamAsync(UploadInputStreamRequest request, ActionListener<MultipartUploadPartResponse> listener);

    /**
     * Upload input stream synchronously.
     *
     * @param request the upload input stream request
     * @return the upload input stream response
     */
    MultipartUploadPartResponse uploadInputStream(UploadInputStreamRequest request);

    /**
     * Fluent api to upload input stream.
     *
     * @return the request builder
     */
    default UploadInputStreamRequest.Builder prepareUploadInputStream() {
        return new UploadInputStreamRequest.Builder(this);
    }

    /**
     * Upload string asynchronously.
     *
     * @param request the upload string request
     * @return An ActionFuture containing the upload string response
     */
    ActionFuture<MultipartUploadPartResponse> UploadStringAsync(UploadStringRequest request);

    /**
     * Upload string asynchronously.
     *
     * @param request  the upload string request
     * @param listener the callback listener after action is done
     */
    void UploadStringAsync(UploadStringRequest request, ActionListener<MultipartUploadPartResponse> listener);

    /**
     * Upload string synchronously.
     *
     * @param request the upload string request
     * @return the upload string response
     */
    MultipartUploadPartResponse UploadString(UploadStringRequest request);

    /**
     * Fluent api to upload string.
     *
     * @return the request builder
     */
    default UploadStringRequest.Builder prepareUploadString() {
        return new UploadStringRequest.Builder(this);
    }

    /**
     * Complete multipart upload asynchronously.
     *
     * @param request the complete multipart upload request
     * @return An ActionFuture containing the complete multipart upload response
     */
    ActionFuture<CompleteMultipartUploadResponse> completeMultipartUploadAsync(CompleteMultipartUploadRequest request);

    /**
     * Complete multipart upload asynchronously.
     *
     * @param request  the complete multipart upload request
     * @param listener the callback listener after action is done
     */
    void completeMultipartUploadAsync(CompleteMultipartUploadRequest request, ActionListener<CompleteMultipartUploadResponse> listener);

    /**
     * Complete multipart upload synchronously.
     *
     * @param request the complete multipart upload request
     * @return the complete multipart upload response
     */
    CompleteMultipartUploadResponse completeMultipartUpload(CompleteMultipartUploadRequest request);

    /**
     * Fluent api to complete multipart upload.
     *
     * @return the request builder
     */
    default CompleteMultipartUploadRequest.Builder prepareCompleteMultipartUpload() {
        return new CompleteMultipartUploadRequest.Builder(this);
    }

    /**
     * Abort multipart upload asynchronously.
     *
     * @param request the abort multipart upload request
     * @return An ActionFuture containing the abort multipart upload response
     */
    ActionFuture<AbortMultipartUploadResponse> abortMultipartUploadAsync(AbortMultipartUploadRequest request);

    /**
     * Abort multipart upload asynchronously.
     *
     * @param request  the abort multipart upload request
     * @param listener the callback listener after action is done
     */
    void abortMultipartUploadAsync(AbortMultipartUploadRequest request, ActionListener<AbortMultipartUploadResponse> listener);

    /**
     * Abort multipart upload asynchronously.
     *
     * @param request the abort multipart upload request
     * @return the abort multipart upload response
     */
    AbortMultipartUploadResponse abortMultipartUpload(AbortMultipartUploadRequest request);

    /**
     * Fluent api to abort multipart upload.
     *
     * @return the request builder
     */
    default AbortMultipartUploadRequest.Builder prepareAbortMultipartUpload() {
        return new AbortMultipartUploadRequest.Builder(this);
    }

//    void appendObject();
//
//    void putObjectRetention();
//
//    void getObjectRetention();
//
//    void putObjectLegalHold();
//
//    void getObjectLegalHold();
}
