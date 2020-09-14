package io.ceph.rgw.client.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.regex.Pattern;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/2/27.
 */
public final class AddressUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddressUtil.class);
    private static final Pattern IPV4_PATTERN = Pattern.compile("[0-9]{1,3}(\\.[0-9]{1,3}){3,}");

    private AddressUtil() {
        throw new RuntimeException();
    }

    public static String getHostAddress() {
        InetAddress address = getHostInetAddress();
        return address == null ? "127.0.0.1" : address.getHostAddress();
    }

    public static String getHostName() {
        InetAddress address = getHostInetAddress();
        return address == null ? "localhost" : address.getHostName();
    }

    public static InetAddress getHostInetAddress() {
        InetAddress addr = null;
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            if (interfaces != null) {
                while (interfaces.hasMoreElements()) {
                    NetworkInterface nic = interfaces.nextElement();
                    Enumeration<InetAddress> addrs = nic.getInetAddresses();
                    while (addrs.hasMoreElements()) {
                        addr = addrs.nextElement();
                        if (addr != null && !addr.isLoopbackAddress() && IPV4_PATTERN.matcher(addr.getHostAddress()).matches()
                                && !"127.0.0.1".equals(addr.getHostAddress()) && !"0.0.0.0".equals(addr.getHostAddress())) {
                            return addr;
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("Failed to get network interfaces.", e);
            try {
                addr = InetAddress.getLocalHost();
            } catch (UnknownHostException inner) {
                inner.addSuppressed(e);
                LOGGER.error("Failed to get localhost address.", inner);
            }
        }
        return addr;
    }

    public static int getPort(URI uri) {
        int port = uri.getPort();
        if (port == -1) {
            if ("http".equalsIgnoreCase(uri.getScheme())) {
                port = 80;
            } else if ("https".equalsIgnoreCase(uri.getScheme())) {
                port = 443;
            }
        }
        return port;
    }
}
