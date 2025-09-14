package com.atomiccache.cassandra.impl.lock.stmt;

import com.atomiccache.api.lock.IAtomicLock;
import com.atomiccache.cassandra.impl.lock.AtomicCacheLockCassandraDaoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AtomicLockCassandraImpl implements IAtomicLock {

    private static final Logger LOG = LoggerFactory.getLogger(AtomicLockCassandraImpl.class);

    private final AtomicCacheLockCassandraDaoImpl dao;
    private final String                          key;

    public AtomicLockCassandraImpl(AtomicCacheLockCassandraDaoImpl dao, String key) {
        this.dao = dao;
        this.key = key;
    }

    @Override
    public void unlock() {
        if (!dao.deleteLock(key)) {
            LOG.warn("Failed to unlock key: {}", key);
        }
    }
}
