package io.ceph.rgw.client.model;

import io.ceph.rgw.client.util.StringPairCollection;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhuangshuo on 2020/3/1.
 */
public class Metadata extends StringPairCollection {
    private final String cacheControl;
    private final String disposition;
    private final String encoding;
    private final String contentLanguage;
    private final Long contentLength;
    private final String md5;
    private final String contentType;
    private final Date httpExpiresDate;

    public Metadata(Map<String, String> userMetadata,
                    String cacheControl, String disposition,
                    String encoding, String contentLanguage,
                    Long contentLength, String md5,
                    String contentType, Date httpExpiresDate) {
        super(userMetadata);
        this.cacheControl = cacheControl;
        this.disposition = disposition;
        this.encoding = encoding;
        this.contentLanguage = contentLanguage;
        this.contentLength = contentLength;
        this.md5 = md5;
        this.contentType = contentType;
        this.httpExpiresDate = httpExpiresDate;
    }

    public String getCacheControl() {
        return cacheControl;
    }

    public String getContentDisposition() {
        return disposition;
    }

    public String getContentEncoding() {
        return encoding;
    }

    public String getContentLanguage() {
        return contentLanguage;
    }

    public Long getContentLength() {
        return contentLength;
    }

    public String getContentMD5() {
        return md5;
    }

    public String getContentType() {
        return contentType;
    }

    public Date getHttpExpiresDate() {
        return httpExpiresDate;
    }

    @Override
    public String toString() {
        return "Metadata{" +
                "cacheControl='" + cacheControl + '\'' +
                ", disposition='" + disposition + '\'' +
                ", encoding='" + encoding + '\'' +
                ", contentLanguage='" + contentLanguage + '\'' +
                ", contentLength=" + contentLength +
                ", md5='" + md5 + '\'' +
                ", contentType='" + contentType + '\'' +
                ", httpExpiresDate=" + httpExpiresDate +
                "} " + super.toString();
    }

    static class NestedBuilder<T extends NestedBuilder<T>> extends NestedGenericBuilder<T, Metadata> {
        final Map<String, String> map;
        String cacheControl;
        String disposition;
        String encoding;
        String contentLanguage;
        Long contentLength;
        String md5;
        String contentType;
        Date httpExpiresDate;

        NestedBuilder() {
            this.map = new HashMap<>();
        }

        public T add(String key, String value) {
            map.put(key, value);
            return self();
        }

        public T withCacheControl(String cacheControl) {
            this.cacheControl = cacheControl;
            return self();
        }

        public T withContentDisposition(String disposition) {
            this.disposition = disposition;
            return self();
        }

        public T withContentEncoding(String encoding) {
            this.encoding = encoding;
            return self();
        }

        public T withContentLanguage(String contentLanguage) {
            this.contentLanguage = contentLanguage;
            return self();
        }

        public T withContentLength(Long contentLength) {
            this.contentLength = contentLength;
            return self();
        }

        public T withContentMD5(String md5) {
            this.md5 = md5;
            return self();
        }

        public T withContentType(String contentType) {
            this.contentType = contentType;
            return self();
        }

        public T withHttpExpiresDate(Date httpExpiresDate) {
            this.httpExpiresDate = httpExpiresDate;
            return self();
        }

        @Override
        Metadata build() {
            return new Metadata(map, cacheControl, disposition, encoding,
                    contentLanguage, contentLength, md5, contentType, httpExpiresDate);
        }
    }

    public static class Builder extends NestedBuilder<Builder> {

        @Override
        public Metadata build() {
            return super.build();
        }
    }
}
