package io.ceph.rgw.client.core.object;

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
@Category(ObjectTests.class)
public class ObjectACLTest extends BaseObjectClientTest {
    @Test
    public void testSync() {
        logResponse(objectClient.preparePutObjectACL()
                .withBucketName(bucket)
                .withKey(key)
                .withCannedACL(CannedACL.PUBLIC_READ)
                .run());
        logResponse(objectClient.prepareGetObjectACL().withBucketName(bucket).withKey(key).run());
    }

    @Test
    public void testCallback() throws InterruptedException {
        BaseClientTest.Latch latch = newLatch();
        objectClient.preparePutObjectACL()
                .withBucketName(bucket)
                .withKey(key)
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
        logResponse(objectClient.prepareGetObjectACL().withBucketName(bucket).withKey(key).run());
    }

    @Test
    public void testAsync() {
        logResponse(objectClient.preparePutObjectACL()
                .withBucketName(bucket)
                .withKey(key)
                .withACL()
                .withOwner("admin", "admin")
                .addGrant()
                .withGrantee("sync", "rgw syncer")
                .withPermission(Permission.FULL_CONTROL)
                .endGrant()
                .endACL()
                .execute());
        logResponse(objectClient.prepareGetObjectACL().withBucketName(bucket).withKey(key).execute());
    }
}
