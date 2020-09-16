package io.ceph.rgw.client.core.object;

import io.ceph.rgw.client.model.GetStringResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/7/9.
 */
public class PutObjectTest extends BaseObjectClientTest {
    @After
    @Override
    public void tearDown() throws Exception {
        objectClient.prepareDeleteObject()
                .withBucketName(bucket)
                .withKey(key)
                .run();
        super.tearDown();
    }

    @Test
    public void testSync() {
//        logResponse(objectClient.preparePutObject()
//                .withBucketName(bucket)
//                .withKey(key)
//                .withBuffer(PutObjectTest.class.getSimpleName() + " testSync")
//                .run());
//        GetStringResponse response = logResponse(objectClient.prepareGetString()
//                .withBucketName(bucket)
//                .withKey(key)
//                .run());
//        Assert.assertEquals(PutStringTest.class.getSimpleName() + " testSync", response.getContent());
    }

    @Test
    public void testCallback() throws InterruptedException {
        Latch latch = newLatch();
        objectClient.preparePutString()
                .withBucketName(bucket)
                .withKey(key)
                .withValue(PutObjectTest.class.getSimpleName() + " testCallback")
                .execute(newActionListener(latch));
        latch.await();
        GetStringResponse response = logResponse(objectClient.prepareGetString()
                .withBucketName(bucket)
                .withKey(key)
                .execute());
        Assert.assertEquals(StringTest.class.getSimpleName() + " testCallback", response.getContent());
    }

    @Test
    public void testAsync() {
        logResponse(objectClient.preparePutString()
                .withBucketName(bucket)
                .withKey(key)
                .withValue(PutObjectTest.class.getSimpleName() + " testAsync")
                .execute());
        GetStringResponse response = logResponse(objectClient.prepareGetString()
                .withBucketName(bucket)
                .withKey(key)
                .execute());
        Assert.assertEquals(PutObjectTest.class.getSimpleName() + " testAsync", response.getContent());
    }

    @Test
    public void testOverwritten() {
        logResponse(objectClient.preparePutString()
                .withBucketName(bucket)
                .withKey(key)
                .withValue(PutObjectTest.class.getSimpleName() + " testOverwritten 1")
                .execute());
        GetStringResponse response = logResponse(objectClient.prepareGetString()
                .withBucketName(bucket)
                .withKey(key)
                .execute());
        Assert.assertEquals(StringTest.class.getSimpleName() + " testOverwritten 1", response.getContent());
        logResponse(objectClient.preparePutString()
                .withBucketName(bucket)
                .withKey(key)
                .withValue(StringTest.class.getSimpleName() + " testOverwritten 2")
                .execute());
        response = logResponse(objectClient.prepareGetString()
                .withBucketName(bucket)
                .withKey(key)
                .execute());
        Assert.assertEquals(PutObjectTest.class.getSimpleName() + " testOverwritten 2", response.getContent());
    }

    @Override
    boolean isCreateObject() {
        return false;
    }
}
