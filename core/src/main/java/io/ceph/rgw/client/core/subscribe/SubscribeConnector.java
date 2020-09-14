package io.ceph.rgw.client.core.subscribe;

import io.ceph.rgw.client.action.ActionListener;
import io.ceph.rgw.client.action.SubscribeActionFuture;
import io.ceph.rgw.client.util.AbstractClosable;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.CompositeByteBuf;
import io.netty.channel.*;
import io.netty.channel.pool.ChannelPool;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.channel.pool.SimpleChannelPool;
import io.netty.handler.codec.http.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import io.netty.util.ByteProcessor;
import io.netty.util.ReferenceCounted;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/2/28.
 */
public class SubscribeConnector extends AbstractClosable {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubscribeConnector.class);
    private static final AttributeKey<SubscribeActionFuture<FullHttpResponse>> FUTURE_KEY = AttributeKey.valueOf("FUTURE");
    private static final AttributeKey<ChannelPool> POOL_KEY = AttributeKey.valueOf("POOL");
    private static final AttributeKey<SubscribeResponseState> RESPONSE_STATE_KEY = AttributeKey.valueOf("RESPONSE_STATE");
    private final ChannelPool pool;
    private final RequestInterceptor interceptor;

    protected SubscribeConnector(Bootstrap bootstrap, ChannelPoolHandler handler, Integer maxConnections, RequestInterceptor interceptor) {
        if (maxConnections != null && maxConnections > 0) {
            this.pool = new FixedChannelPool(bootstrap, handler, maxConnections);
        } else {
            this.pool = new SimpleChannelPool(bootstrap, handler);
        }
        this.interceptor = Objects.requireNonNull(interceptor);
    }

    public void execute(FullHttpRequest request, SubscribeActionFuture<FullHttpResponse> future) {
        interceptor.intercept(request);
        pool.acquire().addListener(acquireChannelListener(request, future));
    }

    private GenericFutureListener<? extends Future<Channel>> acquireChannelListener(FullHttpRequest request, SubscribeActionFuture<FullHttpResponse> actionFuture) {
        Objects.requireNonNull(actionFuture);
        ChannelProgressiveFutureListener progressListener = LoggingProgressListener.create(LOGGER, request);
        return future -> {
            if (future.isSuccess()) {
                Channel channel = null;
                try {
                    channel = future.get();
                    channel.attr(POOL_KEY).set(pool);
                    request.touch("send-request");
                    ChannelPromise promise = channel.newPromise().addListener(sendRequestListener(actionFuture))/*.addListener(progressListener)*/;
                    if (actionFuture.isCancelled()) {
                        return;
                    }
                    actionFuture.setFuture(channel.write(request, promise));
                    channel.attr(FUTURE_KEY).set(actionFuture);
                    channel.flush();
                } catch (Throwable cause) {
                    LOGGER.error("Failed to send request [{}] in channel [{}].", request, channel, cause);
                    releaseChannel(pool, channel);
                    actionFuture.onFailure(cause);
                }
            } else {
                LOGGER.error("Failed to acquire channel from pool.", future.cause());
                actionFuture.onFailure(future.cause());
            }
        };
    }

    private GenericFutureListener<ChannelFuture> sendRequestListener(ActionListener<?> listener) {
        return future -> {
            if (future.isSuccess()) {
                LOGGER.debug("Finished to send request to [{}].", future.channel().remoteAddress());
            } else {
                LOGGER.error("Failed to send request to [{}].", future.channel().remoteAddress(), future.cause());
                releaseChannel(pool, future.channel());
                listener.onFailure(future.cause());
            }
        };
    }

    private static void releaseChannel(ChannelPool pool, Channel channel) {
        try {
            if (channel != null) {
                channel.attr(FUTURE_KEY).set(null);
                if (channel.isOpen()) {
                    channel.close();
                }
                pool.release(channel);
            }
        } catch (Throwable e) {
            LOGGER.error("Failed to release channel.", e);
        }
    }

    private static void releaseChannel(Channel channel) {
        releaseChannel(channel.attr(POOL_KEY).get(), channel);
    }

    @Override
    protected void doClose() {
        try {
            pool.close();
            LOGGER.info("Finished to close channel pool.");
        } catch (Exception e) {
            LOGGER.error("Failed to close channel pool.", e);
        }
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    private static class SubscribeResponseState {
        private final CompositeByteBuf content;
        private HttpResponse response;
        private int offset;
        private int length;

        private SubscribeResponseState(ByteBufAllocator allocator) {
            this.content = allocator.compositeBuffer();
        }


        public HttpResponse getResponse() {
            return response;
        }

        private void setResponse(HttpResponse response) {
            this.response = response;
            if (response instanceof FullHttpResponse) {
                append(((FullHttpResponse) response).content());
            }
        }

        private HttpResponseStatus getResponseStatus() {
            return response == null ? null : response.status();
        }

        private void append(ByteBuf content) {
            this.content.addComponent(true, content.retain());
        }

        private void release() {
            content.release();
        }

        private int findEndOfLine() {
            int totalLength = content.readableBytes();
            int i = content.forEachByte(content.readerIndex() + offset, totalLength - offset, ByteProcessor.FIND_LF);
            if (i >= 0) {
                offset = 0;
                if (i > 0 && content.getByte(i - 1) == '\r') {
                    i--;
                }
            } else {
                offset = totalLength;
            }
            return i;
        }

        private List<FullHttpResponse> getFullHttpResponses(boolean last) {
            if (response == null || content.readableBytes() <= 0) {
                return Collections.emptyList();
            }
            if (response != null) {
                String s = response.headers().get(HttpHeaderNames.CONTENT_LENGTH);
                if (StringUtils.isNotBlank(s) && Long.parseLong(s) >= content.readableBytes()
                        || last && Objects.equals(response.headers().get(HttpHeaderNames.TRANSFER_ENCODING), HttpHeaderValues.CHUNKED.toString())) {
                    FullHttpResponse httpResponse = new DefaultFullHttpResponse(response.protocolVersion(), response.status(), content.readRetainedSlice(content.readableBytes()));
                    httpResponse.headers().setAll(response.headers());
                    return Collections.singletonList(httpResponse);
                }
            }
            List<FullHttpResponse> list = new LinkedList<>();
            FullHttpResponse httpResponse;
            int index;
            while (true) {
                if (length == 0) {
                    index = findEndOfLine();
                    if (index < 0) {
                        break;
                    }
                    length = Integer.parseInt(new String(ByteBufUtil.getBytes(content.readSlice(index - content.readerIndex()))));
                    if (content.readByte() == '\r') {
                        content.skipBytes(1);
                    }
//                    content.skipBytes(content.getByte(index) == '\r' ? 2 : 1);
                } else {
                    if (length > content.readableBytes()) {
                        break;
                    }
                    index = findEndOfLine();
                    if (index < 0) {
                        break;
                    }
                    httpResponse = new DefaultFullHttpResponse(response.protocolVersion(), response.status(), content.readRetainedSlice(length));
                    httpResponse.headers().setAll(response.headers());
                    list.add(httpResponse);
                    if (content.readByte() == '\r') {
                        content.skipBytes(1);
                    }
//                    content.skipBytes(content.getByte(length + 1) == '\r' ? 2 : 1);
                    length = 0;
                }
            }
            return list.size() == 0 ? Collections.emptyList() : list;
        }
    }

    @ChannelHandler.Sharable
    static class SubscribeResponseHandler extends SimpleChannelInboundHandler<Object> {
        SubscribeResponseHandler() {
        }

        private static SubscribeActionFuture<FullHttpResponse> getActionFuture(ChannelHandlerContext ctx) {
            return ctx.channel().attr(FUTURE_KEY).get();
        }

        private static SubscribeResponseState getResponseState(ChannelHandlerContext ctx, boolean create) {
            SubscribeResponseState state = ctx.channel().attr(RESPONSE_STATE_KEY).get();
            if (state == null && create) {
                state = new SubscribeResponseState(ctx.alloc());
                ctx.channel().attr(RESPONSE_STATE_KEY).set(state);
            }
            return state;
        }

        private static void release(ChannelHandlerContext ctx) {
            releaseChannel(ctx.channel());
            SubscribeResponseState state = getResponseState(ctx, false);
            if (state != null) {
                state.release();
            }
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            LOGGER.debug("Channel received message [{}].", msg);
            if (msg instanceof ReferenceCounted) {
                ((ReferenceCounted) msg).touch("receive-response");
            }
            SubscribeActionFuture<FullHttpResponse> future = getActionFuture(ctx);
            if (future.isCancelled()) {
                release(ctx);
                return;
            }
            SubscribeResponseState state = getResponseState(ctx, true);
            if (msg instanceof HttpResponse) {
                state.setResponse((HttpResponse) msg);
                List<FullHttpResponse> responses = state.getFullHttpResponses(false);
                if (responses.size() > 0) {
                    responses.forEach(future::onSuccess);
                }
            } else if (msg instanceof HttpContent) {
                boolean last = msg instanceof LastHttpContent;
                if (last && Objects.equals(HttpResponseStatus.CONTINUE, state.getResponseStatus())) {
                    return;
                }
                state.append(((HttpContent) msg).content());
                List<FullHttpResponse> responses = state.getFullHttpResponses(last);
                if (responses.size() > 0) {
                    responses.forEach(future::onSuccess);
                }
                if (last) {
                    release(ctx);
                }
            } else {
                throw new IllegalStateException("unknown object type: " + msg.getClass().getName());
            }
        }

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if (evt instanceof IdleStateEvent && ((IdleStateEvent) evt).state() == IdleState.READER_IDLE) {
                SubscribeActionFuture<FullHttpResponse> future = getActionFuture(ctx);
                if (future != null && future.isCancelled()) {
                    release(ctx);
                }
                return;
            }
            super.userEventTriggered(ctx, evt);
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            try {
                SubscribeActionFuture<FullHttpResponse> future = getActionFuture(ctx);
                if (future == null || future.isCancelled()) {
                    return;
                }
                SubscribeResponseState state = getResponseState(ctx, false);
                if (state != null) {
                    List<FullHttpResponse> responses = state.getFullHttpResponses(true);
                    if (responses.size() > 0) {
                        responses.forEach(future::onSuccess);
                    }
                }
            } finally {
                release(ctx);
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            LOGGER.error("Channel [{}] exception caught.", ctx.channel(), cause);
            try {
                SubscribeActionFuture<FullHttpResponse> future = getActionFuture(ctx);
                if (future == null || future.isCancelled()) {
                    return;
                }
                future.onFailure(cause);
            } finally {
                release(ctx);

            }
        }
    }
}
