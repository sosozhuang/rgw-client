package io.ceph.rgw.client.core.object;

import io.ceph.rgw.client.util.IOUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.*;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/7/9.
 */
@Category(ObjectTests.class)
public class FileTest extends BaseObjectClientTest {
    @After
    @Override
    public void tearDown() throws Exception {
        objectClient.prepareDeleteObject()
                .withBucketName(bucket)
                .withKey(key)
                .run();
        super.tearDown();
    }

    private File writeFile(String content) {
        File tempFile = null;
        FileOutputStream os = null;
        try {
            tempFile = File.createTempFile(key, null);
            os = new FileOutputStream(tempFile);
            os.write(content.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (tempFile != null) {
                tempFile.deleteOnExit();
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException ignored) {
                }
            }
        }
        return tempFile;
    }

    @Test
    public void testSync() {
        File in = writeFile(FileTest.class.getSimpleName() + " testSync");
        logResponse(objectClient.preparePutFile()
                .withBucketName(bucket)
                .withKey(key)
                .withValue(in)
                .run());
        File out = new File(in.getAbsolutePath() + ".out");
        out.deleteOnExit();
        logResponse(objectClient.prepareGetFile()
                .withBucketName(bucket)
                .withKey(key)
                .withPath(out.toPath())
                .run());

        try {
            Assert.assertEquals(FileTest.class.getSimpleName() + " testSync", IOUtil.toString(new FileInputStream(out)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCallback() throws InterruptedException {
        Latch latch = newLatch();
        objectClient.preparePutFile()
                .withBucketName(bucket)
                .withKey(key)
                .withValue(writeFile(FileTest.class.getSimpleName() + " testCallback"))
                .execute(newActionListener(latch));
        latch.await();
    }

    @Test
    public void testAsync() {
        File in = writeFile(FileTest.class.getSimpleName() + " testAsync");
        logResponse(objectClient.preparePutFile()
                .withBucketName(bucket)
                .withKey(key)
                .withValue(in)
                .execute());
        File out = new File(in.getAbsolutePath() + ".out");
        out.deleteOnExit();
        logResponse(objectClient.prepareGetFile()
                .withBucketName(bucket)
                .withKey(key)
                .withPath(out.toPath())
                .execute());

        try {
            Assert.assertEquals(FileTest.class.getSimpleName() + " testAsync", IOUtil.toString(new FileInputStream(out)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testOverwritten() {
        File in = writeFile(FileTest.class.getSimpleName() + " testOverwritten 1");
        logResponse(objectClient.preparePutFile()
                .withBucketName(bucket)
                .withKey(key)
                .withValue(in)
                .execute());
        File out = new File(in.getAbsolutePath() + ".out");
        logResponse(objectClient.prepareGetFile()
                .withBucketName(bucket)
                .withKey(key)
                .withPath(out.toPath())
                .run());
        try {
            Assert.assertEquals(FileTest.class.getSimpleName() + " testOverwritten 1", IOUtil.toString(new FileInputStream(out)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Assert.assertTrue(in.delete());
        Assert.assertTrue(out.delete());
        in = writeFile(FileTest.class.getSimpleName() + " testOverwritten 2");
        logResponse(objectClient.preparePutFile()
                .withBucketName(bucket)
                .withKey(key)
                .withValue(in)
                .execute());
        out = new File(in.getAbsolutePath() + ".out");
        out.deleteOnExit();
        logResponse(objectClient.prepareGetFile()
                .withBucketName(bucket)
                .withKey(key)
                .withPath(out.toPath())
                .run());
        try {
            Assert.assertEquals(FileTest.class.getSimpleName() + " testOverwritten 2", IOUtil.toString(new FileInputStream(out)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    boolean isCreateObject() {
        return false;
    }
}
