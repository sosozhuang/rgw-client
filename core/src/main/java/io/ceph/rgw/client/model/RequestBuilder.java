package io.ceph.rgw.client.model;

import io.ceph.rgw.client.action.ActionFuture;
import io.ceph.rgw.client.action.ActionListener;

/**
 * Interface to create a Request and execute the Action.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/3/2.
 */
public interface RequestBuilder<REQ extends Request, RESP extends Response> extends Builder<REQ> {
    /**
     * Runs the Action and returns a Response.
     *
     * @return response
     */
    RESP run();

    /**
     * Execute the Action and returns a ActionFuture.
     *
     * @return a ActionFuture representing pending completion of the Action
     */
    ActionFuture<RESP> execute();

    /**
     * Execute the action.
     *
     * @param listener the listener for action response or exception
     */
    void execute(ActionListener<RESP> listener);
}
