package com.atomiccache.cassandra.impl.cache.dao.stmt;

import com.datastax.oss.driver.api.core.ConsistencyLevel;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.atomiccache.cassandra.impl.cache.model.AtomicCacheRecordState;
import com.atomiccache.cassandra.impl.cache.model.SelectResult;

import java.util.Optional;

public class SelectStatement {

    private final CqlSession        session;
    private final PreparedStatement stmt;

    public SelectStatement(CqlSession session, String aTableName) {
        this.session = session;

        stmt = session.prepare(
              "select * from " + aTableName + "\n" +
              " where key = ?;"
        );
    }

    public Optional<SelectResult> select(String key) {
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
            SelectResult.builder()
                .value           ( row.getString("value"))
                .state           (AtomicCacheRecordState.fromString(row.getString("state")))
                .build()
        );
    }
}
