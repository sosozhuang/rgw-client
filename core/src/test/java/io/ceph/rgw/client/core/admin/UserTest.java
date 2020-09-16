package io.ceph.rgw.client.core.admin;

import io.ceph.rgw.client.exception.NoSuchUserException;
import io.ceph.rgw.client.model.admin.CreateUserResponse;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/8/5.
 */
public class UserTest extends BaseAdminClientTest {
    @Test
    public void testSync() {
        String uid = "zhuangshuo";
        String name = "Zhuang Shuo";
        int maxBuckets = 9999;
        CreateUserResponse resp1 = logResponse(adminClient.prepareCreateUser()
                .withUid(uid)
                .withDisplayName(name)
                .withMaxBuckets(maxBuckets)
                .withGenerateKey(Boolean.TRUE)
                .run());
        Assert.assertEquals(uid, resp1.getUserInfo().getUserId());
        Assert.assertEquals(name, resp1.getUserInfo().getDisplayName());
        Assert.assertEquals(9999, (int) resp1.getUserInfo().getMaxBuckets());
        logResponse(adminClient.prepareRemoveUser()
                .withUid(uid)
                .withPurgeData(Boolean.TRUE)
                .run());
        try {
            adminClient.prepareGetUserInfo()
                    .withUid(uid)
                    .run();
        } catch (Exception e) {
            Assert.assertTrue(e.getCause() instanceof NoSuchUserException);
        }
    }

    @Test
    public void testCallback() throws InterruptedException {
        Latch latch = newLatch();
        adminClient.prepareCreateUser()
                .withUid("zhuangshuo")
                .withDisplayName("Zhuang Shuo")
                .withEmail("zhuangshuo@ceph.io")
                .withGenerateKey(Boolean.TRUE)
                .execute(newActionListener(latch));
        latch.await();
        logResponse(adminClient.prepareRemoveUser()
                .withUid("testuser")
                .withPurgeData(Boolean.TRUE)
                .run());
    }

    @Test
    public void testAsync() {
        logResponse(adminClient.prepareCreateUser()
                .withUid("zhuangshuo")
                .withDisplayName("Zhuang Shuo")
                .withEmail("zhuangshuo@ceph.io")
                .withGenerateKey(Boolean.TRUE)
                .execute());
        logResponse(adminClient.prepareRemoveUser()
                .withUid("testuser")
                .withPurgeData(Boolean.TRUE)
                .run());
    }

}
