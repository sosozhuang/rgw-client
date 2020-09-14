package io.ceph.rgw.client.core.async;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.ceph.rgw.client.exception.*;
import io.ceph.rgw.client.model.admin.*;
import io.ceph.rgw.client.model.admin.tranform.*;
import software.amazon.awssdk.awscore.client.handler.AwsAsyncClientHandler;
import software.amazon.awssdk.core.Response;
import software.amazon.awssdk.core.SdkClient;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.client.config.SdkClientConfiguration;
import software.amazon.awssdk.core.client.handler.AsyncClientHandler;
import software.amazon.awssdk.core.client.handler.ClientExecutionParams;
import software.amazon.awssdk.core.http.HttpResponseHandler;
import software.amazon.awssdk.core.runtime.transform.Marshaller;
import software.amazon.awssdk.protocols.core.ExceptionMetadata;
import software.amazon.awssdk.protocols.xml.AwsS3ProtocolFactory;
import software.amazon.awssdk.protocols.xml.XmlOperationMetadata;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.utils.CompletableFutureUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/7/27.
 */
public class AdminAsyncConnector implements SdkClient {
    private static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper();
        OBJECT_MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    private final AsyncClientHandler clientHandler;
    private final AwsS3ProtocolFactory protocolFactory;
    private final SdkClientConfiguration clientConfiguration;
    private final Map<Class<?>, Marshaller<?>> marshallers;

    private final XmlOperationMetadata operationMetadata;

    protected AdminAsyncConnector(SdkClientConfiguration clientConfiguration) {
        this.clientHandler = new AwsAsyncClientHandler(clientConfiguration);
        this.clientConfiguration = clientConfiguration;
        this.protocolFactory = initProtocolFactory();
        this.operationMetadata = new XmlOperationMetadata().withHasStreamingSuccessResponse(false);
        this.marshallers = initMarshallers();
    }

    public static AdminAsyncConnectorBuilder builder() {
        return new AdminAsyncConnectorBuilder();
    }

