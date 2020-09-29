package io.ceph.rgw.client.core.object;

import io.ceph.rgw.client.core.io.ObjectWriter;
import io.ceph.rgw.client.exception.RGWException;
import io.ceph.rgw.client.exception.RGWServerException;
import io.ceph.rgw.client.model.GetInputStreamResponse;
import io.ceph.rgw.client.model.GetStringResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.*;
import java.nio.ByteBuffer;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/8/12.
 */
@Category(ObjectTests.class)
public class ByteBufSingleWriterTest extends ObjectWriterTest {
    private ObjectWriter makeObjectWriter() {
        return new ObjectWriter.Builder(objectClient)
                .withBuffer()
                .withSingleWriteThread()
                .withBucketName(bucket)
                .withKey(key)
                .build();
    }

    @Test(expected = RGWServerException.class)
    public void testCancel() {
        ObjectWriter writer = makeObjectWriter();
        writer.write("1");
        writer.write(new byte[]{1, 2});
        writer.write((ByteBuffer) ByteBuffer.allocate(4).putInt(1).flip());
        Assert.assertTrue(writer.cancel());
        objectClient.prepareGetObjectInfo()
                .withBucketName(bucket)
                .withKey(key)
                .run();
    }

    @Test(expected = RGWException.class)
    public void testNoWrite() {
        ObjectWriter writer = makeObjectWriter();
        writer.complete();
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalState() {
        ObjectWriter writer = makeObjectWriter();
        Assert.assertTrue(writer.cancel());
        writer.complete();
    }

    @Test
    public void testSmallString() {
        ObjectWriter writer = makeObjectWriter();
        StringBuilder builder = new StringBuilder();
        String temp;
        for (int i = 1; i <= 100; i++) {
            temp = RandomStringUtils.random(i);
            writer.write(temp);
            builder.append(temp);
        }
        logResponse(writer.complete());
        Assert.assertFalse(writer.cancel());
        GetStringResponse resp = logResponse(objectClient.prepareGetString()
                .withBucketName(bucket)
                .withKey(key)
                .run());
        Assert.assertEquals(builder.toString(), resp.getContent());
    }

    @Test
    public void testSmallBytes() {
        ObjectWriter writer = makeObjectWriter();
        StringBuilder builder = new StringBuilder();
        String temp;
        for (int i = 1; i <= 100; i++) {
            temp = RandomStringUtils.random(i);
            writer.write(temp.getBytes());
            builder.append(temp);
        }
        logResponse(writer.complete());
        Assert.assertFalse(writer.cancel());
        GetStringResponse resp = logResponse(objectClient.prepareGetString()
                .withBucketName(bucket)
                .withKey(key)
                .run());
        Assert.assertEquals(builder.toString(), resp.getContent());
    }

    @Test
    public void testSmallBuffer() {
        ObjectWriter writer = makeObjectWriter();
        StringBuilder builder = new StringBuilder();
        String temp;
        for (int i = 1; i <= 100; i++) {
            temp = RandomStringUtils.random(i);
            writer.write(ByteBuffer.wrap(temp.getBytes()));
            builder.append(temp);
        }
        logResponse(writer.complete());
        Assert.assertFalse(writer.cancel());
        GetStringResponse resp = logResponse(objectClient.prepareGetString()
                .withBucketName(bucket)
                .withKey(key)
                .run());
        Assert.assertEquals(builder.toString(), resp.getContent());
    }

    @Test
    public void testSmallFile() throws IOException {
        ObjectWriter writer = makeObjectWriter();
        String temp;
        OutputStream os;
        File file = createTempFile();
        try {
            os = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        for (int i = 1; i <= 100; i++) {
            temp = RandomStringUtils.random(i);
            writer.write(writeFile(temp));
            os.write(temp.getBytes());
        }
        os.close();
        logResponse(writer.complete());
        Assert.assertFalse(writer.cancel());
        GetInputStreamResponse resp = logResponse(objectClient.prepareGetInputStream()
                .withBucketName(bucket)
                .withKey(key)
                .run());
        Assert.assertTrue(isEqual(new FileInputStream(file), resp.getContent()));
    }

    @Test
    public void testLargeString() throws IOException {
        ObjectWriter writer = makeObjectWriter();
        String temp;
        OutputStream os;
        File file = createTempFile();
        try {
            os = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < ObjectWriter.DEFAULT_CHUNK_SIZE * 6; i += 4099) {
            temp = RandomStringUtils.random(4099);
            writer.write(temp);
            os.write(temp.getBytes());
        }
        os.close();
        logResponse(writer.complete());
        Assert.assertFalse(writer.cancel());
        GetInputStreamResponse resp = logResponse(objectClient.prepareGetInputStream()
                .withBucketName(bucket)
                .withKey(key)
                .run());
        Assert.assertTrue(isEqual(new FileInputStream(file), resp.getContent()));
    }

    @Test
    public void testLargeBytes() throws IOException {
        ObjectWriter writer = makeObjectWriter();
        byte[] temp;
        OutputStream os;
        File file = createTempFile();
        try {
            os = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < ObjectWriter.DEFAULT_CHUNK_SIZE * 6; i += 4099) {
            temp = RandomStringUtils.random(4099).getBytes();
            writer.write(temp);
            os.write(temp);
        }
        os.close();
        logResponse(writer.complete());
        Assert.assertFalse(writer.cancel());
        GetInputStreamResponse resp = logResponse(objectClient.prepareGetInputStream()
                .withBucketName(bucket)
                .withKey(key)
                .run());
        Assert.assertTrue(isEqual(new FileInputStream(file), resp.getContent()));
    }

    @Test
    public void testLargeBuffer() throws IOException {
        ObjectWriter writer = makeObjectWriter();
        byte[] temp;
        OutputStream os;
        File file = createTempFile();
        try {
            os = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < ObjectWriter.DEFAULT_CHUNK_SIZE * 6; i += 4099) {
            temp = RandomStringUtils.random(4099).getBytes();
            writer.write(ByteBuffer.wrap(temp));
            os.write(temp);
        }
        os.close();
        logResponse(writer.complete());
        Assert.assertFalse(writer.cancel());
        GetInputStreamResponse resp = logResponse(objectClient.prepareGetInputStream()
                .withBucketName(bucket)
                .withKey(key)
                .run());
        Assert.assertTrue(isEqual(new FileInputStream(file), resp.getContent()));
    }

    @Test
    public void testLargeFile() throws IOException {
        ObjectWriter writer = makeObjectWriter();
        String temp;
        OutputStream os;
        File file = createTempFile();
        try {
            os = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < ObjectWriter.DEFAULT_CHUNK_SIZE * 6; i += ObjectWriter.DEFAULT_CHUNK_SIZE + 7) {
            temp = RandomStringUtils.random(ObjectWriter.DEFAULT_CHUNK_SIZE + 7);
            writer.write(writeFile(temp));
            os.write(temp.getBytes());
        }
        os.close();
        logResponse(writer.complete());
        Assert.assertFalse(writer.cancel());
        GetInputStreamResponse resp = logResponse(objectClient.prepareGetInputStream()
                .withBucketName(bucket)
                .withKey(key)
                .run());
        Assert.assertTrue(isEqual(new FileInputStream(file), resp.getContent()));
    }
}
