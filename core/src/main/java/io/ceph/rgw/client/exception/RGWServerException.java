package io.ceph.rgw.client.exception;

import java.util.List;
import java.util.Map;

/**
 * Represents that exception is returned from remote server.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/2/27.
 */
public class RGWServerException extends RGWException {
    private static final long serialVersionUID = 7259138097550497632L;
    private int status;
    private String requestId;
    private List<Map.Entry<String, String>> headers;
    private String detail;

    public RGWServerException(String message) {
        super(message);
    }

    public RGWServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public RGWServerException(Throwable cause) {
        super(cause);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public List<Map.Entry<String, String>> getHeaders() {
        return headers;
    }

    public void setHeaders(List<Map.Entry<String, String>> headers) {
        this.headers = headers;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "RGWServerException{" +
                "status=" + status +
                ", requestId='" + requestId + '\'' +
                ", headers=" + headers +
                ", detail='" + detail + '\'' +
                "} " + super.toString();
    }
}
