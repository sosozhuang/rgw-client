package io.ceph.rgw.client.core.admin;

import io.ceph.rgw.client.model.admin.ModifyUserResponse;
import io.ceph.rgw.client.model.admin.UserCap;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/7/31.
 */
@Category(AdminTests.class)
public class ModifyUserTest extends BaseAdminClientTest {
    @Test
    public void testSync() {
        ModifyUserResponse resp = logResponse(adminClient.prepareModifyUser()
                .withUid(userId)
                .withDisplayName("modified")
                .run());
        Assert.assertEquals("modified", resp.getUserInfo().getDisplayName());
    }

    @Test
    public void testCallback() throws InterruptedException {
        Latch latch = newLatch();
        adminClient.prepareModifyUser()
                .withUid(userId)
                .withDisplayName("modified")
                .addUserCap(UserCap.Type.ZONE, UserCap.Perm.WRITE)
                .execute(newActionListener(latch));
        latch.await();
    }

    @Test
    public void testAsync() {
        logResponse(adminClient.prepareModifyUser()
                .withUid(userId)
                .withAccessKey("a")
                .withSecretKey("s")
                .execute());
    }

    @Override
    boolean isCreateUser() {
        return true;
    }
}
