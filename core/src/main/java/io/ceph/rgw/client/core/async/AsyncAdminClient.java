package io.ceph.rgw.client.core.async;

import io.ceph.rgw.client.AdminClient;
import io.ceph.rgw.client.action.ActionExecutor;
import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;
import io.ceph.rgw.client.core.Connectors;
import io.ceph.rgw.client.model.admin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

/**
 * An Async implementation of AdminClient.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/3/16.
 */
public class AsyncAdminClient extends AsyncConnectorAware<AdminAsyncConnector> implements AdminClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncAdminClient.class);

    public AsyncAdminClient(Connectors<AdminAsyncConnector> connectors, ActionExecutor executor) {
        super(connectors, executor);
    }

    @Override
    public ActionFuture<GetUsageResponse> getUsageAsync(GetUsageRequest request) {
        return executor.execute(() -> doGetUsage(request));
    }

    @Override
    public void getUsageAsync(GetUsageRequest request, ActionListener<GetUsageResponse> listener) {
        executor.execute(() -> doGetUsage(request), listener);
    }

    @Override
    public GetUsageResponse getUsage(GetUsageRequest request) {
        return getUsageAsync(request).actionGet();
    }

    private CompletableFuture<GetUsageResponse> doGetUsage(GetUsageRequest request) {
        return doAction(connector -> connector.getUsage(request));
    }

    @Override
    public ActionFuture<TrimUsageResponse> trimUsageAsync(TrimUsageRequest request) {
        return executor.execute(() -> doTrimUsage(request));
    }

    @Override
    public void trimUsageAsync(TrimUsageRequest request, ActionListener<TrimUsageResponse> listener) {
        executor.execute(() -> doTrimUsage(request), listener);
    }

    @Override
    public TrimUsageResponse trimUsage(TrimUsageRequest request) {
        return trimUsageAsync(request).actionGet();
    }

    private CompletableFuture<TrimUsageResponse> doTrimUsage(TrimUsageRequest request) {
        return doAction(connector -> connector.trimUsage(request));
    }

    @Override
    public ActionFuture<GetUserInfoResponse> getUserInfoAsync(GetUserInfoRequest request) {
        return executor.execute(() -> doGetUserInfo(request));
    }

    @Override
    public void getUserInfoAsync(GetUserInfoRequest request, ActionListener<GetUserInfoResponse> listener) {
        executor.execute(() -> doGetUserInfo(request), listener);
    }

    @Override
    public GetUserInfoResponse getUserInfo(GetUserInfoRequest request) {
        return getUserInfoAsync(request).actionGet();
    }

    private CompletableFuture<GetUserInfoResponse> doGetUserInfo(GetUserInfoRequest request) {
        return doAction(connector -> connector.getUserInfo(request));
    }

    @Override
    public ActionFuture<CreateUserResponse> createUserAsync(CreateUserRequest request) {
        return executor.execute(() -> doCreateUser(request));
    }

    @Override
    public void createUserAsync(CreateUserRequest request, ActionListener<CreateUserResponse> listener) {
        executor.execute(() -> doCreateUser(request), listener);
    }

    @Override
    public CreateUserResponse createUser(CreateUserRequest request) {
        return createUserAsync(request).actionGet();
    }

    private CompletableFuture<CreateUserResponse> doCreateUser(CreateUserRequest request) {
        return doAction(connector -> connector.createUser(request));
    }

    @Override
    public ActionFuture<ModifyUserResponse> modifyUserAsync(ModifyUserRequest request) {
        return executor.execute(() -> doModifyUser(request));
    }

    @Override
    public void modifyUserAsync(ModifyUserRequest request, ActionListener<ModifyUserResponse> listener) {
        executor.execute(() -> doModifyUser(request), listener);
    }

    @Override
    public ModifyUserResponse modifyUser(ModifyUserRequest request) {
        return modifyUserAsync(request).actionGet();
    }

    private CompletableFuture<ModifyUserResponse> doModifyUser(ModifyUserRequest request) {
        return doAction(connector -> connector.modifyUser(request));
    }

    @Override
    public ActionFuture<RemoveUserResponse> removeUserAsync(RemoveUserRequest request) {
        return executor.execute(() -> doRemoveUser(request));
    }

    @Override
    public void removeUserAsync(RemoveUserRequest request, ActionListener<RemoveUserResponse> listener) {
        executor.execute(() -> doRemoveUser(request), listener);
    }

    @Override
    public RemoveUserResponse removeUser(RemoveUserRequest request) {
        return removeUserAsync(request).actionGet();
    }

    private CompletableFuture<RemoveUserResponse> doRemoveUser(RemoveUserRequest request) {
        return doAction(connector -> connector.removeUser(request));
    }

    @Override
    public ActionFuture<CreateSubUserResponse> createSubUserAsync(CreateSubUserRequest request) {
        return executor.execute(() -> doCreateSubUser(request));
    }

    @Override
    public void createSubUserAsync(CreateSubUserRequest request, ActionListener<CreateSubUserResponse> listener) {
        executor.execute(() -> doCreateSubUser(request), listener);
    }

    @Override
    public CreateSubUserResponse createSubUser(CreateSubUserRequest request) {
        return createSubUserAsync(request).actionGet();
    }

    private CompletableFuture<CreateSubUserResponse> doCreateSubUser(CreateSubUserRequest request) {
        return doAction(connector -> connector.createSubUser(request));
    }

    @Override
    public ActionFuture<ModifySubUserResponse> modifySubUserAsync(ModifySubUserRequest request) {
        return executor.execute(() -> doModifySubUser(request));
    }

    @Override
    public void modifySubUserAsync(ModifySubUserRequest request, ActionListener<ModifySubUserResponse> listener) {
        executor.execute(() -> doModifySubUser(request), listener);
    }

    @Override
    public ModifySubUserResponse modifySubUser(ModifySubUserRequest request) {
        return modifySubUserAsync(request).actionGet();
    }

    private CompletableFuture<ModifySubUserResponse> doModifySubUser(ModifySubUserRequest request) {
        return doAction(connector -> connector.modifySubUser(request));
    }

    @Override
    public ActionFuture<RemoveSubUserResponse> removeSubUserAsync(RemoveSubUserRequest request) {
        return executor.execute(() -> doRemoveSubUser(request));
    }

    @Override
    public void removeSubUserAsync(RemoveSubUserRequest request, ActionListener<RemoveSubUserResponse> listener) {
        executor.execute(() -> doRemoveSubUser(request), listener);
    }

    @Override
    public RemoveSubUserResponse removeSubUser(RemoveSubUserRequest request) {
        return removeSubUserAsync(request).actionGet();
    }

    private CompletableFuture<RemoveSubUserResponse> doRemoveSubUser(RemoveSubUserRequest request) {
        return doAction(connector -> connector.removeSubUser(request));
    }

    @Override
    public ActionFuture<CreateKeyResponse> createKeyAsync(CreateKeyRequest request) {
        return executor.execute(() -> doCreateKey(request));
    }

    @Override
    public void createKeyAsync(CreateKeyRequest request, ActionListener<CreateKeyResponse> listener) {
        executor.execute(() -> doCreateKey(request), listener);
    }

    @Override
    public CreateKeyResponse createKey(CreateKeyRequest request) {
        return createKeyAsync(request).actionGet();
    }

    private CompletableFuture<CreateKeyResponse> doCreateKey(CreateKeyRequest request) {
        return doAction(connector -> connector.createKey(request));
    }

    @Override
    public ActionFuture<RemoveKeyResponse> removeKeyAsync(RemoveKeyRequest request) {
        return executor.execute(() -> doRemoveKey(request));
    }

    @Override
    public void removeKeyAsync(RemoveKeyRequest request, ActionListener<RemoveKeyResponse> listener) {
        executor.execute(() -> doRemoveKey(request), listener);
    }

    @Override
    public RemoveKeyResponse removeKey(RemoveKeyRequest request) {
        return removeKeyAsync(request).actionGet();
    }

    private CompletableFuture<RemoveKeyResponse> doRemoveKey(RemoveKeyRequest request) {
        return doAction(connector -> connector.removeKey(request));
    }

    @Override
    public ActionFuture<GetBucketInfoResponse> getBucketInfoAsync(GetBucketInfoRequest request) {
        return executor.execute(() -> doGetBucketInfo(request));
    }

    @Override
    public void getBucketInfoAsync(GetBucketInfoRequest request, ActionListener<GetBucketInfoResponse> listener) {
        executor.execute(() -> doGetBucketInfo(request), listener);
    }

    @Override
    public GetBucketInfoResponse getBucketInfo(GetBucketInfoRequest request) {
        return getBucketInfoAsync(request).actionGet();
    }

    private CompletableFuture<GetBucketInfoResponse> doGetBucketInfo(GetBucketInfoRequest request) {
        return doAction(connector -> connector.getBucketInfo(request));
    }

    @Override
    public ActionFuture<CheckBucketIndexResponse> checkBucketIndexAsync(CheckBucketIndexRequest request) {
        return executor.execute(() -> doCheckBucketIndex(request));
    }

    @Override
    public void checkBucketIndexAsync(CheckBucketIndexRequest request, ActionListener<CheckBucketIndexResponse> listener) {
        executor.execute(() -> doCheckBucketIndex(request), listener);
    }

    @Override
    public CheckBucketIndexResponse checkBucketIndex(CheckBucketIndexRequest request) {
        return checkBucketIndexAsync(request).actionGet();
    }

    private CompletableFuture<CheckBucketIndexResponse> doCheckBucketIndex(CheckBucketIndexRequest request) {
        return doAction(connector -> connector.checkBucketIndex(request));
    }

    @Override
    public ActionFuture<RemoveBucketResponse> removeBucketAsync(RemoveBucketRequest request) {
        return executor.execute(() -> doRemoveBucket(request));
    }

    @Override
    public void removeBucketAsync(RemoveBucketRequest request, ActionListener<RemoveBucketResponse> listener) {
        executor.execute(() -> doRemoveBucket(request), listener);
    }

    @Override
    public RemoveBucketResponse removeBucket(RemoveBucketRequest request) {
        return removeBucketAsync(request).actionGet();
    }

    private CompletableFuture<RemoveBucketResponse> doRemoveBucket(RemoveBucketRequest request) {
        return doAction(connector -> connector.removeBucket(request));
    }

    @Override
    public ActionFuture<UnlinkBucketResponse> unlinkBucketAsync(UnlinkBucketRequest request) {
        return executor.execute(() -> doUnlinkBucket(request));
    }

    @Override
    public void unlinkBucketAsync(UnlinkBucketRequest request, ActionListener<UnlinkBucketResponse> listener) {
        executor.execute(() -> doUnlinkBucket(request), listener);
    }

    @Override
    public UnlinkBucketResponse unlinkBucket(UnlinkBucketRequest request) {
        return unlinkBucketAsync(request).actionGet();
    }

    private CompletableFuture<UnlinkBucketResponse> doUnlinkBucket(UnlinkBucketRequest request) {
        return doAction(connector -> connector.unlinkBucket(request));
    }

    @Override
    public ActionFuture<LinkBucketResponse> linkBucketAsync(LinkBucketRequest request) {
        return executor.execute(() -> doLinkBucket(request));
    }

    @Override
    public void linkBucketAsync(LinkBucketRequest request, ActionListener<LinkBucketResponse> listener) {
        executor.execute(() -> doLinkBucket(request), listener);
    }

    @Override
    public LinkBucketResponse linkBucket(LinkBucketRequest request) {
        return linkBucketAsync(request).actionGet();
    }

    private CompletableFuture<LinkBucketResponse> doLinkBucket(LinkBucketRequest request) {
        return doAction(connector -> connector.linkBucket(request));
    }

    @Override
    public ActionFuture<RemoveObjectResponse> removeObjectAsync(RemoveObjectRequest request) {
        return executor.execute(() -> doRemoveObject(request));
    }

    @Override
    public void removeObjectAsync(RemoveObjectRequest request, ActionListener<RemoveObjectResponse> listener) {
        executor.execute(() -> doRemoveObject(request), listener);
    }

    @Override
    public RemoveObjectResponse removeObject(RemoveObjectRequest request) {
        return removeObjectAsync(request).actionGet();
    }

    private CompletableFuture<RemoveObjectResponse> doRemoveObject(RemoveObjectRequest request) {
        return doAction(connector -> connector.removeObject(request));
    }

    @Override
    public ActionFuture<GetPolicyResponse> getPolicyAsync(GetPolicyRequest request) {
        return executor.execute(() -> doGetPolicy(request));
    }

    @Override
    public void getPolicyAsync(GetPolicyRequest request, ActionListener<GetPolicyResponse> listener) {
        executor.execute(() -> doGetPolicy(request), listener);
    }

    @Override
    public GetPolicyResponse getPolicy(GetPolicyRequest request) {
        return getPolicyAsync(request).actionGet();
    }

    private CompletableFuture<GetPolicyResponse> doGetPolicy(GetPolicyRequest request) {
        return doAction(connector -> connector.getPolicy(request));
    }

    @Override
    public ActionFuture<AddUserCapabilityResponse> addUserCapabilityAsync(AddUserCapabilityRequest request) {
        return executor.execute(() -> doAddUserCapability(request));
    }

    @Override
    public void addUserCapabilityAsync(AddUserCapabilityRequest request, ActionListener<AddUserCapabilityResponse> listener) {
        executor.execute(() -> doAddUserCapability(request), listener);
    }

    @Override
    public AddUserCapabilityResponse addUserCapability(AddUserCapabilityRequest request) {
        return addUserCapabilityAsync(request).actionGet();
    }

    private CompletableFuture<AddUserCapabilityResponse> doAddUserCapability(AddUserCapabilityRequest request) {
        return doAction(connector -> connector.addUserCapability(request));
    }

    @Override
    public ActionFuture<RemoveUserCapabilityResponse> removeUserCapabilityAsync(RemoveUserCapabilityRequest request) {
        return executor.execute(() -> doRemoveUserCapability(request));
    }

    @Override
    public void removeUserCapabilityAsync(RemoveUserCapabilityRequest request, ActionListener<RemoveUserCapabilityResponse> listener) {
        executor.execute(() -> doRemoveUserCapability(request), listener);
    }

    @Override
    public RemoveUserCapabilityResponse removeUserCapability(RemoveUserCapabilityRequest request) {
        return removeUserCapabilityAsync(request).actionGet();
    }

    private CompletableFuture<RemoveUserCapabilityResponse> doRemoveUserCapability(RemoveUserCapabilityRequest request) {
        return doAction(connector -> connector.removeUserCapability(request));
    }

    @Override
    public ActionFuture<GetUserQuotaResponse> getUserQuotaAsync(GetUserQuotaRequest request) {
        return executor.execute(() -> doGetUserQuota(request));
    }

    @Override
    public void getUserQuotaAsync(GetUserQuotaRequest request, ActionListener<GetUserQuotaResponse> listener) {
        executor.execute(() -> doGetUserQuota(request), listener);
    }

    @Override
    public GetUserQuotaResponse getUserQuota(GetUserQuotaRequest request) {
        return getUserQuotaAsync(request).actionGet();
    }

    private CompletableFuture<GetUserQuotaResponse> doGetUserQuota(GetUserQuotaRequest request) {
        return doAction(connector -> connector.getUserQuota(request));
    }

    @Override
    public ActionFuture<SetUserQuotaResponse> setUserQuotaAsync(SetUserQuotaRequest request) {
        return executor.execute(() -> doSetUserQuota(request));
    }

    @Override
    public void setUserQuotaAsync(SetUserQuotaRequest request, ActionListener<SetUserQuotaResponse> listener) {
        executor.execute(() -> doSetUserQuota(request), listener);
    }

    @Override
    public SetUserQuotaResponse setUserQuota(SetUserQuotaRequest request) {
        return setUserQuotaAsync(request).actionGet();
    }

    private CompletableFuture<SetUserQuotaResponse> doSetUserQuota(SetUserQuotaRequest request) {
        return doAction(connector -> connector.setUserQuota(request));
    }

    @Override
    public ActionFuture<GetBucketQuotaResponse> getBucketQuotaAsync(GetBucketQuotaRequest request) {
        return executor.execute(() -> doGetBucketQuota(request));
    }

    @Override
    public void getBucketQuotaAsync(GetBucketQuotaRequest request, ActionListener<GetBucketQuotaResponse> listener) {
        executor.execute(() -> doGetBucketQuota(request), listener);
    }

    @Override
    public GetBucketQuotaResponse getBucketQuota(GetBucketQuotaRequest request) {
        return getBucketQuotaAsync(request).actionGet();
    }

    private CompletableFuture<GetBucketQuotaResponse> doGetBucketQuota(GetBucketQuotaRequest request) {
        return doAction(connector -> connector.getBucketQuota(request));
    }

    @Override
    public ActionFuture<SetBucketQuotaResponse> setBucketQuotaAsync(SetBucketQuotaRequest request) {
        return executor.execute(() -> doSetBucketQuota(request));
    }

    @Override
    public void setBucketQuotaAsync(SetBucketQuotaRequest request, ActionListener<SetBucketQuotaResponse> listener) {
        executor.execute(() -> doSetBucketQuota(request), listener);
    }

    @Override
    public SetBucketQuotaResponse setBucketQuota(SetBucketQuotaRequest request) {
        return setBucketQuotaAsync(request).actionGet();
    }

    private CompletableFuture<SetBucketQuotaResponse> doSetBucketQuota(SetBucketQuotaRequest request) {
        return doAction(connector -> connector.setBucketQuota(request));
    }
}
