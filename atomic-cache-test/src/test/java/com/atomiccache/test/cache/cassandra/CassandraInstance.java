package com.atomiccache.test.cache.cassandra;

import com.atomiccache.api.cache.IAtomicCache;
import com.atomiccache.api.lock.IAtomicCacheLock;
import com.atomiccache.api.map.IAtomicCacheMap;
import com.atomiccache.cassandra.AtomicCacheCassandraBuilder;
import com.atomiccache.cassandra.AtomicCacheLockCassandraBuilder;
import com.atomiccache.cassandra.AtomicCacheMapCassandraBuilder;
import com.atomiccache.test.cache.example.ExampleCacheSerializer;
import com.atomiccache.test.cache.example.model.ExampleKey;
import com.atomiccache.test.cache.example.model.ExampleValue;
import com.datastax.oss.driver.api.core.CqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.List;

public class CassandraInstance {

    private static final Logger LOG = LoggerFactory.getLogger( CassandraInstance.class );

    public static final IAtomicCache<ExampleKey, ExampleValue>    CASSANDRA_CACHE;
    public static final IAtomicCacheMap<ExampleKey, ExampleValue> CASSANDRA_CACHE_MAP;
    public static final IAtomicCacheLock<ExampleKey>              CASSANDRA_CACHE_LOCK;

    static {
        CqlSession session = CqlSession.builder()
                .addContactPoints(List.of(InetSocketAddress.createUnresolved("127.0.0.1", 9042)))
                .withLocalDatacenter("datacenter1")
                .withAuthCredentials("cassandra", "cassandra")
                .withKeyspace("atomic_cache_test")
                .build();

        CASSANDRA_CACHE = new AtomicCacheCassandraBuilder<ExampleKey, ExampleValue>()
                .serializer(new ExampleCacheSerializer())
                .session(session)
                .tableName("atomic_cache")
                .build();

        CASSANDRA_CACHE_LOCK = new AtomicCacheLockCassandraBuilder<ExampleKey>()
                .session(session)
                .tableName("atomic_cache_lock")
                .keySerializer(new ExampleCacheSerializer())
                .sleepBetweenChecks(Duration.ofMillis(500))
                .build();

        CASSANDRA_CACHE_MAP = new AtomicCacheMapCassandraBuilder<ExampleKey, ExampleValue>()
                .serializer(new ExampleCacheSerializer())
                .session(session)
                .tableName("atomic_cache_map")
                .build();

        session.execute("truncate table atomic_cache");
        session.execute("truncate table atomic_cache_lock");
        session.execute("truncate table atomic_cache_map");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOG.debug("Shutting down Cassandra session");
            session.close();
        }));
    }
}
