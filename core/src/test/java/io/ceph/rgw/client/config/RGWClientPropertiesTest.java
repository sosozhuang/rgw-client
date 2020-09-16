package io.ceph.rgw.client.config;

import io.ceph.rgw.client.exception.RGWException;
import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Paths;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/4/26.
 */
public class RGWClientPropertiesTest {
    @Test
    public void testFile() {
        String file = Paths.get("src", "test", "resources", "rgwclient.properties").toAbsolutePath().toString();
        RGWClientProperties properties = RGWClientProperties.loadFromFile(file);
        asserts(properties);
    }

    @Test
    public void testResource() {
        RGWClientProperties properties = RGWClientProperties.loadFromResource("rgwclient.properties");
        asserts(properties);
    }

    @Test
    public void testBuilder() {
        RGWClientProperties properties = new RGWClientProperties.Builder()
                .withApplicationName("core-tester")
                .withConnector()
                .withStorage()
                .withEndpoint("192.168.100.1:8080")
                .withAccessKey("1234")
                .withSecretKey("5678")
                .endStorage()
                .withStorage()
                .withEndpoint("192.168.100.2:8080")
                .withAccessKey("abcd")
                .withSecretKey("efgh")
                .endStorage()
                .withSubscribe()
                .withEndpoint("192.168.100.3:8080")
                .endSubscribe()
                .endConnector()
                .withActionThreadPool()
                .withCoreSize(1)
                .withMaxSize(2)
                .withKeepAlive(12345)
                .withQueueSize(4)
                .endThreadPool()
                .withListenerThreadPool()
                .withCoreSize(5)
                .withMaxSize(200)
                .withKeepAlive(54321)
                .withQueueSize(10)
                .endThreadPool()
                .build();
        asserts(properties);
    }

    private static void asserts(RGWClientProperties properties) {
        Assert.assertEquals("core-tester", properties.getApplicationName());
        Assert.assertEquals(2, properties.getConnector().getStorages().size());
        Assert.assertEquals(1, properties.getConnector().getSubscribes().size());
        Assert.assertEquals("192.168.100.1:8080", properties.getConnector().getStorages().get(0).getEndpoint());
        Assert.assertEquals("1234", properties.getConnector().getStorages().get(0).getAccessKey());
        Assert.assertEquals("5678", properties.getConnector().getStorages().get(0).getSecretKey());
        Assert.assertEquals("192.168.100.2:8080", properties.getConnector().getStorages().get(1).getEndpoint());
        Assert.assertEquals("abcd", properties.getConnector().getStorages().get(1).getAccessKey());
        Assert.assertEquals("efgh", properties.getConnector().getStorages().get(1).getSecretKey());
        Assert.assertEquals("192.168.100.3:8080", properties.getConnector().getSubscribes().get(0).getEndpoint());
        Assert.assertEquals(RGWClientProperties.DEFAULT_ENABLE_ADMIN, properties.isEnableAdmin());
        Assert.assertEquals(RGWClientProperties.DEFAULT_ENABLE_BUCKET, properties.isEnableBucket());
        Assert.assertEquals(RGWClientProperties.DEFAULT_ENABLE_OBJECT, properties.isEnableObject());
        RGWClientProperties.ThreadPoolProperties threadPool = properties.getThreadPools().get("action");
        Assert.assertEquals(1, threadPool.getCoreSize().intValue());
        Assert.assertEquals(2, threadPool.getMaxSize().intValue());
        Assert.assertEquals(12345, threadPool.getKeepAlive().intValue());
        Assert.assertEquals(4, threadPool.getQueueSize().intValue());
        threadPool = properties.getThreadPools().get("listener");
        Assert.assertEquals(5, threadPool.getCoreSize().intValue());
        Assert.assertEquals(200, threadPool.getMaxSize().intValue());
        Assert.assertEquals(54321, threadPool.getKeepAlive().intValue());
        Assert.assertEquals(10, threadPool.getQueueSize().intValue());
    }

    @Test(expected = RGWException.class)
    public void testNotExists() {
        RGWClientProperties.loadFromFile("not exists");
    }
}
