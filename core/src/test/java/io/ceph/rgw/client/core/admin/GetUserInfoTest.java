package io.ceph.rgw.client.core.admin;

import io.ceph.rgw.client.exception.RGWServerException;
import io.ceph.rgw.client.model.admin.GetUserInfoResponse;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/7/31.
 */
public class GetUserInfoTest extends BaseAdminClientTest {
    @Test(expected = RGWServerException.class)
    public void testNotExists() {
        adminClient.prepareGetUserInfo()
                .withUid("not exists")
                .run();
    }

    @Test
    public void testSync() {
        GetUserInfoResponse resp = logResponse(adminClient.prepareGetUserInfo()
                .withUid(userId)
                .run());
        Assert.assertEquals(userId, resp.getUserInfo().getUserId());
    }

    @Test
    public void testCallback() throws InterruptedException {
        Latch latch = newLatch();
        adminClient.prepareGetUserInfo()
                .withUid(userId)
                .execute(newActionListener(latch));
        latch.await();
    }

    @Test
    public void testAsync() {
        GetUserInfoResponse resp = logResponse(adminClient.prepareGetUserInfo()
                .withUid(userId)
                .execute());
        Assert.assertEquals(userId, resp.getUserInfo().getUserId());
    }

    @Override
    boolean isCreateUser() {
        return true;
    }
}
