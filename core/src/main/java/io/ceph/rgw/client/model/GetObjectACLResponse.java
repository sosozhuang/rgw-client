package io.ceph.rgw.client.model;

/**
 * Created by zhuangshuo on 2020/3/16.
 */
public class GetObjectACLResponse implements ObjectResponse {
    private final ACL acl;

    public GetObjectACLResponse(ACL acl) {
        this.acl = acl;
    }

    public ACL getACL() {
        return acl;
    }

    @Override
    public String toString() {
        return "GetObjectACLResponse{" +
                "acl=" + acl +
                '}';
    }
}
