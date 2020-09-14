package io.ceph.rgw.client.core.async;

import software.amazon.awssdk.auth.signer.AwsS3V4Signer;
import software.amazon.awssdk.awscore.client.builder.AwsAsyncClientBuilder;
import software.amazon.awssdk.awscore.client.builder.AwsDefaultClientBuilder;
import software.amazon.awssdk.core.client.config.SdkAdvancedClientOption;
import software.amazon.awssdk.core.client.config.SdkClientConfiguration;
import software.amazon.awssdk.core.client.config.SdkClientOption;
import software.amazon.awssdk.core.interceptor.ClasspathInterceptorChainFactory;
import software.amazon.awssdk.core.interceptor.ExecutionInterceptor;
import software.amazon.awssdk.core.signer.Signer;
import software.amazon.awssdk.services.s3.S3BaseClientBuilder;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.utils.CollectionUtils;

import java.util.List;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/7/29.
 */
class AdminAsyncConnectorBuilder extends AwsDefaultClientBuilder<AdminAsyncConnectorBuilder, AdminAsyncConnector> implements AwsAsyncClientBuilder<AdminAsyncConnectorBuilder, AdminAsyncConnector>, S3BaseClientBuilder<AdminAsyncConnectorBuilder, AdminAsyncConnector> {
    @Override
    protected String serviceEndpointPrefix() {
        return "admin";
    }

    @Override
    protected String signingName() {
        return "admin";
    }

    @Override
    protected String serviceName() {
        return "admin";
    }

    @Override
    protected SdkClientConfiguration mergeServiceDefaults(SdkClientConfiguration config) {
        return config.merge(c -> c.option(SdkAdvancedClientOption.SIGNER, defaultSigner())
                .option(SdkClientOption.CRC32_FROM_COMPRESSED_DATA_ENABLED, false)
                .option(SdkClientOption.SERVICE_CONFIGURATION, S3Configuration.builder().build()));
    }

    @Override
    protected SdkClientConfiguration finalizeServiceConfiguration(SdkClientConfiguration config) {
        ClasspathInterceptorChainFactory interceptorFactory = new ClasspathInterceptorChainFactory();
        List<ExecutionInterceptor> interceptors = interceptorFactory
                .getInterceptors("software/amazon/awssdk/services/s3/execution.interceptors");
        interceptors = CollectionUtils.mergeLists(interceptors, config.option(SdkClientOption.EXECUTION_INTERCEPTORS));
        S3Configuration.Builder c = ((S3Configuration) config.option(SdkClientOption.SERVICE_CONFIGURATION)).toBuilder();
        c.profileFile(c.profileFile() != null ? c.profileFile() : config.option(SdkClientOption.PROFILE_FILE)).profileName(
                c.profileName() != null ? c.profileName() : config.option(SdkClientOption.PROFILE_NAME));
        return config.toBuilder().option(SdkClientOption.EXECUTION_INTERCEPTORS, interceptors)
                .option(SdkClientOption.SERVICE_CONFIGURATION, c.build()).build();
    }

    private Signer defaultSigner() {
        return AwsS3V4Signer.create();
    }

    @Override
    protected AdminAsyncConnector buildClient() {
        return new AdminAsyncConnector(super.asyncClientConfiguration());
    }

    @Override
    public AdminAsyncConnectorBuilder serviceConfiguration(S3Configuration serviceConfiguration) {
        clientConfiguration.option(SdkClientOption.SERVICE_CONFIGURATION, serviceConfiguration);
        return this;
    }
}
