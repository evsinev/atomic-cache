package com.payneteasy.atomiccache.cassandra.impl.dao.stmt;

import com.datastax.oss.driver.api.core.ConsistencyLevel;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;

public class SetErrorStatement {

    private final CqlSession        session;
    private final PreparedStatement stmt;

    public SetErrorStatement(CqlSession aSession, String aTableName) {
        session = aSession;

        stmt = session.prepare(
              "update " + aTableName + "\n" +
              " set\n" +
              "   state = 'ERROR',\n" +
              "   error_message = ?\n" +
              " where key = ?\n" +
              " if exists"
        );
    }

    public void setError(String keyText, String aErrorMessage) {
        ResultSet rs = session.execute(
                stmt.boundStatementBuilder()
                        .setString  (0, keyText)
                        .setString  (1, aErrorMessage)
                        .setSerialConsistencyLevel(ConsistencyLevel.LOCAL_SERIAL)
                        .setConsistencyLevel(ConsistencyLevel.LOCAL_QUORUM)
                        .build()
        );

        if (!rs.wasApplied()) {
            throw new IllegalStateException("Cannot set an error state because it was not applied for key '" + keyText + "'" );
        }
    }

}
