package io.ceph.rgw.client.core;

import io.ceph.rgw.client.Clients;
import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;
import io.ceph.rgw.client.config.RGWClientProperties;
import io.ceph.rgw.client.model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/7/31.
 */
public abstract class BaseClientTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseClientTest.class);
    protected static Clients clients;

    protected static void setUpClient(String resource) throws IOException {
        RGWClientProperties properties = RGWClientProperties.loadFromResource(resource);
        clients = Clients.create(properties);
    }

    protected <T extends Response> T logResponse(T response) {
        LOGGER.debug("{}: {}.", response.getClass().getSimpleName(), response);
        return response;
    }

    protected <T extends Response> T logResponse(ActionFuture<T> future) {
        return logResponse(future.actionGet());
    }

    protected Latch newLatch() {
        return new Latch(1);
    }

    protected static class Latch extends CountDownLatch {
        private Throwable cause;

        Latch(int count) {
            super(count);
        }

        @Override
        public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
            boolean b = super.await(timeout, unit);
            if (cause != null) {
                throw new RuntimeException(cause);
            }
            return b;
        }

        @Override
        public void await() throws InterruptedException {
            super.await();
            if (cause != null) {
                throw new RuntimeException(cause);
            }
        }
    }

    protected <T extends Response> ActionListener<T> newActionListener(Latch latch) {
        return new ActionListener<T>() {
            @Override
            public void onSuccess(T response) {
                try {
                    logResponse(response);
                } finally {
                    latch.countDown();
                }
            }

            @Override
            public void onFailure(Throwable cause) {
                try {
                    latch.cause = cause;
                } finally {
                    latch.countDown();
                }
            }
        };
    }
}
