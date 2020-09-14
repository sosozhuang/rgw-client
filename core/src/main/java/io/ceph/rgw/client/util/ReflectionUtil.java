package io.ceph.rgw.client.util;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;

/**
 * @author zhuangshuo
 * Created by zhuangshuo on 2020/6/15.
 */
public final class ReflectionUtil {
    private static volatile Unsafe unsafe;

    private ReflectionUtil() {
        throw new RuntimeException();
    }

    public static Unsafe getUnsafe() {
        if (unsafe == null) {
            synchronized (ReflectionUtil.class) {
                if (unsafe == null) {
                    try {
                        final PrivilegedExceptionAction<Unsafe> action = () -> {
                            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
                            theUnsafe.setAccessible(true);
                            return (Unsafe) theUnsafe.get(null);
                        };
                        unsafe = AccessController.doPrivileged(action);
                    } catch (Exception e) {
                        throw new Error(e);
                    }
                }
            }
        }
        return unsafe;
    }
}
