package io.ceph.rgw.client.exception;

import io.ceph.rgw.client.util.AtomicArray;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Represents that is a composite of one or more other exceptions.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/7/24.
 */
public class RGWCompositeException extends RGWException {
    private final AtomicArray<Throwable> exceptions;

    public RGWCompositeException(String message) {
        super(message);
        this.exceptions = new AtomicArray<>();
        addCause(this);
    }

    public RGWCompositeException(String message, Throwable cause) {
        super(message, cause);
        this.exceptions = new AtomicArray<>();
        addCause(cause);
    }

    public RGWCompositeException(Throwable cause) {
        super(cause);
        this.exceptions = new AtomicArray<>();
        addCause(cause);
    }

    public void addCause(Throwable cause) {
        exceptions.add(cause);
    }

    public Collection<Throwable> getExceptions() {
        return Collections.unmodifiableCollection(Arrays.asList(exceptions.getAll()));
    }
}
