package io.ceph.rgw.client.core.admin;

import io.ceph.rgw.client.model.admin.CreateSubUserResponse;
import io.ceph.rgw.client.model.admin.KeyType;
import io.ceph.rgw.client.model.admin.ModifySubUserResponse;
import io.ceph.rgw.client.model.admin.SubUser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/8/5.
 */
@Category(AdminTests.class)
public class SubUserTest extends BaseAdminClientTest {
    @Test
    public void testSync() {
        String subUserId = "subuser-" + System.currentTimeMillis();
        CreateSubUserResponse resp1 = logResponse(adminClient.prepareCreateSubUser()
                .withUid(userId)
                .withSubUser(subUserId)
                .withKeyType(KeyType.S3)
                .withAccess(SubUser.Permission.READ)
                .withGenerateSecret(Boolean.TRUE)
                .run());
        Assert.assertEquals(1, resp1.getSubUsers().size());
        SubUser subUser = resp1.getSubUsers().get(0);
        Assert.assertEquals(subUserId, subUser.getId().split(":")[1]);
        Assert.assertEquals(SubUser.Permission.READ, subUser.getPermission());
        ModifySubUserResponse resp2 = logResponse(adminClient.prepareModifySubUser()
                .withUid(userId)
                .withSubUser(subUserId)
                .withSecret("s")
//                .withAccess(SubUser.Permission.NONE)
                .run());
        Assert.assertEquals(1, resp2.getSubUsers().size());
        subUser = resp2.getSubUsers().get(0);
        Assert.assertEquals(SubUser.Permission.NONE, subUser.getPermission());
        logResponse(adminClient.prepareRemoveSubUser()
                .withUid(userId)
                .withSubUser(subUserId)
                .withPurgeKeys(Boolean.TRUE)
                .run());
    }

    @Test
    public void testCallback() throws InterruptedException {
        Latch latch = newLatch();
        adminClient.prepareCreateKey()
                .withUid(userId)
                .withKeyType(KeyType.S3)
                .withAccessKey("a")
                .withSecretKey("s")
                .withGenerateKey(Boolean.FALSE)
                .execute(newActionListener(latch));
        latch.await();
        logResponse(adminClient.prepareRemoveKey()
                .withUid(userId)
                .withKeyType(KeyType.S3)
                .withAccessKey("a")
                .run());
    }

    @Test
    public void testAsync() {
        String subUserId = "subuser-" + System.currentTimeMillis();
        CreateSubUserResponse resp = logResponse(adminClient.prepareCreateSubUser()
                .withUid(userId)
                .withSubUser(subUserId)
                .withKeyType(KeyType.S3)
                .withAccess(SubUser.Permission.READ)
                .withGenerateSecret(Boolean.TRUE)
                .execute());
        Assert.assertEquals(1, resp.getSubUsers().size());
        SubUser subUser = resp.getSubUsers().get(0);
        Assert.assertEquals(subUserId, subUser.getId().split(":")[1]);
        Assert.assertEquals(SubUser.Permission.READ, subUser.getPermission());
        logResponse(adminClient.prepareRemoveSubUser()
                .withUid(userId)
                .withSubUser(subUserId)
                .withPurgeKeys(Boolean.TRUE)
                .execute());
    }

    @Override
    boolean isCreateUser() {
        return true;
    }
}
