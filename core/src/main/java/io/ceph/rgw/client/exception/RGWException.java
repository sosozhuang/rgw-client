package io.ceph.rgw.client.exception;

/**
 * Base Exception of this library.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/2/27.
 * @see RGWClientException
 * @see RGWServerException
 */
public class RGWException extends RuntimeException {
    private static final long serialVersionUID = -4493080131364534854L;

    public RGWException(String message) {
        super(message);
    }

    public RGWException(String message, Throwable cause) {
        super(message, cause);
    }

    public RGWException(Throwable cause) {
        super(cause);
    }
}
