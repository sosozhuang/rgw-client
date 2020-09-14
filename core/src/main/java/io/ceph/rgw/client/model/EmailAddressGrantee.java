package io.ceph.rgw.client.model;

import org.apache.commons.lang3.Validate;

/**
 * Created by zhuangshuo on 2020/7/9.
 */
public class EmailAddressGrantee implements Grantee {
    private final String email;

    public EmailAddressGrantee(String email) {
        this.email = Validate.notBlank(email);
    }

    @Override
    public String getType() {
        return "emailAddress";
    }

    @Override
    public String getId() {
        return email;
    }

    @Override
    public String toString() {
        return "EmailAddressGrantee{" +
                "email='" + email + '\'' +
                '}';
    }
}
