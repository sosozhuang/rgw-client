package io.ceph.rgw.client.core.async;

import io.ceph.rgw.client.config.RGWClientProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.utils.StringUtils;

import java.net.URI;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/7/2.
 */
public class S3AsyncConnectors extends AsyncConnectors<S3AsyncClient> {
    private static final Logger LOGGER = LoggerFactory.getLogger(S3AsyncConnectors.class);

    public S3AsyncConnectors(RGWClientProperties.ConnectorProperties properties) {
        super(properties);
    }

    @Override
    protected S3AsyncClient create(RGWClientProperties.EndpointProperties endpointProperties) {
        StaticCredentialsProvider provider = StaticCredentialsProvider.create(AwsBasicCredentials.create(endpointProperties.getAccessKey(), endpointProperties.getSecretKey()));
        S3AsyncClient asyncClient = S3AsyncClient.builder()
                .credentialsProvider(provider)
                .endpointOverride(URI.create(endpointProperties.getProtocol() + "://" + endpointProperties.getEndpoint()))
                .region(StringUtils.isNotBlank(endpointProperties.getRegion()) ? Region.of(endpointProperties.getRegion()) : Region.US_WEST_2)
                .httpClient(httpClient)
                .overrideConfiguration(overrideConf)
                .build();
        getLogger().debug("Finished to create s3 async connector for [{}]", endpointProperties.getEndpoint());
        return asyncClient;
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}