    private AwsS3ProtocolFactory initProtocolFactory() {
        return AwsS3ProtocolFactory
                .builder()
                .registerModeledException(
                        ExceptionMetadata.builder().httpStatusCode(409).errorCode("UserExists").exceptionBuilderSupplier(UserExistsException::builder)
                                .build())
                .registerModeledException(
                        ExceptionMetadata.builder().httpStatusCode(409).errorCode("EmailExists").exceptionBuilderSupplier(EmailExistsException::builder)
                                .build())
                .registerModeledException(
                        ExceptionMetadata.builder().httpStatusCode(409).errorCode("SubUserExists").exceptionBuilderSupplier(SubUserExistsException::builder)
                                .build())
                .registerModeledException(
                        ExceptionMetadata.builder().httpStatusCode(400).errorCode("InvalidAccess").exceptionBuilderSupplier(InvalidAccessException::builder)
                                .build())
                .registerModeledException(
                        ExceptionMetadata.builder().httpStatusCode(400).errorCode("InvalidAccessKeyId").exceptionBuilderSupplier(InvalidAccessKeyException::builder)
                                .build())
                .registerModeledException(
                        ExceptionMetadata.builder().httpStatusCode(400).errorCode("InvalidKeyType").exceptionBuilderSupplier(InvalidKeyTypeException::builder)
                                .build())
                .registerModeledException(
                        ExceptionMetadata.builder().httpStatusCode(400).errorCode("InvalidSecretKey").exceptionBuilderSupplier(InvalidSecretKeyException::builder)
                                .build())
                .registerModeledException(
                        ExceptionMetadata.builder().httpStatusCode(409).errorCode("KeyExists").exceptionBuilderSupplier(KeyExistsException::builder)
                                .build())
                .registerModeledException(
                        ExceptionMetadata.builder().httpStatusCode(409).errorCode("IndexRepairFailed").exceptionBuilderSupplier(IndexRepairFailedException::builder)
                                .build())
                .registerModeledException(
                        ExceptionMetadata.builder().httpStatusCode(409).errorCode("BucketNotEmpty").exceptionBuilderSupplier(BucketNotEmptyException::builder)
                                .build())
                .registerModeledException(
                        ExceptionMetadata.builder().httpStatusCode(409).errorCode("BucketUnlinkFailed").exceptionBuilderSupplier(BucketUnlinkFailedException::builder)
                                .build())
                .registerModeledException(
                        ExceptionMetadata.builder().httpStatusCode(409).errorCode("BucketLinkFailed").exceptionBuilderSupplier(BucketLinkFailedException::builder)
                                .build())
                .registerModeledException(
                        ExceptionMetadata.builder().httpStatusCode(409).errorCode("ObjectRemovalFailed").exceptionBuilderSupplier(ObjectRemovalFailedException::builder)
                                .build())
                .registerModeledException(
                        ExceptionMetadata.builder().httpStatusCode(400).errorCode("InCompleteBody").exceptionBuilderSupplier(InCompleteBodyException::builder)
                                .build())
                .registerModeledException(
                        ExceptionMetadata.builder().httpStatusCode(400).errorCode("InvalidCapability").exceptionBuilderSupplier(InvalidCapabilityException::builder)
                                .build())
                .registerModeledException(
                        ExceptionMetadata.builder().httpStatusCode(404).errorCode("NoSuchCap").exceptionBuilderSupplier(NoSuchCapException::builder)
                                .build())
                .registerModeledException(
                        ExceptionMetadata.builder().httpStatusCode(409).errorCode("UserExists").exceptionBuilderSupplier(UserExistsException::builder)
                                .build())
                .registerModeledException(
                        ExceptionMetadata.builder().httpStatusCode(403).errorCode("AccessDenied").exceptionBuilderSupplier(AccessDeniedException::builder)
                                .build())
                .registerModeledException(
                        ExceptionMetadata.builder().httpStatusCode(500).errorCode("InternalError").exceptionBuilderSupplier(InternalErrorException::builder)
                                .build())
                .registerModeledException(
                        ExceptionMetadata.builder().httpStatusCode(404).errorCode("NoSuchUser").exceptionBuilderSupplier(NoSuchUserException::builder)
                                .build())
                .registerModeledException(
                        ExceptionMetadata.builder().httpStatusCode(404).errorCode("NoSuchBucket").exceptionBuilderSupplier(NoSuchBucketException::builder)
                                .build())
                .registerModeledException(
                        ExceptionMetadata.builder().httpStatusCode(404).errorCode("NoSuchKey").exceptionBuilderSupplier(NoSuchKeyException::builder)
                                .build()).clientConfiguration(clientConfiguration)
                .defaultServiceExceptionSupplier(S3Exception::builder).build();
    }

