package io.ceph.rgw.client.spring.autoconfigure;

import io.ceph.rgw.client.config.Configuration;
import io.ceph.rgw.client.config.RGWClientProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/2/27.
 */
@ConfigurationProperties(RGWClientAutoProperties.NAME)
public final class RGWClientAutoProperties extends RGWClientProperties {
    public static final String NAME = "rgwclient";
    @Value("${spring.application.name}")
    private String applicationName;
    private Boolean enableTrace;
    private Map<String, String> hystrix;

    @Override
    public String getApplicationName() {
        return applicationName;
    }

    public Boolean isEnableTrace() {
        return enableTrace;
    }

    public void setEnableTrace(Boolean enableTrace) {
        this.enableTrace = enableTrace;
    }

    public Map<String, String> getHystrix() {
        return hystrix;
    }

    public void setHystrix(Map<String, String> hystrix) {
        this.hystrix = hystrix;
    }

    @PostConstruct
    private void init() {
        super.hystrixConfig = new Configuration(hystrix);
        validate();
    }

    @Override
    public String toString() {
        return "RGWClientAutoProperties{" +
                "applicationName='" + applicationName + '\'' +
                ", enableTrace=" + enableTrace +
                "} " + super.toString();
    }
}
