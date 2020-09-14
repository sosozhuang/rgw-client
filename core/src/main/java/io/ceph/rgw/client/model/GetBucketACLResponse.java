package io.ceph.rgw.client.model;

/**
 * Created by zhuangshuo on 2020/3/17.
 */
public class GetBucketACLResponse implements BucketResponse {
    private final ACL acl;

    public GetBucketACLResponse(ACL acl) {
        this.acl = acl;
    }

    public ACL getACL() {
        return acl;
    }

    @Override
    public String toString() {
        return "GetBucketACLResponse{" +
                "acl=" + acl +
                '}';
    }
}
