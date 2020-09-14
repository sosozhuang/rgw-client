package io.ceph.rgw.client.converter;

import io.ceph.rgw.client.model.notification.SubscribeObjectRequest;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Map;

/**
 * Accepts {@link io.ceph.rgw.client.model.notification.SubscribeRequest} and converts to netty {@link FullHttpRequest}.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/8/24.
 * @see io.ceph.rgw.client.SubscribeClient
 * @see SubscribeResponseConverter
 */
public final class SubscribeRequestConverter extends ObjectMapperConverter {
    private SubscribeRequestConverter() {
        throw new RuntimeException();
    }

    private static URI createURI(String path) {
        return createURI(path, Collections.emptyMap());
    }

    private static URI createURI(String path, String key, String value) {
        return createURI(path, Collections.singletonMap(key, value));
    }

    private static URI createURI(String path, Map<String, String> params) {
        try {
            URIBuilder builder = new URIBuilder();
            builder.setPath(path);
            for (Map.Entry<String, String> param : params.entrySet()) {
                builder.addParameter(param.getKey(), param.getValue());
            }
            return builder.build();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static FullHttpRequest subscribeObject(SubscribeObjectRequest src) {
        String condition = new String(Base64.encodeBase64(src.getCondition().getBytes()));
        return new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, createURI("/subscribe", "condition", condition).toString());
    }
}
