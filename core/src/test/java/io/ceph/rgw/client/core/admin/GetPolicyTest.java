package io.ceph.rgw.client.core.admin;

import io.ceph.rgw.client.exception.RGWServerException;
import org.junit.Test;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/7/31.
 */
public class GetPolicyTest extends BaseAdminClientTest {
    @Test(expected = RGWServerException.class)
    public void testNoBucket() {
        logResponse(adminClient.prepareGetPolicy()
                .withBucket("not exists")
                .run());
    }

    @Test(expected = RGWServerException.class)
    public void testNoKey() {
        logResponse(adminClient.prepareGetPolicy()
                .withBucket("test")
                .withObject("not exists")
                .run());
    }

    @Test
    public void testSync() {
        logResponse(adminClient.prepareGetPolicy()
                .withBucket(bucket)
                .run());
    }

    @Test
    public void testCallback() throws InterruptedException {
        Latch latch = newLatch();
        adminClient.prepareGetPolicy()
                .withBucket(bucket)
                .execute(newActionListener(latch));
        latch.await();
    }

    @Test
    public void testAsync() {
        logResponse(adminClient.prepareGetPolicy()
                .withBucket(bucket)
                .execute());
    }

    @Override
    protected boolean isCreateBucket() {
        return true;
    }
}
