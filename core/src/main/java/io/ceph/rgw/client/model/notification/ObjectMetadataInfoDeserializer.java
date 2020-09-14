package io.ceph.rgw.client.model.notification;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import io.ceph.rgw.client.model.Metadata;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;

import static io.ceph.rgw.client.model.notification.ObjectMetadataInfoSerializer.CREATE_TIME_FORMATTER;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/9/4.
 */
public class ObjectMetadataInfoDeserializer extends JsonDeserializer<ObjectMetadataInfo> {
    @Override
    public ObjectMetadataInfo deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
        ObjectAttrs attrs = null;
        Bucket bucket = null;
        String keyName = null, keyInstance = null;
        Metadata.Builder builder = new Metadata.Builder();
        JsonToken token;
        String name;
        int l = 0;
        while (!parser.isClosed()) {
            token = parser.nextToken();
            if (JsonToken.END_OBJECT.equals(token)) {
                l--;
            } else if (JsonToken.FIELD_NAME.equals(token)) {
                name = parser.getCurrentName();
                parser.nextToken();
                if (l == 0) {
                    switch (name) {
                        case "bucket":
                            bucket = new Bucket(null, parser.getText(), null);
                            break;
                        case "name":
                            keyName = parser.getText();
                            break;
                        case "instance":
                            keyInstance = parser.getText();
                            break;
                        case "create_time":
                            attrs = new ObjectAttrs(Date.from(Instant.from(CREATE_TIME_FORMATTER.parse(parser.getText()))));
                            break;
                        case "meta":
                            l++;
                            break;
                    }
                } else if (l == 1) {
                    switch (name) {
                        case "cache_control":
                            builder.withCacheControl(parser.getText());
                            break;
                        case "content_disposition":
                            builder.withContentDisposition(parser.getText());
                            break;
                        case "content_encoding":
                            builder.withContentEncoding(parser.getText());
                            break;
                        case "content_language":
                            builder.withContentLanguage(parser.getText());
                            break;
                        case "content_length":
                            builder.withContentLength(parser.getValueAsLong());
                            break;
                        case "md5":
                            builder.withContentMD5(parser.getText());
                            break;
                        case "content_type":
                            builder.withContentType(parser.getText());
                            break;
                        case "expires_date":
                            builder.withHttpExpiresDate(Date.from(Instant.from(CREATE_TIME_FORMATTER.parse(parser.getText()))));
                            break;
                        case "user":
                            l++;
                            break;
                    }
                } else if (l == 2) {
                    builder.add(name, parser.getText());
                }
            }
        }
        ObjectKey key = new ObjectKey(keyInstance, keyName);
        ObjectInfo info = new ObjectInfo(attrs, bucket, key);
        return new ObjectMetadataInfo(info, builder.build());
    }
}
