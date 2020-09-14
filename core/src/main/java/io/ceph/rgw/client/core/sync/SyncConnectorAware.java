package io.ceph.rgw.client.core.sync;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import io.ceph.rgw.client.action.ActionExecutor;
import io.ceph.rgw.client.core.ConnectorAware;
import io.ceph.rgw.client.core.Connectors;
import io.ceph.rgw.client.exception.RGWClientException;
import io.ceph.rgw.client.exception.RGWException;
import io.ceph.rgw.client.exception.RGWServerException;
import io.ceph.rgw.client.util.IOUtil;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/6/4.
 */
public abstract class SyncConnectorAware extends ConnectorAware<AmazonS3> {
    protected SyncConnectorAware(Connectors<AmazonS3> connectors, ActionExecutor executor) {
        super(connectors, executor);
    }

    @Override
    protected RGWException handleException(AmazonS3 connector, Throwable cause) {
        if (cause instanceof AmazonServiceException) {
            if (((AmazonServiceException) cause).getErrorType() == AmazonServiceException.ErrorType.Service || IOUtil.isIOException(cause)) {
                connectors.markFailure(connector, cause);
            }
            RGWServerException exception = new RGWServerException(cause);
            exception.setStatus(((AmazonServiceException) cause).getStatusCode());
            exception.setRequestId(((AmazonServiceException) cause).getRequestId());
            exception.setHeaders(((AmazonServiceException) cause).getHttpHeaders().entrySet().stream().collect(Collectors.toList()));
            if (cause instanceof AmazonS3Exception) {
                exception.setDetail(((AmazonS3Exception) cause).getErrorResponseXml());
            }
            return exception;
        } else if (cause instanceof SdkClientException) {
            if (IOUtil.isIOException(cause)) {
                connectors.markFailure(connector, cause);
            }
            return new RGWClientException(cause);
        }
        return super.handleException(connector, cause);
    }

    <R> R doAction(Function<AmazonS3, R> function) {
        AmazonS3 connector = null;
        R response;
        try {
            connector = connectors.get();
            response = function.apply(connector);
            connectors.markSuccess(connector);
            return response;
        } catch (Throwable cause) {
            throw handleException(connector, cause);
        }
    }
}
