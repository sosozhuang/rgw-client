package io.ceph.rgw.client.converter;

import io.ceph.rgw.client.exception.RGWException;
import io.ceph.rgw.client.exception.RGWServerException;
import io.ceph.rgw.client.model.notification.ObjectMetadataInfo;
import io.ceph.rgw.client.model.notification.SubscribeObjectResponse;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.io.IOException;

/**
 * Accepts netty {@link FullHttpResponse} and converts to {@link io.ceph.rgw.client.model.notification.SubscribeResponse}.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/8/24.
 * @see io.ceph.rgw.client.SubscribeClient
 * @see SubscribeRequestConverter
 */
public final class SubscribeResponseConverter extends ObjectMapperConverter {
    private SubscribeResponseConverter() {
        throw new RuntimeException();
    }

    public static SubscribeObjectResponse subscribeObject(FullHttpResponse response) {
        ByteBuf content = response.content();
        try {
            if (response.status().code() != HttpResponseStatus.OK.code()) {
                RGWServerException exception = new RGWServerException(response.status().reasonPhrase());
                exception.setStatus(response.status().code());
                exception.setHeaders(response.headers().entries());
                exception.setDetail(new String(ByteBufUtil.getBytes(content)));
                throw exception;
            }
            ObjectMetadataInfo info = MAPPER.readValue(ByteBufUtil.getBytes(content), ObjectMetadataInfo.class);
            return new SubscribeObjectResponse(info);
        } catch (IOException e) {
            throw new RGWException(e);
        } finally {
            content.release();
        }
    }
}
