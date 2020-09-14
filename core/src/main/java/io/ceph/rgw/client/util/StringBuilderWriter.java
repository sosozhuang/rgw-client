package io.ceph.rgw.client.util;

import java.io.Writer;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/4/1.
 */
public class StringBuilderWriter extends Writer {
    private final StringBuilder builder;

    public StringBuilderWriter() {
        this.builder = new StringBuilder();
    }

    public StringBuilderWriter(int capacity) {
        this.builder = new StringBuilder(capacity);
    }

    public StringBuilderWriter(StringBuilder builder) {
        this.builder = builder != null ? builder : new StringBuilder();
    }

    @Override
    public Writer append(char value) {
        builder.append(value);
        return this;
    }

    @Override
    public Writer append(CharSequence value) {
        builder.append(value);
        return this;
    }

    @Override
    public Writer append(CharSequence value, int start, int end) {
        builder.append(value, start, end);
        return this;
    }

    @Override
    public void close() {
    }

    @Override
    public void flush() {
    }


    @Override
    public void write(String value) {
        builder.append(value);
    }

    @Override
    public void write(char[] value, int offset, int length) {
        builder.append(value, offset, length);
    }

    @Override
    public String toString() {
        return builder.toString();
    }
}