    private Map<Class<?>, Marshaller<?>> initMarshallers() {
        Map<Class<?>, Marshaller<?>> marshallers = new HashMap<>();
        marshallers.put(AddUserCapabilityRequest.class, new AddUserCapabilityRequestMarshaller(protocolFactory));
        marshallers.put(CheckBucketIndexRequest.class, new CheckBucketIndexRequestMarshaller(protocolFactory));
        marshallers.put(CreateKeyRequest.class, new CreateKeyRequestMarshaller(protocolFactory));
        marshallers.put(CreateSubUserRequest.class, new CreateSubUserRequestMarshaller(protocolFactory));
        marshallers.put(CreateUserRequest.class, new CreateUserRequestMarshaller(protocolFactory));
        marshallers.put(GetBucketInfoRequest.class, new GetBucketInfoRequestMarshaller(protocolFactory));
        marshallers.put(GetBucketQuotaRequest.class, new GetBucketQuotaRequestMarshaller(protocolFactory));
        marshallers.put(GetPolicyRequest.class, new GetPolicyRequestMarshaller(protocolFactory));
        marshallers.put(GetUsageRequest.class, new GetUsageRequestMarshaller(protocolFactory));
        marshallers.put(GetUserInfoRequest.class, new GetUserInfoRequestMarshaller(protocolFactory));
        marshallers.put(GetUserQuotaRequest.class, new GetUserQuotaRequestMarshaller(protocolFactory));
        marshallers.put(LinkBucketRequest.class, new LinkBucketRequestMarshaller(protocolFactory));
        marshallers.put(ModifySubUserRequest.class, new ModifySubUserRequestMarshaller(protocolFactory));
        marshallers.put(ModifyUserRequest.class, new ModifyUserRequestMarshaller(protocolFactory));
        marshallers.put(RemoveBucketRequest.class, new RemoveBucketRequestMarshaller(protocolFactory));
        marshallers.put(RemoveKeyRequest.class, new RemoveKeyRequestMarshaller(protocolFactory));
        marshallers.put(RemoveObjectRequest.class, new RemoveObjectRequestMarshaller(protocolFactory));
        marshallers.put(RemoveSubUserRequest.class, new RemoveSubUserRequestMarshaller(protocolFactory));
        marshallers.put(RemoveUserCapabilityRequest.class, new RemoveUserCapabilityRequestMarshaller(protocolFactory));
        marshallers.put(RemoveUserRequest.class, new RemoveUserRequestMarshaller(protocolFactory));
        marshallers.put(SetBucketQuotaRequest.class, new SetBucketQuotaRequestMarshaller(protocolFactory));
        marshallers.put(SetUserQuotaRequest.class, new SetUserQuotaRequestMarshaller(protocolFactory));
        marshallers.put(TrimUsageRequest.class, new TrimUsageRequestMarshaller(protocolFactory));
        marshallers.put(UnlinkBucketRequest.class, new UnlinkBucketRequestMarshaller(protocolFactory));
        return marshallers;
    }

    @SuppressWarnings("unchecked")
    private <T> Marshaller<T> getMarshaller(Class<T> clazz) {
        return (Marshaller<T>) marshallers.get(clazz);
    }

    public CompletableFuture<GetUsageResponse> getUsage(GetUsageRequest request) {
        try {
            HttpResponseHandler<Response<GetUsageResponse>> responseHandler = protocolFactory
                    .createCombinedResponseHandler(GetUsageResponse.Builder::new, operationMetadata);

            return clientHandler.execute(new ClientExecutionParams<GetUsageRequest, GetUsageResponse>()
                    .withOperationName("GetUsage")
                    .withMarshaller(getMarshaller(GetUsageRequest.class))
                    .withCombinedResponseHandler(responseHandler).withInput(request));
        } catch (Throwable t) {
            return CompletableFutureUtils.failedFuture(t);
        }
    }

    public CompletableFuture<TrimUsageResponse> trimUsage(TrimUsageRequest request) {
        try {
            HttpResponseHandler<Response<TrimUsageResponse>> responseHandler = protocolFactory
                    .createCombinedResponseHandler(TrimUsageResponse.Builder::new, operationMetadata);

            return clientHandler.execute(new ClientExecutionParams<TrimUsageRequest, TrimUsageResponse>()
                    .withOperationName("TrimUsage")
                    .withMarshaller(getMarshaller(TrimUsageRequest.class))
                    .withCombinedResponseHandler(responseHandler).withInput(request));
        } catch (Throwable t) {
            return CompletableFutureUtils.failedFuture(t);
        }
    }

    public CompletableFuture<GetUserInfoResponse> getUserInfo(GetUserInfoRequest request) {
        try {
            HttpResponseHandler<Response<GetUserInfoResponse>> responseHandler = protocolFactory
                    .createCombinedResponseHandler(GetUserInfoResponse.Builder::new, operationMetadata);

            return clientHandler.execute(new ClientExecutionParams<GetUserInfoRequest, GetUserInfoResponse>()
                    .withOperationName("GetUserInfo")
                    .withMarshaller(getMarshaller(GetUserInfoRequest.class))
                    .withCombinedResponseHandler(responseHandler).withInput(request));
        } catch (Throwable t) {
            return CompletableFutureUtils.failedFuture(t);
        }
    }

