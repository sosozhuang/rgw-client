RGW Client
======
An [Ceph](https://ceph.io/) RADOS [Object Gateway](https://ceph.io/ceph-storage/object-storage/) client library, compatible with Amazon S3 API, supports bucket, object and admin operations.

Features:
------
* Amazon S3 v1/v2 sdk
* Support object, bucket and admin operations
* Support subscribe object event notification
* Asynchronous and listener callback
* Fluent unified APIs provided for easy assembling request
* Allow custom ActionExecutor to intercept actions and listeners
* Load balancing for multiple endpoints
* Fault tolerance
* Hide complexity of writing small or large object
* Concurrent writing
* Non blocking io and zero copy
* Reuse direct byte buffer
* Support Spring Boot
* Trace timing data of actions
* Automatically release resources

### Prerequisites
Install a Ceph storage cluster and object gateway. see Ceph Documentation for details.

### Installation
1. Clone the repository from Github.
```bash
git clone https://github.com/sosozhuang/rgw-client.git
```
2. Install maven packages.
Since execute `install` will also run tests by default, change the cluster address and s3 credentials in properties files
placed in test resources path before installing.
```bash
mvn clean install
```
Or, skip test cases with
```bash
mvn -DskipTests=true clean install
```

### Quick start
* Simple Java Application
1. Add dependency in Maven.
```xml
<dependency>
    <groupId>io.ceph</groupId>
    <artifactId>rgw-client-core-async</artifactId>
    <version>1.0.0</version>
    <type>pom</type>
</dependency>
```

2. Copy the content below into a properties file, replace endpoint, accessKey and secretKey with actual values. 
```properties
rgwclient.application.name = example
rgwclient.connector.storages[0].endpoint = 192.168.100.3:8080
rgwclient.connector.storages[0].accessKey = access_key
rgwclient.connector.storages[0].secretKey = secret_key
```
Properties file can come from jar files on your classpath.

3. Create Clients and execute an action.
```java
// load from file
RGWClientProperties properties = RGWClientProperties.loadFromFile("rgwclient.properties");
// load from class path
// RGWClientProperties properties = RGWClientProperties.loadFromResource("rgwclient.properties");
Clients clients = Clients.create(properties);
ObjectClient objectClient = clients.getObject();
ActionFuture<PutObjectResponse> future = objectClient.preparePutString()
    .withBucketName("bucket")
    .withKey("object")
    .withValue("hello world!")
    .execute();
// wait until completed
System.out.println(future.actionGet());
```

* Spring Boot application
1. Add dependency in Maven.
```xml
<dependency>
    <groupId>io.ceph</groupId>
    <artifactId>rgw-client-core-async</artifactId>
    <version>1.0.0</version>
    <type>pom</type>
</dependency>
<dependency>
    <groupId>io.ceph</groupId>
    <artifactId>rgw-client-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

2. Copy the content below into `application.properties`, replace endpoint, accessKey and secretKey with actual values. 
```properties
spring.application.name = example
rgwclient.connector.storages[0].endpoint = 192.168.100.3:8080
rgwclient.connector.storages[0].accessKey = access_key
rgwclient.connector.storages[0].secretKey = secret_key
```

3. Inject ObjectClient or other beans and execute an action.
```java
@Autowired
ObjectClient objectClient;

GetStringResponse response = objectClient.prepareGetString()
    .withBucketName("bucket")
    .withKey("object")
    .run();
System.out.println(response.getContent());
```

### configurations
Various properties can be specified inside the properties file(Spring Boot applications use `application.properties`). The full configurations are described below:

| Name                                       | Type    | Default | Required | Description                                                                                               |
| ------------------------------------------ | ------- | ------- | -------  |---------------------------------------------------------------------------------------------------------- |
| rgwclient.application.name                 | String  |         | True     | Name of application                                                                                       |
| spring.application.name                    | String  |         | True     | Name of application, only available for Spring Boot applications                                          |
| rgwclient.enableAdmin                      | Boolean | False   | True     | Whether enable admin client                                                                               |
| rgwclient.enableBucket                     | Boolean | False   | True     | Whether enable bucket client                                                                              |
| rgwclient.enableObject                     | Boolean | True    | True     | Whether enable object client                                                                              |
| rgwclient.connector                        | Object  |         |          | Properties of storage and subscribe endpoints                                                             |
| rgwclient.connector.storages               | List    |         | True     | Properties of storage endpoint list                                                                       | 
| rgwclient.connector.storages[*].endpoint   | String  |         | True     | S3 storage endpoint to connect                                                                            |
| rgwclient.connector.storages[*].region     | String  |         | True     | Region of s3 endpoint                                                                                     |
| rgwclient.connector.storages[*].protocol   | String  | HTTP    | True     | Protocol of s3 endpoint, should be 'HTTP' or 'HTTPS'                                                      |
| rgwclient.connector.storages[*].accessKey  | String  |         | True     | Access key of s3 endpoint                                                                                 |
| rgwclient.connector.storages[*].secretKey  | String  |         | True     | Access secret key of s3 endpoint                                                                          |
| rgwclient.connector.subscribes             | List    |         | False    | Properties of subscribe endpoint list                                                                     |
| rgwclient.connector.subscribes[].endpoint  | String  |         | False    | The subscribe endpoint to connect                                                                         |
| rgwclient.connector.maxConnections         | Integer | 30      | True     | Maximum number of connections to opened                                                                   |
| rgwclient.connector.connectionTimeout      | Integer | 5000    | True     | Amount of time to wait when establishing a connection before timeout, time unit is milliseconds           |
| rgwclient.connector.socketTimeout          | Integer | 10000   | True     | Amount of time to wait when reading/writing data before timeout, time unit is milliseconds                |
| rgwclient.connector.connectionMaxIdle      | Long    | 60000   | True     | Maximum amount of time an idle connection will wait for reuse before discarded, time unit is milliseconds |
| rgwclient.connector.enableGzip             | Boolean | True    | True     | Whether enable gzip                                                                                       |
| rgwclient.connector.enableKeepAlive        | Boolean | True    | True     | Whether enable keep-alive at TCP layer                                                                    |
| rgwclient.connector.enableRetry            | Boolean | False   | True     | Whether enable retry if action failed                                                                     |
| rgwclient.connector.maxRetries             | Integer | 3       | False    | Maximum number to retry failed action                                                                     |
| rgwclient.connector.baseDelayTime          | Integer | 1500    | False    | Amount of time to base delay                                                                              |
| rgwclient.connector.maxBackoffTime         | Integer | 20000   | False    | Maximum amount of time to delay when executing the retry action                                           |
| rgwclient.threadPools[action].coreSize     | Integer | Number of processors | True   | Number of action threads to keep in the pool                                                   |
| rgwclient.threadPools[action].maxSize      | Integer | 4 times of coreSize  | True   | Maximum number of action threads to allow in the pool                                          |
| rgwclient.threadPools[action].keepAlive    | Integer | 60000   | True     | Maximum time that excess idle action threads will wait for new tasks before terminating                   |
| rgwclient.threadPools[action].queueSize    | Integer | 0       | True     | Size of queue to use for holding action tasks before they are executed                                    |
| rgwclient.threadPools[listener].coreSize   | Integer | Number of processors | True   | Number of listener threads to keep in the pool                                                 |
| rgwclient.threadPools[listener].maxSize    | Integer | Number of processors | True   | Maximum number of listener threads to allow in the pool                                        |
| rgwclient.threadPools[listener].keepAlive  | Integer | 0       | True     | Maximum time that excess idle listener threads will wait for new tasks before terminating                 |
| rgwclient.threadPools[listener].queueSize  | Integer | 20      | True     | Size of queue to use for holding listener tasks before they are executed                                  |
| rgwclient.enableHyxtrix                    | Boolean | False   | False    | When enable hystrix to provide latency and fault tolerance                                                |
| rgwclient.hystrix.*                        | String  | False   | False    | Properties for hystrix command, see HystrixCommandProperties.Setter for details                           |
| rgwclient.enableTrace                      | Boolean | False   | False    | When enable zipkin to trace action, only available for Spring Boot Application                            |


### Notice
This library uses Unsafe API concurrency control.

License
-------
Code is licensed under the [Apache License 2.0](https://github.com/sosozhuang/rgw-client/blob/master/LICENSE).