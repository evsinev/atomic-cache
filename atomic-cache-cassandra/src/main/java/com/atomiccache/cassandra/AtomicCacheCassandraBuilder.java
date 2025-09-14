package com.atomiccache.cassandra;

import com.atomiccache.api.serializer.*;
import com.datastax.oss.driver.api.core.CqlSession;
import com.atomiccache.api.cache.IAtomicCache;
import com.atomiccache.cassandra.impl.cache.AtomicCacheCassandraImpl;
import com.atomiccache.cassandra.impl.cache.dao.CassandraDaoImpl;

import java.time.Duration;
import java.util.Objects;

public class AtomicCacheCassandraBuilder<K, V> {

    private IAtomicCacheKeySerializer<K>     keySerializer;
    private IAtomicCacheValueSerializer<V>   valueSerializer;
    private IAtomicCacheValueDeserializer<V> valueDeserializer;
    private String                           tableName;
    private CqlSession                       session;
    private Duration                         rowTtl              = Duration.ofHours(2);
    private Duration                         maxLockWait         = Duration.ofSeconds(30);
    private Duration                         maxLockFallbackTime = Duration.ofSeconds(45);
    private Duration                         sleepBetweenChecks  = Duration.ofMillis(250);

    public AtomicCacheCassandraBuilder<K, V> serializer(IAtomicCacheSerializer<K, V> serializer) {
        keySerializer = serializer;
        valueSerializer = serializer;
        valueDeserializer = serializer;
        return this;
    }

    public AtomicCacheCassandraBuilder<K, V> keySerializer(IAtomicCacheKeySerializer<K> keySerializer) {
        this.keySerializer = keySerializer;
        return this;
    }

    public AtomicCacheCassandraBuilder<K, V> valueSerializer(IAtomicCacheValueSerializer<V> valueSerializer) {
        this.valueSerializer = valueSerializer;
        return this;
    }

    public AtomicCacheCassandraBuilder<K, V> valueDeserializer(IAtomicCacheValueDeserializer<V> valueDeserializer) {
        this.valueDeserializer = valueDeserializer;
        return this;
    }

    public AtomicCacheCassandraBuilder<K, V> tableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public AtomicCacheCassandraBuilder<K, V> session(CqlSession session) {
        this.session = session;
        return this;
    }

    public AtomicCacheCassandraBuilder<K, V> rowTtl(Duration rowTtl) {
        this.rowTtl = rowTtl;
        return this;
    }

    public AtomicCacheCassandraBuilder<K, V> maxLockWait(Duration maxLockWait) {
        this.maxLockWait = maxLockWait;
        return this;
    }

    public AtomicCacheCassandraBuilder<K, V> maxLockFallbackTime(Duration maxLockFallbackTime) {
        this.maxLockFallbackTime = maxLockFallbackTime;
        return this;
    }

    public AtomicCacheCassandraBuilder<K, V> sleepBetweenChecks(Duration sleepBetweenChecks) {
        this.sleepBetweenChecks = sleepBetweenChecks;
        return this;
    }

    public IAtomicCache<K, V> build() {
        Objects.requireNonNull(session   , "No session");
        Objects.requireNonNull(tableName , "No table name");

        return new AtomicCacheCassandraImpl<>(
                  new AtomicCacheSerializerContainer<>(keySerializer, valueDeserializer, valueSerializer)
                , new CassandraDaoImpl(session, tableName, rowTtl)
                , maxLockWait
                , maxLockFallbackTime
                , sleepBetweenChecks
        );
    }

}
