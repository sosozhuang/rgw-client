package io.ceph.rgw.client.model;

import java.util.Date;

/**
 * Created by zhuangshuo on 2020/3/23.
 */
public class Bucket {
    private final String name;
    private final Owner owner;
    private final Date creationDate;

    public Bucket(String name, Owner owner, Date creationDate) {
        this.name = name;
        this.owner = owner;
        this.creationDate = creationDate;
    }

    public String getName() {
        return name;
    }

    public Owner getOwner() {
        return owner;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    @Override
    public String toString() {
        return "Bucket{" +
                "name='" + name + '\'' +
                ", owner=" + owner +
                ", creationDate=" + creationDate +
                '}';
    }
}
