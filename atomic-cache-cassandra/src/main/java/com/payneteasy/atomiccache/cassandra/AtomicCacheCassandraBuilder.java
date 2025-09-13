package com.payneteasy.atomiccache.cassandra;

import com.datastax.oss.driver.api.core.CqlSession;
import com.payneteasy.atomiccache.api.IAtomicCache;
import com.payneteasy.atomiccache.api.serializer.IAtomicCacheSerializer;
import com.payneteasy.atomiccache.cassandra.impl.AtomicCacheCassandraImpl;
import com.payneteasy.atomiccache.cassandra.impl.dao.CassandraDaoImpl;

import java.time.Duration;
import java.util.Objects;

public class AtomicCacheCassandraBuilder<K, V> {

    private IAtomicCacheSerializer<K, V> serializer;
    private String                       tableName;
    private CqlSession                   session;
    private Duration                     rowTtl              = Duration.ofHours(2);
    private Duration                     maxLockWait         = Duration.ofSeconds(30);
    private Duration                     maxLockFallbackTime = Duration.ofSeconds(45);
    private Duration                     sleepBetweenChecks  = Duration.ofMillis(250);

    public AtomicCacheCassandraBuilder<K, V> setSerializer(IAtomicCacheSerializer<K, V> serializer) {
        this.serializer = serializer;
        return this;
    }

    public AtomicCacheCassandraBuilder<K, V> setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public AtomicCacheCassandraBuilder<K, V> setSession(CqlSession session) {
        this.session = session;
        return this;
    }

    public AtomicCacheCassandraBuilder<K, V> setRowTtl(Duration rowTtl) {
        this.rowTtl = rowTtl;
        return this;
    }

    public AtomicCacheCassandraBuilder<K, V> setMaxLockWait(Duration maxLockWait) {
        this.maxLockWait = maxLockWait;
        return this;
    }

    public AtomicCacheCassandraBuilder<K, V> setMaxLockFallbackTime(Duration maxLockFallbackTime) {
        this.maxLockFallbackTime = maxLockFallbackTime;
        return this;
    }

    public AtomicCacheCassandraBuilder<K, V> setSleepBetweenChecks(Duration sleepBetweenChecks) {
        this.sleepBetweenChecks = sleepBetweenChecks;
        return this;
    }

    public IAtomicCache<K, V> build() {
        Objects.requireNonNull(serializer, "No serializer");
        Objects.requireNonNull(session   , "No session");
        Objects.requireNonNull(tableName , "No table name");

        return new AtomicCacheCassandraImpl<>(
                serializer
                , serializer
                , serializer
                , new CassandraDaoImpl(session, tableName, rowTtl)
                , maxLockWait
                , maxLockFallbackTime
                , sleepBetweenChecks
        );
    }

}
