package io.ceph.rgw.client;

import com.amazonaws.services.s3.AmazonS3;
import io.ceph.rgw.client.action.*;
import io.ceph.rgw.client.circuitbreaker.HystrixActionExecutor;
import io.ceph.rgw.client.concurrent.ThreadPool;
import io.ceph.rgw.client.config.RGWClientProperties;
import io.ceph.rgw.client.core.Connectors;
import io.ceph.rgw.client.core.async.AdminAsyncConnector;
import io.ceph.rgw.client.core.async.AsyncAdminClient;
import io.ceph.rgw.client.core.async.AsyncBucketClient;
import io.ceph.rgw.client.core.async.AsyncObjectClient;
import io.ceph.rgw.client.core.subscribe.DefaultSubscribeClient;
import io.ceph.rgw.client.core.subscribe.SubscribeConnector;
import io.ceph.rgw.client.core.sync.SyncBucketClient;
import io.ceph.rgw.client.core.sync.SyncObjectClient;
import io.ceph.rgw.client.util.AbstractClosable;
import io.ceph.rgw.client.util.AddressUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.s3.S3AsyncClient;

import java.util.*;
import java.util.function.Supplier;

/**
 * This Object creates instance of AdminClient/BucketClient/ObjectClient/SubscribeClient if the corresponding property is enabled.
 * Get the client to perform operations by calling the get...() method.
 * Resources(connectors and thread pool) of this object is automatically released before system shutdown.
 * For avoiding resource exhaustion, call {@link Clients#close()} promptly.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/3/4.
 * @see AdminClient
 * @see BucketClient
 * @see ObjectClient
 * @see SubscribeClient
 */
public final class Clients extends AbstractClosable {
    private static final Logger LOGGER = LoggerFactory.getLogger(Clients.class);
    private final RGWClientProperties properties;
    private final ThreadPool threadPool;
    private final Map<String, Connectors> connectorsMap;
    private final Supplier<BucketClient> bucketClientSupplier;
    private final Supplier<ObjectClient> objectClientSupplier;
    private final Supplier<AdminClient> adminClientSupplier;
    private final Supplier<SubscribeClient> subscribeClientSupplier;

    @SuppressWarnings("unchecked")
    private Clients(RGWClientProperties properties, Map<String, Connectors> connectorsMap, List<ActionExecutorDecorator> decorators) {
        this.properties = Objects.requireNonNull(properties);
        LOGGER.info("Using RGWClientProperties: {}.", properties);
        this.connectorsMap = new HashMap<>(connectorsMap);
        this.threadPool = new ThreadPool(properties.getThreadPools());
        Boolean async = Connectors.isAsync();
        ActionExecutor actionExecutor = initializeStorageActionExecutor(async, decorators);
        Supplier<?> unsupportedSupplier = () -> {
            throw new UnsupportedOperationException("client is not enabled");
        };
        if (properties.isEnableBucket()) {
            Connectors s3Connectors = this.connectorsMap.computeIfAbsent("s3Connectors", k -> Connectors.createS3(properties.getConnector()));
            this.bucketClientSupplier = initializeBucketClient(async, s3Connectors, actionExecutor);
        } else {
            this.bucketClientSupplier = (Supplier<BucketClient>) unsupportedSupplier;
        }
        if (properties.isEnableObject()) {
            Connectors s3Connectors = this.connectorsMap.computeIfAbsent("s3Connectors", k -> Connectors.createS3(properties.getConnector()));
            this.objectClientSupplier = initializeObjectClient(properties.getApplicationName(), async, s3Connectors, actionExecutor);
        } else {
            this.objectClientSupplier = (Supplier<ObjectClient>) unsupportedSupplier;
        }
        if (properties.isEnableAdmin()) {
            Connectors adminConnectors = this.connectorsMap.computeIfAbsent("adminConnectors", k -> Connectors.createAdmin(properties.getConnector()));
            this.adminClientSupplier = initializeAdminClient(adminConnectors, actionExecutor);
        } else {
            this.adminClientSupplier = (Supplier<AdminClient>) unsupportedSupplier;
        }
        List<RGWClientProperties.EndpointProperties> subscribes = properties.getConnector().getSubscribes();
        if (subscribes != null && subscribes.size() > 0) {
            Connectors subscribeConnectors = this.connectorsMap.computeIfAbsent("subscribeConnectors", k -> Connectors.createSubscribe(properties.getConnector()));
            actionExecutor = initializeSubscribeActionExecutor(decorators);
            this.subscribeClientSupplier = initializeSubscribeClient(subscribeConnectors, actionExecutor);
        } else {
            this.subscribeClientSupplier = (Supplier<SubscribeClient>) unsupportedSupplier;
        }
    }

