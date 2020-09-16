package io.ceph.rgw.client.spring;

import brave.Tracer;
import brave.Tracing;
import brave.sampler.Sampler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import zipkin2.reporter.Reporter;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/9/9.
 */
@SpringBootConfiguration
public class TracerConfiguration {
    @Bean
    Tracer tracer(@Value("${spring.application.name}") String applicationName) {
        Tracing tracing = Tracing.newBuilder().sampler(Sampler.ALWAYS_SAMPLE).localServiceName(applicationName).spanReporter(Reporter.CONSOLE).build();
        return tracing.tracer();
    }
}
