package io.ceph.rgw.client.action;

/**
 * A listener for Response or exception.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/3/2.
 */
public interface ActionListener<R> {
    /**
     * handle action success.
     *
     * @param response action response
     */
    void onSuccess(R response);

    /**
     * handle action failure caused by throwable.
     *
     * @param cause failed exception
     */
    void onFailure(Throwable cause);
}
