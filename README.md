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

License
-------

Code is licensed under the [Apache License 2.0](https://github.com/sosozhuang/rgw-client/blob/master/LICENSE).