package io.ceph.rgw.client.core.admin;

import io.ceph.rgw.client.model.admin.GetUserQuotaResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/7/31.
 */
@Category(AdminTests.class)
public class UserQuotaTest extends BaseAdminClientTest {
    @Test
    public void testSync() {
        GetUserQuotaResponse resp1 = logResponse(adminClient.prepareGetUserQuota()
                .withUid(userId)
                .run());
        Assert.assertFalse(resp1.getQuota().isEnabled());
        logResponse(adminClient.prepareSetUserQuota()
                .withUid(userId)
                .withEnabled(Boolean.TRUE)
                .withCheckOnRaw(Boolean.TRUE)
                .withMaxSize(1234)
                .withMaxObjects(9999)
                .run());
        resp1 = logResponse(adminClient.prepareGetUserQuota()
                .withUid(userId)
                .run());
        Assert.assertTrue(resp1.getQuota().isEnabled());
        Assert.assertTrue(resp1.getQuota().isCheckOnRaw());
        Assert.assertEquals(1234, (int) resp1.getQuota().getMaxSize());
        Assert.assertEquals(9999, (int) resp1.getQuota().getMaxObjects());
    }

    @Test
    public void testCallback() throws InterruptedException {
        Latch latch = newLatch();
        adminClient.prepareGetUserQuota()
                .withUid(userId)
                .execute(newActionListener(latch));
        latch.await();
        latch = newLatch();
        adminClient.prepareSetUserQuota()
                .withUid(userId)
                .withEnabled(Boolean.TRUE)
                .withCheckOnRaw(Boolean.TRUE)
                .withMaxSize(1234)
                .withMaxObjects(9999)
                .execute(newActionListener(latch));
        latch.await();
    }

    @Test
    public void testAsync() {
        GetUserQuotaResponse resp1 = logResponse(adminClient.prepareGetUserQuota()
                .withUid(userId)
                .execute());
        Assert.assertFalse(resp1.getQuota().isEnabled());
        logResponse(adminClient.prepareSetUserQuota()
                .withUid(userId)
                .withEnabled(Boolean.TRUE)
                .withCheckOnRaw(Boolean.TRUE)
                .withMaxSize(1234)
                .withMaxObjects(9999)
                .execute());
        resp1 = logResponse(adminClient.prepareGetUserQuota()
                .withUid(userId)
                .execute());
        Assert.assertTrue(resp1.getQuota().isEnabled());
        Assert.assertTrue(resp1.getQuota().isCheckOnRaw());
        Assert.assertEquals(1234, (int) resp1.getQuota().getMaxSize());
        Assert.assertEquals(9999, (int) resp1.getQuota().getMaxObjects());
    }

    @Override
    boolean isCreateUser() {
        return true;
    }
}
