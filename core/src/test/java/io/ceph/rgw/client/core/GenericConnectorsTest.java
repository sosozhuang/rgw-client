package io.ceph.rgw.client.core;

import io.ceph.rgw.client.config.RGWClientProperties;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ForkJoinPool;

public class GenericConnectorsTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(GenericConnectorsTest.class);
    private static MockedConnectors connectors;

    @BeforeClass
    public static void setUp() {
        RGWClientProperties properties = RGWClientProperties.loadFromResource("rgwclient.properties");
        connectors = new MockedConnectors(properties.getConnector());
    }

    static class MockedConnectors extends GenericConnectors<String> {

        private MockedConnectors(RGWClientProperties.ConnectorProperties properties) {
            super(properties);
        }

        @Override
        protected String doGet() {
            return null;
        }

        @Override
        protected void doClose() {

        }

        @Override
        protected Logger getLogger() {
            return LOGGER;
        }

        @Override
        public boolean isAllowRequest(String connector) {
            return super.isAllowRequest(connector);
        }
    }

    @Test
    public void testAllSuccess() throws InterruptedException {
        int count = 100;
        CountDownLatch latch = new CountDownLatch(count);
        for (int i = 0; i < count; i++) {
            ForkJoinPool.commonPool().execute(() -> {
                connectors.markSuccess("1");
                latch.countDown();
            });
        }
        latch.await();
        Assert.assertTrue(connectors.isAllowRequest("1"));
    }

    @Test
    public void testAllFailure() throws InterruptedException {
        int count = 100;
        CountDownLatch latch = new CountDownLatch(count);
        for (int i = 0; i < count; i++) {
            ForkJoinPool.commonPool().execute(() -> {
                connectors.markFailure("1", new RuntimeException("test"));
                latch.countDown();
            });
        }
        latch.await();
        Assert.assertFalse(connectors.isAllowRequest("1"));
    }

    @Test
    public void testSuccessAfterFailure() throws InterruptedException {
        int count = 10;
        CountDownLatch latch = new CountDownLatch(count);
        for (int i = 0; i < count; i++) {
            ForkJoinPool.commonPool().execute(() -> {
                connectors.markFailure("1", new RuntimeException("test"));
                latch.countDown();
            });
        }
        latch.await();
        Assert.assertFalse(connectors.isAllowRequest("1"));
        Assert.assertFalse(connectors.isAllowRequest("1"));
        connectors.markSuccess("1");
        Assert.assertTrue(connectors.isAllowRequest("1"));
    }

    @Test
    public void testEvenSplit() throws InterruptedException {
        int count = 10;
        CountDownLatch latch = new CountDownLatch(count);
        for (int i = 0; i < count; i += 2) {
            ForkJoinPool.commonPool().execute(() -> {
                connectors.markFailure("1", new RuntimeException("test"));
                latch.countDown();
            });
            ForkJoinPool.commonPool().execute(() -> {
                connectors.markSuccess("2");
                latch.countDown();
            });
        }
        latch.await();
        Assert.assertFalse(connectors.isAllowRequest("1"));
        Assert.assertTrue(connectors.isAllowRequest("2"));
    }
}
