package com.atomiccache.cassandra.impl.map.stmt;

import com.datastax.oss.driver.api.core.ConsistencyLevel;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;

import java.time.Duration;


public class MapPutStatement {

    private final CqlSession        session;
    private final PreparedStatement stmt;

    public MapPutStatement(CqlSession session, String aTableName) {
        this.session = session;

        String query = "update " + aTableName + " using ttl ? set value = ? where key = ? if always_null = null";
        try {
            stmt = session.prepare(query);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid query: " + query, e);
        }
    }

    public void put(String key, String value, Duration aTtl) {
        ResultSet rs = session.execute(
                stmt.boundStatementBuilder()
                        .setInt   (0, (int) aTtl.getSeconds())
                        .setString (1, value)
                        .setString (2, key)
                        .setSerialConsistencyLevel(ConsistencyLevel.LOCAL_SERIAL)
                        .setConsistencyLevel(ConsistencyLevel.LOCAL_QUORUM)
                        .build()
        );
    }
}
