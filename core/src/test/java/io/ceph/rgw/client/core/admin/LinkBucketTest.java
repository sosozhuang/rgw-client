package io.ceph.rgw.client.core.admin;

import io.ceph.rgw.client.model.admin.GetBucketInfoResponse;
import org.junit.Test;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/8/5.
 */
public class LinkBucketTest extends BaseAdminClientTest {
    @Test
    public void testSync() {
        GetBucketInfoResponse resp = logResponse(adminClient.prepareGetBucketInfo()
                .withBucket(bucket)
                .withStats(Boolean.TRUE)
                .run());
        logResponse(adminClient.prepareLinkBucket()
                .withBucket(bucket)
                .withBucketId(resp.getBucketInfoList().get(0).getId())
                .withUid(userId)
                .run());
        logResponse(adminClient.prepareUnlinkBucket()
                .withBucket(bucket)
                .withUid(userId)
                .run());
    }

    @Test
    public void testCallback() throws InterruptedException {
        GetBucketInfoResponse resp = logResponse(adminClient.prepareGetBucketInfo()
                .withBucket(bucket)
                .withStats(Boolean.TRUE)
                .run());
        Latch latch = newLatch();
        adminClient.prepareLinkBucket()
                .withBucket(bucket)
                .withBucketId(resp.getBucketInfoList().get(0).getId())
                .withUid(userId)
                .execute(newActionListener(latch));
        latch.await();
        latch = newLatch();
        adminClient.prepareUnlinkBucket()
                .withBucket(bucket)
                .withUid(userId)
                .execute(newActionListener(latch));
        latch.await();
    }

    @Test
    public void testAsync() {
        GetBucketInfoResponse resp = logResponse(adminClient.prepareGetBucketInfo()
                .withBucket(bucket)
                .withStats(Boolean.TRUE)
                .execute());
        logResponse(adminClient.prepareLinkBucket()
                .withBucket(bucket)
                .withBucketId(resp.getBucketInfoList().get(0).getId())
                .withUid(userId)
                .execute());
        logResponse(adminClient.prepareUnlinkBucket()
                .withBucket(bucket)
                .withUid(userId)
                .execute());
    }

    @Override
    protected boolean isCreateBucket() {
        return true;
    }

    @Override
    boolean isCreateUser() {
        return true;
    }
}
