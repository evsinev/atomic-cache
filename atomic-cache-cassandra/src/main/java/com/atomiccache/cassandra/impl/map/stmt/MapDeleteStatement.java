package com.atomiccache.cassandra.impl.map.stmt;

import com.datastax.oss.driver.api.core.ConsistencyLevel;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;


public class MapDeleteStatement {

    private final CqlSession        session;
    private final PreparedStatement stmt;

    public MapDeleteStatement(CqlSession session, String aTableName) {
        this.session = session;

        stmt = session.prepare(
                "delete from " + aTableName + " where key = ? if always_null = null"
        );
    }

    public void removeKey(String key) {
        ResultSet rs = session.execute(
                stmt.boundStatementBuilder()
                        .setString (0, key)
                        .setSerialConsistencyLevel(ConsistencyLevel.LOCAL_SERIAL)
                        .setConsistencyLevel(ConsistencyLevel.LOCAL_QUORUM)
                        .build()
        );
    }
}
