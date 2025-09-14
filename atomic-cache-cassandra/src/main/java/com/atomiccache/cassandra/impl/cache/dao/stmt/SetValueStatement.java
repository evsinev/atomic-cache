package com.atomiccache.cassandra.impl.cache.dao.stmt;

import com.datastax.oss.driver.api.core.ConsistencyLevel;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;

public class SetValueStatement {

    private final CqlSession        session;
    private final PreparedStatement stmt;

    public SetValueStatement(CqlSession aSession, String aTableName) {
        session = aSession;

        stmt = session.prepare(
              "update " + aTableName + "\n" +
              " set\n" +
              "   state = 'FETCHED',\n" +
              "   value = ?\n" +
              " where key = ?\n" +
              " if exists"
        );
    }

    public void setValue(String keyText, String aValue) {
        ResultSet rs = session.execute(
                stmt.boundStatementBuilder()
                        .setString  (0, aValue)
                        .setString  (1, keyText)
                        .setSerialConsistencyLevel(ConsistencyLevel.LOCAL_SERIAL)
                        .setConsistencyLevel(ConsistencyLevel.LOCAL_QUORUM)
                        .build()
        );

        if (!rs.wasApplied()) {
            throw new IllegalStateException("Cannot set value because it was not applied for key '" + keyText + "'" );
        }
    }

}
