package io.ceph.rgw.client.core.admin;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Date;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/7/31.
 */
@Category(AdminTests.class)
public class TrimUsageTest extends BaseAdminClientTest {
    @Test
    public void testSync() {
        logResponse(adminClient.prepareTrimUsage()
                .withStart(new Date(System.currentTimeMillis() - 360 * 24 * 3600))
                .withEnd(new Date(System.currentTimeMillis()))
                .withRemoveAll(Boolean.TRUE)
                .run());
    }

    @Test
    public void testCallback() throws InterruptedException {
        Latch latch = newLatch();
        adminClient.prepareTrimUsage()
                .withStart(new Date(System.currentTimeMillis() - 360 * 24 * 3600))
                .withEnd(new Date(System.currentTimeMillis()))
                .execute(newActionListener(latch));
        latch.await();
    }

    @Test
    public void testAsync() {
        logResponse(adminClient.prepareTrimUsage()
                .withRemoveAll(false)
                .execute());
    }
}
