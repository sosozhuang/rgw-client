package io.ceph.rgw.client.core.io;

import io.ceph.rgw.client.ObjectClient;
import io.ceph.rgw.client.action.ActionListener;
import io.ceph.rgw.client.model.ACL;
import io.ceph.rgw.client.model.BasePutObjectResponse;
import io.ceph.rgw.client.model.CannedACL;
import io.ceph.rgw.client.model.Metadata;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;

/**
 * Reusable {@link ByteBuf} based ObjectWriter, writes data to a direct ByteBuf, and uploads buffer data.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/7/15.
 */
abstract class ByteBufObjectWriter extends AbstractObjectWriter {
    final int ratio;
    final ByteBuf byteBuf;
    private final int mask;
    private final int chunkMask;
    private final int ratioMask;

    ByteBufObjectWriter(ObjectClient client, String bucketName, String key, Metadata metadata, ACL acl, CannedACL cannedACL, int chunkSize, int ratio, boolean pooled) {
        super(client, bucketName, key, metadata, acl, cannedACL, chunkSize);
        if (ratio < DEFAULT_RATIO) {
            this.ratio = DEFAULT_RATIO;
        } else if (ratio > 64) {
            this.ratio = 64;
        } else {
            this.ratio = nextRatio(ratio);
        }
        int cap = this.chunkSize * this.ratio;
        if (pooled) {
            this.byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer(cap, cap);
        } else {
            this.byteBuf = UnpooledByteBufAllocator.DEFAULT.directBuffer(cap, cap);
        }
        this.mask = this.byteBuf.maxCapacity() - 1;
        this.chunkMask = this.chunkSize - 1;
        this.ratioMask = this.ratio - 1;
    }

    private static int nextRatio(int ratio) {
        ratio--;
        ratio |= ratio >>> 1;
        ratio |= ratio >>> 2;
        ratio |= ratio >>> 4;
        return ++ratio;
    }

    long leftChunkSeq(long seq) {
        return seq & ~chunkMask;
    }

    long rightChunkSeq(long seq) {
        seq--;
        seq |= chunkMask;
        return ++seq;
    }

    int writtenInChunk(long seq) {
        return (int) (seq & chunkMask);
    }

    int writableInChunk(long seq) {
        return (int) ((seq & chunkMask ^ chunkMask) + 1);
    }

    int multiChunkSize(int m) {
        return m << chunkShift;
    }

    int calIndex(long seq) {
        return (int) (seq & mask);
    }

    int chunkIndex(long seq) {
        return (int) (seq >>> chunkShift) & ratioMask;
    }

    int chunkIndex(int chunkSeq) {
        return chunkSeq & ratioMask;
    }

    void putBuffer(ActionListener<BasePutObjectResponse> listener) {
        client.preparePutByteBuffer()
                .withBucketName(getBucketName())
                .withKey(getKey())
                .withValue(byteBuf.nioBuffer(0, calIndex(getWriterSeq() + 1)))
                .withMetadata(getMetadata())
                .withCannedACL(getCannedACL())
                .withACL(getACL())
                .execute(completeListener(listener));
    }

    abstract long getWriterSeq();

    @Override
    void doClose() {
        if (byteBuf != null) {
            byteBuf.release();
        }
    }
}
