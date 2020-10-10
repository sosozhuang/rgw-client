package io.ceph.rgw.client.perf;

import io.ceph.rgw.client.BucketClient;
import io.ceph.rgw.client.Clients;
import io.ceph.rgw.client.ObjectClient;
import io.ceph.rgw.client.config.RGWClientProperties;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3, batchSize = 2)
@Measurement(iterations = 10, batchSize = 10)
@Fork(jvmArgsPrepend = {"-server", "-Xms1G", "-Xmx1G", "-XX:MaxDirectMemorySize=4G", "-XX:+AlwaysPreTouch", "-XX:+UseG1GC"})
public abstract class ObjectPerfTest {
    @State(Scope.Benchmark)
    public static class ClientState {
        BucketClient bucketClient;
        ObjectClient objectClient;
        String bucket, key;

        @Setup(Level.Trial)
        public void setUp() {
            RGWClientProperties properties = RGWClientProperties.loadFromResource("perf.properties");
            Clients clients = Clients.create(properties);
            bucketClient = clients.getBucket();
            objectClient = clients.getObject();
            bucket = "bucket-" + System.currentTimeMillis();
            bucketClient.prepareCreateBucket()
                    .withBucketName(bucket)
                    .run();
        }

        @TearDown(Level.Trial)
        public void tearDown() {
            bucketClient.prepareDeleteBucket()
                    .withBucketName(bucket)
                    .run();
        }

        @Setup(Level.Invocation)
        public void setUpKey() {
            key = "object-" + System.currentTimeMillis();
        }

        @TearDown(Level.Invocation)
        public void tearDownKey() {
            objectClient.prepareDeleteObject()
                    .withBucketName(bucket)
                    .withKey(key)
                    .run();
        }
    }
}
