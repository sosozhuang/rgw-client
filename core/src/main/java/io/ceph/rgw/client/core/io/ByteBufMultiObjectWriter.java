package io.ceph.rgw.client.core.io;

import io.ceph.rgw.client.ObjectClient;
import io.ceph.rgw.client.action.ActionListener;
import io.ceph.rgw.client.exception.RGWException;
import io.ceph.rgw.client.model.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Unsafe;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.LockSupport;
import java.util.stream.IntStream;

/**
 * Designed for multiple write threads.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/7/16.
 * @see ByteBufSingleObjectWriter
 */
class ByteBufMultiObjectWriter extends ByteBufObjectWriter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ByteBufMultiObjectWriter.class);
    private static final long BASE = Unsafe.ARRAY_INT_BASE_OFFSET;
    private static final long SCALE = Unsafe.ARRAY_INT_INDEX_SCALE;

    private final int[] writableFlags;
    private final AtomicInteger[] writtenCounters;
    private final int shift;
    private final AtomicLong writerSeq;

    public ByteBufMultiObjectWriter(ObjectClient client, String bucketName, String key, Metadata metadata, ACL acl, CannedACL cannedACL, int chunkSize, int ratio, boolean pooled) {
        super(client, bucketName, key, metadata, acl, cannedACL, chunkSize, ratio, pooled);
        this.writableFlags = new int[this.ratio];
        this.writtenCounters = IntStream.range(0, this.ratio).mapToObj(i -> new AtomicInteger(0)).toArray(AtomicInteger[]::new);
        this.shift = log2(this.byteBuf.capacity());
        this.writerSeq = new AtomicLong(INIT_SEQ);
    }

    private boolean isWritable(int chunkSeq, int flag) {
        return UNSAFE.getIntVolatile(writableFlags, chunkIndex(chunkSeq) * SCALE + BASE) == flag;
    }

    private void setWritableFlag(long seq) {
        long address = chunkIndex(seq) * SCALE + BASE;
        UNSAFE.getAndAddInt(writableFlags, address, 1);
    }

    private int calWritableFlag(long seq) {
        return (int) (seq >>> shift);
    }

    private long calWritable(long seq, long length) {
        long hi = seq + length;
        int chunkSeq = chunkSeq(seq);
        int chunks = 0;
        while (chunks < writableFlags.length && seq <= hi) {
            if (!isWritable(chunkSeq, calWritableFlag(seq))) {
                if (chunks == 0) {
                    checkException();
                    LockSupport.parkNanos(50);
                } else {
                    break;
                }
            } else {
                seq += chunkSize;
                chunkSeq++;
                chunks++;
            }
        }
        return Math.min(multiChunkSize(chunks) - writtenInChunk(seq), length);
    }

    @Override
    void doWrite(byte[] bytes, int offset, int length) throws Throwable {
        int writable, index, limit;
        long start = writerSeq.getAndAdd(length) + 1;
        LOGGER.debug("Buffer writer sequence [{}].", start + length - 1);
        AtomicInteger written;
        while (length > 0) {
            writable = (int) calWritable(start, length);
            index = calIndex(start);
            limit = byteBuf.capacity() - index;
            if (writable > limit) {
//                byteBuf.setBytes(index, bytes, offset, limit);
//                byteBuf.setBytes(0, bytes, limit, writable - limit);
                byteBuf.nioBuffer(index, limit).put(bytes, offset, limit);
                byteBuf.nioBuffer(0, writable - limit).put(bytes, limit, writable - limit);
            } else {
//                byteBuf.setBytes(index, bytes, offset, writable);
                byteBuf.nioBuffer(index, writable).put(bytes, offset, writable);
            }
            offset += writable;
            length -= writable;
            while (writable > 0) {
                written = writtenCounters[chunkIndex(start)];
                limit = Math.min(writableInChunk(start), writable);
                if (limit == chunkSize) {
                    if (start == 0) {
                        initiateUpload();
                    }
                    upload(start);
                } else {
                    if (written.addAndGet(limit) == chunkSize && written.compareAndSet(chunkSize, 0)) {
                        if (leftChunkSeq(start) == 0) {
                            initiateUpload();
                        }
                        upload(leftChunkSeq(start));
                    }
                }
                writable -= limit;
                start += limit;
            }
        }
    }

    @Override
    void doWrite(ByteBuffer buffer) throws Throwable {
        int writable, index, limit, length = buffer.remaining(), tempLimit, bufferLimit = buffer.limit();
        long start = writerSeq.getAndAdd(length) + 1;
        LOGGER.debug("Buffer writer sequence [{}].", start + length - 1);
        AtomicInteger written;
        while ((length = buffer.remaining()) > 0) {
            writable = (int) calWritable(start, length);
            index = calIndex(start);
            limit = byteBuf.capacity() - index;
            if (writable > limit) {
                tempLimit = buffer.position() + limit;
                buffer.limit(tempLimit);
//                byteBuf.setBytes(index, buffer);
                byteBuf.nioBuffer(index, limit).put(buffer);
                buffer.limit(tempLimit + writable - limit).position(tempLimit);
//                byteBuf.setBytes(0, buffer);
                byteBuf.nioBuffer(0, writable - limit).put(buffer);
            } else {
                buffer.limit(buffer.position() + writable);
//                byteBuf.setBytes(index, buffer);
                byteBuf.nioBuffer(index, writable).put(buffer);
            }
            buffer.limit(bufferLimit);
            while (writable > 0) {
                written = writtenCounters[chunkIndex(start)];
                limit = Math.min(writableInChunk(start), writable);
                if (limit == chunkSize) {
                    if (start == 0) {
                        initiateUpload();
                    }
                    upload(start);
                } else {
                    if (written.addAndGet(limit) == chunkSize && written.compareAndSet(chunkSize, 0)) {
                        if (leftChunkSeq(start) == 0) {
                            initiateUpload();
                        }
                        upload(leftChunkSeq(start));
                    }
                }
                writable -= limit;
                start += limit;
            }
        }
    }

    @Override
    void doWrite(FileChannel file, long offset, long length) throws Throwable {
        int writable, index, limit;
        long start = writerSeq.getAndAdd(length) + 1;
        LOGGER.debug("Buffer writer sequence [{}].", start + length - 1);
        AtomicInteger written;
        while (length > 0) {
            writable = (int) calWritable(start, length);
            index = calIndex(start);
            limit = byteBuf.capacity() - index;
            if (writable > limit) {
//                byteBuf.setBytes(index, file, offset, limit);
//                byteBuf.setBytes(0, file, limit, writable - limit);
                file.read(byteBuf.nioBuffer(index, limit), offset);
                file.read(byteBuf.nioBuffer(0, writable - limit), limit);
            } else {
//                byteBuf.setBytes(index, file, offset, writable);
                file.read(byteBuf.nioBuffer(index, writable), offset);
            }
            offset += writable;
            length -= writable;
            while (writable > 0) {
                written = writtenCounters[chunkIndex(start)];
                limit = Math.min(writableInChunk(start), writable);
                if (limit == chunkSize) {
                    if (start == 0) {
                        initiateUpload();
                    }
                    upload(start);
                } else {
                    if (written.addAndGet(limit) == chunkSize && written.compareAndSet(chunkSize, 0)) {
                        if (leftChunkSeq(start) == 0) {
                            initiateUpload();
                        }
                        upload(leftChunkSeq(start));
                    }
                }
                writable -= limit;
                start += limit;
            }
        }
    }

    private void upload(long seq) {
        upload(seq, chunkSize, null);
    }

    private void upload(long seq, int size, ActionListener<BasePutObjectResponse> listener) {
        checkUploadId();
        boolean last = listener != null;
        ByteBuffer buffer = byteBuf.nioBuffer(calIndex(seq), size);
        int num = chunkSeq(seq) + 1;
        LOGGER.debug("Start to upload buffer [{}], size [{}], range[{}, {}).", num, size, seq, seq + size);
        Runnable r = () -> client.prepareUploadByteBuffer()
                .withBucketName(getBucketName())
                .withKey(getKey())
                .withUpload(buffer)
                .withUploadId(uploadId)
                .withPartNumber(num)
                .withPartSize(size)
                .withLastPart(last)
                .execute(last ? lastUploadListener(num, seq, listener) : uploadListener(num, seq));
        if (!last) {
            safelyUpload(r);
        } else {
            r.run();
        }
    }

    private ActionListener<MultipartUploadPartResponse> uploadListener(int num, long seq) {
        return new ActionListener<MultipartUploadPartResponse>() {
            @Override
            public void onSuccess(MultipartUploadPartResponse response) {
                setWritableFlag(seq);
                addPartETag(num, response.getETag());
                exitWrite();
                LOGGER.info("Finished to upload part [{}], eTag [{}].", num, response.getETag());
            }

            @Override
            public void onFailure(Throwable cause) {
                setException(cause);
                LOGGER.error("Failed to upload part [{}]", num, cause);
            }
        };
    }

    private ActionListener<MultipartUploadPartResponse> lastUploadListener(int num, long seq, ActionListener<BasePutObjectResponse> listener) {
        return new ActionListener<MultipartUploadPartResponse>() {
            @Override
            public void onSuccess(MultipartUploadPartResponse response) {
                setWritableFlag(seq);
                addPartETag(num, response.getETag());
                exitWrite();
                LOGGER.info("Finished to upload last part [{}], eTag [{}].", num, response.getETag());
                completeUpload(listener);
            }

            @Override
            public void onFailure(Throwable cause) {
                setException(cause);
                LOGGER.error("Failed to upload last part [{}]", num, cause);
                listener.onFailure(cause);
            }
        };
    }

    @Override
    public void doCompleteAsync(ActionListener<BasePutObjectResponse> listener) throws Throwable {
        if (writerSeq.get() == INIT_SEQ) {
            throw new RGWException("cannot complete empty object");
        }
        if (StringUtils.isNotBlank(uploadId)) {
            long seq = writerSeq.get();
            int size = writtenCounters[chunkIndex(seq)].get();
            if (size > 0) {
                upload(leftChunkSeq(seq), size, listener);
            } else {
                completeUpload(listener);
            }
        } else {
            putBuffer(listener);
        }
    }

    @Override
    public long getWriterSeq() {
        return writerSeq.get();
    }
}
