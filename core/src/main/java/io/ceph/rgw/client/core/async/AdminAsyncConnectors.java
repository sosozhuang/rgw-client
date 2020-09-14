package io.ceph.rgw.client.core.async;

import io.ceph.rgw.client.config.RGWClientProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.utils.StringUtils;

import java.net.URI;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/7/2.
 */
public class AdminAsyncConnectors extends AsyncConnectors<AdminAsyncConnector> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminAsyncConnectors.class);

    public AdminAsyncConnectors(RGWClientProperties.ConnectorProperties properties) {
        super(properties);
    }

    @Override
    protected AdminAsyncConnector create(RGWClientProperties.EndpointProperties endpointProperties) {
        StaticCredentialsProvider provider = StaticCredentialsProvider.create(AwsBasicCredentials.create(endpointProperties.getAccessKey(), endpointProperties.getSecretKey()));
        AdminAsyncConnector adminConnector = AdminAsyncConnector.builder()
                .credentialsProvider(provider)
                .endpointOverride(URI.create(endpointProperties.getProtocol() + "://" + endpointProperties.getEndpoint()))
                .region(StringUtils.isNotBlank(endpointProperties.getRegion()) ? Region.of(endpointProperties.getRegion()) : Region.US_WEST_2)
                .httpClient(httpClient)
                .overrideConfiguration(overrideConf)
                .build();
        getLogger().debug("Finished to create admin async connector for [{}]", endpointProperties.getEndpoint());
        return adminConnector;
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}
