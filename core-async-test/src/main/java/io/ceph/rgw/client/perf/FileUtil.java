package io.ceph.rgw.client.perf;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public final class FileUtil {
    private FileUtil() {
        throw new RuntimeException();
    }

    public static File writeFile(int size) {
        File tempFile = null;
        FileOutputStream os = null;
        try {
            tempFile = File.createTempFile(RandomStringUtils.randomAlphabetic(64), null);
            os = new FileOutputStream(tempFile);
            byte[] bytes = RandomStringUtils.random(1024).getBytes();
            for (int i = 0; i < size; i++) {
                os.write(bytes);
            }
        } catch (IOException e) {
            if (tempFile != null) {
                tempFile.delete();
            }
            throw new RuntimeException(e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException ignored) {
                }
            }
        }
        return tempFile;
    }

    public static File createTempFile() {
        File tempFile;
        try {
            tempFile = File.createTempFile(RandomStringUtils.randomAlphabetic(64), null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return tempFile;
    }
}