    public CompletableFuture<CreateUserResponse> createUser(CreateUserRequest request) {
        try {
            HttpResponseHandler<Response<CreateUserResponse>> responseHandler = protocolFactory
                    .createCombinedResponseHandler(CreateUserResponse.Builder::new, operationMetadata);

            return clientHandler.execute(new ClientExecutionParams<CreateUserRequest, CreateUserResponse>()
                    .withOperationName("CreateUser")
                    .withMarshaller(getMarshaller(CreateUserRequest.class))
                    .withCombinedResponseHandler(responseHandler).withInput(request));
        } catch (Throwable t) {
            return CompletableFutureUtils.failedFuture(t);
        }
    }

    public CompletableFuture<ModifyUserResponse> modifyUser(ModifyUserRequest request) {
        try {
            HttpResponseHandler<Response<ModifyUserResponse>> responseHandler = protocolFactory
                    .createCombinedResponseHandler(ModifyUserResponse.Builder::new, operationMetadata);

            return clientHandler.execute(new ClientExecutionParams<ModifyUserRequest, ModifyUserResponse>()
                    .withOperationName("ModifyUser")
                    .withMarshaller(getMarshaller(ModifyUserRequest.class))
                    .withCombinedResponseHandler(responseHandler).withInput(request));
        } catch (Throwable t) {
            return CompletableFutureUtils.failedFuture(t);
        }
    }

    public CompletableFuture<RemoveUserResponse> removeUser(RemoveUserRequest request) {
        try {
            HttpResponseHandler<Response<RemoveUserResponse>> responseHandler = protocolFactory
                    .createCombinedResponseHandler(RemoveUserResponse.Builder::new, operationMetadata);

            return clientHandler.execute(new ClientExecutionParams<RemoveUserRequest, RemoveUserResponse>()
                    .withOperationName("RemoveUser")
                    .withMarshaller(getMarshaller(RemoveUserRequest.class))
                    .withCombinedResponseHandler(responseHandler).withInput(request));
        } catch (Throwable t) {
            return CompletableFutureUtils.failedFuture(t);
        }
    }

    public CompletableFuture<CreateSubUserResponse> createSubUser(CreateSubUserRequest request) {
        try {
            HttpResponseHandler<Response<CreateSubUserResponse>> responseHandler = protocolFactory
                    .createCombinedResponseHandler(CreateSubUserResponse.Builder::new, operationMetadata);

            return clientHandler.execute(new ClientExecutionParams<CreateSubUserRequest, CreateSubUserResponse>()
                    .withOperationName("CreateSubUser")
                    .withMarshaller(getMarshaller(CreateSubUserRequest.class))
                    .withCombinedResponseHandler(responseHandler).withInput(request));
        } catch (Throwable t) {
            return CompletableFutureUtils.failedFuture(t);
        }
    }

    public CompletableFuture<ModifySubUserResponse> modifySubUser(ModifySubUserRequest request) {
        try {
            HttpResponseHandler<Response<ModifySubUserResponse>> responseHandler = protocolFactory
                    .createCombinedResponseHandler(ModifySubUserResponse.Builder::new, operationMetadata);

            return clientHandler.execute(new ClientExecutionParams<ModifySubUserRequest, ModifySubUserResponse>()
                    .withOperationName("ModifySubUser")
                    .withMarshaller(getMarshaller(ModifySubUserRequest.class))
                    .withCombinedResponseHandler(responseHandler).withInput(request));
        } catch (Throwable t) {
            return CompletableFutureUtils.failedFuture(t);
        }
    }

    public CompletableFuture<RemoveSubUserResponse> removeSubUser(RemoveSubUserRequest request) {
        try {
            HttpResponseHandler<Response<RemoveSubUserResponse>> responseHandler = protocolFactory
                    .createCombinedResponseHandler(RemoveSubUserResponse.Builder::new, operationMetadata);

            return clientHandler.execute(new ClientExecutionParams<RemoveSubUserRequest, RemoveSubUserResponse>()
                    .withOperationName("RemoveSubUser")
                    .withMarshaller(getMarshaller(RemoveSubUserRequest.class))
                    .withCombinedResponseHandler(responseHandler).withInput(request));
        } catch (Throwable t) {
            return CompletableFutureUtils.failedFuture(t);
        }
    }

