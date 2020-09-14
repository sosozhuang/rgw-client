package io.ceph.rgw.client;

import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;
import io.ceph.rgw.client.model.admin.*;

/**
 * A client that provides <a href="https://docs.ceph.com/docs/master/radosgw/adminops/">admin operations</a>, sends AdminRequest and receives AdminResponse.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/2/27.
 * @see Clients#getAdmin()
 * @see AdminRequest
 * @see AdminResponse
 */
public interface AdminClient {
    /**
     * Get usage asynchronously.
     *
     * @param request the get usage request
     * @return an ActionFuture containing the get usage response
     */
    ActionFuture<GetUsageResponse> getUsageAsync(GetUsageRequest request);

    /**
     * Get usage asynchronously.
     *
     * @param request  the get usage request
     * @param listener the callback listener after action is done
     */
    void getUsageAsync(GetUsageRequest request, ActionListener<GetUsageResponse> listener);

    /**
     * Get usage synchronously.
     *
     * @param request the get usage request
     * @return the get usage response
     */
    GetUsageResponse getUsage(GetUsageRequest request);

    /**
     * Fluent api to get usage.
     *
     * @return the request builder
     */
    default GetUsageRequest.Builder prepareGetUsage() {
        return new GetUsageRequest.Builder(this);
    }

    /**
     * Trim usage asynchronously.
     *
     * @param request the trim usage request
     * @return An ActionFuture containing the trim usage response
     */
    ActionFuture<TrimUsageResponse> trimUsageAsync(TrimUsageRequest request);

    /**
     * Trim usage asynchronously.
     *
     * @param request  the trim usage request
     * @param listener the callback listener after action is done
     */
    void trimUsageAsync(TrimUsageRequest request, ActionListener<TrimUsageResponse> listener);

    /**
     * Trim usage synchronously.
     *
     * @param request the trim usage request
     * @return the trim usage response
     */
    TrimUsageResponse trimUsage(TrimUsageRequest request);

    /**
     * Fluent api to trim usage.
     *
     * @return the request builder
     */
    default TrimUsageRequest.Builder prepareTrimUsage() {
        return new TrimUsageRequest.Builder(this);
    }

    /**
     * Get user info asynchronously.
     *
     * @param request the get user info request
     * @return An ActionFuture containing the get user info response
     */
    ActionFuture<GetUserInfoResponse> getUserInfoAsync(GetUserInfoRequest request);

    /**
     * Get user info asynchronously.
     *
     * @param request  the get user info request
     * @param listener the callback listener after action is done
     */
    void getUserInfoAsync(GetUserInfoRequest request, ActionListener<GetUserInfoResponse> listener);

    /**
     * Get user info synchronously.
     *
     * @param request the get user info request
     * @return the get user info response
     */
    GetUserInfoResponse getUserInfo(GetUserInfoRequest request);

    /**
     * Fluent api to get user info.
     *
     * @return the request builder
     */
    default GetUserInfoRequest.Builder prepareGetUserInfo() {
        return new GetUserInfoRequest.Builder(this);
    }

    /**
     * Create user asynchronously.
     *
     * @param request the create user request
     * @return An ActionFuture containing the create user response
     */
    ActionFuture<CreateUserResponse> createUserAsync(CreateUserRequest request);

    /**
     * Create user asynchronously.
     *
     * @param request  the create user request
     * @param listener the callback listener after action is done
     */
    void createUserAsync(CreateUserRequest request, ActionListener<CreateUserResponse> listener);

    /**
     * Create user synchronously.
     *
     * @param request the create user request
     * @return the create user response
     */
    CreateUserResponse createUser(CreateUserRequest request);

    /**
     * Fluent api to create user.
     *
     * @return the request builder
     */
    default CreateUserRequest.Builder prepareCreateUser() {
        return new CreateUserRequest.Builder(this);
    }

    /**
     * Modify user asynchronously.
     *
     * @param request the modify user request
     * @return An ActionFuture containing the modify user response
     */
    ActionFuture<ModifyUserResponse> modifyUserAsync(ModifyUserRequest request);

    /**
     * Modify user asynchronously.
     *
     * @param request  the modify user request
     * @param listener the callback listener after action is done
     */
    void modifyUserAsync(ModifyUserRequest request, ActionListener<ModifyUserResponse> listener);

    /**
     * Modify user synchronously.
     *
     * @param request the modify user request
     * @return the modify user response
     */
    ModifyUserResponse modifyUser(ModifyUserRequest request);

