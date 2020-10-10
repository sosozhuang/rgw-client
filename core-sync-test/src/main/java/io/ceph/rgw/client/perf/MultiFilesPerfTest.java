package io.ceph.rgw.client.perf;

import io.ceph.rgw.client.ObjectClient;
import io.ceph.rgw.client.core.io.ObjectWriter;
import io.ceph.rgw.client.model.*;
import org.openjdk.jmh.annotations.*;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.Collectors;

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
            fileObjectWriter = ObjectWriter.builder(objectClient)
                    .withFile(createTempFile().getAbsolutePath())
                    .withBucketName(bucket)
                    .withKey(key)
                    .build();
            byteBufMultiObjectWriter = ObjectWriter.builder(objectClient)
                    .withBuffer()
                    .withMultiWriteThread()
                    .withBucketName(bucket)
                    .withKey(key)
                    .build();
            byteBufSingleObjectWriter = ObjectWriter.builder(objectClient)
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
        List<File> fileList;

        abstract int getSize();

        @Setup(Level.Trial)
        public void setUp() {
            fileList = new LinkedList<>();
            for (int i = 0; i < num; i++) {
                fileList.add(writeFile(getSize()));
            }
        }

        @TearDown(Level.Trial)
        public void tearDown() {
            if (fileList != null && fileList.size() > 0) {
                fileList.forEach(File::delete);
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
    public CompleteMultipartUploadResponse multipartUpload(ClientState clientState, LargeFileState fileState) {
        ObjectClient objectClient = clientState.objectClient;
        InitiateMultipartUploadResponse initiateResponse = objectClient.prepareInitiateMultipartUpload()
                .withBucketName(clientState.bucket)
                .withKey(clientState.key)
                .run();
        List<PartETag> eTags = new LinkedList<>();
        for (int i = 1; i <= fileState.num; i++) {
            File file = fileState.fileList.get(i + 1);
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
    public BasePutObjectResponse fileWriterSingleThread(WriterState writerState, FileState fileState) {
        ObjectWriter writer = writerState.fileObjectWriter;
        fileState.fileList.forEach(writer::write);
        return writer.complete();
    }

    @Benchmark
    public BasePutObjectResponse fileWriterMultiThread(WriterState writerState, FileState fileState) throws ExecutionException, InterruptedException {
        ObjectWriter writer = writerState.fileObjectWriter;
        List<ForkJoinTask<?>> tasks = fileState.fileList.stream()
                .map(file -> ForkJoinPool.commonPool().submit(() -> writer.write(file)))
                .collect(Collectors.toList());
        for (ForkJoinTask<?> task : tasks) {
            task.get();
        }
        return writer.complete();
    }

    @Benchmark
    public BasePutObjectResponse byteBufSingleWriter(WriterState writerState, FileState fileState) {
        ObjectWriter writer = writerState.byteBufSingleObjectWriter;
        fileState.fileList.forEach(writer::write);
        return writer.complete();
    }

    @Benchmark
    public BasePutObjectResponse byteBufMultiWriterSingleThread(WriterState writerState, FileState fileState) {
        ObjectWriter writer = writerState.byteBufMultiObjectWriter;
        fileState.fileList.forEach(writer::write);
        return writer.complete();
    }

    @Benchmark
    public BasePutObjectResponse byteBufMultiWriterMultiThread(WriterState writerState, FileState fileState) throws ExecutionException, InterruptedException {
        ObjectWriter writer = writerState.byteBufMultiObjectWriter;
        List<ForkJoinTask<?>> tasks = fileState.fileList.stream()
                .map(file -> ForkJoinPool.commonPool().submit(() -> writer.write(file)))
                .collect(Collectors.toList());
        for (ForkJoinTask<?> task : tasks) {
            task.get();
        }
        return writer.complete();
    }
}