    public CompletableFuture<CreateKeyResponse> createKey(CreateKeyRequest request) {
        try {
            HttpResponseHandler<Response<CreateKeyResponse>> responseHandler = protocolFactory
                    .createCombinedResponseHandler(() -> new CreateKeyResponse.Builder(request), operationMetadata);

            return clientHandler.execute(new ClientExecutionParams<CreateKeyRequest, CreateKeyResponse>()
                    .withOperationName("CreateKey")
                    .withMarshaller(getMarshaller(CreateKeyRequest.class))
                    .withCombinedResponseHandler(responseHandler).withInput(request));
        } catch (Throwable t) {
            return CompletableFutureUtils.failedFuture(t);
        }
    }

    public CompletableFuture<RemoveKeyResponse> removeKey(RemoveKeyRequest request) {
        try {
            HttpResponseHandler<Response<RemoveKeyResponse>> responseHandler = protocolFactory
                    .createCombinedResponseHandler(RemoveKeyResponse.Builder::new, operationMetadata);

            return clientHandler.execute(new ClientExecutionParams<RemoveKeyRequest, RemoveKeyResponse>()
                    .withOperationName("RemoveKey")
                    .withMarshaller(getMarshaller(RemoveKeyRequest.class))
                    .withCombinedResponseHandler(responseHandler).withInput(request));
        } catch (Throwable t) {
            return CompletableFutureUtils.failedFuture(t);
        }
    }

    public CompletableFuture<GetBucketInfoResponse> getBucketInfo(GetBucketInfoRequest request) {
        try {
            HttpResponseHandler<Response<GetBucketInfoResponse>> responseHandler = protocolFactory
                    .createCombinedResponseHandler(() -> new GetBucketInfoResponse.Builder(request), operationMetadata);

            return clientHandler.execute(new ClientExecutionParams<GetBucketInfoRequest, GetBucketInfoResponse>()
                    .withOperationName("GetBucketInfo")
                    .withMarshaller(getMarshaller(GetBucketInfoRequest.class))
                    .withCombinedResponseHandler(responseHandler).withInput(request));
        } catch (Throwable t) {
            return CompletableFutureUtils.failedFuture(t);
        }
    }

    public CompletableFuture<CheckBucketIndexResponse> checkBucketIndex(CheckBucketIndexRequest request) {
        try {
            HttpResponseHandler<Response<CheckBucketIndexResponse>> responseHandler = protocolFactory
                    .createCombinedResponseHandler(CheckBucketIndexResponse.Builder::new, operationMetadata);

            return clientHandler.execute(new ClientExecutionParams<CheckBucketIndexRequest, CheckBucketIndexResponse>()
                    .withOperationName("CheckBucketIndex")
                    .withMarshaller(getMarshaller(CheckBucketIndexRequest.class))
                    .withCombinedResponseHandler(responseHandler).withInput(request));
        } catch (Throwable t) {
            return CompletableFutureUtils.failedFuture(t);
        }
    }

    public CompletableFuture<RemoveBucketResponse> removeBucket(RemoveBucketRequest request) {
        try {
            HttpResponseHandler<Response<RemoveBucketResponse>> responseHandler = protocolFactory
                    .createCombinedResponseHandler(RemoveBucketResponse.Builder::new, operationMetadata);

            return clientHandler.execute(new ClientExecutionParams<RemoveBucketRequest, RemoveBucketResponse>()
                    .withOperationName("RemoveBucket")
                    .withMarshaller(getMarshaller(RemoveBucketRequest.class))
                    .withCombinedResponseHandler(responseHandler).withInput(request));
        } catch (Throwable t) {
            return CompletableFutureUtils.failedFuture(t);
        }
    }

