package io.ceph.rgw.client.core.bucket;

import org.junit.Test;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/10/10.
 */
public class ListObjectsTest extends BaseBucketClientTest {

    @Test
    public void testSync() {
        logResponse(bucketClient.prepareListObjects()
                .withBucketName(bucket)
                .run());
    }

    @Test
    public void testCallback() throws InterruptedException {
        Latch latch = newLatch();
        bucketClient.prepareListObjects()
                .withBucketName(bucket)
                .execute(newActionListener(latch));
        latch.await();
    }

    @Test
    public void testAsync() {
        logResponse(bucketClient.prepareListObjects()
                .withBucketName(bucket)
                .execute());
    }
}
