package io.ceph.rgw.client.core.bucket;

import io.ceph.rgw.client.model.GetBucketResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/7/9.
 */
@Category(BucketTests.class)
public class GetBucketTest extends BaseBucketClientTest {
    @Test
    public void testSync() {
        logResponse(bucketClient.prepareGetBucket().withBucketName(bucket).run());
    }

    @Test
    public void testCallback() throws InterruptedException {
        Latch latch = newLatch();
        bucketClient.prepareGetBucket().withBucketName(bucket).execute(newActionListener(latch));
        latch.await();
    }

    @Test
    public void testAsync() {
        logResponse(bucketClient.prepareGetBucket().withBucketName(bucket).execute());
    }

    @Test
    public void testNotExists() {
        GetBucketResponse response = logResponse(bucketClient.prepareGetBucket().withBucketName("notexists").run());
        Assert.assertFalse(response.isExist());
    }
}
