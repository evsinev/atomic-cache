package com.atomiccache.cassandra.impl.lock.stmt;

import com.datastax.oss.driver.api.core.ConsistencyLevel;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;

import java.time.Duration;


public class LockInsertStatement {

    private final CqlSession        session;
    private final PreparedStatement stmt;

    public LockInsertStatement(CqlSession session, String aTableName) {
        this.session = session;

        stmt = session.prepare(
                "insert into " + aTableName + " (key, owner) values (?, ?) if not exists using ttl ?"
        );
    }

    public boolean insertLock(String key, String owner, Duration aLockTtl) {
        ResultSet execute = session.execute(
                stmt.boundStatementBuilder()
                        .setString (0, key)
                        .setString (1, owner)
                        .setInt   (2, (int) aLockTtl.getSeconds())
                        .setSerialConsistencyLevel(ConsistencyLevel.LOCAL_SERIAL)
                        .setConsistencyLevel(ConsistencyLevel.LOCAL_QUORUM)
                        .build()
        );

        return execute.wasApplied();
    }
}