    /**
     * Fluent api to modify user.
     *
     * @return the request builder
     */
    default ModifyUserRequest.Builder prepareModifyUser() {
        return new ModifyUserRequest.Builder(this);
    }

    /**
     * Remove user asynchronously.
     *
     * @param request the remove user request
     * @return An ActionFuture containing the remove user response
     */
    ActionFuture<RemoveUserResponse> removeUserAsync(RemoveUserRequest request);

    /**
     * Remove user asynchronously.
     *
     * @param request  the remove user request
     * @param listener the callback listener after action is done
     */
    void removeUserAsync(RemoveUserRequest request, ActionListener<RemoveUserResponse> listener);

    /**
     * Remove user synchronously.
     *
     * @param request the remove user request
     * @return the remove user response
     */
    RemoveUserResponse removeUser(RemoveUserRequest request);

    /**
     * Fluent api to remove user.
     *
     * @return the request builder
     */
    default RemoveUserRequest.Builder prepareRemoveUser() {
        return new RemoveUserRequest.Builder(this);
    }

    /**
     * Create sub user asynchronously.
     *
     * @param request the create sub user request
     * @return An ActionFuture containing the create sub user response
     */
    ActionFuture<CreateSubUserResponse> createSubUserAsync(CreateSubUserRequest request);

    /**
     * Create sub user asynchronously.
     *
     * @param request  the create sub user request
     * @param listener the callback listener after action is done
     */
    void createSubUserAsync(CreateSubUserRequest request, ActionListener<CreateSubUserResponse> listener);

    /**
     * Create sub user synchronously.
     *
     * @param request the create sub user request
     * @return the create sub user response
     */
    CreateSubUserResponse createSubUser(CreateSubUserRequest request);

    /**
     * Fluent api to create sub user.
     *
     * @return the request builder
     */
    default CreateSubUserRequest.Builder prepareCreateSubUser() {
        return new CreateSubUserRequest.Builder(this);
    }

    /**
     * Modify sub user asynchronously.
     *
     * @param request the modify sub user request
     * @return An ActionFuture containing the modify sub user response
     */
    ActionFuture<ModifySubUserResponse> modifySubUserAsync(ModifySubUserRequest request);

    /**
     * Modify sub user asynchronously.
     *
     * @param request  the modify sub user request
     * @param listener the callback listener after action is done
     */
    void modifySubUserAsync(ModifySubUserRequest request, ActionListener<ModifySubUserResponse> listener);

    /**
     * Modify sub user synchronously.
     *
     * @param request the modify sub user request
     * @return the modify sub user response
     */
    ModifySubUserResponse modifySubUser(ModifySubUserRequest request);

    /**
     * Fluent api to modify sub user.
     *
     * @return the request builder
     */
    default ModifySubUserRequest.Builder prepareModifySubUser() {
        return new ModifySubUserRequest.Builder(this);
    }

    /**
     * Remove sub user asynchronously.
     *
     * @param request the remove sub user request
     * @return An ActionFuture containing the remove sub user response
     */
    ActionFuture<RemoveSubUserResponse> removeSubUserAsync(RemoveSubUserRequest request);

    /**
     * Remove sub user asynchronously.
     *
     * @param request  the remove sub user request
     * @param listener the callback listener after action is done
     */
    void removeSubUserAsync(RemoveSubUserRequest request, ActionListener<RemoveSubUserResponse> listener);

    /**
     * Remove sub user synchronously.
     *
     * @param request the remove sub user request
     * @return the remove sub user response
     */
    RemoveSubUserResponse removeSubUser(RemoveSubUserRequest request);

    /**
     * Fluent api to remove sub user.
     *
     * @return the request builder
     */
    default RemoveSubUserRequest.Builder prepareRemoveSubUser() {
        return new RemoveSubUserRequest.Builder(this);
    }

    /**
     * Create key asynchronously.
     *
     * @param request the create key request
     * @return An ActionFuture containing the create key response
     */
    ActionFuture<CreateKeyResponse> createKeyAsync(CreateKeyRequest request);

    /**
     * Create key asynchronously.
     *
     * @param request  the create key request
     * @param listener the callback listener after action is done
     */
    void createKeyAsync(CreateKeyRequest request, ActionListener<CreateKeyResponse> listener);

    /**
     * Create key synchronously.
     *
     * @param request the create key request
     * @return the create key response
     */
    CreateKeyResponse createKey(CreateKeyRequest request);

