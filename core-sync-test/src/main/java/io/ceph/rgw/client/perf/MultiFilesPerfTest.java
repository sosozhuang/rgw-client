package io.ceph.rgw.client.perf;

import io.ceph.rgw.client.ObjectClient;
import io.ceph.rgw.client.core.io.ObjectWriter;
import io.ceph.rgw.client.model.*;
import org.openjdk.jmh.annotations.*;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static io.ceph.rgw.client.perf.FileUtil.createTempFile;
import static io.ceph.rgw.client.perf.FileUtil.writeFile;

public class MultiFilesPerfTest extends ObjectPerfTest {
    @State(Scope.Benchmark)
    public static class WriterState extends ClientState {
        ObjectWriter fileObjectWriter;
        ObjectWriter byteBufMultiObjectWriter;
        ObjectWriter byteBufSingleObjectWriter;

        @Setup(Level.Invocation)
        public void setUpKey() {
            super.setUpKey();
            fileObjectWriter = new ObjectWriter.Builder(objectClient)
                    .withFile(createTempFile().getAbsolutePath())
                    .withBucketName(bucket)
                    .withKey(key)
                    .build();
            byteBufMultiObjectWriter = new ObjectWriter.Builder(objectClient)
                    .withBuffer()
                    .withMultiWriteThread()
                    .withBucketName(bucket)
                    .withKey(key)
                    .build();
            byteBufSingleObjectWriter = new ObjectWriter.Builder(objectClient)
                    .withBuffer()
                    .withSingleWriteThread()
                    .withBucketName(bucket)
                    .withKey(key)
                    .build();
        }
    }

    @State(Scope.Benchmark)
    public static abstract class AbstractFileState {
        @Param({"4", "8", "16", "32"})
        int num;
        BlockingQueue<File> files;

        abstract int getSize();

        @Setup(Level.Trial)
        public void setUp() {
            files = new LinkedBlockingQueue<>(num);
            for (int i = 0; i < num; i++) {
                files.add(writeFile(getSize()));
            }
        }

        @TearDown(Level.Trial)
        public void tearDown() {
            if (files != null && files.size() > 0) {
                files.forEach(File::delete);
            }
        }
    }

    public static class FileState extends AbstractFileState {
        @Param({"2048", "4096", "8192", "16384", "32768", "65536", "131072", "262144", "524288", "1048576"})
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
    public CompleteMultipartUploadResponse testMultipartUpload(ClientState clientState, LargeFileState fileState) {
        ObjectClient objectClient = clientState.objectClient;
        InitiateMultipartUploadResponse initiateResponse = objectClient.prepareInitiateMultipartUpload()
                .withBucketName(clientState.bucket)
                .withKey(clientState.key)
                .run();
        List<PartETag> eTags = new LinkedList<>();
        for (int i = 0; i < fileState.num; i++) {
            File file = fileState.files.poll();
            MultipartUploadPartResponse uploadPartResponse = objectClient.prepareUploadFile()
                    .withBucketName(clientState.bucket)
                    .withKey(clientState.key)
                    .withUpload(file)
                    .withUploadId(initiateResponse.getUploadId())
                    .withPartNumber(i)
                    .withPartSize(file.length())
                    .withLastPart(true)
                    .run();
            eTags.add(new PartETag(i, uploadPartResponse.getETag()));
        }
        return objectClient.prepareCompleteMultipartUpload()
                .withBucketName(clientState.bucket)
                .withKey(clientState.key)
                .withUploadId(initiateResponse.getUploadId())
                .withPartETags(eTags)
                .run();
    }

    @Benchmark
    public BasePutObjectResponse testFileWriterSingleThread(WriterState writerState, FileState fileState) {
        ObjectWriter writer = writerState.fileObjectWriter;
        while (!fileState.files.isEmpty()) {
            writer.write(fileState.files.poll());
        }
        return writer.complete();
    }

    @Benchmark
    @Group("smallFileWriter")
    @GroupThreads(4)
    public void testFileWriterMultiThread(WriterState writerState, FileState fileState) {
        ObjectWriter writer = writerState.fileObjectWriter;
        File file = fileState.files.poll();
        if (file != null) {
            writer.write(file);
        }
    }

    @Benchmark
    @Group("smallFileWriter")
    @GroupThreads(1)
    public BasePutObjectResponse testFileWriterComplete(WriterState writerState, FileState fileState) {
        while (fileState.files.isEmpty()) {
        }
        return writerState.fileObjectWriter.complete();
    }

    @Benchmark
    public BasePutObjectResponse testByteBufSingleWriter(WriterState writerState, FileState fileState) {
        ObjectWriter writer = writerState.byteBufSingleObjectWriter;
        while (!fileState.files.isEmpty()) {
            writer.write(fileState.files.poll());
        }
        return writer.complete();
    }

    @Benchmark
    public BasePutObjectResponse testByteBufMultiWriterSingleThread(WriterState writerState, FileState fileState) {
        ObjectWriter writer = writerState.byteBufMultiObjectWriter;
        while (!fileState.files.isEmpty()) {
            writer.write(fileState.files.poll());
        }
        return writer.complete();
    }

    @Benchmark
    @Group("byteBufMultiWriter")
    @GroupThreads(4)
    public void testByteBufMultiWriterMultiThread(WriterState writerState, FileState fileState) {
        ObjectWriter writer = writerState.byteBufMultiObjectWriter;
        File file = fileState.files.poll();
        if (file != null) {
            writer.write(file);
        }
    }

    @Benchmark
    @Group("byteBufMultiWriter")
    @GroupThreads(1)
    public BasePutObjectResponse testByteBufMultiWriterComplete(WriterState writerState, FileState fileState) {
        while (fileState.files.isEmpty()) {
        }
        return writerState.byteBufMultiObjectWriter.complete();
    }
}
