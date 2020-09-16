package io.ceph.rgw.client.core.admin;

import io.ceph.rgw.client.AdminClient;
import io.ceph.rgw.client.core.bucket.BaseBucketClientTest;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.IOException;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/7/10.
 */
public abstract class BaseAdminClientTest extends BaseBucketClientTest {
    protected static AdminClient adminClient;
    String userId;
    String subUserId;

    @BeforeClass
    public static void setUpClient() throws IOException {
        BaseBucketClientTest.setUpClient("admin.properties");
        adminClient = clients.getAdmin();
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        userId = "user-" + System.currentTimeMillis();
        if (isCreateUser() || isCreateSubUser()) {
            adminClient.prepareCreateUser()
                    .withUid(userId)
                    .withDisplayName("test user")
                    .run();
        }
        subUserId = "subuser-" + System.currentTimeMillis();
        if (isCreateSubUser()) {
            adminClient.prepareCreateSubUser()
                    .withUid(userId)
                    .withSubUser(subUserId)
                    .run();
        }
    }

    @After
    public void tearDown() throws Exception {
        if (isCreateSubUser()) {
            adminClient.prepareRemoveSubUser()
                    .withUid(userId)
                    .withSubUser(subUserId)
                    .withPurgeKeys(Boolean.TRUE)
                    .run();
        }
        if (isCreateUser() && StringUtils.isNotBlank(userId)) {
            adminClient.prepareRemoveUser()
                    .withUid(userId)
                    .withPurgeData(Boolean.TRUE)
                    .run();
        }
        super.tearDown();
    }

    @Override
    protected boolean isCreateBucket() {
        return false;
    }

    boolean isCreateUser() {
        return false;
    }

    boolean isCreateSubUser() {
        return false;
    }
}
