package com.atomiccache.cassandra;

import com.atomiccache.api.lock.IAtomicCacheLock;
import com.atomiccache.api.serializer.IAtomicCacheKeySerializer;
import com.atomiccache.cassandra.impl.lock.AtomicCacheLockCassandraDaoImpl;
import com.atomiccache.cassandra.impl.lock.AtomicCacheLockCassandraImpl;
import com.datastax.oss.driver.api.core.CqlSession;

import java.time.Duration;
import java.util.Objects;

public class AtomicCacheLockCassandraBuilder<K> {

    private IAtomicCacheKeySerializer<K>     keySerializer;
    private String                           tableName;
    private CqlSession                       session;
    private Duration                         maxLockWait         = Duration.ofSeconds(30);
    private Duration                         sleepBetweenChecks  = Duration.ofMillis(250);

    public AtomicCacheLockCassandraBuilder<K> keySerializer(IAtomicCacheKeySerializer<K> keySerializer) {
        this.keySerializer = keySerializer;
        return this;
    }

    public AtomicCacheLockCassandraBuilder<K> tableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public AtomicCacheLockCassandraBuilder<K> session(CqlSession session) {
        this.session = session;
        return this;
    }

    public AtomicCacheLockCassandraBuilder<K> maxLockWait(Duration maxLockWait) {
        this.maxLockWait = maxLockWait;
        return this;
    }

    public AtomicCacheLockCassandraBuilder<K> sleepBetweenChecks(Duration sleepBetweenChecks) {
        this.sleepBetweenChecks = sleepBetweenChecks;
        return this;
    }

    public IAtomicCacheLock<K> build() {
        Objects.requireNonNull(session   , "No session");
        Objects.requireNonNull(tableName , "No table name");

        return new AtomicCacheLockCassandraImpl<>(
                  keySerializer
                , new AtomicCacheLockCassandraDaoImpl(session, tableName)
                , maxLockWait
                , sleepBetweenChecks
        );
    }

}
