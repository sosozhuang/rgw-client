package io.ceph.rgw.client.core.bucket;

import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/7/9.
 */
@RunWith(Parameterized.class)
public class GetBucketTest extends BaseBucketClientTest {
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[]{true}, new Object[]{false});
    }

    private final boolean createBucket;

    public GetBucketTest(boolean createBucket) {
        this.createBucket = createBucket;
    }

    @Test
    public void testSync() {
        Assume.assumeTrue(createBucket);
        logResponse(bucketClient.prepareGetBucket().withBucketName(bucket).run());
    }

    @Test
    public void testCallback() throws InterruptedException {
        Assume.assumeTrue(createBucket);
        Latch latch = newLatch();
        bucketClient.prepareGetBucket().withBucketName(bucket).execute(newActionListener(latch));
        latch.await();
    }

    @Test
    public void testAsync() {
        Assume.assumeTrue(createBucket);
        logResponse(bucketClient.prepareGetBucket().withBucketName(bucket).execute());
    }

    @Test(expected = Test.None.class)
    public void testNotExists() {
        Assume.assumeFalse(createBucket);
        logResponse(bucketClient.prepareGetBucket().withBucketName("notexists").run());
    }

    @Override
    public boolean isCreateBucket() {
        return createBucket;
    }
}
