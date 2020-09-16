package io.ceph.rgw.client.core.bucket;

import io.ceph.rgw.client.BucketClient;
import io.ceph.rgw.client.core.BaseClientTest;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.IOException;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/7/8.
 */
public abstract class BaseBucketClientTest extends BaseClientTest {
    protected static BucketClient bucketClient;
    protected String bucket;

    @BeforeClass
    public static void setUpClient() throws IOException {
        BaseClientTest.setUpClient("bucket.properties");
        bucketClient = clients.getBucket();
    }

    protected static void setUpClient(String resource) throws IOException {
        BaseClientTest.setUpClient(resource);
        bucketClient = clients.getBucket();
    }

    @Before
    public void setUp() throws Exception {
        bucket = "bucket-" + System.currentTimeMillis();
        if (isCreateBucket()) {
            bucketClient.prepareCreateBucket()
                    .withBucketName(bucket)
                    .run();
        }
    }

    @After
    public void tearDown() throws Exception {
        if (isCreateBucket()) {
            bucketClient.prepareDeleteBucket()
                    .withBucketName(bucket)
                    .run();
        }
    }

    protected boolean isCreateBucket() {
        return true;
    }
}
