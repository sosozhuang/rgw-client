package io.ceph.rgw.client.core.admin;

import io.ceph.rgw.client.model.admin.AddUserCapabilityResponse;
import io.ceph.rgw.client.model.admin.RemoveUserCapabilityResponse;
import io.ceph.rgw.client.model.admin.UserCap;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/8/6.
 */
public class UserCapabilityTest extends BaseAdminClientTest {
    @Test
    public void testSync() {
        AddUserCapabilityResponse resp1 = logResponse(adminClient.prepareAddUserCapability()
                .withUid(userId)
                .addUserCap(UserCap.Type.ZONE, UserCap.Perm.WRITE)
                .addUserCap(UserCap.Type.ZONE, UserCap.Perm.READ)
                .addUserCap(UserCap.Type.USERS, UserCap.Perm.READ)
                .run());
        Assert.assertEquals(2, resp1.getUserCaps().size());
        List<UserCap.Type> types = resp1.getUserCaps().stream().map(UserCap::getType).collect(Collectors.toList());
        Assert.assertTrue(types.contains(UserCap.Type.ZONE));
        Assert.assertTrue(types.contains(UserCap.Type.USERS));
        for (UserCap userCap : resp1.getUserCaps()) {
            if (userCap.getType() == UserCap.Type.ZONE) {
                Assert.assertEquals(UserCap.Perm.READ_WRITE, userCap.getPerm());
            } else if (userCap.getType() == UserCap.Type.USERS) {
                Assert.assertEquals(UserCap.Perm.READ, userCap.getPerm());
            }
        }
        RemoveUserCapabilityResponse resp2 = logResponse(adminClient.prepareRemoveUserCapability()
                .withUid(userId)
                .addUserCap(UserCap.Type.ZONE, UserCap.Perm.WRITE)
                .addUserCap(UserCap.Type.USERS, UserCap.Perm.READ)
                .run());
        Assert.assertEquals(1, resp2.getUserCaps().size());
        Assert.assertEquals(UserCap.Type.ZONE, resp2.getUserCaps().get(0).getType());
        Assert.assertEquals(UserCap.Perm.READ, resp2.getUserCaps().get(0).getPerm());
    }

    @Test
    public void testCallback() throws InterruptedException {
        Latch latch = newLatch();
        adminClient.prepareAddUserCapability()
                .withUid(userId)
                .addUserCap(UserCap.Type.BUCKETS, UserCap.Perm.READ_WRITE)
                .addUserCap(UserCap.Type.USAGE, UserCap.Perm.READ)
                .addUserCap(UserCap.Type.USERS, UserCap.Perm.WRITE)
                .execute(newActionListener(latch));
        latch.await();
    }

    @Test
    public void testAsync() {
        logResponse(adminClient.prepareAddUserCapability()
                .withUid(userId)
                .addUserCap(UserCap.Type.METADATA, UserCap.Perm.WRITE)
                .addUserCap(UserCap.Type.ZONE, UserCap.Perm.READ_WRITE)
                .addUserCap(UserCap.Type.USERS, UserCap.Perm.READ_WRITE)
                .execute());
    }

    @Override
    boolean isCreateUser() {
        return true;
    }
}
