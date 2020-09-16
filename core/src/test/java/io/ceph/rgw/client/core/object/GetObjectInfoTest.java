package io.ceph.rgw.client.core.object;

import org.junit.Test;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/7/9.
 */
public class GetObjectInfoTest extends BaseObjectClientTest {
    @Test
    public void testSync() {
        logResponse(objectClient.prepareGetObjectInfo()
                .withBucketName(bucket)
                .withKey(key)
                .run());
    }

    @Test
    public void testCallback() throws InterruptedException {
        Latch latch = newLatch();
        objectClient.prepareGetObjectInfo()
                .withBucketName(bucket)
                .withKey(key)
                .execute(newActionListener(latch));
        latch.await();
    }

    @Test
    public void testAsync() {
        logResponse(objectClient.prepareGetObjectInfo()
                .withBucketName(bucket)
                .withKey(key)
                .execute());
    }

}
