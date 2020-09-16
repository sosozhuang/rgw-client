package io.ceph.rgw.client.core.object;

import io.ceph.rgw.client.model.GetInputStreamResponse;
import io.ceph.rgw.client.util.IOUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/7/9.
 */
public class InputStreamTest extends BaseObjectClientTest {
    @After
    @Override
    public void tearDown() throws Exception {
        objectClient.prepareDeleteObject()
                .withBucketName(bucket)
                .withKey(key)
                .run();
        super.tearDown();
    }

    private InputStream createInputStream(String content) {
        return new ByteArrayInputStream(content.getBytes());
    }

    @Test
    public void testSync() {
        logResponse(objectClient.preparePutInputStream()
                .withBucketName(bucket)
                .withKey(key)
                .withValue(createInputStream(InputStreamTest.class.getSimpleName() + " testSync"))
                .run());
        GetInputStreamResponse resp = logResponse(objectClient.prepareGetInputStream()
                .withBucketName(bucket)
                .withKey(key)
                .run());
        Assert.assertEquals(InputStreamTest.class.getSimpleName() + " testSync", IOUtil.toString(resp.getContent()));
    }

    @Test
    public void testCallback() throws InterruptedException {
        Latch latch = newLatch();
        objectClient.preparePutInputStream()
                .withBucketName(bucket)
                .withKey(key)
                .withValue(createInputStream(InputStreamTest.class.getSimpleName() + " testCallback"))
                .execute(newActionListener(latch));
        latch.await();
        GetInputStreamResponse resp = logResponse(objectClient.prepareGetInputStream()
                .withBucketName(bucket)
                .withKey(key)
                .execute());
        Assert.assertEquals(InputStreamTest.class.getSimpleName() + " testCallback", IOUtil.toString(resp.getContent()));
    }

    @Test
    public void testAsync() {
        logResponse(objectClient.preparePutInputStream()
                .withBucketName(bucket)
                .withKey(key)
                .withValue(createInputStream(InputStreamTest.class.getSimpleName() + " testAsync"))
                .execute());
        GetInputStreamResponse response = logResponse(objectClient.prepareGetInputStream()
                .withBucketName(bucket)
                .withKey(key)
                .execute());
        Assert.assertEquals(InputStreamTest.class.getSimpleName() + " testAsync", IOUtil.toString(response.getContent()));
    }

    @Test
    public void testOverwritten() {
        logResponse(objectClient.preparePutInputStream()
                .withBucketName(bucket)
                .withKey(key)
                .withValue(createInputStream(InputStreamTest.class.getSimpleName() + " testOverwritten 1"))
                .execute());
        GetInputStreamResponse response = logResponse(objectClient.prepareGetInputStream()
                .withBucketName(bucket)
                .withKey(key)
                .execute());
        Assert.assertEquals(InputStreamTest.class.getSimpleName() + " testOverwritten 1", IOUtil.toString(response.getContent()));
        logResponse(objectClient.preparePutInputStream()
                .withBucketName(bucket)
                .withKey(key)
                .withValue(createInputStream(InputStreamTest.class.getSimpleName() + " testOverwritten 2"))
                .execute());
        response = logResponse(objectClient.prepareGetInputStream()
                .withBucketName(bucket)
                .withKey(key)
                .execute());
        Assert.assertEquals(InputStreamTest.class.getSimpleName() + " testOverwritten 2", IOUtil.toString(response.getContent()));
    }

    @Override
    boolean isCreateObject() {
        return false;
    }
}
