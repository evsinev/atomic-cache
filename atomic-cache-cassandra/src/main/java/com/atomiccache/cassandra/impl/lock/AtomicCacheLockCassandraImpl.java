package com.atomiccache.cassandra.impl.lock;

import com.atomiccache.api.lock.IAtomicCacheLock;
import com.atomiccache.api.lock.IAtomicLock;
import com.atomiccache.api.serializer.IAtomicCacheKeySerializer;
import com.atomiccache.cassandra.impl.lock.stmt.AtomicLockCassandraImpl;
import com.atomiccache.cassandra.impl.lock.stmt.LockSelectResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

public class AtomicCacheLockCassandraImpl<K> implements IAtomicCacheLock<K> {

    private static final Logger LOG = LoggerFactory.getLogger( AtomicCacheLockCassandraImpl.class );

    private final IAtomicCacheKeySerializer<K> keySerializer;

    private final AtomicCacheLockCassandraDaoImpl dao;

    private final Duration maxLockWait        ;
    private final Duration sleepBetweenChecks;

    public AtomicCacheLockCassandraImpl(IAtomicCacheKeySerializer<K> keySerializer, AtomicCacheLockCassandraDaoImpl dao, Duration maxLockWait, Duration sleepBetweenChecks) {
        this.keySerializer      = keySerializer;
        this.dao                = dao;
        this.maxLockWait        = maxLockWait;
        this.sleepBetweenChecks = sleepBetweenChecks;
    }

    @Override
    public IAtomicLock lock(K aKey, Duration aDuration) {
        String keyText = keySerializer.serializeKey(aKey);
        MDC.put("key", keyText);
        try {
            return doLock(keyText, Thread.currentThread().getName() + "-" + UUID.randomUUID(), aDuration);
        } finally {
            MDC.remove("key");
        }
    }

    private IAtomicLock doLock(String aKey, String aOwner, Duration aDuration) {
        boolean isLockCreated = dao.insertLock(aKey, aOwner, aDuration);

        if (isLockCreated) {
            return new AtomicLockCassandraImpl(dao, aKey);
        }

        return waitUnlockInLoop(aKey, aOwner, aDuration);
    }

    private IAtomicLock waitUnlockInLoop(String aKey, String aOwner, Duration aDuration) {
        ThreadCondition condition = new ThreadCondition(sleepBetweenChecks, LOG);

        while (condition.awaitCanRun()) {
            Optional<LockSelectResult> selectResult = dao.selectLock(aKey);
            if (!selectResult.isPresent()) {
                break;
            }

            LOG.debug("Waiting for unlock: {}, lock: {}", aKey, selectResult.get());
        }

        return doLock(aKey, aOwner, aDuration);
    }
}
