package io.ceph.rgw.client.core.admin;

import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/8/5.
 */
@Category(AdminTests.class)
public class CheckBucketIndexTest extends BaseAdminClientTest {
    @Test
    public void testSync() {
        logResponse(adminClient.prepareCheckBucketIndex()
                .withBucket(bucket)
                .run());
    }

    @Test
    public void testCallback() throws InterruptedException {
        Latch latch = newLatch();
        adminClient.prepareCheckBucketIndex()
                .withBucket("test")
                .execute(newActionListener(latch));
        latch.await();
    }

    @Test
    public void testAsync() {
        logResponse(adminClient.prepareCheckBucketIndex()
                .withBucket("test")
                .execute());
    }

    @Override
    protected boolean isCreateBucket() {
        return true;
    }
}
