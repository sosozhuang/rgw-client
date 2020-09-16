package io.ceph.rgw.client.core.bucket;

import io.ceph.rgw.client.model.Bucket;
import io.ceph.rgw.client.model.ListBucketsResponse;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/7/8.
 */
public class ListBucketsTest extends BaseBucketClientTest {

    @Test
    public void testSync() {
        ListBucketsResponse resp = logResponse(bucketClient.prepareListBuckets().run());
        Bucket info = resp.getBuckets().stream().filter(b -> b.getName().equals(bucket)).findFirst().orElse(null);
        Assert.assertNotNull(info);
    }

    @Test
    public void testCallback() throws InterruptedException {
        Latch latch = newLatch();
        bucketClient.prepareListBuckets().execute(newActionListener(latch));
        latch.await();
    }

    @Test
    public void testAsync() {
        ListBucketsResponse resp = logResponse(bucketClient.prepareListBuckets().execute());
        Bucket info = resp.getBuckets().stream().filter(b -> b.getName().equals(bucket)).findFirst().orElse(null);
        Assert.assertNotNull(info);
    }

    @Override
    protected boolean isCreateBucket() {
        return true;
    }
}
