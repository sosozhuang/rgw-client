package io.ceph.rgw.client.core.subscribe;

import io.netty.handler.codec.http.HttpRequest;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/6/24.
 */
public interface RequestInterceptor {
    void intercept(HttpRequest request);

    RequestInterceptor NOOP = request -> {
    };
}