    /**
     * Get the admin client to perform admin operations.
     *
     * @return the admin client
     * @throws UnsupportedOperationException if {@link RGWClientProperties#isEnableAdmin()} returns false
     */
    public AdminClient getAdmin() {
        return adminClientSupplier.get();
    }

    /**
     * Get the bucket client to perform bucket operations.
     *
     * @return the bucket client
     * @throws UnsupportedOperationException if {@link RGWClientProperties#isEnableBucket()} returns false
     */
    public BucketClient getBucket() {
        return bucketClientSupplier.get();
    }

    /**
     * Get the object client to perform object operations.
     *
     * @return the object client
     * @throws UnsupportedOperationException if {@link RGWClientProperties#isEnableObject()} returns false
     */
    public ObjectClient getObject() {
        return objectClientSupplier.get();
    }

    /**
     * Get the subscribe client to perform subscribe operations.
     *
     * @return the subscribe client
     * @throws UnsupportedOperationException if {@link RGWClientProperties.ConnectorProperties#getSubscribes()} is empty
     */
    public SubscribeClient getSubscribe() {
        return subscribeClientSupplier.get();
    }

    /**
     * Create a new Object.
     *
     * @param properties the properties of clients
     * @return clients
     */
    public static Clients create(RGWClientProperties properties) {
        return create(properties, Collections.emptyMap(), Collections.emptyList());
    }

    /**
     * Create a new Object.
     *
     * @param properties    the properties of clients
     * @param connectorsMap map of {@link Connectors}, valid keys are "s3Connectors"/"adminConnectors"/"subscribeConnectors"
     * @return clients
     */
    public static Clients create(RGWClientProperties properties, Map<String, Connectors> connectorsMap) {
        return create(properties, connectorsMap, Collections.emptyList());
    }

    /**
     * Create a new Object.
     *
     * @param properties the properties of clients
     * @param decorators list of ActionExecutorDecorator
     * @return clients
     */
    public static Clients create(RGWClientProperties properties, List<ActionExecutorDecorator> decorators) {
        return create(properties, Collections.emptyMap(), decorators);
    }

    /**
     * Create a new Object.
     *
     * @param properties    the properties of clients
     * @param connectorsMap map of {@link Connectors}, valid keys are "s3Connectors"/"adminConnectors"/"subscribeConnectors"
     * @param decorators    list of ActionExecutorDecorator
     * @return clients
     */
    public static Clients create(RGWClientProperties properties, Map<String, Connectors> connectorsMap, List<ActionExecutorDecorator> decorators) {
        Clients clients = new Clients(properties, connectorsMap, decorators);
        Runtime.getRuntime().addShutdownHook(new Thread(clients::close));
        return clients;
    }

    private ActionExecutor initializeStorageActionExecutor(Boolean async, List<ActionExecutorDecorator> decorators) {
        List<ActionExecutorDecorator> chain = new LinkedList<>();
        if (decorators != null && decorators.size() > 0) {
            chain.addAll(decorators);
        }
        chain.add(this::hystrixActionExecutor);
        chain.add(this::retryStorageActionExecutor);
        ActionExecutor actionExecutor;
        if (async) {
            chain.add(this::asyncListenerActionExecutor);
            actionExecutor = new AsyncActionExecutor();
        } else {
            chain.add(this::syncListenerActionExecutor);
            actionExecutor = new SyncActionExecutor(threadPool.get("action"));
        }
        for (ActionExecutorDecorator decorator : chain) {
            actionExecutor = decorator.decorate(actionExecutor);
        }
        return actionExecutor;
    }

    private ActionExecutor initializeSubscribeActionExecutor(List<ActionExecutorDecorator> decorators) {
        List<ActionExecutorDecorator> chain = new LinkedList<>();
        if (decorators != null && decorators.size() > 0) {
            chain.addAll(decorators);
        }
        chain.add(this::retrySubscribeActionExecutor);
        chain.add(this::subscribeListenerActionExecutor);
        ActionExecutor actionExecutor = new SubscribeActionExecutor();
        for (ActionExecutorDecorator decorator : chain) {
            actionExecutor = decorator.decorate(actionExecutor);
        }
        return actionExecutor;
    }

