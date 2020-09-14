package io.ceph.rgw.client.core;

import com.amazonaws.services.s3.AmazonS3Client;
import io.ceph.rgw.client.config.RGWClientProperties;
import io.ceph.rgw.client.core.async.AdminAsyncConnectors;
import io.ceph.rgw.client.core.async.S3AsyncConnectors;
import io.ceph.rgw.client.core.subscribe.SubscribeConnectors;
import io.ceph.rgw.client.core.sync.SyncConnectors;
import software.amazon.awssdk.services.s3.S3AsyncClient;

import java.io.Closeable;
import java.util.function.Supplier;

/**
 * A Connectors contains single or multiple connector(s), creates connector with {@link io.ceph.rgw.client.config.RGWClientProperties.ConnectorProperties}.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/3/6.
 */
public interface Connectors<T> extends Supplier<T>, Closeable {
    /**
     * {@link io.ceph.rgw.client.action.Action} of the connector is finished.
     */
    void markSuccess(T connector);

    /**
     * {@link io.ceph.rgw.client.action.Action} of the connector is failed.
     */
    void markFailure(T connector, Throwable cause);

    /**
     * Close connectors, and release resources.
     */
    @Override
    void close();

    /**
     * Checks if async module is on the classpath.
     *
     * @return true if async module is found, false otherwise.
     * @throws RuntimeException if cannot find any amazon s3 client library
     */
    static boolean isAsync() {
        try {
            S3AsyncClient.builder();
            return true;
        } catch (Throwable t1) {
            try {
                AmazonS3Client.builder();
                return false;
            } catch (Throwable t2) {
                throw new RuntimeException("cannot find any aws s3 dependency, please add sync/async as dependency");
            }
        }
    }

    /**
     * Create s3 connectors to perform s3 operations.
     *
     * @param properties the connector properties
     * @return the s3 connectors
     * @see S3AsyncConnectors
     * @see SyncConnectors
     */
    static Connectors<?> createS3(RGWClientProperties.ConnectorProperties properties) {
        if (isAsync()) {
            return new S3AsyncConnectors(properties);
        } else {
            return new SyncConnectors(properties);
        }
    }

    /**
     * Create admin connectors to perform admin operations.
     *
     * @param properties the connector properties
     * @return the admin connectors
     * @throws UnsupportedOperationException if cannot find async module
     * @see AdminAsyncConnectors
     */
    static Connectors<?> createAdmin(RGWClientProperties.ConnectorProperties properties) {
        if (isAsync()) {
            return new AdminAsyncConnectors(properties);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Create subscribe connectors to perform subscribe operations.
     *
     * @param properties the connector properties
     * @return the subscribe connectors
     * @see SubscribeConnectors
     */
    static Connectors<?> createSubscribe(RGWClientProperties.ConnectorProperties properties) {
        return new SubscribeConnectors(properties);
    }
}
