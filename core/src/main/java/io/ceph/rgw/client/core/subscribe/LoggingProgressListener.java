package io.ceph.rgw.client.core.subscribe;

import io.netty.channel.ChannelProgressiveFuture;
import io.netty.channel.ChannelProgressiveFutureListener;
import io.netty.handler.codec.http.HttpRequest;
import org.slf4j.Logger;

import java.util.Objects;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/6/15.
 */
public class LoggingProgressListener implements ChannelProgressiveFutureListener {
    private final Logger logger;
    private final HttpRequest request;

    private LoggingProgressListener(Logger logger, HttpRequest request) {
        this.logger = Objects.requireNonNull(logger);
        this.request = Objects.requireNonNull(request);
    }

    @Override
    public void operationProgressed(ChannelProgressiveFuture future, long progress, long total) throws Exception {
        logger.debug("Request [{}], remote address [{}], progress [{}], total[{}].", request, future.channel().remoteAddress(), progress, total);
    }

    @Override
    public void operationComplete(ChannelProgressiveFuture future) throws Exception {
        if (future.isSuccess()) {
            logger.debug("Request [{}] completed, remote address [{}].", request, future.channel().remoteAddress());
        } else if (future.isCancelled()) {
            logger.debug("Request [{}] cancelled, remote address [{}].", request, future.channel().remoteAddress());
        } else if (future.cause() != null) {
            logger.debug("Request [{}] throw an exception, remote address [{}].", request, future.channel().remoteAddress(), future.cause());
        }
    }

    static final ChannelProgressiveFutureListener NOOP = new ChannelProgressiveFutureListener() {
        @Override
        public void operationProgressed(ChannelProgressiveFuture future, long progress, long total) throws Exception {
        }

        @Override
        public void operationComplete(ChannelProgressiveFuture future) throws Exception {
        }
    };

    static ChannelProgressiveFutureListener create(Logger logger, HttpRequest request) {
        if (!logger.isDebugEnabled()) {
            return NOOP;
        }
        return new LoggingProgressListener(logger, request);
    }
}
