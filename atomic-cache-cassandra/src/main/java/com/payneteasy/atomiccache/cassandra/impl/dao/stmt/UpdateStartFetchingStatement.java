package com.payneteasy.atomiccache.cassandra.impl.dao.stmt;

import com.datastax.oss.driver.api.core.ConsistencyLevel;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.payneteasy.atomiccache.cassandra.impl.model.UpdateStartFetchingResult;

import java.time.Instant;
import java.util.UUID;

public class UpdateStartFetchingStatement {

    private final CqlSession        session;
    private final PreparedStatement stmt;

    public UpdateStartFetchingStatement(CqlSession aSession, String aTableName) {
        session = aSession;

        stmt = session.prepare(
              "update " + aTableName + "\n" +
              " set\n" +
              "   state = 'FETCHING',\n" +
              "   started_fetching_at = ?,\n" +
              "   fetch_id = ?\n" +
              " where key = ?\n" +
              " if fetch_id = ?"
        );
    }

    public UpdateStartFetchingResult updateStartedFetchingAt(String keyText, Instant now, UUID aPreviousFetchId, UUID aNewFetchId) {
        ResultSet rs = session.execute(
                stmt.boundStatementBuilder()
                        .setInstant (0, now)
                        .setUuid    (1, aNewFetchId)
                        .setString  (2, keyText)
                        .setUuid    (3, aPreviousFetchId)
                        .setSerialConsistencyLevel(ConsistencyLevel.LOCAL_SERIAL)
                        .setConsistencyLevel(ConsistencyLevel.LOCAL_QUORUM)
                        .build()
        );

        if (rs.wasApplied()) {
            return UpdateStartFetchingResult.builder()
                    .applied(true)
                    .build();
        }

        Row row = rs.one();
        if (row == null) {
            throw new IllegalStateException("No any row for key " + keyText);
        }

        return UpdateStartFetchingResult.builder()
                .applied(false)
                .fetchId(row.getUuid("fetch_id"))
                .build();
    }

}