    /**
     * Fluent api to create key.
     *
     * @return the request builder
     */
    default CreateKeyRequest.Builder prepareCreateKey() {
        return new CreateKeyRequest.Builder(this);
    }

    /**
     * Remove key asynchronously.
     *
     * @param request the remove key request
     * @return An ActionFuture containing the remove key response
     */
    ActionFuture<RemoveKeyResponse> removeKeyAsync(RemoveKeyRequest request);

    /**
     * Remove key asynchronously.
     *
     * @param request  the remove key request
     * @param listener the callback listener after action is done
     */
    void removeKeyAsync(RemoveKeyRequest request, ActionListener<RemoveKeyResponse> listener);

    /**
     * Remove key synchronously.
     *
     * @param request the remove key request
     * @return the remove key response
     */
    RemoveKeyResponse removeKey(RemoveKeyRequest request);

    /**
     * Fluent api to remove key.
     *
     * @return the request builder
     */
    default RemoveKeyRequest.Builder prepareRemoveKey() {
        return new RemoveKeyRequest.Builder(this);
    }

    /**
     * Get bucket info asynchronously.
     *
     * @param request the get bucket info request
     * @return An ActionFuture containing the get bucket info response
     */
    ActionFuture<GetBucketInfoResponse> getBucketInfoAsync(GetBucketInfoRequest request);

    /**
     * Get bucket info asynchronously.
     *
     * @param request  the get bucket info request
     * @param listener the callback listener after action is done
     */
    void getBucketInfoAsync(GetBucketInfoRequest request, ActionListener<GetBucketInfoResponse> listener);

    /**
     * Get bucket info synchronously.
     *
     * @param request the get bucket info request
     * @return
     */
    GetBucketInfoResponse getBucketInfo(GetBucketInfoRequest request);

    /**
     * Fluent api to get bucket info.
     *
     * @return the request builder
     */
    default GetBucketInfoRequest.Builder prepareGetBucketInfo() {
        return new GetBucketInfoRequest.Builder(this);
    }

    /**
     * Check bucket index asynchronously.
     *
     * @param request the check bucket index request
     * @return An ActionFuture containing the check bucket index response
     */
    ActionFuture<CheckBucketIndexResponse> checkBucketIndexAsync(CheckBucketIndexRequest request);

    /**
     * Check bucket index asynchronously.
     *
     * @param request  the check bucket index request
     * @param listener the callback listener after action is done
     */
    void checkBucketIndexAsync(CheckBucketIndexRequest request, ActionListener<CheckBucketIndexResponse> listener);

    /**
     * Check bucket index synchronously.
     *
     * @param request the check bucket index request
     * @return the check bucket index response
     */
    CheckBucketIndexResponse checkBucketIndex(CheckBucketIndexRequest request);

    /**
     * Fluent api to check bucket index.
     *
     * @return the request builder
     */
    default CheckBucketIndexRequest.Builder prepareCheckBucketIndex() {
        return new CheckBucketIndexRequest.Builder(this);
    }

    /**
     * Remove bucket asynchronously.
     *
     * @param request the remove bucket request
     * @return An ActionFuture containing the remove bucket response
     */
    ActionFuture<RemoveBucketResponse> removeBucketAsync(RemoveBucketRequest request);

    /**
     * Remove bucket asynchronously.
     *
     * @param request  the remove bucket request
     * @param listener the callback listener after action is done
     */
    void removeBucketAsync(RemoveBucketRequest request, ActionListener<RemoveBucketResponse> listener);

    /**
     * Remove bucket synchronously.
     *
     * @param request the remove bucket request
     * @return the remove bucket response
     */
    RemoveBucketResponse removeBucket(RemoveBucketRequest request);

    /**
     * Fluent api to remove bucket.
     *
     * @return the request builder
     */
    default RemoveBucketRequest.Builder prepareRemoveBucket() {
        return new RemoveBucketRequest.Builder(this);
    }

    /**
     * Unlink bucket asynchronously.
     *
     * @param request the unlink bucket request
     * @return An ActionFuture containing the unlink bucket response
     */
    ActionFuture<UnlinkBucketResponse> unlinkBucketAsync(UnlinkBucketRequest request);

    /**
     * Unlink bucket asynchronously.
     *
     * @param request  the unlink bucket request
     * @param listener the callback listener after action is done
     */
    void unlinkBucketAsync(UnlinkBucketRequest request, ActionListener<UnlinkBucketResponse> listener);

