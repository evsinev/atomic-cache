package com.payneteasy.atomiccache.cassandra;

import com.datastax.oss.driver.api.core.CqlSession;
import com.payneteasy.atomiccache.api.IAtomicCache;
import com.payneteasy.atomiccache.api.serializer.gson.AtomicCacheGsonSerializer;
import com.payneteasy.atomiccache.example.model.ExampleKey;
import com.payneteasy.atomiccache.example.model.ExampleValue;

import java.net.InetSocketAddress;
import java.util.List;

public class CassandraInstance {

    public static final IAtomicCache<ExampleKey, ExampleValue> CASSANDRA_CACHE;

    static {
        CqlSession session = CqlSession.builder()
                .addContactPoints(List.of(InetSocketAddress.createUnresolved("127.0.0.1", 9042)))
                .withLocalDatacenter("datacenter1")
                .withAuthCredentials("cassandra", "cassandra")
                .withKeyspace("atomic_cache_test")
                .build();

        CASSANDRA_CACHE = new AtomicCacheCassandraBuilder<ExampleKey, ExampleValue>()
                .setSerializer(new AtomicCacheGsonSerializer<>(ExampleValue.class))
                .setSession(session)
                .setTableName("atomic_cache")
                .build();

        session.execute("truncate table atomic_cache");

        Runtime.getRuntime().addShutdownHook(new Thread(session::close));
    }
}