    public CompletableFuture<UnlinkBucketResponse> unlinkBucket(UnlinkBucketRequest request) {
        try {
            HttpResponseHandler<Response<UnlinkBucketResponse>> responseHandler = protocolFactory
                    .createCombinedResponseHandler(UnlinkBucketResponse.Builder::new, operationMetadata);

            return clientHandler.execute(new ClientExecutionParams<UnlinkBucketRequest, UnlinkBucketResponse>()
                    .withOperationName("UnlinkBucket")
                    .withMarshaller(getMarshaller(UnlinkBucketRequest.class))
                    .withCombinedResponseHandler(responseHandler).withInput(request));
        } catch (Throwable t) {
            return CompletableFutureUtils.failedFuture(t);
        }
    }

    public CompletableFuture<LinkBucketResponse> linkBucket(LinkBucketRequest request) {
        try {
            HttpResponseHandler<Response<LinkBucketResponse>> responseHandler = protocolFactory
                    .createCombinedResponseHandler(LinkBucketResponse.Builder::new, operationMetadata);

            return clientHandler.execute(new ClientExecutionParams<LinkBucketRequest, LinkBucketResponse>()
                    .withOperationName("LinkBucket")
                    .withMarshaller(getMarshaller(LinkBucketRequest.class))
                    .withCombinedResponseHandler(responseHandler).withInput(request));
        } catch (Throwable t) {
            return CompletableFutureUtils.failedFuture(t);
        }
    }

    public CompletableFuture<RemoveObjectResponse> removeObject(RemoveObjectRequest request) {
        try {
            HttpResponseHandler<Response<RemoveObjectResponse>> responseHandler = protocolFactory
                    .createCombinedResponseHandler(RemoveObjectResponse.Builder::new, operationMetadata);

            return clientHandler.execute(new ClientExecutionParams<RemoveObjectRequest, RemoveObjectResponse>()
                    .withOperationName("RemoveObject")
                    .withMarshaller(getMarshaller(RemoveObjectRequest.class))
                    .withCombinedResponseHandler(responseHandler).withInput(request));
        } catch (Throwable t) {
            return CompletableFutureUtils.failedFuture(t);
        }
    }

    public CompletableFuture<GetPolicyResponse> getPolicy(GetPolicyRequest request) {
        try {
            HttpResponseHandler<Response<GetPolicyResponse>> responseHandler = protocolFactory
                    .createCombinedResponseHandler(GetPolicyResponse.Builder::new, operationMetadata);

            return clientHandler.execute(new ClientExecutionParams<GetPolicyRequest, GetPolicyResponse>()
                    .withOperationName("GetPolicy")
                    .withMarshaller(getMarshaller(GetPolicyRequest.class))
                    .withCombinedResponseHandler(responseHandler).withInput(request));
        } catch (Throwable t) {
            return CompletableFutureUtils.failedFuture(t);
        }
    }

    public CompletableFuture<AddUserCapabilityResponse> addUserCapability(AddUserCapabilityRequest request) {
        try {
            HttpResponseHandler<Response<AddUserCapabilityResponse>> responseHandler = protocolFactory
                    .createCombinedResponseHandler(AddUserCapabilityResponse.Builder::new, operationMetadata);

            return clientHandler.execute(new ClientExecutionParams<AddUserCapabilityRequest, AddUserCapabilityResponse>()
                    .withOperationName("AddUserCapability")
                    .withMarshaller(getMarshaller(AddUserCapabilityRequest.class))
                    .withCombinedResponseHandler(responseHandler).withInput(request));
        } catch (Throwable t) {
            return CompletableFutureUtils.failedFuture(t);
        }
    }

