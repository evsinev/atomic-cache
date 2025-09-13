# Atomic Cache

Atomic Cache is a Java library that provides thread-safe caching with Cassandra as a backend storage option. 

It helps manage concurrent access to cached data with atomic operations.

## Overview

The library is designed to solve the problem of cache stampede (or thundering herd) 
when multiple threads or processes try to refresh the same cached item simultaneously. 

Atomic Cache ensures that only one thread performs the expensive fetch operation 
while others wait for the result.

## Features

- Thread-safe caching mechanism
- Atomic operations for cache access and updates
- Cassandra integration for distributed environments
- Customizable serialization using your preferred serializer
- Simple builder pattern for configuration

## Modules

The project consists of several modules:
- `atomic-cache-api`: Core interfaces for the cache service
- `atomic-cache-cassandra`: Cassandra implementation of the cache service
- `atomic-cache-test`: Test utilities and examples

## Usage

### Maven Dependency

Add the following dependencies to your project:

```xml
<dependency>
    <groupId>com.payneteasy.atomic-cache</groupId>
    <artifactId>atomic-cache-api</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
<dependency>
    <groupId>com.payneteasy.atomic-cache</groupId>
    <artifactId>atomic-cache-cassandra</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### Creating a Cassandra Cache

```java
// Create a CqlSession
CqlSession session = CqlSession.builder()
        .addContactPoints(List.of(InetSocketAddress.createUnresolved("127.0.0.1", 9042)))
        .withLocalDatacenter("datacenter1")
        .withAuthCredentials("username", "password")
        .withKeyspace("your_keyspace")
        .build();

// Create the cache service
IAtomicCacheService<YourKeyType, YourValueType> cacheService = new AtomicCacheCassandraBuilder<YourKeyType, YourValueType>()
        .setSerializer(new AtomicCacheGsonSerializer<>(YourValueType.class)) // Or your custom serializer
        .setSession(session)
        .setTableName("your_cache_table")
        .build();
```

### Using the Cache

```java
// Define a key
YourKeyType key = new YourKeyType();

// Define a function to fetch the value if not in cache
IFetchFunction<YourKeyType, YourValueType> fetchFunction = theKey -> {
    // Perform expensive operation to get the value
    return expensiveOperation(theKey);
};

// Get value from cache, or fetch if not present
YourValueType value = cacheService.getOrFetch(key, fetchFunction);
```

## Requirements

- Java 21+
- Cassandra database (for Cassandra implementation)
- Lombok

## Running tests

### Creating cassandra keyspace
                       
#### Single node

```cassandraql
CREATE KEYSPACE atomic_cache_test
    WITH REPLICATION = {
        'class' : 'SimpleStrategy',
        'replication_factor' : 1
        };
```

#### Multiple nodes

```cassandraql
CREATE KEYSPACE atomic_cache_test 
    WITH replication = {'class': 'NetworkTopologyStrategy', 'datacenter1': '3'}  
         AND durable_writes = true;
```

### Creating table

```cassandraql
CREATE TABLE atomic_cache
(
    key                 text,
    state               text,
    value               text,
    started_fetching_at timestamp,
    error_message       text,
    fetch_id            uuid,
    PRIMARY KEY (key)
) with default_time_to_live = 7200;
```
