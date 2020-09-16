package io.ceph.rgw.client.core.object;

import io.ceph.rgw.client.ObjectClient;
import io.ceph.rgw.client.core.BaseClientTest;
import io.ceph.rgw.client.core.bucket.BaseBucketClientTest;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.IOException;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/7/10.
 */
public abstract class BaseObjectClientTest extends BaseBucketClientTest {
    static ObjectClient objectClient;
    String key;

    @BeforeClass
    public static void setUpClient() throws IOException {
        BaseClientTest.setUpClient("object.properties");
        bucketClient = clients.getBucket();
        objectClient = clients.getObject();
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        key = "object-" + System.currentTimeMillis();
        if (isCreateBucket() && isCreateObject()) {
            objectClient.preparePutString()
                    .withBucketName(bucket)
                    .withKey(key)
                    .withValue("test case: bucket[" + bucket + "] key[" + key + "]")
                    .run();
        }
    }

    @After
    @Override
    public void tearDown() throws Exception {
        if (isCreateBucket() && isCreateObject()) {
            objectClient.prepareDeleteObject()
                    .withBucketName(bucket)
                    .withKey(key)
                    .run();
        }
        super.tearDown();
    }

    boolean isCreateObject() {
        return true;
    }
}
