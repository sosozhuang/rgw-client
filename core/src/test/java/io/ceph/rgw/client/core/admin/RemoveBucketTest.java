package io.ceph.rgw.client.core.admin;

import io.ceph.rgw.client.exception.RGWServerException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/8/5.
 */
@Category(AdminTests.class)
public class RemoveBucketTest extends BaseAdminClientTest {
    @Test(expected = RGWServerException.class)
    public void testNoBucket() {
        try {
            logResponse(adminClient.prepareRemoveBucket()
                    .withBucket("not exists")
                    .run());
        } catch (Exception e) {
            Assert.assertTrue(e.getCause() instanceof NoSuchKeyException);
            throw e;
        }
    }

    @Test
    public void testSync() {
        logResponse(adminClient.prepareRemoveBucket()
                .withBucket(bucket)
                .withPurgeObjects(Boolean.TRUE)
                .run());
    }

    @Test
    public void testCallback() throws InterruptedException {
        Latch latch = newLatch();
        adminClient.prepareRemoveBucket()
                .withBucket(bucket)
                .execute(newActionListener(latch));
        latch.await();
    }

    @Test
    public void testAsync() {
        logResponse(adminClient.prepareRemoveBucket()
                .withBucket(bucket)
                .execute());
    }

    @Override
    protected boolean isCreateBucket() {
        return true;
    }
}
