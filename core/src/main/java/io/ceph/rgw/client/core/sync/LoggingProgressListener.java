package io.ceph.rgw.client.core.sync;

import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressListener;
import io.ceph.rgw.client.model.Request;
import org.slf4j.Logger;

import java.util.Objects;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/3/2.
 */
public class LoggingProgressListener implements ProgressListener {
    private final Logger logger;
    private final Request request;

    private LoggingProgressListener(Logger logger, Request request) {
        this.logger = Objects.requireNonNull(logger);
        this.request = Objects.requireNonNull(request);
    }

    @Override
    public void progressChanged(ProgressEvent event) {
        logger.debug("Request [{}], event type[{}], bytes[{}], bytes transferred[{}].", request, event.getEventType(), event.getBytesTransferred(), event.getBytes());
    }

    static ProgressListener create(Logger logger, Request request) {
        if (!logger.isDebugEnabled()) {
            return ProgressListener.NOOP;
        }
        return new LoggingProgressListener(logger, request);
    }
}
