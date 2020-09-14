package io.ceph.rgw.client.core.async;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.PooledByteBufAllocator;
import org.apache.commons.lang3.RandomStringUtils;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.core.async.SdkPublisher;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/8/26.
 */
class InputStreamAsyncResponseTransformer implements AsyncResponseTransformer<GetObjectResponse, Map.Entry<GetObjectResponse, InputStream>> {
    private final CompletableFuture<InputStream> cf;
    private volatile GetObjectResponse response;

    InputStreamAsyncResponseTransformer() {
        this.cf = new CompletableFuture<>();
    }

    @Override
    public CompletableFuture<Map.Entry<GetObjectResponse, InputStream>> prepare() {
        return cf.thenApply(is -> new AbstractMap.SimpleImmutableEntry<>(response, is));
    }

    @Override
    public void onResponse(GetObjectResponse response) {
        this.response = response;
    }

    @Override
    public void onStream(SdkPublisher<ByteBuffer> publisher) {
        publisher.subscribe(new InputStreamSubscriber(cf, response == null ? null : response.contentLength()));
    }

    @Override
    public void exceptionOccurred(Throwable throwable) {
        cf.completeExceptionally(throwable);
    }

    private static class InputStreamSubscriber implements Subscriber<ByteBuffer> {
        private static final int MAX_BUFFER_CAPACITY = 1 << 22;
        private final CompletableFuture<InputStream> future;
        private final ByteBuf byteBuf;
        private volatile File file;
        private volatile FileChannel channel;
        private Subscription subscription;

        InputStreamSubscriber(CompletableFuture<InputStream> future, Long length) {
            this.future = future;
            if (length != null && length > MAX_BUFFER_CAPACITY) {
                createTempFile();
                this.byteBuf = null;
            } else {
                this.byteBuf = PooledByteBufAllocator.DEFAULT.heapBuffer(4096, MAX_BUFFER_CAPACITY);
            }
        }

        private void createTempFile() {
            try {
                this.file = File.createTempFile(RandomStringUtils.randomAlphabetic(32), null);
                this.channel = FileChannel.open(file.toPath(), StandardOpenOption.WRITE, StandardOpenOption.APPEND);
            } catch (IOException e) {
                future.completeExceptionally(e);
            }
        }

        @Override
        public void onSubscribe(Subscription s) {
            if (this.subscription != null) {
                s.cancel();
                return;
            }
            this.subscription = s;
            subscription.request(Long.MAX_VALUE);
        }

        private boolean capacityCheck(int length) {
            if (byteBuf.readableBytes() + length > MAX_BUFFER_CAPACITY) {
                return false;
            }
            while (byteBuf.writableBytes() < length) {
                byteBuf.capacity(byteBuf.capacity() << 1);
            }
            return true;
        }

        @Override
        public void onNext(ByteBuffer buffer) {
            if (future.isCancelled()) {
                subscription.cancel();
                return;
            }
            try {
                if (channel != null) {
                    channel.write(buffer);
                } else {
                    if (capacityCheck(buffer.remaining())) {
                        byteBuf.writeBytes(buffer);
                    } else {
                        createTempFile();
                        byteBuf.readBytes(channel, 0, byteBuf.readableBytes());
                        onNext(buffer);
                    }
                }
            } catch (Exception ignored) {
            }
            subscription.request(1);
        }

        @Override
        public void onError(Throwable throwable) {
            try {
                if (byteBuf != null) {
                    byteBuf.release();
                }
                if (file != null) {
                    try {
                        channel.close();
                    } catch (IOException e) {
                        throwable.addSuppressed(e);
                    }
                    file.delete();
                    file = null;
                }
            } finally {
                completeExceptionally(throwable);
            }
        }

        @Override
        public void onComplete() {
            if (file == null) {
                complete(new ByteBufInputStream(byteBuf, true));
            } else {
                try {
                    channel.close();
                    complete(new FileInputStream(file));
                } catch (IOException e) {
                    completeExceptionally(e);
                } finally {
                    file.deleteOnExit();
                    file = null;
                }
            }
        }

        private void complete(InputStream is) {
            if (!future.isCancelled()) {
                future.complete(is);
            }
        }

        private void completeExceptionally(Throwable cause) {
            if (!future.isCancelled()) {
                future.completeExceptionally(cause);
            }
        }
    }
}
