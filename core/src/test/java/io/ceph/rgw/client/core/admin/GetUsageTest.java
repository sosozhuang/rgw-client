package io.ceph.rgw.client.core.admin;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Date;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/7/31.
 */
@Category(AdminTests.class)
public class GetUsageTest extends BaseAdminClientTest {
    @Test
    public void testSync() {
        logResponse(adminClient.prepareGetUsage()
                .withStart(new Date(System.currentTimeMillis() - 360 * 24 * 3600))
                .withEnd(new Date(System.currentTimeMillis()))
                .withShowEntries(Boolean.TRUE)
                .withShowSummary(Boolean.TRUE)
                .run());
    }

    @Test
    public void testCallback() throws InterruptedException {
        Latch latch = newLatch();
        adminClient.prepareGetUsage()
                .withUid(userId)
                .withShowEntries(Boolean.TRUE)
                .withShowSummary(Boolean.TRUE)
                .execute(newActionListener(latch));
        latch.await();
    }

    @Test
    public void testAsync() {
        logResponse(adminClient.prepareGetUsage()
                .withUid(userId)
                .withShowEntries(Boolean.TRUE)
                .withShowSummary(Boolean.TRUE)
                .execute());
    }

    @Override
    boolean isCreateUser() {
        return true;
    }
}
