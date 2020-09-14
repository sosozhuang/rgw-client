package io.ceph.rgw.client.model.admin.tranform;

import io.ceph.rgw.client.model.admin.UnlinkBucketRequest;
import software.amazon.awssdk.http.SdkHttpMethod;
import software.amazon.awssdk.protocols.core.OperationInfo;
import software.amazon.awssdk.protocols.xml.AwsXmlProtocolFactory;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/7/31.
 */
public class UnlinkBucketRequestMarshaller extends AdminRequestMarshaller<UnlinkBucketRequest> {
    private static final OperationInfo SDK_OPERATION_BINDING = OperationInfo.builder().requestUri("/admin/bucket?format=xml")
            .httpMethod(SdkHttpMethod.POST).hasExplicitPayloadMember(false).hasPayloadMembers(false)
            .putAdditionalMetadata(AwsXmlProtocolFactory.ROOT_MARSHALL_LOCATION_ATTRIBUTE, null)
            .putAdditionalMetadata(AwsXmlProtocolFactory.XML_NAMESPACE_ATTRIBUTE, null).build();

    public UnlinkBucketRequestMarshaller(AwsXmlProtocolFactory protocolFactory) {
        super(protocolFactory);
    }

    @Override
    protected OperationInfo getOperationInfo() {
        return SDK_OPERATION_BINDING;
    }
}
