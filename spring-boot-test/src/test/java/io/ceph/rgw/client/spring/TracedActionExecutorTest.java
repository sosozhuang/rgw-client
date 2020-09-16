package io.ceph.rgw.client.spring;

import io.ceph.rgw.client.BucketClient;
import io.ceph.rgw.client.ObjectClient;
import io.ceph.rgw.client.model.GetStringResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/4/26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {TracedActionExecutorTest.class, TracerConfiguration.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:config/trace.properties")
@SpringBootApplication
public class TracedActionExecutorTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(TracedActionExecutorTest.class);
    @Autowired
    private BucketClient bucketClient;
    @Autowired
    private ObjectClient objectClient;
    private String bucket;
    private String key;

    @Before
    public void setUp() throws Exception {
        bucket = "bucket-" + System.currentTimeMillis();
        key = "object-" + System.currentTimeMillis();
        bucketClient.prepareCreateBucket()
                .withBucketName(bucket)
                .run();
    }

    @After
    public void tearDown() throws Exception {
        bucketClient.prepareDeleteBucket()
                .withBucketName(bucket)
                .run();
    }

    @Test
    public void testPutString() {
        String content = TracedActionExecutorTest.class.getSimpleName() + " testPutString";
        objectClient.preparePutString()
                .withBucketName(bucket)
                .withKey(key)
                .withValue(content)
                .run();
        GetStringResponse response = objectClient.prepareGetString()
                .withBucketName(bucket)
                .withKey(key)
                .run();
        Assert.assertEquals(content, response.getContent());

        objectClient.prepareDeleteObject()
                .withBucketName(bucket)
                .withKey(key)
                .run();
    }
}
