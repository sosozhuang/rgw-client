package io.ceph.rgw.client.model.admin.tranform;

import io.ceph.rgw.client.model.admin.SetUserQuotaRequest;
import software.amazon.awssdk.http.SdkHttpMethod;
import software.amazon.awssdk.protocols.core.OperationInfo;
import software.amazon.awssdk.protocols.xml.AwsXmlProtocolFactory;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/7/31.
 */
public class SetUserQuotaRequestMarshaller extends AdminRequestMarshaller<SetUserQuotaRequest> {
    private static final OperationInfo SDK_OPERATION_BINDING = OperationInfo.builder().requestUri("/admin/user?quota&quota-type=user")
            .httpMethod(SdkHttpMethod.PUT).hasExplicitPayloadMember(false).hasPayloadMembers(false)
            .putAdditionalMetadata(AwsXmlProtocolFactory.ROOT_MARSHALL_LOCATION_ATTRIBUTE, null)
            .putAdditionalMetadata(AwsXmlProtocolFactory.XML_NAMESPACE_ATTRIBUTE, null).build();

    public SetUserQuotaRequestMarshaller(AwsXmlProtocolFactory protocolFactory) {
        super(protocolFactory);
    }

    @Override
    protected OperationInfo getOperationInfo() {
        return SDK_OPERATION_BINDING;
    }
}
