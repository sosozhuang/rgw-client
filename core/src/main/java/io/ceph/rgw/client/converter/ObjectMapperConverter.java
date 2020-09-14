package io.ceph.rgw.client.converter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import java.text.SimpleDateFormat;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/6/19.
 */
abstract class ObjectMapperConverter {
    static final ObjectMapper MAPPER;

    static {
        MAPPER = new ObjectMapper();
        MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES))
                .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS'Z'"));
    }
}
