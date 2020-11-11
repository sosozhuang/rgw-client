package io.ceph.rgw.client.config;

import io.ceph.rgw.client.exception.RGWException;
import io.ceph.rgw.client.util.AddressUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Properties for {@link io.ceph.rgw.client.Clients}.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/2/27.
 */
public class RGWClientProperties {
    private static final Logger LOGGER = LoggerFactory.getLogger(RGWClientProperties.class);
    private static final String PREFIX = "rgwclient.";
    private static final String APPLICATION_NAME = PREFIX + "application.name";
    private static final String ENABLE_ADMIN = PREFIX + "enableAdmin";
    private static final String ENABLE_BUCKET = PREFIX + "enableBucket";
    private static final String ENABLE_OBJECT = PREFIX + "enableObject";
    private static final String CONNECTOR_PREFIX = PREFIX + "connector";
    private static final String THREAD_POOLS_PREFIX = PREFIX + "threadPools";
    private static final String ENABLE_HYSTRIX = PREFIX + "enableHystrix";
    private static final String HYSTRIX_PREFIX = PREFIX + "hystrix";
    public static final Boolean DEFAULT_ENABLE_ADMIN = false;
    public static final Boolean DEFAULT_ENABLE_BUCKET = true;
    public static final Boolean DEFAULT_ENABLE_OBJECT = true;
    public static final Boolean DEFAULT_ENABLE_HYSTRIX = false;

    protected String applicationName;
    protected Boolean enableAdmin;
    protected Boolean enableBucket;
    protected Boolean enableObject;
    protected Boolean enableHystrix;
    protected ConnectorProperties connector;
    protected Map<String, ThreadPoolProperties> threadPools;
    protected Configuration hystrixConfig;

    protected RGWClientProperties() {
    }

    private RGWClientProperties(Builder builder) {
        this.applicationName = builder.applicationName;
        this.enableAdmin = builder.enableAdmin;
        this.enableBucket = builder.enableBucket;
        this.enableObject = builder.enableObject;
        this.enableHystrix = builder.enableHystrix;
        this.connector = builder.connector;
        this.threadPools = builder.threadPools;
        this.hystrixConfig = new Configuration(builder.hystrix);
        this.validate();
    }

