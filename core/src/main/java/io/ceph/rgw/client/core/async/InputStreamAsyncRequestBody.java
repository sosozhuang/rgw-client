package io.ceph.rgw.client.core.async;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.internal.util.NoopSubscription;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/7/3.
 */
public class InputStreamAsyncRequestBody implements AsyncRequestBody {
    private final InputStream inputStream;
    private final long length;

    public InputStreamAsyncRequestBody(InputStream inputStream) {
        this.inputStream = Objects.requireNonNull(inputStream);
        try {
            this.length = this.inputStream.available();
        } catch (IOException e) {
            throw new IllegalArgumentException("cannot get number of bytes", e);
        }
    }

    public InputStreamAsyncRequestBody(String s) {
        this(s, null);
    }

    public InputStreamAsyncRequestBody(String s, Charset charset) {
        this(new ByteArrayInputStream(charset == null ? s.getBytes() : s.getBytes(charset)));
    }

    @Override
    public Optional<Long> contentLength() {
        return Optional.of(length);
    }

    @Override
    public void subscribe(Subscriber<? super ByteBuffer> s) {
        Objects.requireNonNull(s);
        try {
            s.onSubscribe(new InputStreamSubscription(inputStream, s));
        } catch (Throwable t) {
            s.onSubscribe(new NoopSubscription(s));
            s.onError(t);
        }
    }

    private static class InputStreamSubscription implements Subscription {
        private final InputStream inputStream;
        private final Subscriber<? super ByteBuffer> subscriber;
        private final AtomicLong request;
        private final AtomicBoolean reading;
        private final AtomicBoolean done;

        InputStreamSubscription(InputStream inputStream, Subscriber<? super ByteBuffer> subscriber) {
            this.inputStream = inputStream;
            this.subscriber = subscriber;
            this.request = new AtomicLong(0);
            this.reading = new AtomicBoolean(false);
            this.done = new AtomicBoolean(false);
        }

        @Override
        public void request(long n) {
            if (done.get()) {
                return;
            }
            if (n < 1) {
                signalOnError(new IllegalArgumentException("cannot request non positive number: " + n));
            } else {
                try {
                    request.getAndUpdate(initial -> {
                        if (Long.MAX_VALUE - initial < n) {
                            return Long.MAX_VALUE;
                        } else {
                            return initial + n;
                        }
                    });
                    if (reading.compareAndSet(false, true)) {
                        doRead();
                    }
                } catch (Throwable t) {
                    signalOnError(t);
                }
            }
        }

        private void doRead() {
            byte[] bytes = new byte[4096];
            int n;
            try {
                n = inputStream.read(bytes);
            } catch (IOException e) {
                signalOnError(e);
                closeInputStream();
                return;
            }
            if (n > 0) {
                ByteBuffer buffer = ByteBuffer.allocate(n);
                buffer.put(bytes, 0, n);
                buffer.flip();
                signalOnNext(buffer);
            }
            if (n < 4096) {
                signalOnComplete();
                closeInputStream();
            } else {
                if (request.decrementAndGet() > 0) {
                    doRead();
                    return;
                }
            }
            reading.set(false);
        }

        @Override
        public void cancel() {
            if (done.compareAndSet(false, true)) {
                closeInputStream();
            }
        }

        private void closeInputStream() {
            try {
                inputStream.close();
            } catch (IOException e) {
                signalOnError(e);
            }
        }

        private void signalOnNext(ByteBuffer bb) {
            if (done.compareAndSet(false, true)) {
                subscriber.onNext(bb);
            }
        }

        private void signalOnComplete() {
            if (done.compareAndSet(false, true)) {
                subscriber.onComplete();
            }
        }

        private void signalOnError(Throwable t) {
            if (done.compareAndSet(false, true)) {
                subscriber.onError(t);
            }
        }
    }
}
