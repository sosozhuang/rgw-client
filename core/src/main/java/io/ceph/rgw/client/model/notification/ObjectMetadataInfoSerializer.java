package io.ceph.rgw.client.model.notification;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/5/22.
 */
public class ObjectMetadataInfoSerializer extends JsonSerializer<ObjectMetadataInfo> {
    static final DateTimeFormatter CREATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").withZone(ZoneOffset.ofHours(8));

    @Override
    public void serialize(ObjectMetadataInfo info, JsonGenerator generator, SerializerProvider provider) throws IOException, JsonProcessingException {
        generator.writeStartObject();
        generator.writeStringField("bucket", info.getInfo().getBucket().getName());
        generator.writeStringField("name", info.getInfo().getKey().getName());
        generator.writeStringField("instance", info.getInfo().getKey().getInstance());
        generator.writeStringField("create_time", CREATE_TIME_FORMATTER.format(info.getInfo().getAttrs().getMtime().toInstant()));
        generator.writeObjectFieldStart("meta");
        if (StringUtils.isNotBlank(info.getMetadata().getCacheControl())) {
            generator.writeStringField("cache_control", info.getMetadata().getCacheControl());
        }
        if (StringUtils.isNotBlank(info.getMetadata().getContentDisposition())) {
            generator.writeStringField("content_disposition", info.getMetadata().getContentDisposition());
        }
        if (StringUtils.isNotBlank(info.getMetadata().getContentEncoding())) {
            generator.writeStringField("content_encoding", info.getMetadata().getContentEncoding());
        }
        if (StringUtils.isNotBlank(info.getMetadata().getContentLanguage())) {
            generator.writeStringField("content_language", info.getMetadata().getContentLanguage());
        }
        generator.writeNumberField("content_length", info.getMetadata().getContentLength());
        if (StringUtils.isNotBlank(info.getMetadata().getContentMD5())) {
            generator.writeStringField("md5", info.getMetadata().getContentMD5());
        }
        if (StringUtils.isNotBlank(info.getMetadata().getContentType())) {
            generator.writeStringField("content_type", info.getMetadata().getContentType());
        }
        if (null != info.getMetadata().getHttpExpiresDate()) {
            generator.writeStringField("expires_date", CREATE_TIME_FORMATTER.format(info.getMetadata().getHttpExpiresDate().toInstant()));
        }
        if (info.getMetadata().all().size() > 0) {
            generator.writeObjectFieldStart("user");
            for (Map.Entry<String, String> entry : info.getMetadata()) {
                generator.writeStringField(entry.getKey(), entry.getValue());
            }
            generator.writeEndObject();
        }
        generator.writeEndObject();
        generator.writeEndObject();
        generator.flush();
        generator.close();
    }
}
