package io.ceph.rgw.client.action;

import io.ceph.rgw.client.exception.RGWException;
import io.ceph.rgw.client.model.Response;
import io.ceph.rgw.client.util.StringBuilderWriter;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * An ActionListener that writes response(with file suffix 'ok') or exception(with file suffix 'failed') message in file under specified path.
 *
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/7/8.
 */
public class ResponseFileActionListener<R extends Response> implements ActionListener<R> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseFileActionListener.class);
    private final String path;
    private final boolean overwrite;

    /**
     * @param path file path contains file name
     */
    public ResponseFileActionListener(String path) {
        this(path, false);
    }

    /**
     * @param path      file path contains file name
     * @param overwrite overwrite existing file
     */
    public ResponseFileActionListener(String path, boolean overwrite) {
        this.path = Validate.notBlank(path);
        this.overwrite = overwrite;
    }

    @Override
    public void onSuccess(R response) {
        write(Paths.get(path + ".ok"), ByteBuffer.wrap(response.toString().getBytes()));
    }

    @Override
    public void onFailure(Throwable cause) {
        StringBuilderWriter writer = new StringBuilderWriter();
        cause.printStackTrace(new PrintWriter(writer));
        write(Paths.get(path + ".failed"), ByteBuffer.wrap(writer.toString().getBytes()));
    }

    private void write(Path path, ByteBuffer buffer) {
        FileChannel channel = null;
        try {
            channel = FileChannel.open(path, StandardOpenOption.WRITE, overwrite ? StandardOpenOption.TRUNCATE_EXISTING : StandardOpenOption.CREATE_NEW);
            channel.write(buffer);
        } catch (IOException e) {
            throw new RGWException(e);
        } finally {
            if (channel != null) {
                try {
                    channel.close();
                } catch (IOException e) {
                    LOGGER.error("Failed to close channel.", e);
                }
            }
        }
    }
}