    public CompletableFuture<RemoveUserCapabilityResponse> removeUserCapability(RemoveUserCapabilityRequest request) {
        try {
            HttpResponseHandler<Response<RemoveUserCapabilityResponse>> responseHandler = protocolFactory
                    .createCombinedResponseHandler(RemoveUserCapabilityResponse.Builder::new, operationMetadata);

            return clientHandler.execute(new ClientExecutionParams<RemoveUserCapabilityRequest, RemoveUserCapabilityResponse>()
                    .withOperationName("RemoveUserCapability")
                    .withMarshaller(getMarshaller(RemoveUserCapabilityRequest.class))
                    .withCombinedResponseHandler(responseHandler).withInput(request));
        } catch (Throwable t) {
            return CompletableFutureUtils.failedFuture(t);
        }
    }

    public CompletableFuture<GetUserQuotaResponse> getUserQuota(GetUserQuotaRequest request) {
        try {
            HttpResponseHandler<Response<GetUserQuotaResponse>> responseHandler = protocolFactory
                    .createCombinedResponseHandler(GetUserQuotaResponse.Builder::new, operationMetadata);

            return clientHandler.execute(new ClientExecutionParams<GetUserQuotaRequest, GetUserQuotaResponse>()
                    .withOperationName("GetUserQuota")
                    .withMarshaller(getMarshaller(GetUserQuotaRequest.class))
                    .withCombinedResponseHandler(responseHandler).withInput(request));
        } catch (Throwable t) {
            return CompletableFutureUtils.failedFuture(t);
        }
    }

    public CompletableFuture<SetUserQuotaResponse> setUserQuota(SetUserQuotaRequest request) {
        try {
            HttpResponseHandler<Response<SetUserQuotaResponse>> responseHandler = protocolFactory
                    .createCombinedResponseHandler(SetUserQuotaResponse.Builder::new, operationMetadata);

            return clientHandler.execute(new ClientExecutionParams<SetUserQuotaRequest, SetUserQuotaResponse>()
                    .withOperationName("SetUserQuota")
                    .withMarshaller(getMarshaller(SetUserQuotaRequest.class))
                    .withAsyncRequestBody(AsyncRequestBody.fromBytes(OBJECT_MAPPER.writeValueAsBytes(request.getQuota())))
                    .withCombinedResponseHandler(responseHandler).withInput(request));
        } catch (Throwable t) {
            return CompletableFutureUtils.failedFuture(t);
        }
    }

    public CompletableFuture<GetBucketQuotaResponse> getBucketQuota(GetBucketQuotaRequest request) {
        try {
            HttpResponseHandler<Response<GetBucketQuotaResponse>> responseHandler = protocolFactory
                    .createCombinedResponseHandler(GetBucketQuotaResponse.Builder::new, operationMetadata);

            return clientHandler.execute(new ClientExecutionParams<GetBucketQuotaRequest, GetBucketQuotaResponse>()
                    .withOperationName("GetBucketQuota")
                    .withMarshaller(getMarshaller(GetBucketQuotaRequest.class))
                    .withCombinedResponseHandler(responseHandler).withInput(request));
        } catch (Throwable t) {
            return CompletableFutureUtils.failedFuture(t);
        }
    }

    public CompletableFuture<SetBucketQuotaResponse> setBucketQuota(SetBucketQuotaRequest request) {
        try {
            HttpResponseHandler<Response<SetBucketQuotaResponse>> responseHandler = protocolFactory
                    .createCombinedResponseHandler(SetBucketQuotaResponse.Builder::new, operationMetadata);

            return clientHandler.execute(new ClientExecutionParams<SetBucketQuotaRequest, SetBucketQuotaResponse>()
                    .withOperationName("SetBucketQuota")
                    .withMarshaller(getMarshaller(SetBucketQuotaRequest.class))
                    .withAsyncRequestBody(AsyncRequestBody.fromBytes(OBJECT_MAPPER.writeValueAsBytes(request.getQuota())))
                    .withCombinedResponseHandler(responseHandler).withInput(request));
        } catch (Throwable t) {
            return CompletableFutureUtils.failedFuture(t);
        }
    }

    @Override
    public String serviceName() {
        return "admin";
    }

    @Override
    public void close() {
        clientHandler.close();
    }
}
