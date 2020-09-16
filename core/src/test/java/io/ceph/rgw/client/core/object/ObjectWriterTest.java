package io.ceph.rgw.client.core.object;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/8/19.
 */
abstract class ObjectWriterTest extends BaseObjectClientTest {
    @After
    @Override
    public void tearDown() throws Exception {
        if (!StringUtils.isAnyBlank(bucket, key)) {
            objectClient.prepareDeleteObject()
                    .withBucketName(bucket)
                    .withKey(key)
                    .run();
        }
        super.tearDown();
    }

    @Override
    boolean isCreateObject() {
        return false;
    }

    @Override
    protected boolean isCreateBucket() {
        return false;
    }

    static File createTempFile() {
        File tempFile;
        try {
            tempFile = File.createTempFile(RandomStringUtils.randomAlphabetic(64), null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        tempFile.deleteOnExit();
        return tempFile;
    }

    static File writeFile(String content) {
        File tempFile = null;
        FileOutputStream os = null;
        try {
            tempFile = File.createTempFile(RandomStringUtils.randomAlphabetic(64), null);
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

    static boolean isEqual(InputStream is1, InputStream is2) throws IOException {
        ReadableByteChannel ch1 = Channels.newChannel(is1);
        ReadableByteChannel ch2 = Channels.newChannel(is2);
        ByteBuffer buf1 = ByteBuffer.allocateDirect(1024);
        ByteBuffer buf2 = ByteBuffer.allocateDirect(1024);
        try {
            int n1, n2;
            while (true) {
                n1 = ch1.read(buf1);
                n2 = ch2.read(buf2);
                if (n1 == -1 || n2 == -1) {
                    return n1 == n2;
                }
                buf1.flip();
                buf2.flip();
                for (int i = 0; i < Math.min(n1, n2); i++) {
                    if (buf1.get() != buf2.get()) {
                        return false;
                    }
                }
                buf1.compact();
                buf2.compact();
            }
        } finally {
            try {
                is1.close();
            } catch (IOException ignored) {
            }
            try {
                is2.close();
            } catch (IOException ignored) {
            }
        }
    }
}
