package io.ceph.rgw.client.core.io;

import io.ceph.rgw.client.ObjectClient;
import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;
import io.ceph.rgw.client.action.ListenableActionFuture;
import io.ceph.rgw.client.exception.RGWCompositeException;
import io.ceph.rgw.client.exception.RGWException;
import io.ceph.rgw.client.model.*;
import io.ceph.rgw.client.util.ReflectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Unsafe;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Base class of ObjectWriter, provides common functionality like state management, exception handling.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/7/14.
 */
abstract class AbstractObjectWriter extends PutObjectRequest implements ObjectWriter {
    static final int INIT_SEQ = -1;
    static final Unsafe UNSAFE;
    static final int NEW = 0;
    static final int COMPLETING = 1;
    static final int NORMAL = 2;
    static final int CANCELLING = 3;
    static final int CANCELLED = 4;
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractObjectWriter.class);
    private static final long STATE_OFFSET;
    private static final long EXCEPTION_OFFSET;
    private static final long ARRAY_BASE_OFFSET;
    private static final long ARRAY_INDEX_SCALE;
    private static final long ETAGS_OFFSET;

    static {
        UNSAFE = ReflectionUtil.getUnsafe();
        try {
            STATE_OFFSET = UNSAFE.objectFieldOffset(AbstractObjectWriter.class.getDeclaredField("state"));
            EXCEPTION_OFFSET = UNSAFE.objectFieldOffset(AbstractObjectWriter.class.getDeclaredField("exception"));
            ARRAY_BASE_OFFSET = UNSAFE.arrayBaseOffset(PartETag[].class);
            ARRAY_INDEX_SCALE = UNSAFE.arrayIndexScale(PartETag[].class);
            ETAGS_OFFSET = UNSAFE.objectFieldOffset(AbstractObjectWriter.class.getDeclaredField("eTags"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    final ObjectClient client;
    final int chunkSize;
    final int chunkShift;
    volatile String uploadId;
    volatile RGWCompositeException exception;
    private final AtomicBoolean closed;
    private volatile int state;
    private volatile PartETag[] eTags;

    AbstractObjectWriter(ObjectClient client, String bucketName, String key, Metadata metadata, ACL acl, CannedACL cannedACL, int chunkSize) {
        super(bucketName, key, metadata, acl, cannedACL);
        this.client = Objects.requireNonNull(client);
        if (chunkSize <= 0) {
            this.chunkSize = DEFAULT_CHUNK_SIZE;
        } else if (chunkSize >= DEFAULT_CHUNK_SIZE * 4) {
            this.chunkSize = DEFAULT_CHUNK_SIZE * 4;
        } else {
            this.chunkSize = nextMultiChunkSize(chunkSize);
        }
        this.chunkShift = log2(this.chunkSize);
        this.uploadId = null;
        this.exception = null;
        this.closed = new AtomicBoolean(false);
        this.state = NEW;
        this.eTags = new PartETag[4];
    }

    static int log2(int i) {
        int r = 0;
        while ((i >>= 1) != 0) {
            ++r;
        }
        return r;
    }

    private static int nextMultiChunkSize(int size) {
        size--;
        size |= size >>> 1;
        size |= size >>> 2;
        size |= size >>> 4;
        size |= size >>> 8;
        size |= size >>> 16;
        return ++size;
    }

    private static void checkLength(byte[] bytes, int offset, int length) {
        if ((offset < 0) || (offset > bytes.length) || (length < 0) ||
                ((offset + length) - bytes.length > 0)) {
            throw new IndexOutOfBoundsException();
        }
    }

    private static void checkLength(File file, long offset, long length) {
        if ((offset < 0) || (offset > file.length()) || (length < 0) ||
                ((offset + length) - file.length() > 0)) {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public ActionFuture<BasePutObjectResponse> completeAsync() {
        ListenableActionFuture<BasePutObjectResponse> future = new ListenableActionFuture<>();
        completeAsync(future);
        return future;
    }

    @Override
    public void completeAsync(ActionListener<BasePutObjectResponse> listener) {
        try {
            enterComplete();
            doCompleteAsync(listener);
        } catch (Throwable cause) {
            listener.onFailure(cause);
        }
    }

    abstract void doCompleteAsync(ActionListener<BasePutObjectResponse> listener) throws Throwable;

    int chunkSeq(long seq) {
        return (int) (seq >>> chunkShift);
    }

    void initiateUpload() {
        safelyUpload(() -> client.prepareInitiateMultipartUpload()
                .withBucketName(getBucketName())
                .withKey(getKey())
                .withMetadata(getMetadata())
                .withCannedACL(getCannedACL())
                .withACL(getACL())
                .execute(initListener()));
    }

    private ActionListener<InitiateMultipartUploadResponse> initListener() {
        return new ActionListener<InitiateMultipartUploadResponse>() {
            @Override
            public void onSuccess(InitiateMultipartUploadResponse response) {
                uploadId = response.getUploadId();
                exitWrite();
                LOGGER.info("Finished to init multipart upload [{}].", response);
            }

            @Override
            public void onFailure(Throwable cause) {
                setException(cause);
                LOGGER.error("Failed to init multipart upload.", cause);
            }
        };
    }

    private void rangeCheck(int index) {
        PartETag[] prev, update;
        int len;
        do {
            prev = eTags;
            if ((len = prev.length) > index) {
                return;
            }
            do {
                len <<= 1;
            } while (len <= index);
            update = new PartETag[len];
            System.arraycopy(prev, 0, update, 0, prev.length);
        } while (!UNSAFE.compareAndSwapObject(this, ETAGS_OFFSET, prev, update));
    }

    void addPartETag(int num, String eTag) {
        rangeCheck(num);
        long address = num * ARRAY_INDEX_SCALE + ARRAY_BASE_OFFSET;
        UNSAFE.putObjectVolatile(eTags, address, new PartETag(num, eTag));
    }

    private Collection<PartETag> getPartETags() {
        List<PartETag> list = Stream.of(eTags).filter(Objects::nonNull).collect(Collectors.toList());
        LOGGER.debug("Final PartETag list is {}.", list);
        return list;
    }

    void completeUpload(ActionListener<BasePutObjectResponse> listener) {
        client.prepareCompleteMultipartUpload()
                .withBucketName(getBucketName())
                .withKey(getKey())
                .withUploadId(uploadId)
                .withPartETags(getPartETags())
                .execute(completeListener(listener));
    }

    <R extends BasePutObjectResponse> ActionListener<R> completeListener(ActionListener<BasePutObjectResponse> listener) {
        return new ActionListener<R>() {
            @Override
            public void onSuccess(R response) {
                exitComplete();
                close();
                listener.onSuccess(response);
                LOGGER.info("Finished to complete multipart upload.", response);
            }

            @Override
            public void onFailure(Throwable cause) {
                setException(cause);
                close();
                listener.onFailure(cause);
                LOGGER.error("Failed to complete multipart upload.", cause);
            }
        };
    }

    @Override
    public void write(byte[] bytes, int offset, int length) {
        checkLength(bytes, offset, length);
        if (length == 0) {
            return;
        }
        enterWrite();
        try {
            doWrite(bytes, offset, length);
            exitWrite();
        } catch (Throwable e) {
            setException(e);
            throw exception;
        }
    }

    abstract void doWrite(byte[] bytes, int offset, int length) throws Throwable;

    @Override
    public void write(ByteBuffer buffer) {
        if (!buffer.hasRemaining()) {
            return;
        }
        enterWrite();
        try {
            doWrite(buffer);
            exitWrite();
        } catch (Throwable e) {
            setException(e);
            throw exception;
        }
    }

    abstract void doWrite(ByteBuffer buffer) throws Throwable;

    @Override
    public void write(File file, long offset, long length) {
        checkLength(file, offset, length);
        if (length == 0) {
            return;
        }
        enterWrite();
        FileChannel source = null;
        try {
            source = FileChannel.open(file.toPath(), StandardOpenOption.READ);
            doWrite(source, offset, length);
            exitWrite();
        } catch (Throwable e) {
            setException(e);
            throw exception;
        } finally {
            if (source != null) {
                try {
                    source.close();
                } catch (IOException e) {
                    LOGGER.error("Failed to close source file channel [{}].", file, e);
                }
            }
        }
    }

    abstract void doWrite(FileChannel file, long offset, long length) throws Throwable;

    private boolean casState(int expect, int update) {
        return UNSAFE.compareAndSwapInt(this, STATE_OFFSET, expect, update);
    }

    private void setState(int state) {
        UNSAFE.putOrderedInt(this, STATE_OFFSET, state);
    }

    private String getState() {
        switch (state) {
            case NEW:
                return "new";
            case COMPLETING:
                return "completing";
            case NORMAL:
                return "normal";
            case CANCELLING:
                return "cancelling";
            case CANCELLED:
                return "cancelled";
            default:
                return "writing/uploading";
        }
    }

    private void enterWrite() {
        int s;
        do {
            s = state;
            if (s > NEW) {
                throw new IllegalStateException("cannot write, current state is " + getState());
            }
        } while (!casState(s, s - 1));
    }

    void exitWrite() {
        int s;
        do {
            s = state;
        } while (s < NEW && !casState(s, s + 1));
    }

    void safelyUpload(Runnable r) {
        enterWrite();
        try {
            r.run();
        } catch (Throwable cause) {
            exitWrite();
        }
    }

    public boolean isDone() {
        return state == NORMAL || state == CANCELLED;
    }

    void setException(Throwable cause) {
        RGWException e;
        if (cause instanceof RGWException) {
            e = (RGWException) cause;
        } else {
            e = new RGWException(cause);
        }
        if (!UNSAFE.compareAndSwapObject(this, EXCEPTION_OFFSET, null, new RGWCompositeException(e))) {
            exception.addCause(e);
        }
        exitWrite();
    }

    void checkException() {
        if (exception != null) {
            throw exception;
        }
    }

    void checkUploadId() {
        while (StringUtils.isBlank(uploadId)) {
            checkException();
            LockSupport.parkNanos(50);
        }
    }

    private void enterComplete() {
        do {
            if (state > NEW) {
                throw new IllegalStateException("cannot complete, current state is " + getState());
            }
            checkException();
        } while (!casState(NEW, COMPLETING));
    }

    private void exitComplete() {
        setState(NORMAL);
    }

    private void exitCancel() {
        setState(CANCELLED);
    }

    @Override
    public boolean cancel() {
        do {
            switch (state) {
                case COMPLETING:
                case NORMAL:
                case CANCELLING:
                    return false;
                case CANCELLED:
                    return true;
            }
        } while (!casState(NEW, CANCELLING));
        try {
            if (StringUtils.isNotBlank(uploadId)) {
                client.prepareAbortMultipartUpload()
                        .withBucketName(getBucketName())
                        .withKey(getKey())
                        .withUploadId(uploadId)
                        .execute()
                        .actionGet();
            }
            close();
            return true;
        } finally {
            exitCancel();
        }
    }

    void close() {
        if (closed.compareAndSet(false, true)) {
            doClose();
        }
    }

    abstract void doClose();

    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }
}
