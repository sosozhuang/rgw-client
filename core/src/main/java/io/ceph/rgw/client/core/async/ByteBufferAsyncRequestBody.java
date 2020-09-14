package io.ceph.rgw.client.core.async;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.internal.util.NoopSubscription;

import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.Optional;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/7/15.
 */
public class ByteBufferAsyncRequestBody implements AsyncRequestBody {
    private final ByteBuffer buffer;

    public ByteBufferAsyncRequestBody(ByteBuffer buffer) {
        this.buffer = Objects.requireNonNull(buffer);
    }

    @Override
    public Optional<Long> contentLength() {
        return Optional.of((long) buffer.remaining());
    }

    @Override
    public void subscribe(Subscriber<? super ByteBuffer> s) {
        Objects.requireNonNull(s);
        try {
            s.onSubscribe(new Subscription() {
                private boolean done = false;

                @Override
                public void request(long n) {
                    if (done) {
                        return;
                    }
                    if (n > 0) {
                        done = true;
                        s.onNext(buffer);
                        s.onComplete();
                    } else {
                        s.onError(new IllegalArgumentException("cannot request non positive number: " + n));
                    }
                }

                @Override
                public void cancel() {
                    done = true;
                }
            });
        } catch (Throwable t) {
            s.onSubscribe(new NoopSubscription(s));
            s.onError(t);
        }
    }
}