    private ActionExecutor hystrixActionExecutor(ActionExecutor actionExecutor) {
        if (properties.isEnableHystrix()) {
            return new HystrixActionExecutor(actionExecutor, properties.getHystrixConfig());
        }
        return actionExecutor;
    }

    private ActionExecutor retryStorageActionExecutor(ActionExecutor actionExecutor) {
        return retryActionExecutor(properties.getConnector().getStorages(), actionExecutor);
    }

    private ActionExecutor retrySubscribeActionExecutor(ActionExecutor actionExecutor) {
        return retryActionExecutor(properties.getConnector().getSubscribes(), actionExecutor);
    }

    private ActionExecutor retryActionExecutor(List<RGWClientProperties.EndpointProperties> propertiesList, ActionExecutor actionExecutor) {
        RGWClientProperties.ConnectorProperties connProps = properties.getConnector();
        if (connProps.isEnableRetry() && propertiesList.size() > 1) {
            actionExecutor = new RetryActionExecutor(actionExecutor, threadPool.getScheduler(),
                    connProps.getMaxRetries(), connProps.getBaseDelayTime(), connProps.getMaxBackoffTime());
        }
        return actionExecutor;
    }

//    private ActionExecutor listenerActionExecutor(ActionExecutor actionExecutor) {
//        return new ListenerActionExecutor(actionExecutor, threadPool.get("listener"));
//    }

    private ActionExecutor syncListenerActionExecutor(ActionExecutor actionExecutor) {
        return new SyncListenerActionExecutor(actionExecutor, threadPool.get("listener"));
    }

    private ActionExecutor asyncListenerActionExecutor(ActionExecutor actionExecutor) {
        return new AsyncListenerActionExecutor(actionExecutor, threadPool.get("listener"));
    }

    private ActionExecutor subscribeListenerActionExecutor(ActionExecutor actionExecutor) {
        return new SubscribeListenerActionExecutor(actionExecutor, threadPool.get("listener"));
    }

    @SuppressWarnings("unchecked")
    private static Supplier<BucketClient> initializeBucketClient(Boolean async, Connectors<?> connectors, ActionExecutor actionExecutor) {
        BucketClient bucketClient;
        if (async) {
            bucketClient = new AsyncBucketClient((Connectors<S3AsyncClient>) connectors, actionExecutor);
        } else {
            bucketClient = new SyncBucketClient((Connectors<AmazonS3>) connectors, actionExecutor);
        }
        return () -> bucketClient;
    }

    @SuppressWarnings("unchecked")
    private static Supplier<ObjectClient> initializeObjectClient(String applicationName, Boolean async, Connectors<?> connectors, ActionExecutor actionExecutor) {
        ObjectClient objectClient;
        if (async) {
            objectClient = new AsyncObjectClient((Connectors<S3AsyncClient>) connectors, actionExecutor, initializeMetadata(applicationName));
        } else {
            objectClient = new SyncObjectClient((Connectors<AmazonS3>) connectors, actionExecutor, initializeMetadata(applicationName));
        }
        return () -> objectClient;
    }

    @SuppressWarnings("unchecked")
    private static Supplier<AdminClient> initializeAdminClient(Connectors<?> connectors, ActionExecutor actionExecutor) {
        AdminClient adminClient = new AsyncAdminClient((Connectors<AdminAsyncConnector>) connectors, actionExecutor);
        return () -> adminClient;
    }

    @SuppressWarnings("unchecked")
    private static Supplier<SubscribeClient> initializeSubscribeClient(Connectors<?> connectors, ActionExecutor actionExecutor) {
        SubscribeClient subscribeClient = new DefaultSubscribeClient((Connectors<SubscribeConnector>) connectors, actionExecutor);
        return () -> subscribeClient;
    }

    private static Map<String, String> initializeMetadata(String applicationName) {
        Map<String, String> metadata = new HashMap<>(3);
        metadata.put("service", applicationName);
        metadata.put("ip", AddressUtil.getHostAddress());
        metadata.put("hostname", AddressUtil.getHostName());
        return metadata;
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    /**
     * Releases resources, closes connections to remote server, closes thread pool.
     * Invocation has no additional effect if already closed.
     * Can be invoked manually, or from system's shutdown notification.
     *
     * @see Connectors#close()
     * @see ThreadPool#close()
     */
    @Override
    public void doClose() {
        for (Map.Entry<String, Connectors> entry : connectorsMap.entrySet()) {
            entry.getValue().close();
        }
        threadPool.close();
    }
}
