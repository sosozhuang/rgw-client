package io.ceph.rgw.client.core.bucket;

import io.ceph.rgw.client.core.BaseClientTest;
import io.ceph.rgw.client.model.CannedACL;
import io.ceph.rgw.client.model.GroupGrantee;
import io.ceph.rgw.client.model.Permission;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/7/9.
 */
@Category(BucketTests.class)
public class BucketACLTest extends BaseBucketClientTest {

    @Test
    public void testSync() {
        logResponse(bucketClient.preparePutBucketACL()
                .withBucketName(bucket)
                .withCannedACL(CannedACL.PUBLIC_READ)
                .run());
        logResponse(bucketClient.prepareGetBucketACL().withBucketName(bucket).run());
    }

    @Test
    public void testCallback() throws InterruptedException {
        BaseClientTest.Latch latch = newLatch();
        bucketClient.preparePutBucketACL()
                .withBucketName(bucket)
                .withACL()
                .withOwner("admin", "admin")
                .addGrant()
                .withGrantee(GroupGrantee.AUTHENTICATED_USERS)
                .withPermission(Permission.READ)
                .endGrant()
                .addGrant()
                .withGrantee(GroupGrantee.AUTHENTICATED_USERS)
                .withPermission(Permission.WRITE)
                .endGrant()
                .endACL()
                .execute(newActionListener(latch));
        latch.await();
        logResponse(bucketClient.prepareGetBucketACL().withBucketName(bucket).run());
    }

    @Test
    public void testAsync() {
        logResponse(bucketClient.preparePutBucketACL()
                .withBucketName(bucket)
                .withACL()
                .withOwner("admin", "admin")
                .addGrant()
                .withGrantee("sync", "rgw syncer")
                .withPermission(Permission.FULL_CONTROL)
                .endGrant()
                .endACL()
                .execute());
        logResponse(bucketClient.prepareGetBucketACL().withBucketName(bucket).execute());
    }
}
