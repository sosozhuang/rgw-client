package io.ceph.rgw.client;

import io.ceph.rgw.client.core.admin.*;
import io.ceph.rgw.client.core.bucket.*;
import io.ceph.rgw.client.core.object.*;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Categories.class)
@Categories.IncludeCategory({AdminTests.class, BucketTests.class, ObjectTests.class})
@Suite.SuiteClasses({BucketQuotaTest.class, CheckBucketIndexTest.class, GetBucketInfoTest.class,
        GetPolicyTest.class, GetUsageTest.class, GetUserInfoTest.class, KeyTest.class,
        LinkBucketTest.class, ModifyUserTest.class, RemoveBucketTest.class, SubUserTest.class,
        TrimUsageTest.class, UserCapabilityTest.class, UserQuotaTest.class, UserTest.class,
        BucketACLTest.class, BucketTest.class, BucketVersioningTest.class, GetBucketLocationTest.class,
        GetBucketTest.class, ListBucketMultipartUploadsTest.class, ListBucketsTest.class,
        ByteBufMultiWriterTest.class, ByteBufSingleWriterTest.class, CopyObjectTest.class,
        FileObjectWriterTest.class, FileTest.class, GetObjectInfoTest.class, InputStreamTest.class,
        MultipartUploadTest.class, ObjectACLTest.class, PutObjectTest.class, StringTest.class})
public class AsyncTests {
}
