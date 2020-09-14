package io.ceph.rgw.client.core.sync;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.retry.PredefinedBackoffStrategies;
import com.amazonaws.retry.PredefinedRetryPolicies;
import com.amazonaws.retry.RetryPolicy;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import io.ceph.rgw.client.config.RGWClientProperties;
import io.ceph.rgw.client.core.GenericConnectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/3/6.
 */
public class SyncConnectors extends GenericConnectors<AmazonS3> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SyncConnectors.class);
    private final Supplier<AmazonS3> supplier;
    protected final Map<RGWClientProperties.EndpointProperties, AmazonS3> connectors;

    public SyncConnectors(RGWClientProperties.ConnectorProperties properties) {
        super(properties);
        List<RGWClientProperties.EndpointProperties> storages = properties.getStorages();
        if (storages.size() == 1) {
            RGWClientProperties.EndpointProperties storage = storages.get(0);
            this.connectors = Collections.singletonMap(storage, create(storage));
            this.supplier = () -> connectors.get(storage);
        } else {
            AtomicInteger counter = new AtomicInteger(-1);
            Supplier<RGWClientProperties.EndpointProperties> endpointSupplier = () -> storages.get(getNext(counter, storages.size()));
            this.connectors = new ConcurrentHashMap<>(storages.size());
            this.supplier = () -> connectors.computeIfAbsent(endpointSupplier.get(), this::create);
        }
    }

    private Optional<RetryPolicy> retryPolicy() {
        return properties.isEnableRetry() && properties.getStorages().size() == 1 ? Optional.of(new RetryPolicy(new PredefinedRetryPolicies.SDKDefaultRetryCondition(),
                new PredefinedBackoffStrategies.ExponentialBackoffStrategy(properties.getBaseDelayTime(), properties.getMaxBackoffTime()),
                properties.getMaxRetries(), false)) : Optional.empty();
    }

    protected AmazonS3 create(RGWClientProperties.EndpointProperties endpointProperties) {
        BasicAWSCredentials credentials = new BasicAWSCredentials(endpointProperties.getAccessKey(), endpointProperties.getSecretKey());
        ClientConfiguration config = new ClientConfiguration()
                .withProtocol("http".equalsIgnoreCase(endpointProperties.getProtocol()) ? Protocol.HTTP : Protocol.HTTPS)
                .withMaxConnections(properties.getMaxConnections())
                .withConnectionTimeout(properties.getConnectionTimeout())
                .withSocketTimeout(properties.getSocketTimeout())
                .withConnectionMaxIdleMillis(properties.getConnectionMaxIdle())
                .withUserAgentPrefix("rgw_client/1.0.0")
                .withTcpKeepAlive(properties.isEnableKeepAlive())
                .withGzip(properties.isEnableGzip());
        retryPolicy().ifPresent(config::withRetryPolicy);
        AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSCredentialsProvider() {
                    @Override
                    public AWSCredentials getCredentials() {
                        return credentials;
                    }

                    @Override
                    public void refresh() {
                    }
                })
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpointProperties.getEndpoint(), StringUtils.isNotBlank(endpointProperties.getRegion()) ? endpointProperties.getRegion() : Regions.DEFAULT_REGION.getName()))
                .withClientConfiguration(config)
                .build();
        getLogger().debug("Finished to create AmazonS3 for [{}]", endpointProperties.getEndpoint());
        return amazonS3;
    }

    @Override
    public AmazonS3 doGet() {
        return supplier.get();
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @Override
    protected void doClose() {
        for (Map.Entry<RGWClientProperties.EndpointProperties, AmazonS3> entry : connectors.entrySet()) {
            try {
                entry.getValue().shutdown();
                LOGGER.info("Finished to close s3 connector [{}].", entry.getKey().getEndpoint());
            } catch (Throwable cause) {
                LOGGER.error("Failed to close s3 connector [{}].", entry.getKey().getEndpoint(), cause);
            }
        }
    }
}
