package io.ceph.rgw.client.core.object;

import io.ceph.rgw.client.model.GetObjectInfoResponse;
import io.ceph.rgw.client.model.InitiateMultipartUploadResponse;
import io.ceph.rgw.client.model.MultipartUploadPartResponse;
import org.junit.After;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/7/9.
 */
@Category(ObjectTests.class)
public class MultipartUploadTest extends BaseObjectClientTest {
    @After
    @Override
    public void tearDown() throws Exception {
        objectClient.prepareDeleteObject()
                .withBucketName(bucket)
                .withKey(key)
                .run();
        super.tearDown();
    }

    @Test
    public void testSync() {
        InitiateMultipartUploadResponse resp1 = logResponse(objectClient.prepareInitiateMultipartUpload()
                .withBucketName(bucket)
                .withKey(key)
                .withMetadata()
                .add("xxxxx", "xxxxx")
                .endMetadata()
                .run());
        MultipartUploadPartResponse resp2 = logResponse(objectClient.prepareUploadFile()
                .withBucketName(bucket)
                .withKey(key)
                .withPartNumber(111)
                .withUpload("C:\\Users\\T_zhuangshuo_kzx\\Downloads\\中信银行信用卡中心运维管理平台-配置信息库上线部署方案V4.doc")
                .withUploadId(resp1.getUploadId())
                .run());
        MultipartUploadPartResponse resp3 = logResponse(objectClient.prepareUploadString()
                .withBucketName(bucket)
                .withKey(key)
                .withPartNumber(222)
                .withUpload("C:\\Users\\T_zhuangshuo_kzx\\Downloads\\中信银行信用卡中心运维管理平台-配置信息库上线部署方案V4.doc")
                .withUploadId(resp1.getUploadId())
                .run());
        logResponse(objectClient.prepareCompleteMultipartUpload()
                .withBucketName(bucket)
                .withKey(key)
                .addPartETag(111, resp2.getETag())
                .addPartETag(222, resp3.getETag())
                .withUploadId(resp1.getUploadId())
                .run());
        GetObjectInfoResponse resp4 = logResponse(objectClient.prepareGetObjectInfo()
                .withBucketName(bucket)
                .withKey(key)
                .run());
    }

    @Override
    boolean isCreateObject() {
        return false;
    }
}
