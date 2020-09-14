package io.ceph.rgw.client.model.notification;

import java.util.Date;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/5/25.
 */
public class ObjectAttrs {
    private final Date mtime;

    public ObjectAttrs(Date mtime) {
        this.mtime = mtime;
    }

    public Date getMtime() {
        return mtime;
    }

    @Override
    public String toString() {
        return "ObjectAttrs{" +
                "mtime=" + mtime +
                '}';
    }
}
