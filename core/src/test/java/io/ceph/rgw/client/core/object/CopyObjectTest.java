package io.ceph.rgw.client.core.object;

import org.junit.After;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/7/9.
 */
@Category(ObjectTests.class)
public class CopyObjectTest extends BaseObjectClientTest {

    @After
    @Override
    public void tearDown() throws Exception {
        objectClient.prepareDeleteObject()
                .withBucketName(bucket)
                .withKey("copy-" + key)
                .run();
        super.tearDown();
    }

    @Test
    public void testSync() {
        logResponse(objectClient.prepareCopyObject()
                .withSrcBucketName(bucket)
                .withSrcKey(key)
                .withDestBucketName(bucket)
                .withDestKey("copy-" + key)
                .run());
    }

    @Test
    public void testCallback() throws InterruptedException {
        Latch latch = newLatch();
        objectClient.prepareCopyObject()
                .withSrcBucketName(bucket)
                .withSrcKey(key)
                .withDestBucketName(bucket)
                .withDestKey("copy-" + key)
                .execute(newActionListener(latch));
        latch.await();
    }

    @Test
    public void testAsync() {
        logResponse(objectClient.prepareCopyObject()
                .withSrcBucketName(bucket)
                .withSrcKey(key)
                .withDestBucketName(bucket)
                .withDestKey("copy-" + key)
                .execute());
    }
}
