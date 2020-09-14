package io.ceph.rgw.client.core.sync;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/7/20.
 */
public class ByteBufferInputStream extends InputStream {
    private final ByteBuffer buffer;

    public ByteBufferInputStream(ByteBuffer buffer) {
        this.buffer = Objects.requireNonNull(buffer);
    }

    @Override
    public int available() throws IOException {
        return buffer.remaining();
    }

    @Override
    public int read() throws IOException {
        return buffer.hasRemaining() ? buffer.get() & 0xff : -1;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (!buffer.hasRemaining()) {
            return -1;
        }
        len = Math.min(len, buffer.remaining());
        buffer.get(b, off, len);
        return len;
    }

    @Override
    public synchronized void mark(int readlimit) {
        buffer.mark();
    }

    @Override
    public boolean markSupported() {
        return true;
    }

    @Override
    public synchronized void reset() throws IOException {
        buffer.reset();
    }
}
