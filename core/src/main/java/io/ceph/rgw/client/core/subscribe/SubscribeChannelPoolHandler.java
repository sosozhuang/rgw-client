package io.ceph.rgw.client.core.subscribe;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.pool.AbstractChannelPoolHandler;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/6/2.
 */
class SubscribeChannelPoolHandler extends AbstractChannelPoolHandler {

    @Override
    public void channelCreated(Channel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        p.addLast("codec", new HttpClientCodec());
        p.addLast("decompressor", new HttpContentDecompressor());
        p.addLast("idle", new IdleStateHandler(30, 0, 0));
        p.addLast("response", new SubscribeConnector.SubscribeResponseHandler());
    }
}
