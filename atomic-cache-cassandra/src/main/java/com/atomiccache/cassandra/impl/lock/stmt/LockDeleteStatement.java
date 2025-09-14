package com.atomiccache.cassandra.impl.lock.stmt;

import com.datastax.oss.driver.api.core.ConsistencyLevel;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;

import java.time.Duration;


public class LockDeleteStatement {

    private final CqlSession        session;
    private final PreparedStatement stmt;

    public LockDeleteStatement(CqlSession session, String aTableName) {
        this.session = session;

        stmt = session.prepare(
                "delete from " + aTableName + " where key = ? if owner != ?"
        );
    }

    public boolean deleteLock(String key) {
        ResultSet execute = session.execute(
                stmt.boundStatementBuilder()
                        .setString (0, key)
                        .setString (1, "no-owner")
                        .setSerialConsistencyLevel(ConsistencyLevel.LOCAL_SERIAL)
                        .setConsistencyLevel(ConsistencyLevel.LOCAL_QUORUM)
                        .build()
        );

        return execute.wasApplied();
    }
}
