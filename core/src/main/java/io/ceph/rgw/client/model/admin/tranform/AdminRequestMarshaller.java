package io.ceph.rgw.client.model.admin.tranform;

import io.ceph.rgw.client.model.admin.AdminRequest;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.runtime.transform.Marshaller;
import software.amazon.awssdk.http.SdkHttpFullRequest;
import software.amazon.awssdk.protocols.core.OperationInfo;
import software.amazon.awssdk.protocols.xml.AwsXmlProtocolFactory;

import java.util.Objects;

/**
 * Generic class of {@link AdminRequest} marshaller.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/8/3.
 */
abstract class AdminRequestMarshaller<T extends AdminRequest> implements Marshaller<T> {
    private final AwsXmlProtocolFactory protocolFactory;

    AdminRequestMarshaller(AwsXmlProtocolFactory protocolFactory) {
        this.protocolFactory = protocolFactory;
    }

    protected abstract OperationInfo getOperationInfo();

    @Override
    public SdkHttpFullRequest marshall(T in) {
        Objects.requireNonNull(in);
        try {
            return protocolFactory.createProtocolMarshaller(getOperationInfo()).marshall(in);
        } catch (Exception e) {
            throw SdkClientException.builder().message("Unable to marshall request: " + e.getMessage()).cause(e).build();
        }
    }
}
