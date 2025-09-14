package com.payneteasy.atomiccache.cassandra.impl.dao.stmt;

import com.datastax.oss.driver.api.core.ConsistencyLevel;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.payneteasy.atomiccache.cassandra.impl.model.AtomicCacheRecordState;
import com.payneteasy.atomiccache.cassandra.impl.model.InsertResult;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.time.Instant;

public class UpdateErrorStatement {

    private final CqlSession        session;
    private final     PreparedStatement stmt;

    public UpdateErrorStatement(CqlSession session, String aTableName) {
        this.session = session;

        stmt = session.prepare(
              "update " + aTableName + "\n" +
              " set state = 'FETCHED',\n" +
              "   value = ?\n" +
              " where key = ?\n" +
              " if state = 'FETCHING';"
        );
    }

    public InsertResult insert(String key, Instant aStartFetchingAt, String fetchId) {
        ResultSet rs = session.execute(
                stmt.boundStatementBuilder()
                        .setString  (0, key)
                        .setLong    (1, aStartFetchingAt.toEpochMilli())
                        .setString  (2, fetchId)
                        .setSerialConsistencyLevel(ConsistencyLevel.LOCAL_SERIAL)
                        .setConsistencyLevel(ConsistencyLevel.LOCAL_QUORUM)
                        .build()
        );

        if (!rs.wasApplied()) {
            return InsertResult.builder()
                    .wasApplied(true)
                    .build();
        }

        Row row = rs.one();
        if (row == null) {
            throw new IllegalStateException("No any row for key " + key);
        }

        return InsertResult.builder()
                .wasApplied(false)
                .key             ( row.getString("key"))
                .fetchId         ( row.getUuid("fetch_id"))
                .value           ( row.getString("value"))
                .state           (AtomicCacheRecordState.fromString(row.getString("state")))
                .startFetchingAt ( row.getInstant("started_fetching_at"))
                .build();
    }

    public void update(AtomicCacheRecordState error, String key, @NonNull String value) {

    }
//        this.updateStatement   = session.prepare("""
//           update tds_lock
//           set state = 'FETCHED',
//               value = ?
//           where key = ?
//           if state = 'FETCHING';
//        """);
}
