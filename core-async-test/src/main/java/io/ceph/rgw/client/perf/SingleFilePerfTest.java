package io.ceph.rgw.client.perf;

import io.ceph.rgw.client.ObjectClient;
import io.ceph.rgw.client.core.io.ObjectWriter;
import io.ceph.rgw.client.model.*;
import org.openjdk.jmh.annotations.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static io.ceph.rgw.client.perf.FileUtil.createTempFile;
import static io.ceph.rgw.client.perf.FileUtil.writeFile;

public class SingleFilePerfTest extends ObjectPerfTest {
    @State(Scope.Benchmark)
    public static abstract class AbstractFileState {
        abstract int getSize();

        File file;

        @Setup(Level.Trial)
        public void setUp() {
            file = writeFile(getSize());
        }

        @TearDown(Level.Trial)
        public void tearDown() {
            if (file != null) {
                file.delete();
            }
        }
    }

    public static class FileState extends AbstractFileState {
        @Param({"1", "4", "8", "16", "1024", "2048", "4096", "8192", "16384", "32768", "65536", "131072", "262144", "524288", "1048576"})
        int size;

        @Override
        int getSize() {
            return size;
        }
    }

    public static class LargeFileState extends AbstractFileState {
        @Param({"16384", "32768", "65536", "131072", "262144", "524288", "1048576"})
        int size;

        @Override
        int getSize() {
            return size;
        }
    }

    @Benchmark
    public PutObjectResponse testPutFile(ClientState clientState, FileState fileState) {
        return clientState.objectClient.preparePutFile()
                .withBucketName(clientState.bucket)
                .withKey(clientState.key)
                .withValue(fileState.file)
                .run();
    }

    @Benchmark
    public PutObjectResponse testPutInputStream(ClientState clientState, FileState fileState) throws FileNotFoundException {
        return clientState.objectClient.preparePutInputStream()
                .withBucketName(clientState.bucket)
                .withKey(clientState.key)
                .withValue(new FileInputStream(fileState.file))
                .run();
    }

    @Benchmark
    public CompleteMultipartUploadResponse testMultipartUpload(ClientState clientState, LargeFileState fileState) {
        ObjectClient objectClient = clientState.objectClient;
        InitiateMultipartUploadResponse initiateResponse = objectClient.prepareInitiateMultipartUpload()
                .withBucketName(clientState.bucket)
                .withKey(clientState.key)
                .run();
        MultipartUploadPartResponse uploadPartResponse = objectClient.prepareUploadFile()
                .withBucketName(clientState.bucket)
                .withKey(clientState.key)
                .withUpload(fileState.file)
                .withUploadId(initiateResponse.getUploadId())
                .withPartNumber(1)
                .withPartSize(fileState.file.length())
                .withLastPart(true)
                .run();
        return objectClient.prepareCompleteMultipartUpload()
                .withBucketName(clientState.bucket)
                .withKey(clientState.key)
                .withUploadId(initiateResponse.getUploadId())
                .addPartETag(1, uploadPartResponse.getETag())
                .run();

    }

    @Benchmark
    public BasePutObjectResponse testFileWriter(ClientState clientState, FileState fileState) {
        ObjectWriter writer = new ObjectWriter.Builder(clientState.objectClient)
                .withFile(createTempFile().getAbsolutePath())
                .withBucketName(clientState.bucket)
                .withKey(clientState.key)
                .build();
        writer.write(fileState.file);
        return writer.complete();
    }

    @Benchmark
    public BasePutObjectResponse testByteBufSingleWriter(ClientState clientState, FileState fileState) {
        ObjectWriter writer = new ObjectWriter.Builder(clientState.objectClient)
                .withBuffer()
                .withSingleWriteThread()
                .withBucketName(clientState.bucket)
                .withKey(clientState.key)
                .build();
        writer.write(fileState.file);
        return writer.complete();
    }

    @Benchmark
    public BasePutObjectResponse testByteBufMultiWriter(ClientState clientState, FileState fileState) {
        ObjectWriter writer = new ObjectWriter.Builder(clientState.objectClient)
                .withBuffer()
                .withMultiWriteThread()
                .withBucketName(clientState.bucket)
                .withKey(clientState.key)
                .build();
        writer.write(fileState.file);
        return writer.complete();
    }
}
