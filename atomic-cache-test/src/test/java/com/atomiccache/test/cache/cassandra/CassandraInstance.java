package com.atomiccache.test.cache.cassandra;

import com.atomiccache.api.lock.IAtomicCacheLock;
import com.atomiccache.cassandra.AtomicCacheCassandraBuilder;
import com.atomiccache.cassandra.AtomicCacheLockCassandraBuilder;
import com.datastax.oss.driver.api.core.CqlSession;
import com.atomiccache.api.cache.IAtomicCache;
import com.atomiccache.api.serializer.gson.AtomicCacheGsonSerializer;
import com.atomiccache.test.cache.example.model.ExampleKey;
import com.atomiccache.test.cache.example.model.ExampleValue;

import java.net.InetSocketAddress;
import java.util.List;

public class CassandraInstance {

    public static final IAtomicCache<ExampleKey, ExampleValue> CASSANDRA_CACHE;
    public static final IAtomicCacheLock<ExampleKey>           CASSANDRA_CACHE_LOCK;

    static {
        CqlSession session = CqlSession.builder()
                .addContactPoints(List.of(InetSocketAddress.createUnresolved("127.0.0.1", 9042)))
                .withLocalDatacenter("datacenter1")
                .withAuthCredentials("cassandra", "cassandra")
                .withKeyspace("atomic_cache_test")
                .build();

        CASSANDRA_CACHE = new AtomicCacheCassandraBuilder<ExampleKey, ExampleValue>()
                .serializer(new AtomicCacheGsonSerializer<>(ExampleValue.class))
                .session(session)
                .tableName("atomic_cache")
                .build();

        CASSANDRA_CACHE_LOCK = new AtomicCacheLockCassandraBuilder<ExampleKey>()
                .session(session)
                .tableName("atomic_cache_lock")
                .keySerializer(new AtomicCacheGsonSerializer<>(ExampleKey.class))
                .build();

        session.execute("truncate table atomic_cache");
        session.execute("truncate table atomic_cache_lock");

        Runtime.getRuntime().addShutdownHook(new Thread(session::close));
    }
}
