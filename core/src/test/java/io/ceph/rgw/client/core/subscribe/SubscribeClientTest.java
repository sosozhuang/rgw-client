package io.ceph.rgw.client.core.subscribe;

import io.ceph.rgw.client.ObjectClient;
import io.ceph.rgw.client.SubscribeClient;
import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.core.BaseClientTest;
import io.ceph.rgw.client.core.bucket.BaseBucketClientTest;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/7/10.
 */
public class SubscribeClientTest extends BaseBucketClientTest {
    private static ObjectClient objectClient;
    private static SubscribeClient subscribeClient;

    @BeforeClass
    public static void setUpClient() throws IOException {
        BaseClientTest.setUpClient("subscribe.properties");
        bucketClient = clients.getBucket();
        objectClient = clients.getObject();
        subscribeClient = clients.getSubscribe();
    }

    @Test
    public void test() throws InterruptedException {
        Latch latch = newLatch();
        ActionFuture<Void> future = subscribeClient.prepareSubscribeObject()
                .withCondition("[service] matches 'rgwclient-core-.+' && [ip] != null")
                .execute(newActionListener(latch));

        String key = "object-" + System.currentTimeMillis();
        objectClient.preparePutString()
                .withBucketName(bucket)
                .withKey(key)
                .withValue("test case: bucket[" + bucket + "] key[" + key + "]")
                .run();
        latch.await();
        future.cancel(true);
        objectClient.prepareDeleteObject()
                .withBucketName(bucket)
                .withKey(key)
                .run();
    }
}