    /**
     * Unlink bucket synchronously.
     *
     * @param request the unlink bucket request
     * @return the unlink bucket response
     */
    UnlinkBucketResponse unlinkBucket(UnlinkBucketRequest request);

    /**
     * Fluent api to unlink bucket.
     *
     * @return the request builder
     */
    default UnlinkBucketRequest.Builder prepareUnlinkBucket() {
        return new UnlinkBucketRequest.Builder(this);
    }

    /**
     * Link bucket asynchronously.
     *
     * @param request the link bucket request
     * @return An ActionFuture containing the link bucket response
     */
    ActionFuture<LinkBucketResponse> linkBucketAsync(LinkBucketRequest request);

    /**
     * Link bucket asynchronously.
     *
     * @param request  the link bucket request
     * @param listener the callback listener after action is done
     */
    void linkBucketAsync(LinkBucketRequest request, ActionListener<LinkBucketResponse> listener);

    /**
     * Link bucket synchronously.
     *
     * @param request the link bucket request
     * @return the link bucket response
     */
    LinkBucketResponse linkBucket(LinkBucketRequest request);

    /**
     * Fluent api to link bucket.
     *
     * @return the request builder
     */
    default LinkBucketRequest.Builder prepareLinkBucket() {
        return new LinkBucketRequest.Builder(this);
    }

    /**
     * Remove object asynchronously.
     *
     * @param request the remove object request
     * @return An ActionFuture containing the remove object response
     */
    ActionFuture<RemoveObjectResponse> removeObjectAsync(RemoveObjectRequest request);

    /**
     * Remove object asynchronously.
     *
     * @param request  the remove object request
     * @param listener the callback listener after action is done
     */
    void removeObjectAsync(RemoveObjectRequest request, ActionListener<RemoveObjectResponse> listener);

    /**
     * Remove object synchronously.
     *
     * @param request the remove object request
     * @return the remove object response
     */
    RemoveObjectResponse removeObject(RemoveObjectRequest request);

    /**
     * Fluent api to remove object.
     *
     * @return the request builder
     */
    default RemoveObjectRequest.Builder prepareRemoveObject() {
        return new RemoveObjectRequest.Builder(this);
    }

    /**
     * Get policy asynchronously.
     *
     * @param request the get policy request
     * @return An ActionFuture containing the get policy response
     */
    ActionFuture<GetPolicyResponse> getPolicyAsync(GetPolicyRequest request);

    /**
     * Get policy asynchronously.
     *
     * @param request  the get policy request
     * @param listener the callback listener after action is done
     */
    void getPolicyAsync(GetPolicyRequest request, ActionListener<GetPolicyResponse> listener);

    /**
     * Get policy synchronously.
     *
     * @param request the get policy request
     * @return the get policy response
     */
    GetPolicyResponse getPolicy(GetPolicyRequest request);

    /**
     * Fluent api to get policy.
     *
     * @return the request builder
     */
    default GetPolicyRequest.Builder prepareGetPolicy() {
        return new GetPolicyRequest.Builder(this);
    }

    /**
     * Add user capability asynchronously.
     *
     * @param request the add user capability request
     * @return An ActionFuture containing the add user capability response
     */
    ActionFuture<AddUserCapabilityResponse> addUserCapabilityAsync(AddUserCapabilityRequest request);

    /**
     * Add user capability asynchronously.
     *
     * @param request  the add user capability request
     * @param listener the callback listener after action is done
     */
    void addUserCapabilityAsync(AddUserCapabilityRequest request, ActionListener<AddUserCapabilityResponse> listener);

    /**
     * Add user capability synchronously.
     *
     * @param request the add user capability request
     * @return the add user capability response
     */
    AddUserCapabilityResponse addUserCapability(AddUserCapabilityRequest request);

    /**
     * Fluent api to add user capability.
     *
     * @return the request builder
     */
    default AddUserCapabilityRequest.Builder prepareAddUserCapability() {
        return new AddUserCapabilityRequest.Builder(this);
    }

    /**
     * Remove user capability asynchronously.
     *
     * @param request the remove user capability request
     * @return An ActionFuture containing the remove user capability
     */
    ActionFuture<RemoveUserCapabilityResponse> removeUserCapabilityAsync(RemoveUserCapabilityRequest request);

    /**
     * Remove user capability asynchronously.
     *
     * @param request  the remove user capability request
     * @param listener the callback listener after action is done
     */
    void removeUserCapabilityAsync(RemoveUserCapabilityRequest request, ActionListener<RemoveUserCapabilityResponse> listener);

