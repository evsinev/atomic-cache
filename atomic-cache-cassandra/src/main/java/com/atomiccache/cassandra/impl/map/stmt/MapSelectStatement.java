package com.atomiccache.cassandra.impl.map.stmt;

import com.datastax.oss.driver.api.core.ConsistencyLevel;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;

import java.util.Optional;


public class MapSelectStatement {

    private final CqlSession        session;
    private final PreparedStatement stmt;

    public MapSelectStatement(CqlSession session, String aTableName) {
        this.session = session;

        stmt = session.prepare(
                "select value from " + aTableName + " where key = ?"
        );
    }

    public Optional<String> select(String key) {
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

        return Optional.ofNullable(
                row.getString("value")
        );
    }
}
