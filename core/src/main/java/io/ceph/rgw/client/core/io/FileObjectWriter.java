package io.ceph.rgw.client.core.io;

import io.ceph.rgw.client.ObjectClient;
import io.ceph.rgw.client.action.ActionListener;
import io.ceph.rgw.client.exception.RGWException;
import io.ceph.rgw.client.model.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * An ObjectWriter writes data to a file, and uploads file content.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/7/14.
 */
public class FileObjectWriter extends AbstractObjectWriter {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileObjectWriter.class);
    private static final long CLEANER_FIELD_OFFSET;
    private static final Method CLEAN_METHOD;

    static {
        ByteBuffer direct = ByteBuffer.allocateDirect(1);
        long fieldOffset;
        Method clean;
        try {
            Field cleanerField = direct.getClass().getDeclaredField("cleaner");
            fieldOffset = UNSAFE.objectFieldOffset(cleanerField);
            Object cleaner = UNSAFE.getObject(direct, fieldOffset);
            clean = cleaner.getClass().getDeclaredMethod("clean");
            clean.invoke(cleaner);
            LOGGER.debug("Finished to reflect java.nio.ByteBuffer.cleaner().");
        } catch (Throwable t) {
            fieldOffset = -1;
            clean = null;
            LOGGER.debug("Cannot reflect java.nio.ByteBuffer.cleaner().", t);
        }
        CLEANER_FIELD_OFFSET = fieldOffset;
        CLEAN_METHOD = clean;
        releaseBuffer(direct);
    }

    private final File file;
    //    private final RandomAccessFile raf;
    private final FileChannel channel;
    private final AtomicLong readerSeq;
    private final AtomicLong writerSeq;

    public FileObjectWriter(ObjectClient client, String bucketName, String key, Metadata metadata, ACL acl, CannedACL cannedACL, int chunkSize, String path) {
        this(client, bucketName, key, metadata, acl, cannedACL, chunkSize, new File(path));
    }

    public FileObjectWriter(ObjectClient client, String bucketName, String key, Metadata metadata, ACL acl, CannedACL cannedACL, int chunkSize, File file) {
        super(client, bucketName, key, metadata, acl, cannedACL, chunkSize);
        try {
            this.file = Objects.requireNonNull(file);
//            this.raf = new RandomAccessFile(this.file, "rw");
            this.channel = FileChannel.open(file.toPath(), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
            this.readerSeq = new AtomicLong(INIT_SEQ);
            this.writerSeq = new AtomicLong(INIT_SEQ);
        } catch (FileNotFoundException e) {
            throw new RGWException(e);
        } catch (IOException e) {
            close();
            throw new RGWException(e);
        }
    }

    private static void releaseBuffer(ByteBuffer buffer) {
        if (CLEANER_FIELD_OFFSET == -1 || !buffer.isDirect()) {
            return;
        }
        try {
            Object cleaner = UNSAFE.getObject(buffer, CLEANER_FIELD_OFFSET);
            if (cleaner != null) {
                CLEAN_METHOD.invoke(cleaner);
            }
        } catch (Throwable ignored) {
        }
    }

    @Override
    public void doWrite(byte[] bytes, int offset, int length) throws Throwable {
        channel.write(ByteBuffer.wrap(bytes, offset, length));
//        raf.write(bytes, offset, length);
        addWriterSeq(length);
    }

    @Override
    void doWrite(ByteBuffer buffer) throws Throwable {
        int len = buffer.remaining();
        channel.write(buffer);
        addWriterSeq(len);
    }

    @Override
    public void doWrite(FileChannel source, long offset, long length) throws Throwable {
        source.transferTo(offset, length, channel);
        addWriterSeq(length);
    }

    private void addWriterSeq(long length) throws Throwable {
        long r, w = writerSeq.addAndGet(length);
        LOGGER.debug("File [{}] writer sequence [{}].", file, w);
        while (w - (r = readerSeq.get()) >= chunkSize) {
            if (readerSeq.compareAndSet(r, r + chunkSize)) {
                if ((r += 1) == 0) {
                    initiateUpload();
                }
                upload(r);
            }
        }
    }

    private void upload(long offset) throws IOException {
        upload(offset, chunkSize, null);
    }

    private void upload(long offset, long size, ActionListener<BasePutObjectResponse> listener) throws IOException {
        checkUploadId();

        boolean last = listener != null;
        ByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, offset, size);
        int num = chunkSeq(offset) + 1;
        LOGGER.debug("Start to upload file part [{}], size [{}], range[{}, {}).", num, size, offset, offset + size);
        Runnable r = () -> client.prepareUploadByteBuffer()
                .withBucketName(getBucketName())
                .withKey(getKey())
                .withUpload(buffer)
                .withUploadId(uploadId)
                .withPartNumber(num)
                .withPartSize(size)
                .withLastPart(last)
                .execute(last ? lastUploadListener(buffer, num, listener) : uploadListener(buffer, num));
        if (!last) {
            safelyUpload(r);
        } else {
            r.run();
        }
    }

    private ActionListener<MultipartUploadPartResponse> uploadListener(ByteBuffer buffer, int num) {
        return new ActionListener<MultipartUploadPartResponse>() {
            @Override
            public void onSuccess(MultipartUploadPartResponse response) {
                addPartETag(num, response.getETag());
                exitWrite();
                LOGGER.info("Finished to upload part [{}], eTag [{}].", num, response.getETag());
                releaseBuffer(buffer);
            }

            @Override
            public void onFailure(Throwable cause) {
                setException(cause);
                LOGGER.error("Failed to upload part [{}].", num, cause);
                releaseBuffer(buffer);
            }
        };
    }

    private ActionListener<MultipartUploadPartResponse> lastUploadListener(ByteBuffer buffer, int num, ActionListener<BasePutObjectResponse> listener) {
        return new ActionListener<MultipartUploadPartResponse>() {
            @Override
            public void onSuccess(MultipartUploadPartResponse response) {
                addPartETag(num, response.getETag());
                releaseBuffer(buffer);
                exitWrite();
                LOGGER.info("Finished to upload part [{}], eTag [{}].", num, response.getETag());
                completeUpload(listener);
            }

            @Override
            public void onFailure(Throwable cause) {
                setException(cause);
                LOGGER.error("Failed to upload last part [{}].", num, cause);
                releaseBuffer(buffer);
                listener.onFailure(cause);
            }
        };
    }

    @Override
    void doCompleteAsync(ActionListener<BasePutObjectResponse> listener) throws Throwable {
        if (writerSeq.get() == INIT_SEQ) {
            throw new RGWException("cannot complete empty object");
        }
        if (StringUtils.isNotBlank(uploadId)) {
            long r = readerSeq.get(), size;
            if ((size = writerSeq.get() - r) > 0) {
                upload(r + 1, size, listener);
            } else {
                completeUpload(listener);
            }
        } else {
            putFile(listener);
        }
    }

    private void putFile(ActionListener<BasePutObjectResponse> listener) {
        client.preparePutFile()
                .withBucketName(getBucketName())
                .withKey(getKey())
                .withValue(file)
                .withMetadata(getMetadata())
                .withCannedACL(getCannedACL())
                .withACL(getACL())
                .execute(completeListener(listener));
    }

    @Override
    void doClose() {
//        if (raf != null) {
//            try {
//                raf.close();
//            } catch (Throwable cause) {
//                LOGGER.error("Failed to close raf [{}].", file, cause);
//            }
//        }
        if (channel != null) {
            try {
                channel.close();
            } catch (Throwable cause) {
                LOGGER.error("Failed to close file channel [{}].", file, cause);
            }
        }
    }
}
