package io.ceph.rgw.client.model.admin;

import io.ceph.rgw.client.AdminClient;
import io.ceph.rgw.client.model.GenericBuilder;
import io.ceph.rgw.client.model.RequestBuilder;
import software.amazon.awssdk.awscore.AwsRequestOverrideConfiguration;
import software.amazon.awssdk.services.s3.model.S3Request;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Created by zhuangshuo on 2020/3/14.
 */
abstract class AdminRequestBuilder<T extends AdminRequestBuilder<T, REQ, RESP>, REQ extends AdminRequest, RESP extends AdminResponse> extends GenericBuilder<T, REQ> implements RequestBuilder<REQ, RESP>, S3Request.Builder {
    protected final AdminClient client;
    private AwsRequestOverrideConfiguration awsRequestOverrideConfig;

    AdminRequestBuilder() {
        this.client = null;
    }

    AdminRequestBuilder(AdminClient client) {
        this.client = Objects.requireNonNull(client);
    }

    @Override
    public AwsRequestOverrideConfiguration overrideConfiguration() {
        return awsRequestOverrideConfig;
    }

    @Override
    public T overrideConfiguration(AwsRequestOverrideConfiguration awsRequestOverrideConfig) {
        this.awsRequestOverrideConfig = awsRequestOverrideConfig;
        return self();
    }

    @Override
    public T overrideConfiguration(Consumer<AwsRequestOverrideConfiguration.Builder> consumer) {
        AwsRequestOverrideConfiguration.Builder b = AwsRequestOverrideConfiguration.builder();
        consumer.accept(b);
        awsRequestOverrideConfig = b.build();
        return self();
    }
}
