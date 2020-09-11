package io.ceph.rgw.client.model.admin;

import io.ceph.rgw.client.model.Request;
import software.amazon.awssdk.services.s3.model.S3Request;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * A request of an <a href="https://docs.ceph.com/docs/master/radosgw/adminops/">admin operation</a>.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/3/17.
 * @see AdminResponse
 * @see io.ceph.rgw.client.AdminClient
 */
public abstract class AdminRequest extends S3Request implements Request {
    static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withLocale(Locale.CHINA).withZone(ZoneOffset.ofHours(8));

    protected AdminRequest(Builder builder) {
        super(builder);
    }

    static String dateToString(Date date) {
        return Optional.ofNullable(date).map(d -> DATE_FORMATTER.format(d.toInstant())).orElse(null);
    }

    static Date stringToDate(String date) {
        return Optional.ofNullable(date).map(d -> Date.from(Instant.from(DATE_FORMATTER.parse(d)))).orElse(null);
    }

    static <T> List<T> unmodifiableList(List<T> list) {
        return Optional.ofNullable(list).map(Collections::unmodifiableList).orElse(Collections.emptyList());
    }

    static <K, V> Map<K, V> unmodifiableMap(Map<K, V> map) {
        return Optional.ofNullable(map).map(Collections::unmodifiableMap).orElse(Collections.emptyMap());
    }

    static String toString(Object object) {
        return object == null ? null : object.toString();
    }
}
