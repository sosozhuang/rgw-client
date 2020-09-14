package io.ceph.rgw.client.util;

import io.ceph.rgw.client.exception.RGWException;
import io.netty.channel.ConnectTimeoutException;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.WriteTimeoutException;

import java.io.*;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/3/31.
 */
public final class IOUtil {
    private static final Charset UTF8 = Charset.forName("UTF-8");
    private static final List<Class<? extends Throwable>> IO_EXCEPTIONS = Arrays.asList(ConnectException.class, SocketException.class, SocketTimeoutException.class,
            ConnectTimeoutException.class, ReadTimeoutException.class, WriteTimeoutException.class, IOException.class);

    private IOUtil() {
        throw new RuntimeException();
    }

    public static String toString(InputStream is) {
        InputStreamReader reader = new InputStreamReader(is, UTF8);
        try (StringBuilderWriter writer = new StringBuilderWriter()) {
            char[] buffer = new char[4096];
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
            return writer.toString();
        } catch (IOException e) {
            throw new RGWException(e);
        } finally {
            try {
                reader.close();
            } catch (IOException ignored) {
            }
            try {
                is.close();
            } catch (IOException ignored) {
            }
        }
    }

    public static void copyToFile(InputStream is, File file) {
        ReadableByteChannel src = Channels.newChannel(is);
        try (FileChannel dest = new FileOutputStream(file).getChannel()) {
            long offset = 0;
            long count;
            while ((count = dest.transferFrom(src, offset, 4096)) != 0) {
                offset += count;
            }
        } catch (IOException e) {
            throw new RGWException(e);
        } finally {
            try {
                src.close();
            } catch (IOException ignored) {
            }
        }
    }

    public static boolean isIOException(Throwable cause) {
        return isException(IO_EXCEPTIONS, cause);
    }

    public static boolean isException(List<Class<? extends Throwable>> exceptions, Throwable cause) {
        if (exceptions.contains(cause.getClass())) {
            return true;
        }
        cause = cause.getCause();
        if (cause == null) {
            return false;
        }
        boolean found = exceptions.contains(cause.getClass());
        while (!found && cause != null && cause.getCause() != cause) {
            found = exceptions.contains(cause.getClass());
            cause = cause.getCause();
        }
        return found;
    }
}
