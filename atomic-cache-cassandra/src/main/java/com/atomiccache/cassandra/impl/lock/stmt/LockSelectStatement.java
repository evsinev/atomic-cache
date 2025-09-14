package com.atomiccache.cassandra.impl.lock.stmt;

import com.datastax.oss.driver.api.core.ConsistencyLevel;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;

import java.time.Duration;
import java.util.Optional;


public class LockSelectStatement {

    private final CqlSession        session;
    private final PreparedStatement stmt;

    public LockSelectStatement(CqlSession session, String aTableName) {
        this.session = session;

        stmt = session.prepare(
                "select key, owner from " + aTableName + " where key = ?"
        );
    }

    public Optional<LockSelectResult> select(String key) {
        ResultSet rs = session.execute(
                stmt.boundStatementBuilder()
                        .setString  (0, key)
                        .setSerialConsistencyLevel(ConsistencyLevel.LOCAL_SERIAL)
                        .setConsistencyLevel(ConsistencyLevel.LOCAL_QUORUM)
                        .build()
        );

        Row row = rs.one();
        if (row == null) {
            return Optional.empty();
        }

        return Optional.of(
                LockSelectResult.builder()
                        .key          (row.getString("key"))
                        .owner        (row.getString("owner"))
                        .build()
        );
    }
}
