package com.atomiccache.cassandra.impl.lock;

import com.atomiccache.cassandra.impl.lock.stmt.LockDeleteStatement;
import com.atomiccache.cassandra.impl.lock.stmt.LockInsertStatement;
import com.atomiccache.cassandra.impl.lock.stmt.LockSelectResult;
import com.atomiccache.cassandra.impl.lock.stmt.LockSelectStatement;
import com.datastax.oss.driver.api.core.CqlSession;

import java.time.Duration;
import java.util.Optional;

public class AtomicCacheLockCassandraDaoImpl {

    private final LockInsertStatement insertStatement;
    private final LockDeleteStatement deleteStatement;
    private final LockSelectStatement selectStatement;

    public AtomicCacheLockCassandraDaoImpl(CqlSession aSession, String aTableName) {
        insertStatement = new LockInsertStatement(aSession, aTableName);
        deleteStatement = new LockDeleteStatement(aSession, aTableName);
        selectStatement = new LockSelectStatement(aSession, aTableName);
    }

    public boolean insertLock(String key, String owner, Duration aLockTtl) {
        return insertStatement.insertLock(key, owner, aLockTtl);
    }

    public boolean deleteLock(String key) {
        return deleteStatement.deleteLock(key);
    }

    public Optional<LockSelectResult> selectLock(String key) {
        return selectStatement.select(key);
    }

}
