package io.ceph.rgw.client.core.bucket;

import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/7/9.
 */
@Category(BucketTests.class)
public class ListBucketMultipartUploadsTest extends BaseBucketClientTest {

    @Test
    public void testSync() {
        logResponse(bucketClient.prepareListBucketMultipartUploads()
                .withBucketName(bucket).run());
    }

    @Test
    public void testCallback() throws InterruptedException {
        Latch latch = newLatch();
        bucketClient.prepareListBucketMultipartUploads().withBucketName(bucket).execute(newActionListener(latch));
        latch.await();
    }

    @Test
    public void testAsync() {
        logResponse(bucketClient.prepareListBucketMultipartUploads().withBucketName(bucket).execute());
    }
}