    private RGWClientProperties(Configuration config) {
        this.applicationName = config.getString(APPLICATION_NAME);
        this.enableAdmin = config.getBoolean(ENABLE_ADMIN, DEFAULT_ENABLE_ADMIN);
        this.enableBucket = config.getBoolean(ENABLE_BUCKET, DEFAULT_ENABLE_BUCKET);
        this.enableObject = config.getBoolean(ENABLE_OBJECT, DEFAULT_ENABLE_OBJECT);
        this.connector = new ConnectorProperties(config.getSubConfig(CONNECTOR_PREFIX));
        Map<String, Configuration> threadPoolConfigs = config.getSubConfigMap(THREAD_POOLS_PREFIX);
        if (threadPoolConfigs != null && threadPoolConfigs.size() > 0) {
            this.threadPools = threadPoolConfigs.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> new ThreadPoolProperties(e.getValue())));
        } else {
            this.threadPools = Collections.emptyMap();
        }
        this.enableHystrix = config.getBoolean(ENABLE_HYSTRIX, DEFAULT_ENABLE_HYSTRIX);
        this.hystrixConfig = config.getSubConfig(HYSTRIX_PREFIX);
        this.validate();
    }

    public String getApplicationName() {
        return applicationName;
    }

    public Boolean isEnableAdmin() {
        return enableAdmin;
    }

    public Boolean isEnableBucket() {
        return enableBucket;
    }

    public Boolean isEnableObject() {
        return enableObject;
    }

    public Boolean isEnableHystrix() {
        return enableHystrix;
    }

    public ConnectorProperties getConnector() {
        return connector;
    }

    public Map<String, ThreadPoolProperties> getThreadPools() {
        return threadPools;
    }

    public Configuration getHystrixConfig() {
        return hystrixConfig;
    }

    public void setEnableAdmin(Boolean enableAdmin) {
        this.enableAdmin = enableAdmin;
    }

    public void setEnableBucket(Boolean enableBucket) {
        this.enableBucket = enableBucket;
    }

    public void setEnableObject(Boolean enableObject) {
        this.enableObject = enableObject;
    }

    public void setEnableHystrix(Boolean enableHystrix) {
        this.enableHystrix = enableHystrix;
    }

    public void setConnector(ConnectorProperties connector) {
        this.connector = connector;
    }

    public void setThreadPools(Map<String, ThreadPoolProperties> threadPools) {
        this.threadPools = threadPools;
    }

    public void setHystrixConfig(Configuration hystrixConfig) {
        this.hystrixConfig = hystrixConfig;
    }

    protected final void validate() {
        Validate.notBlank(getApplicationName(), "application.name cannot be empty string");
        if (enableAdmin == null) {
            enableAdmin = DEFAULT_ENABLE_ADMIN;
        }
        if (enableBucket == null) {
            enableBucket = DEFAULT_ENABLE_BUCKET;
        }
        if (enableObject == null) {
            enableObject = DEFAULT_ENABLE_OBJECT;
        }
        if (enableHystrix == null) {
            enableHystrix = DEFAULT_ENABLE_HYSTRIX;
        }
        connector.validate();
        if (threadPools == null || threadPools.isEmpty()) {
            LOGGER.info("ThreadPools is not set.");
            threadPools = new HashMap<>(2);
        }
        threadPools.computeIfAbsent("action", k -> new ThreadPoolPropertiesBuilder()
                .withCoreSize(ThreadPoolProperties.DEFAULT_ACTION_CORE_SIZE)
                .withMaxSize(ThreadPoolProperties.DEFAULT_ACTION_MAX_SIZE)
                .withKeepAlive(ThreadPoolProperties.DEFAULT_ACTION_KEEP_ALIVE)
                .withQueueSize(ThreadPoolProperties.DEFAULT_ACTION_QUEUE_SIZE)
                .build());
        threadPools.computeIfAbsent("listener", k -> new ThreadPoolPropertiesBuilder()
                .withCoreSize(ThreadPoolProperties.DEFAULT_LISTENER_CORE_SIZE)
                .withMaxSize(ThreadPoolProperties.DEFAULT_LISTENER_MAX_SIZE)
                .withKeepAlive(ThreadPoolProperties.DEFAULT_LISTENER_KEEP_ALIVE)
                .withQueueSize(ThreadPoolProperties.DEFAULT_LISTENER_QUEUE_SIZE)
                .build());
        Validate.isTrue(threadPools.size() == 2, "threadPools can only set action and listener");
        ThreadPoolProperties action = threadPools.get("action");
        if (action.coreSize == null || action.coreSize <= 0) {
            action.coreSize = ThreadPoolProperties.DEFAULT_ACTION_CORE_SIZE;
        } else if (action.coreSize < 1) {
            action.coreSize = 1;
        } else if (action.coreSize > 64) {
            action.coreSize = 64;
        }
        if (action.maxSize == null || action.maxSize <= 0) {
            action.maxSize = ThreadPoolProperties.DEFAULT_ACTION_MAX_SIZE;
        } else if (action.maxSize < action.coreSize) {
            action.maxSize = action.coreSize;
        } else if (action.maxSize > 128) {
            action.maxSize = 128;
        }
        if (action.keepAlive == null || action.keepAlive <= 0) {
            action.keepAlive = ThreadPoolProperties.DEFAULT_ACTION_KEEP_ALIVE;
        } else if (action.keepAlive < 10_000) {
            action.keepAlive = 10_000;
        } else if (action.keepAlive > 600_000) {
            action.keepAlive = 600_000;
        }
        if (action.queueSize == null || action.queueSize < 0) {
            action.queueSize = ThreadPoolProperties.DEFAULT_ACTION_QUEUE_SIZE;
        }
        ThreadPoolProperties listener = threadPools.get("listener");
        if (listener.coreSize == null || listener.coreSize <= 0) {
            listener.coreSize = ThreadPoolProperties.DEFAULT_LISTENER_CORE_SIZE;
        } else if (listener.coreSize < 1) {
            listener.coreSize = 1;
        } else if (listener.coreSize > 64) {
            listener.coreSize = 64;
        }
        if (listener.maxSize == null || listener.maxSize <= 0) {
            listener.maxSize = ThreadPoolProperties.DEFAULT_LISTENER_MAX_SIZE;
        } else if (listener.maxSize < listener.coreSize) {
            listener.maxSize = listener.coreSize;
        } else if (listener.maxSize < 128) {
            listener.maxSize = 128;
        }
        if (listener.keepAlive == null || listener.keepAlive <= 0) {
            listener.keepAlive = ThreadPoolProperties.DEFAULT_LISTENER_KEEP_ALIVE;
        } else if (listener.keepAlive < 10_000) {
            listener.keepAlive = 10_000;
        } else if (listener.keepAlive > 600_000) {
            listener.keepAlive = 600_000;
        }
        if (listener.queueSize == null || listener.queueSize <= 0) {
            listener.queueSize = ThreadPoolProperties.DEFAULT_LISTENER_QUEUE_SIZE;
        } else if (listener.queueSize < 10) {
            listener.queueSize = 10;
        } else if (listener.queueSize > 128) {
            listener.queueSize = 128;
        }
    }

    @Override
    public String toString() {
        return "RGWClientProperties{" +
                "applicationName='" + applicationName + '\'' +
                ", enableAdmin=" + enableAdmin +
                ", enableBucket=" + enableBucket +
                ", enableObject=" + enableObject +
                ", enableHystrix=" + enableHystrix +
                ", connector=" + connector +
                ", threadPools=" + threadPools +
                ", hystrixConfig=" + hystrixConfig +
                '}';
    }

    public static class ConnectorProperties {
        private static final String STORAGES_PREFIX = "storages";
        private static final String SUBSCRIBES_PREFIX = "subscribes";
        private static final String MAX_CONNECTIONS = "maxConnections";
        private static final String CONNECTION_TIMEOUT = "connectionTimeout";
        private static final String SOCKET_TIMEOUT = "socketTimeout";
        private static final String CONNECTION_MAX_IDLE = "connectionMaxIdle";
        private static final String ENABLE_GZIP = "enableGzip";
        private static final String ENABLE_KEEP_ALIVE = "enableKeepAlive";
        private static final String ENABLE_RETRY = "enableRetry";
        private static final String MAX_RETRIES = "maxRetries";
        private static final String BASE_DELAY_TIME = "baseDelayTime";
        private static final String MAX_BACKOFF_TIME = "maxBackoffTime";
        public static final Integer DEFAULT_MAX_CONNECTIONS = 30;
        public static final Integer DEFAULT_CONNECTION_TIMEOUT = 5_000;
        public static final Integer DEFAULT_SOCKET_TIMEOUT = 10_000;
        public static final Long DEFAULT_CONNECTION_MAX_IDLE = 60_000L;
        public static final Boolean DEFAULT_ENABLE_GZIP = true;
        public static final Boolean DEFAULT_ENABLE_KEEP_ALIVE = true;
        public static final Boolean DEFAULT_ENABLE_RETRY = false;
        public static final Integer DEFAULT_MAX_RETRIES = 3;
        public static final Integer DEFAULT_BASE_DELAY_TIME = 1500;
        public static final Integer DEFAULT_MAX_BACKOFF_TIME = 20_000;
        protected List<EndpointProperties> storages;
        protected List<EndpointProperties> subscribes;
        protected Integer maxConnections;
        protected Integer connectionTimeout;
        protected Integer socketTimeout;
        protected Long connectionMaxIdle;
        protected Boolean enableGzip;
        protected Boolean enableKeepAlive;
        protected Boolean enableRetry;
        protected Integer maxRetries;
        protected Integer baseDelayTime;
        protected Integer maxBackoffTime;

        public ConnectorProperties() {
        }

        private ConnectorProperties(ConnectorPropertiesBuilder builder) {
            this.storages = builder.storages;
            this.subscribes = builder.subscribes;
            this.maxConnections = builder.maxConnections;
            this.connectionTimeout = builder.connectionTimeout;
            this.socketTimeout = builder.socketTimeout;
            this.connectionMaxIdle = builder.connectionMaxIdle;
            this.enableGzip = builder.enableGzip;
            this.enableKeepAlive = builder.enableKeepAlive;
            this.enableRetry = builder.enableRetry;
            this.maxRetries = builder.maxRetries;
            this.baseDelayTime = builder.baseDelayTime;
            this.maxBackoffTime = builder.maxBackoffTime;
        }

        private ConnectorProperties(Configuration config) {
            List<Configuration> endpointConfigs = config.getSubConfigList(STORAGES_PREFIX);
            if (endpointConfigs != null && endpointConfigs.size() > 0) {
                this.storages = endpointConfigs.stream().map(EndpointProperties::new).collect(Collectors.toList());
            } else {
                this.storages = Collections.emptyList();
            }
            endpointConfigs = config.getSubConfigList(SUBSCRIBES_PREFIX);
            if (endpointConfigs != null && endpointConfigs.size() > 0) {
                this.subscribes = endpointConfigs.stream().map(EndpointProperties::new).collect(Collectors.toList());
            } else {
                this.subscribes = Collections.emptyList();
            }
            this.maxConnections = config.getInteger(MAX_CONNECTIONS, DEFAULT_MAX_CONNECTIONS);
            this.connectionTimeout = config.getInteger(CONNECTION_TIMEOUT, DEFAULT_CONNECTION_TIMEOUT);
            this.socketTimeout = config.getInteger(SOCKET_TIMEOUT, DEFAULT_SOCKET_TIMEOUT);
            this.connectionMaxIdle = config.getLong(CONNECTION_MAX_IDLE, DEFAULT_CONNECTION_MAX_IDLE);
            this.enableGzip = config.getBoolean(ENABLE_GZIP, DEFAULT_ENABLE_GZIP);
            this.enableKeepAlive = config.getBoolean(ENABLE_KEEP_ALIVE, DEFAULT_ENABLE_KEEP_ALIVE);
            this.enableRetry = config.getBoolean(ENABLE_RETRY, DEFAULT_ENABLE_RETRY);
            this.maxRetries = config.getInteger(MAX_RETRIES, DEFAULT_MAX_RETRIES);
            this.baseDelayTime = config.getInteger(BASE_DELAY_TIME, DEFAULT_BASE_DELAY_TIME);
            this.maxBackoffTime = config.getInteger(MAX_BACKOFF_TIME, DEFAULT_MAX_BACKOFF_TIME);
        }

        public List<EndpointProperties> getStorages() {
            return storages;
        }

        public List<EndpointProperties> getSubscribes() {
            return subscribes;
        }

        public Integer getMaxConnections() {
            return maxConnections;
        }

        public Integer getConnectionTimeout() {
            return connectionTimeout;
        }

        public Integer getSocketTimeout() {
            return socketTimeout;
        }

        public Long getConnectionMaxIdle() {
            return connectionMaxIdle;
        }

        public Boolean isEnableGzip() {
            return enableGzip;
        }

        public Boolean isEnableKeepAlive() {
            return enableKeepAlive;
        }

        public Boolean isEnableRetry() {
            return enableRetry;
        }

        public Integer getMaxRetries() {
            return maxRetries;
        }

        public Integer getBaseDelayTime() {
            return baseDelayTime;
        }

        public Integer getMaxBackoffTime() {
            return maxBackoffTime;
        }

        public void setStorages(List<EndpointProperties> storages) {
            this.storages = storages;
        }

        public void setSubscribes(List<EndpointProperties> subscribes) {
            this.subscribes = subscribes;
        }

        public void setMaxConnections(Integer maxConnections) {
            this.maxConnections = maxConnections;
        }

        public void setConnectionTimeout(Integer connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
        }

        public void setSocketTimeout(Integer socketTimeout) {
            this.socketTimeout = socketTimeout;
        }

        public void setConnectionMaxIdle(Long connectionMaxIdle) {
            this.connectionMaxIdle = connectionMaxIdle;
        }

        public void setEnableGzip(Boolean enableGzip) {
            this.enableGzip = enableGzip;
        }

        public void setEnableKeepAlive(Boolean enableKeepAlive) {
            this.enableKeepAlive = enableKeepAlive;
        }

        public void setEnableRetry(Boolean enableRetry) {
            this.enableRetry = enableRetry;
        }

        public void setMaxRetries(Integer maxRetries) {
            this.maxRetries = maxRetries;
        }

        public void setBaseDelayTime(Integer baseDelayTime) {
            this.baseDelayTime = baseDelayTime;
        }

        public void setMaxBackoffTime(Integer maxBackoffTime) {
            this.maxBackoffTime = maxBackoffTime;
        }

        private void validate() {
            if ((storages == null || storages.size() == 0) && (subscribes == null || subscribes.size() == 0)) {
                throw new IllegalArgumentException("none of storages and subscribes is set");
            }
            if (storages != null && storages.size() > 0) {
                if (storages.stream().map(EndpointProperties::getEndpoint).distinct().count() != storages.size()) {
                    throw new IllegalArgumentException("cannot specify same endpoint");
                }
                for (EndpointProperties properties : storages) {
                    Validate.notBlank(properties.getAccessKey(), "accessKey cannot be empty string");
                    Validate.notBlank(properties.getSecretKey(), "secretKey cannot be empty string");
                    properties.validate();
                }
            }
            if (subscribes != null && subscribes.size() > 0) {
                if (subscribes.stream().map(EndpointProperties::getEndpoint).distinct().count() != subscribes.size()) {
                    throw new IllegalArgumentException("cannot specify same endpoint");
                }
                subscribes.forEach(EndpointProperties::validate);
            }
            if (maxConnections == null || maxConnections <= 0) {
                LOGGER.warn("Invalid maxConnections value [{}], set to 30.", maxConnections);
                maxConnections = DEFAULT_MAX_CONNECTIONS;
            } else if (maxConnections > 100) {
                LOGGER.warn("maxConnections value [{}] too large, set to 100.", maxConnections);
                maxConnections = 100;
            }
            if (connectionTimeout == null || connectionTimeout < 0) {
                LOGGER.warn("Invalid connectionTimeout value [{}], set to 10000.", connectionTimeout);
                connectionTimeout = DEFAULT_CONNECTION_TIMEOUT;
            } else if (connectionTimeout < 1000) {
                LOGGER.warn("Value connectionTimeout [{}] too short, set to 1000.", connectionTimeout);
                connectionTimeout = 1000;
            } else if (connectionTimeout > 6_000) {
                LOGGER.warn("Value connectionTimeout [{}] too long, set to 6000.", connectionTimeout);
                connectionTimeout = 6_000;
            }
            if (socketTimeout == null || socketTimeout < 0) {
                LOGGER.warn("Invalid socketTimeout value [{}], set to 50000.", socketTimeout);
                socketTimeout = DEFAULT_SOCKET_TIMEOUT;
            } else if (socketTimeout < 5000) {
                LOGGER.warn("Value socketTimeout [{}] too short, set to 5000.", socketTimeout);
                socketTimeout = 5000;
            } else if (socketTimeout > 20_000) {
                LOGGER.warn("Value socketTimeout [{}] too long, set to 20000.", socketTimeout);
                socketTimeout = 20_000;
            }
            if (connectionMaxIdle == null || connectionMaxIdle < 0) {
                LOGGER.warn("Invalid connectionMaxIdle value [{}], set to 60000.", connectionMaxIdle);
                connectionMaxIdle = DEFAULT_CONNECTION_MAX_IDLE;
            } else if (connectionMaxIdle < 10_000L) {
                LOGGER.warn("Value connectionMaxIdle [{}] too short, set to 10000.", connectionMaxIdle);
                connectionMaxIdle = 10_000L;
            } else if (connectionMaxIdle > 600_000) {
                LOGGER.warn("Value connectionMaxIdle [{}] too long, set to 600000.", connectionMaxIdle);
                connectionMaxIdle = 600_000L;
            }
            if (enableGzip == null) {
                enableGzip = DEFAULT_ENABLE_GZIP;
            }
            if (enableKeepAlive == null) {
                enableKeepAlive = DEFAULT_ENABLE_KEEP_ALIVE;
            }
            if (enableRetry == null) {
                enableRetry = DEFAULT_ENABLE_RETRY;
            }
            if (enableRetry) {
                if (maxRetries == null || maxRetries < 0) {
                    maxRetries = DEFAULT_MAX_RETRIES;
                } else if (maxRetries > 3) {
                    maxRetries = 3;
                }
                if (baseDelayTime == null || baseDelayTime < 0) {
                    baseDelayTime = DEFAULT_BASE_DELAY_TIME;
                } else if (baseDelayTime < 1000) {
                    baseDelayTime = 1000;
                } else if (baseDelayTime > 5_000) {
                    baseDelayTime = 5_000;
                }
                if (maxBackoffTime == null || maxBackoffTime < 0) {
                    maxBackoffTime = DEFAULT_MAX_BACKOFF_TIME;
                } else if (maxBackoffTime < baseDelayTime) {
                    maxBackoffTime = baseDelayTime;
                } else if (maxBackoffTime > 30_000) {
                    maxBackoffTime = 30_000;
                }
            }
        }

        @Override
        public String toString() {
            return "ConnectorProperties{" +
                    "storages=" + storages +
                    ", subscribes='" + subscribes + '\'' +
                    ", maxConnections=" + maxConnections +
                    ", connectionTimeout=" + connectionTimeout +
                    ", socketTimeout=" + socketTimeout +
                    ", connectionMaxIdle=" + connectionMaxIdle +
                    ", enableGzip=" + enableGzip +
                    ", enableKeepAlive=" + enableKeepAlive +
                    ", enableRetry=" + enableRetry +
                    ", maxRetries=" + maxRetries +
                    ", baseDelayTime=" + baseDelayTime +
                    ", maxBackoffTime=" + maxBackoffTime +
                    '}';
        }
    }

    public static class EndpointProperties {
        private static final String ENDPOINT = "endpoint";
        private static final String REGION = "region";
        private static final String PROTOCOL = "protocol";
        private static final String ACCESS_KEY = "accessKey";
        private static final String SECRET_KEY = "secretKey";
        public static final String DEFAULT_PROTOCOL = "http";
        protected String endpoint;
        protected String region;
        protected String protocol;
        protected String accessKey;
        protected String secretKey;

        public EndpointProperties() {
        }

        private EndpointProperties(EndpointPropertiesBuilder builder) {
            this.endpoint = builder.endpoint;
            this.region = builder.region;
            this.protocol = builder.protocol;
            this.accessKey = builder.accessKey;
            this.secretKey = builder.secretKey;
        }

        private EndpointProperties(Configuration config) {
            this.endpoint = config.getString(ENDPOINT);
            this.region = config.getString(REGION, "").trim();
            this.protocol = config.getString(PROTOCOL, DEFAULT_PROTOCOL);
            this.accessKey = config.getString(ACCESS_KEY, "").trim();
            this.secretKey = config.getString(SECRET_KEY, "").trim();
        }

        public String getEndpoint() {
            return endpoint;
        }

        public String getRegion() {
            return region;
        }

        public String getProtocol() {
            return protocol;
        }

        public String getAccessKey() {
            return accessKey;
        }

        public String getSecretKey() {
            return secretKey;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public void setProtocol(String protocol) {
            this.protocol = protocol;
        }

        public void setAccessKey(String accessKey) {
            this.accessKey = accessKey;
        }

        public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
        }

        private void validate() {
            Validate.notBlank(endpoint, "endpoint cannot be empty string");
            try {
                URI uri = new URI(protocol + "://" + endpoint);
                if (StringUtils.isBlank(uri.getHost()) || AddressUtil.getPort(uri) < 0) {
                    throw new IllegalArgumentException("invalid endpoint: " + endpoint);
                }
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException("invalid endpoint: " + endpoint, e);
            }
            if (StringUtils.isBlank(protocol)) {
                LOGGER.info("Protocol is empty, set to http.");
                protocol = DEFAULT_PROTOCOL;
            } else {
                protocol = protocol.toLowerCase();
                if (!"http".equals(protocol) && !"https".equals(protocol)) {
                    LOGGER.warn("Invalid protocol value [{}], set to http", protocol);
                    protocol = DEFAULT_PROTOCOL;
                }
            }
        }

        @Override
        public String toString() {
            return "EndpointProperties{" +
                    "endpoint='" + endpoint + '\'' +
                    ", region='" + region + '\'' +
                    ", protocol='" + protocol + '\'' +
                    ", hash(accessKey)='" + (accessKey == null ? null : accessKey.hashCode()) + '\'' +
                    ", hash(secretKey)='" + (secretKey == null ? null : secretKey.hashCode()) + '\'' +
                    '}';
        }
    }

    public static class ThreadPoolProperties {
        private static final String CORE_SIZE = "coreSize";
        private static final String MAX_SIZE = "maxSize";
        private static final String KEEP_ALIVE = "keepAlive";
        private static final String QUEUE_SIZE = "queueSize";
        public static final Integer DEFAULT_ACTION_CORE_SIZE = Runtime.getRuntime().availableProcessors();
        public static final Integer DEFAULT_ACTION_MAX_SIZE = DEFAULT_ACTION_CORE_SIZE * 4;
        public static final Integer DEFAULT_ACTION_KEEP_ALIVE = 60_000;
        public static final Integer DEFAULT_ACTION_QUEUE_SIZE = 0;
        public static final Integer DEFAULT_LISTENER_CORE_SIZE = Runtime.getRuntime().availableProcessors();
        public static final Integer DEFAULT_LISTENER_MAX_SIZE = DEFAULT_LISTENER_CORE_SIZE;
        public static final Integer DEFAULT_LISTENER_KEEP_ALIVE = 0;
        public static final Integer DEFAULT_LISTENER_QUEUE_SIZE = 20;
        protected Integer coreSize;
        protected Integer maxSize;
        protected Integer keepAlive;
        protected Integer queueSize;

        public ThreadPoolProperties() {
        }

        private ThreadPoolProperties(ThreadPoolPropertiesBuilder builder) {
            this.coreSize = builder.coreSize;
            this.maxSize = builder.maxSize;
            this.keepAlive = builder.keepAlive;
            this.queueSize = builder.queueSize;
        }

        private ThreadPoolProperties(Configuration config) {
            this.coreSize = config.getInteger(CORE_SIZE);
            this.maxSize = config.getInteger(MAX_SIZE);
            this.keepAlive = config.getInteger(KEEP_ALIVE);
            this.queueSize = config.getInteger(QUEUE_SIZE);
        }

        public Integer getCoreSize() {
            return coreSize;
        }

        public Integer getMaxSize() {
            return maxSize;
        }

        public Integer getKeepAlive() {
            return keepAlive;
        }

        public Integer getQueueSize() {
            return queueSize;
        }

        public void setCoreSize(Integer coreSize) {
            this.coreSize = coreSize;
        }

        public void setMaxSize(Integer maxSize) {
            this.maxSize = maxSize;
        }

        public void setKeepAlive(Integer keepAlive) {
            this.keepAlive = keepAlive;
        }

        public void setQueueSize(Integer queueSize) {
            this.queueSize = queueSize;
        }

        @Override
        public String toString() {
            return "ThreadPoolProperties{" +
                    "coreSize=" + coreSize +
                    ", maxSize=" + maxSize +
                    ", keepAlive=" + keepAlive +
                    ", queueSize=" + queueSize +
                    '}';
        }
    }

    public static final class Builder {
        private String applicationName;
        private Boolean enableAdmin;
        private Boolean enableBucket;
        private Boolean enableObject;
        private Boolean enableHystrix;
        private ConnectorProperties connector;
        private Map<String, ThreadPoolProperties> threadPools;
        private Map<String, String> hystrix;

        public Builder() {
            this.threadPools = new HashMap<>(2);
            this.hystrix = new HashMap<>();
        }

        public Builder withApplicationName(String applicationName) {
            this.applicationName = applicationName;
            return this;
        }

        public Builder withEnableAdmin(Boolean enableAdmin) {
            this.enableAdmin = enableAdmin;
            return this;
        }

        public Builder withEnableBucket(Boolean enableBucket) {
            this.enableBucket = enableBucket;
            return this;
        }

        public Builder withEnableObject(Boolean enableObject) {
            this.enableObject = enableObject;
            return this;
        }

        public Builder withEnableHystrix(Boolean enableHystrix) {
            this.enableHystrix = enableHystrix;
            return this;
        }

        public Builder withConnector(ConnectorProperties connector) {
            this.connector = connector;
            return this;
        }

        public ConnectorPropertiesBuilder withConnector() {
            return new ConnectorPropertiesBuilder(this);
        }

        public ThreadPoolPropertiesBuilder withActionThreadPool() {
            return new ThreadPoolPropertiesBuilder("action", this);
        }

        public ThreadPoolPropertiesBuilder withListenerThreadPool() {
            return new ThreadPoolPropertiesBuilder("listener", this);
        }

        private Builder withThreadPool(String name, ThreadPoolProperties threadPool) {
            this.threadPools.put(name, threadPool);
            return this;
        }

        public Builder addHystrix(String key, String value) {
            this.hystrix.put(key, value);
            return this;
        }

        public RGWClientProperties build() {
            return new RGWClientProperties(this);
        }
    }

    public static class ConnectorPropertiesBuilder {
        final Builder builder;
        private List<EndpointProperties> storages;
        private List<EndpointProperties> subscribes;
        private Integer maxConnections;
        private Integer connectionTimeout;
        private Integer socketTimeout;
        private Long connectionMaxIdle;
        private Boolean enableGzip;
        private Boolean enableKeepAlive;
        private Boolean enableRetry;
        private Integer maxRetries;
        private Integer baseDelayTime;
        private Integer maxBackoffTime;

        private ConnectorPropertiesBuilder(Builder builder) {
            this.builder = builder;
            this.storages = new LinkedList<>();
            this.subscribes = new LinkedList<>();
        }

        public ConnectorPropertiesBuilder withStorages(List<EndpointProperties> storages) {
            this.storages.clear();
            this.storages.addAll(storages);
            return this;
        }

        public ConnectorPropertiesBuilder withStorage(EndpointProperties storage) {
            this.storages.add(storage);
            return this;
        }

        public StorageEndpointPropertiesBuilder withStorage() {
            return new StorageEndpointPropertiesBuilder(this);
        }

        public ConnectorPropertiesBuilder withSubscribes(List<EndpointProperties> subscribes) {
            this.subscribes.clear();
            this.subscribes.addAll(subscribes);
            return this;
        }

        public ConnectorPropertiesBuilder withSubscribe(EndpointProperties subscribe) {
            this.subscribes.add(subscribe);
            return this;
        }

        public SubscribeEndpointPropertiesBuilder withSubscribe() {
            return new SubscribeEndpointPropertiesBuilder(this);
        }

        public ConnectorPropertiesBuilder withMaxConnections(Integer maxConnections) {
            this.maxConnections = maxConnections;
            return this;
        }

        public ConnectorPropertiesBuilder withConnectionTimeout(Integer connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        public ConnectorPropertiesBuilder withSocketTimeout(Integer socketTimeout) {
            this.socketTimeout = socketTimeout;
            return this;
        }

        public ConnectorPropertiesBuilder withConnectionMaxIdle(Long connectionMaxIdle) {
            this.connectionMaxIdle = connectionMaxIdle;
            return this;
        }

        public ConnectorPropertiesBuilder withEnableGzip(Boolean enableGzip) {
            this.enableGzip = enableGzip;
            return this;
        }

        public ConnectorPropertiesBuilder withEnableKeepAlive(Boolean enableKeepAlive) {
            this.enableKeepAlive = enableKeepAlive;
            return this;
        }

        public ConnectorPropertiesBuilder withEnableRetry(Boolean enableRetry) {
            this.enableRetry = enableRetry;
            return this;
        }

        public ConnectorPropertiesBuilder withMaxRetries(Integer maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public ConnectorPropertiesBuilder withBaseDelayTime(Integer baseDelayTime) {
            this.baseDelayTime = baseDelayTime;
            return this;
        }

        public ConnectorPropertiesBuilder withMaxBackoffTime(Integer maxBackoffTime) {
            this.maxBackoffTime = maxBackoffTime;
            return this;
        }

        public Builder endConnector() {
            return builder.withConnector(new ConnectorProperties(this));
        }
    }

    public static class StorageEndpointPropertiesBuilder extends EndpointPropertiesBuilder<StorageEndpointPropertiesBuilder> {

        private StorageEndpointPropertiesBuilder(ConnectorPropertiesBuilder builder) {
            super(builder);
        }

        public ConnectorPropertiesBuilder endStorage() {
            return builder.withStorage(build());
        }
    }

    public static class SubscribeEndpointPropertiesBuilder extends EndpointPropertiesBuilder<SubscribeEndpointPropertiesBuilder> {

        private SubscribeEndpointPropertiesBuilder(ConnectorPropertiesBuilder builder) {
            super(builder);
        }

        public ConnectorPropertiesBuilder endSubscribe() {
            return builder.withSubscribe(build());
        }
    }


    public static class EndpointPropertiesBuilder<T extends EndpointPropertiesBuilder<T>> {
        final ConnectorPropertiesBuilder builder;
        private String endpoint;
        private String region;
        private String protocol;
        private String accessKey;
        private String secretKey;

        private EndpointPropertiesBuilder(ConnectorPropertiesBuilder builder) {
            this.builder = builder;
        }

        public T withEndpoint(String endpoint) {
            this.endpoint = endpoint;
            return self();
        }

        public T withRegion(String region) {
            this.region = region;
            return self();
        }

        public T withProtocol(String protocol) {
            this.protocol = protocol;
            return self();
        }

        public T withAccessKey(String accessKey) {
            this.accessKey = accessKey;
            return self();
        }

        public T withSecretKey(String secretKey) {
            this.secretKey = secretKey;
            return self();
        }

        @SuppressWarnings("unchecked")
        private T self() {
            return (T) this;
        }

        EndpointProperties build() {
            return new EndpointProperties(this);
        }
    }

    public static final class ThreadPoolPropertiesBuilder {
        private final String name;
        private final Builder builder;
        private Integer coreSize;
        private Integer maxSize;
        private Integer keepAlive;
        private Integer queueSize;

        private ThreadPoolPropertiesBuilder() {
            this.name = null;
            this.builder = null;
        }

        private ThreadPoolPropertiesBuilder(String name, Builder builder) {
            this.name = name;
            this.builder = builder;
        }

        public ThreadPoolPropertiesBuilder withCoreSize(Integer coreSize) {
            this.coreSize = coreSize;
            return this;
        }

        public ThreadPoolPropertiesBuilder withMaxSize(Integer maxSize) {
            this.maxSize = maxSize;
            return this;
        }

        public ThreadPoolPropertiesBuilder withKeepAlive(Integer keepAlive) {
            this.keepAlive = keepAlive;
            return this;
        }

        public ThreadPoolPropertiesBuilder withQueueSize(Integer queueSize) {
            this.queueSize = queueSize;
            return this;
        }

        private ThreadPoolProperties build() {
            return new ThreadPoolProperties(this);
        }

        public Builder endThreadPool() {
            return builder.withThreadPool(name, build());
        }
    }

    public static RGWClientProperties loadFromFile(String file) {
        try {
            return loadFromConfig(new Configuration(new File(file)));
        } catch (IOException e) {
            throw new RGWException(e);
        }
    }

    public static RGWClientProperties loadFromResource(String resource) {
        try {
            return loadFromConfig(new Configuration(resource));
        } catch (IOException e) {
            throw new RGWException(e);
        }
    }

    public static RGWClientProperties loadFromConfig(Configuration config) {
        return new RGWClientProperties(config);
    }
}
