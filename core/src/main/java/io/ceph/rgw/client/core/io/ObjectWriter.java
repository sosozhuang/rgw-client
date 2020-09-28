package io.ceph.rgw.client.core.io;

import io.ceph.rgw.client.ObjectClient;
import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;
import io.ceph.rgw.client.model.BasePutObjectResponse;
import io.ceph.rgw.client.model.ObjectRequest;
import io.ceph.rgw.client.model.PutObjectRequest;
import io.netty.util.CharsetUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * An ObjectWriter is for writing, uploading or completing an object.
 * Object data can be from String, array of bytes, ByteBuffer or File.
 * {@link FileObjectWriter} and {@link ByteBufMultiObjectWriter} are safe for use by multiple write concurrent threads.
 * Operation of complete and cancel are safe to call whenever needed, do not have to wait for write operation to be finished.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/7/24.
 * @see ObjectClient
 */
public interface ObjectWriter extends ObjectRequest {
    /**
     * Default value of chunk size.
     *
     * @see Builder#withChunkSize(int)
     */
    int DEFAULT_CHUNK_SIZE = 1 << 23;

    /**
     * Default value of byte buffer ratio, only used by {@link ByteBufObjectWriter}.
     *
     * @see Builder#withRatio(int)
     */
    int DEFAULT_RATIO = 4;

    /**
     * Write a string.
     *
     * @param s the data
     */
    default void write(String s) {
        write(s.getBytes(CharsetUtil.UTF_8));
    }

    /**
     * Write a string.
     *
     * @param s       the data
     * @param charset the charset to be used to decode the data
     */
    default void write(String s, Charset charset) {
        write(s.getBytes(charset));
    }

    /**
     * Write an array of bytes.
     *
     * @param bytes the data
     */
    default void write(byte[] bytes) {
        write(bytes, 0, bytes.length);
    }

    /**
     * Write an array of bytes.
     *
     * @param bytes  the data
     * @param offset the offset from which to start writing
     * @param length number of bytes to write
     */
    void write(byte[] bytes, int offset, int length);

    /**
     * Write a byte buffer.
     *
     * @param buffer the data
     */
    void write(ByteBuffer buffer);

    /**
     * Write a file.
     *
     * @param file the source file
     */
    default void write(File file) {
        write(file, 0, file.length());
    }

    /**
     * Write a file.
     *
     * @param file   the source file
     * @param offset the offset from which to start writing
     * @param length number of bytes to write
     */
    void write(File file, long offset, long length);

    /**
     * Complete the upload asynchronously.
     *
     * @return An ActionFuture containing the put object response
     */
    ActionFuture<BasePutObjectResponse> completeAsync();

    /**
     * Complete the upload asynchronously.
     *
     * @param listener the callback listener after complete is done
     */
    void completeAsync(ActionListener<BasePutObjectResponse> listener);

    /**
     * Complete the upload synchronously.
     *
     * @return the put object response
     */
    default BasePutObjectResponse complete() {
        return completeAsync().actionGet();
    }

    /**
     * Cancel write or upload of the object.
     *
     * @return true if action can be cancelled, false otherwise
     */
    boolean cancel();

    class Builder extends PutObjectRequest.Builder<Builder, ObjectWriter> {
        private final ObjectClient client;
        private String file;
        private boolean singleWrite;
        private int chunkSize;
        private int ratio;
        private boolean pooled;

        public Builder(ObjectClient client) {
            this.client = client;
            this.pooled = true;
        }

        /**
         * Set writing to file.
         *
         * @param file the file path
         * @return reference of this object
         */
        public Builder withFile(String file) {
            this.file = file;
            return self();
        }

        /**
         * Set writing to buffer.
         *
         * @return reference of this object
         */
        public Builder withBuffer() {
            this.file = null;
            return self();
        }

        /**
         * Set single write thread.
         *
         * @return reference of this object
         */
        public Builder withSingleWriteThread() {
            this.singleWrite = true;
            return self();
        }

        /**
         * Set multiple write threads.
         *
         * @return reference of this object
         */
        public Builder withMultiWriteThread() {
            this.singleWrite = false;
            return self();
        }

        /**
         * Set chunk size.
         *
         * @param chunkSize the chunk size
         * @return reference of this object
         */
        public Builder withChunkSize(int chunkSize) {
            this.chunkSize = chunkSize;
            return self();
        }

        /**
         * Set ratio.
         *
         * @param ratio the ratio
         * @return reference of this object
         */
        public Builder withRatio(int ratio) {
            this.ratio = ratio;
            return self();
        }

        /**
         * Set buffer is pooled or not.
         *
         * @param pooled if used pooled buffer
         * @return reference of this object
         */
        public Builder withPooled(boolean pooled) {
            this.pooled = pooled;
            return self();
        }

        @Override
        public ObjectWriter build() {
            ObjectWriter writer;
            if (StringUtils.isNotBlank(file)) {
                writer = new FileObjectWriter(client, bucketName, key, metadata, acl, cannedACL, chunkSize, file);
            } else if (singleWrite) {
                writer = new ByteBufSingleObjectWriter(client, bucketName, key, metadata, acl, cannedACL, chunkSize, ratio, pooled);
            } else {
                writer = new ByteBufMultiObjectWriter(client, bucketName, key, metadata, acl, cannedACL, chunkSize, ratio, pooled);
            }
            return writer;
        }
    }
}
