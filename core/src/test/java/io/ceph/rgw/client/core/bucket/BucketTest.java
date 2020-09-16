package io.ceph.rgw.client.core.bucket;

import io.ceph.rgw.client.model.CannedACL;
import io.ceph.rgw.client.model.GetBucketResponse;
import io.ceph.rgw.client.model.Permission;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/7/8.
 */
public class BucketTest extends BaseBucketClientTest {

    @After
    public void tearDown() throws Exception {
        bucketClient.prepareDeleteBucket()
                .withBucketName(bucket)
                .run();
    }

    @Test
    public void testSync() {
        logResponse(bucketClient.prepareCreateBucket()
                .withBucketName(bucket)
                .withCannedACL(CannedACL.PRIVATE)
                .run());
        GetBucketResponse resp = logResponse(bucketClient.prepareGetBucket()
                .withBucketName(bucket)
                .run());
        Assert.assertTrue(resp.isExist());
    }

    @Test
    public void testCallback() throws InterruptedException {
        Latch latch = newLatch();
        bucketClient.prepareCreateBucket()
                .withBucketName(bucket)
                .withCannedACL(CannedACL.PRIVATE)
                .execute(newActionListener(latch));
        latch.await();
    }

    @Test
    public void testAsync() {
        logResponse(bucketClient.prepareCreateBucket()
                .withBucketName(bucket)
                .withACL()
                .withOwner("test", "test")
                .addGrant()
                .withGrantee("type", "id")
                .withPermission(Permission.FULL_CONTROL)
                .endGrant()
                .endACL()
                .execute());
        GetBucketResponse resp = logResponse(bucketClient.prepareGetBucket()
                .withBucketName(bucket)
                .execute());
        Assert.assertTrue(resp.isExist());
    }

    @Override
    protected boolean isCreateBucket() {
        return false;
    }
}