    /**
     * Remove user capability synchronously.
     *
     * @param request the remove user capability request
     * @return the remove user capability response
     */
    RemoveUserCapabilityResponse removeUserCapability(RemoveUserCapabilityRequest request);

    /**
     * Fluent api to remove user capability.
     *
     * @return the request builder
     */
    default RemoveUserCapabilityRequest.Builder prepareRemoveUserCapability() {
        return new RemoveUserCapabilityRequest.Builder(this);
    }

    /**
     * Get user quota asynchronously.
     *
     * @param request the get user quota request
     * @return An ActionFuture containing the get user quota response
     */
    ActionFuture<GetUserQuotaResponse> getUserQuotaAsync(GetUserQuotaRequest request);

    /**
     * Get user quota asynchronously.
     *
     * @param request  the get user quota request
     * @param listener the callback listener after action is done
     */
    void getUserQuotaAsync(GetUserQuotaRequest request, ActionListener<GetUserQuotaResponse> listener);

    /**
     * Get user quota synchronously.
     *
     * @param request the get user quota request
     * @return the get user quota response
     */
    GetUserQuotaResponse getUserQuota(GetUserQuotaRequest request);

    /**
     * Fluent api to get user quota.
     *
     * @return the request builder
     */
    default GetUserQuotaRequest.Builder prepareGetUserQuota() {
        return new GetUserQuotaRequest.Builder(this);
    }

    /**
     * Set user quota asynchronously.
     *
     * @param request the set user quota request
     * @return An ActionFuture containing the set user quota response
     */
    ActionFuture<SetUserQuotaResponse> setUserQuotaAsync(SetUserQuotaRequest request);

    /**
     * Set user quota asynchronously.
     *
     * @param request  the set user quota request
     * @param listener the callback listener after action is done
     */
    void setUserQuotaAsync(SetUserQuotaRequest request, ActionListener<SetUserQuotaResponse> listener);

    /**
     * Set user quota synchronously.
     *
     * @param request the set user quota request
     * @return the set user quota response
     */
    SetUserQuotaResponse setUserQuota(SetUserQuotaRequest request);

    /**
     * Fluent api to set user quota.
     *
     * @return the request builder
     */
    default SetUserQuotaRequest.Builder prepareSetUserQuota() {
        return new SetUserQuotaRequest.Builder(this);
    }

    /**
     * Get bucket quota asynchronously.
     *
     * @param request the get bucket quota request
     * @return An ActionFuture containing the get bucket quota response
     */
    ActionFuture<GetBucketQuotaResponse> getBucketQuotaAsync(GetBucketQuotaRequest request);

    /**
     * Get bucket quota asynchronously.
     *
     * @param request  the get bucket quota request
     * @param listener the callback listener after action is done
     */
    void getBucketQuotaAsync(GetBucketQuotaRequest request, ActionListener<GetBucketQuotaResponse> listener);

    /**
     * Get bucket quota synchronously.
     *
     * @param request the get bucket quota request
     * @return the get bucket quota response
     */
    GetBucketQuotaResponse getBucketQuota(GetBucketQuotaRequest request);

    /**
     * Fluent api to get bucket quota.
     *
     * @return the request builder
     */
    default GetBucketQuotaRequest.Builder prepareGetBucketQuota() {
        return new GetBucketQuotaRequest.Builder(this);
    }

    /**
     * Set bucket quota asynchronously.
     *
     * @param request the set bucket quota request
     * @return An ActionFuture containing the set bucket quota response
     */
    ActionFuture<SetBucketQuotaResponse> setBucketQuotaAsync(SetBucketQuotaRequest request);

    /**
     * Set bucket quota asynchronously.
     *
     * @param request  the set bucket quota request
     * @param listener the callback listener after action is done
     */
    void setBucketQuotaAsync(SetBucketQuotaRequest request, ActionListener<SetBucketQuotaResponse> listener);

    /**
     * Set bucket quota synchronously.
     *
     * @param request the set bucket quota request
     * @return the set bucket quota response
     */
    SetBucketQuotaResponse setBucketQuota(SetBucketQuotaRequest request);

    /**
     * Fluent api to set bucket quota.
     *
     * @return the request builder
     */
    default SetBucketQuotaRequest.Builder prepareSetBucketQuota() {
        return new SetBucketQuotaRequest.Builder(this);
    }
}
