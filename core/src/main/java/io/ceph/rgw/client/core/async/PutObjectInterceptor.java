package io.ceph.rgw.client.core.async;

import software.amazon.awssdk.core.interceptor.Context;
import software.amazon.awssdk.core.interceptor.ExecutionAttributes;
import software.amazon.awssdk.core.interceptor.ExecutionInterceptor;
import software.amazon.awssdk.http.SdkHttpRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/8/11.
 */
public class PutObjectInterceptor implements ExecutionInterceptor {
    @Override
    public SdkHttpRequest modifyHttpRequest(Context.ModifyHttpRequest context, ExecutionAttributes executionAttributes) {
        if (context.request() instanceof PutObjectRequest) {
            return context.httpRequest().toBuilder().removeHeader("Expect").build();
        }
        return context.httpRequest();
    }
}
