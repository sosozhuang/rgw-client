package io.ceph.rgw.client.core.admin;

import io.ceph.rgw.client.model.admin.GetBucketInfoResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/7/31.
 */
@Category(AdminTests.class)
public class GetBucketInfoTest extends BaseAdminClientTest {
    @Test
    public void testNotExists() {
        logResponse(adminClient.prepareGetBucketInfo()
                .withBucket("not exists")
                .withStats(Boolean.TRUE)
                .run());
    }

    @Test
    public void testSync() {
        GetBucketInfoResponse resp = logResponse(adminClient.prepareGetBucketInfo()
                .withBucket(bucket)
                .withStats(Boolean.TRUE)
                .run());
        Assert.assertEquals(1, resp.getBucketInfoList().size());
        Assert.assertEquals(bucket, resp.getBucketInfoList().get(0).getBucket());
    }

    @Test
    public void testCallback() throws InterruptedException {
        Latch latch = newLatch();
        adminClient.prepareGetBucketInfo()
                .withUid(userId)
                .execute(newActionListener(latch));
        latch.await();
    }

    @Test
    public void testAsync() {
        GetBucketInfoResponse resp = logResponse(adminClient.prepareGetBucketInfo()
                .withUid(userId)
                .withStats(Boolean.FALSE)
                .execute());
        Assert.assertEquals(0, resp.getBucketInfoList().size());
    }

    @Override
    protected boolean isCreateBucket() {
        return true;
    }

    @Override
    boolean isCreateUser() {
        return true;
    }
}
