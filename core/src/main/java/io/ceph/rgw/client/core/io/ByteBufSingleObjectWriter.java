package io.ceph.rgw.client.core.io;

import io.ceph.rgw.client.ObjectClient;
import io.ceph.rgw.client.action.ActionListener;
import io.ceph.rgw.client.exception.RGWException;
import io.ceph.rgw.client.model.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.locks.LockSupport;

/**
 * Designed for single write thread only, <strong>NOT</strong> safe for multiple write threads.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/7/14.
 * @see ByteBufMultiObjectWriter
 */
class ByteBufSingleObjectWriter extends ByteBufObjectWriter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ByteBufSingleObjectWriter.class);
    private static final long MASK = -1;
    private static final long WRITABLE_OFFSET;

    static {
        try {
            WRITABLE_OFFSET = UNSAFE.objectFieldOffset(ByteBufSingleObjectWriter.class.getDeclaredField("writableFlag"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private volatile long writableFlag;
    private long writerSeq;


    public ByteBufSingleObjectWriter(ObjectClient client, String bucketName, String key, Metadata metadata, ACL acl, CannedACL cannedACL, int chunkSize, int ratio, boolean pooled) {
        super(client, bucketName, key, metadata, acl, cannedACL, chunkSize, ratio, pooled);
        this.writableFlag = 0;
        this.writerSeq = INIT_SEQ;
    }

    private long calWritable(long seq, long length) {
        if (writableFlag == 0) {
            return Math.min(byteBuf.capacity() - writtenInChunk(seq), length);
        }
        int index = chunkIndex(seq);
        int chunks;
        while ((chunks = writableChunkSize(index)) == 0) {
            checkException();
            LockSupport.parkNanos(50);
        }
        return Math.min(chunks - writtenInChunk(seq), length);
    }

    private int writableChunkSize(int dis) {
        long w = writableFlag;
        int n = Long.numberOfTrailingZeros(w >>> dis | MASK << ratio - dis);
        if (n == ratio - dis) {
            n += Long.numberOfTrailingZeros(w | MASK << dis);
        }
        return n == 0 ? 0 : multiChunkSize(n);
    }

    private boolean casWritableFlag(long expect, long update) {
        return UNSAFE.compareAndSwapLong(this, WRITABLE_OFFSET, expect, update);
    }

    private void setWritableFlag(int chunkSeq) {
        long w;
        do {
            w = writableFlag;
        } while (exception == null && !casWritableFlag(w, w | 1 << chunkIndex(chunkSeq)));
    }

    private void clearWritableFlag(int chunkSeq) {
        long w;
        do {
            w = writableFlag;
        } while (!casWritableFlag(w, w & ~(1 << chunkIndex(chunkSeq))));
    }

    @Override
    public void doWrite(byte[] bytes, int offset, int length) throws Throwable {
        long start;
        int writable, index, limit;
        while (length > 0) {
            start = writerSeq + 1;
            writable = (int) calWritable(start, length);
            index = calIndex(start);
            limit = byteBuf.capacity() - index;
            if (writable > limit) {
                byteBuf.setBytes(index, bytes, offset, limit);
                byteBuf.setBytes(0, bytes, limit, writable - limit);
            } else {
                byteBuf.setBytes(index, bytes, offset, writable);
            }
            writerSeq += writable;
            LOGGER.debug("Buffer writer sequence [{}].", writerSeq);
            index = chunkSeq(start);
            limit = chunkSeq(writerSeq + 1);
            while (limit > index) {
                if (index == 0) {
                    initiateUpload();
                }
                upload(index);
                index += 1;
            }
            offset += writable;
            length -= writable;
        }
    }

    @Override
    void doWrite(ByteBuffer buffer) throws Throwable {
        long start;
        int length, writable, index, limit, tempLimit, bufferLimit = buffer.limit();
        while ((length = buffer.remaining()) > 0) {
            start = writerSeq + 1;
            writable = (int) calWritable(start, length);
            index = calIndex(start);
            limit = byteBuf.capacity() - index;
            if (writable > limit) {
                tempLimit = buffer.position() + limit;
                buffer.limit(tempLimit);
                byteBuf.setBytes(index, buffer);
                buffer.limit(tempLimit + writable - limit).position(tempLimit);
                byteBuf.setBytes(0, buffer);
            } else {
                tempLimit = buffer.position() + writable;
                buffer.limit(tempLimit);
                byteBuf.setBytes(index, buffer);
            }
            buffer.limit(bufferLimit);
            writerSeq += writable;
            LOGGER.debug("Buffer writer sequence [{}].", writerSeq);
            index = chunkSeq(start);
            limit = chunkSeq(writerSeq + 1);
            while (limit > index) {
                if (index == 0) {
                    initiateUpload();
                }
                upload(index);
                index += 1;
            }
        }
    }

    @Override
    public void doWrite(FileChannel source, long offset, long length) throws Throwable {
        long start;
        int writable, index, limit;
        while (length > 0) {
            start = writerSeq + 1;
            writable = (int) calWritable(start, length);
            index = calIndex(start);
            limit = byteBuf.capacity() - index;
            if (writable > limit) {
                byteBuf.setBytes(index, source, offset, limit);
                byteBuf.setBytes(0, source, limit, writable - limit);
            } else {
                byteBuf.setBytes(index, source, offset, writable);
            }
            writerSeq += writable;
            LOGGER.debug("Buffer writer sequence [{}].", writerSeq);
            index = chunkSeq(start);
            limit = chunkSeq(writerSeq + 1);
            while (limit > index) {
                if (index == 0) {
                    initiateUpload();
                }
                upload(index);
                index += 1;
            }
            offset += writable;
            length -= writable;
        }
    }

    private void upload(int chunkSeq) {
        upload(chunkSeq, chunkSize, null);
    }

    private void upload(int chunkSeq, int size, ActionListener<BasePutObjectResponse> listener) {
        setWritableFlag(chunkSeq);
        checkUploadId();
        boolean last = listener != null;
        int offset = multiChunkSize(chunkSeq);
        ByteBuffer buffer = byteBuf.nioBuffer(calIndex(offset), size);
        LOGGER.debug("Start to upload part [{}], size [{}], buffer range[{}, {}).", chunkSeq + 1, size, offset, offset + size);
        Runnable r = () -> client.prepareUploadByteBuffer()
                .withBucketName(getBucketName())
                .withKey(getKey())
                .withUpload(buffer)
                .withUploadId(uploadId)
                .withPartNumber(chunkSeq + 1)
                .withPartSize(size)
                .withLastPart(last)
                .execute(last ? lastUploadListener(chunkSeq, listener) : uploadListener(chunkSeq));
        if (!last) {
            safelyUpload(r);
        } else {
            r.run();
        }
    }

    private ActionListener<MultipartUploadPartResponse> uploadListener(int index) {
        return new ActionListener<MultipartUploadPartResponse>() {
            @Override
            public void onSuccess(MultipartUploadPartResponse response) {
                clearWritableFlag(index);
                addPartETag(index + 1, response.getETag());
                exitWrite();
                LOGGER.info("Finished to upload part [{}], eTag [{}].", index + 1, response.getETag());
            }

            @Override
            public void onFailure(Throwable cause) {
                setException(cause);
                LOGGER.error("Failed to upload part [{}]", index + 1, cause);
            }
        };
    }

    private ActionListener<MultipartUploadPartResponse> lastUploadListener(int index, ActionListener<BasePutObjectResponse> listener) {
        return new ActionListener<MultipartUploadPartResponse>() {
            @Override
            public void onSuccess(MultipartUploadPartResponse response) {
                clearWritableFlag(index);
                addPartETag(index + 1, response.getETag());
                exitWrite();
                LOGGER.info("Finished to upload last part [{}], eTag [{}].", index + 1, response.getETag());
                completeUpload(listener);
            }

            @Override
            public void onFailure(Throwable cause) {
                setException(cause);
                LOGGER.error("Failed to upload last part [{}]", index + 1, cause);
                listener.onFailure(cause);
            }
        };
    }

    @Override
    void doCompleteAsync(ActionListener<BasePutObjectResponse> listener) throws Throwable {
        if (writerSeq == INIT_SEQ) {
            throw new RGWException("cannot complete empty object");
        }
        if (StringUtils.isNotBlank(uploadId)) {
            int size;
            if ((size = writtenInChunk(writerSeq + 1)) > 0) {
                upload(chunkSeq(writerSeq + 1), size, listener);
            } else {
                completeUpload(listener);
            }
        } else {
            putBuffer(listener);
        }
    }

    @Override
    public long getWriterSeq() {
        return writerSeq;
    }
}
