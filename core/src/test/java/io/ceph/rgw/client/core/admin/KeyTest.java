package io.ceph.rgw.client.core.admin;

import io.ceph.rgw.client.exception.InvalidAccessKeyException;
import io.ceph.rgw.client.exception.RGWServerException;
import io.ceph.rgw.client.model.admin.CreateKeyResponse;
import io.ceph.rgw.client.model.admin.GetUserInfoResponse;
import io.ceph.rgw.client.model.admin.KeyType;
import io.ceph.rgw.client.model.admin.S3Credential;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/8/5.
 */
public class KeyTest extends BaseAdminClientTest {
    private static final String AKEY = "a";
    private static final String SKEY = "s";

    @Test(expected = RGWServerException.class)
    public void testInvalidKey() {
        try {
            adminClient.prepareRemoveKey()
                    .withUid(userId)
                    .withKeyType(KeyType.S3)
                    .withAccessKey(AKEY)
                    .run();
        } catch (Exception e) {
            Assert.assertTrue(e.getCause() instanceof InvalidAccessKeyException);
            throw e;
        }
    }

    @Test
    public void testSync() {
        CreateKeyResponse resp1 = logResponse(adminClient.prepareCreateKey()
                .withUid(userId)
                .withKeyType(KeyType.S3)
                .withAccessKey(AKEY)
                .withSecretKey(SKEY)
                .withGenerateKey(Boolean.FALSE)
                .run());
        Assert.assertEquals(2, resp1.getS3Credentials().size());
        S3Credential credential = resp1.getS3Credentials().stream().filter(s3Credential -> s3Credential.getAccessKey().equals(AKEY)).findFirst().orElse(null);
        Assert.assertEquals(AKEY, credential.getAccessKey());
        Assert.assertEquals(SKEY, credential.getSecretKey());
        logResponse(adminClient.prepareRemoveKey()
                .withUid(userId)
                .withKeyType(KeyType.S3)
                .withAccessKey(AKEY)
                .run());
        GetUserInfoResponse resp2 = logResponse(adminClient.prepareGetUserInfo()
                .withUid(userId)
                .run());
        Assert.assertEquals(1, resp2.getUserInfo().getS3Credentials().size());
    }

    @Test
    public void testCallback() throws InterruptedException {
        Latch latch = newLatch();
        adminClient.prepareCreateKey()
                .withUid(userId)
                .withSubUser(subUserId)
                .withKeyType(KeyType.SWIFT)
                .withGenerateKey(Boolean.TRUE)
                .execute(newActionListener(latch));
        latch.await();
        GetUserInfoResponse resp2 = logResponse(adminClient.prepareGetUserInfo()
                .withUid(userId)
                .run());
        Assert.assertEquals(1, resp2.getUserInfo().getSwiftCredentials().size());

        logResponse(adminClient.prepareRemoveKey()
                .withUid(userId)
                .withKeyType(KeyType.SWIFT)
                .withSubUser(subUserId)
                .run());
    }

    @Test
    public void testAsync() {
        CreateKeyResponse resp1 = logResponse(adminClient.prepareCreateKey()
                .withUid(userId)
                .withKeyType(KeyType.S3)
                .withAccessKey(AKEY)
                .withSecretKey(SKEY)
                .withGenerateKey(Boolean.FALSE)
                .execute());
        Assert.assertEquals(2, resp1.getS3Credentials().size());
        S3Credential credential = resp1.getS3Credentials().stream().filter(s3Credential -> s3Credential.getAccessKey().equals(AKEY)).findFirst().orElse(null);
        Assert.assertEquals(AKEY, credential.getAccessKey());
        Assert.assertEquals(SKEY, credential.getSecretKey());
        logResponse(adminClient.prepareRemoveKey()
                .withUid("admin")
                .withKeyType(KeyType.S3)
                .withAccessKey("a")
                .run());
    }

    @Override
    boolean isCreateUser() {
        return true;
    }

    @Override
    boolean isCreateSubUser() {
        return true;
    }
}
