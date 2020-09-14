package io.ceph.rgw.client.model;

import io.ceph.rgw.client.util.IOUtil;
import org.apache.commons.lang3.Validate;

import java.io.File;
import java.io.InputStream;

/**
 * Created by zhuangshuo on 2020/3/13.
 */
public class GetObjectResponse extends BaseGetObjectResponse {
    public GetObjectResponse(String versionId, Integer taggingCount, Metadata metadata, InputStream content) {
        super(versionId, taggingCount, metadata, content);
    }

    public File getAsFile(String name) {
        File file = new File(Validate.notBlank(name));
        IOUtil.copyToFile(doGetContent(), file);
        return file;
    }

    public InputStream getAsInputStream() {
        return doGetContent();
    }

    public String getAsString() {
        return IOUtil.toString(doGetContent());
    }

    @Override
    public String toString() {
        return "GetObjectResponse{} " + super.toString();
    }
}
