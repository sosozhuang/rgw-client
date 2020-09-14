package io.ceph.rgw.client.core;

import io.ceph.rgw.client.action.ActionExecutor;
import io.ceph.rgw.client.exception.RGWException;
import io.ceph.rgw.client.util.IOUtil;

import java.util.Objects;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/3/14.
 */
public abstract class ConnectorAware<C> {
    protected final Connectors<C> connectors;
    protected final ActionExecutor executor;

    protected ConnectorAware(Connectors<C> connectors, ActionExecutor executor) {
        this.connectors = Objects.requireNonNull(connectors);
        this.executor = Objects.requireNonNull(executor);
    }

    protected RGWException handleException(Throwable cause) {
        return handleException(null, cause);
    }

    protected RGWException handleException(C connector, Throwable cause) {
        if (cause instanceof RGWException) {
            return (RGWException) cause;
        }
        if (connector != null && IOUtil.isIOException(cause)) {
            connectors.markFailure(connector, cause);
        }
        return new RGWException(cause);
    }
}
