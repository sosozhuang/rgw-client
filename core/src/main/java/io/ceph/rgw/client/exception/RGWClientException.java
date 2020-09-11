package io.ceph.rgw.client.exception;

/**
 * Represents that exception is thrown by client.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/2/27.
 */
public class RGWClientException extends RGWException {
    private static final long serialVersionUID = -7357211848682805987L;

    public RGWClientException(String message) {
        super(message);
    }

    public RGWClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public RGWClientException(Throwable cause) {
        super(cause);
    }
}
