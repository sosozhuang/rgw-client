package io.ceph.rgw.client.core.bucket;

import io.ceph.rgw.client.model.BucketVersioning;
import io.ceph.rgw.client.model.GetBucketVersioningResponse;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/7/9.
 */
public class BucketVersioningTest extends BaseBucketClientTest {
    @Test
    public void testSync() {
        GetBucketVersioningResponse resp = logResponse(bucketClient.prepareGetBucketVersioning()
                .withBucketName(bucket)
                .run());
        Assert.assertEquals(BucketVersioning.OFF, resp.getBucketVersioning());
        logResponse(bucketClient.preparePutBucketVersioning()
                .withBucketName(bucket)
                .withBucketVersioning(BucketVersioning.ENABLED)
                .run());
        resp = logResponse(bucketClient.prepareGetBucketVersioning()
                .withBucketName(bucket)
                .run());
        Assert.assertEquals(BucketVersioning.ENABLED, resp.getBucketVersioning());
    }

    @Test
    public void testCallback() throws InterruptedException {
        GetBucketVersioningResponse resp = logResponse(bucketClient.prepareGetBucketVersioning()
                .withBucketName(bucket)
                .run());
        Assert.assertNull(resp.getBucketVersioning());
        Latch latch = newLatch();
        bucketClient.preparePutBucketVersioning()
                .withBucketName(bucket)
                .withBucketVersioning(BucketVersioning.SUSPENDED)
                .execute(newActionListener(latch));
        latch.await();
        resp = logResponse(bucketClient.prepareGetBucketVersioning()
                .withBucketName(bucket)
                .run());
        Assert.assertEquals(BucketVersioning.SUSPENDED, resp.getBucketVersioning());
    }

    @Test
    public void testAsync() {
        GetBucketVersioningResponse resp = logResponse(bucketClient.prepareGetBucketVersioning()
                .withBucketName(bucket)
                .execute());
        Assert.assertNull(resp.getBucketVersioning());
        logResponse(bucketClient.preparePutBucketVersioning()
                .withBucketName(bucket)
                .withBucketVersioning(BucketVersioning.ENABLED)
                .execute());
        resp = logResponse(bucketClient.prepareGetBucketVersioning()
                .withBucketName(bucket)
                .execute());
        Assert.assertEquals(BucketVersioning.ENABLED, resp.getBucketVersioning());
    }
}
