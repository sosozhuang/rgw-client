package io.ceph.rgw.client.model.admin.tranform;

import io.ceph.rgw.client.model.admin.RemoveSubUserRequest;
import software.amazon.awssdk.http.SdkHttpMethod;
import software.amazon.awssdk.protocols.core.OperationInfo;
import software.amazon.awssdk.protocols.xml.AwsXmlProtocolFactory;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/7/31.
 */
public class RemoveSubUserRequestMarshaller extends AdminRequestMarshaller<RemoveSubUserRequest> {
    private static final OperationInfo SDK_OPERATION_BINDING = OperationInfo.builder().requestUri("/admin/user?format=xml")
            .httpMethod(SdkHttpMethod.DELETE).hasExplicitPayloadMember(false).hasPayloadMembers(false)
            .putAdditionalMetadata(AwsXmlProtocolFactory.ROOT_MARSHALL_LOCATION_ATTRIBUTE, null)
            .putAdditionalMetadata(AwsXmlProtocolFactory.XML_NAMESPACE_ATTRIBUTE, null).build();

    public RemoveSubUserRequestMarshaller(AwsXmlProtocolFactory protocolFactory) {
        super(protocolFactory);
    }

    @Override
    protected OperationInfo getOperationInfo() {
        return SDK_OPERATION_BINDING;
    }
}
