package com.payneteasy.atomiccache.cassandra.impl.dao.stmt;

import com.datastax.oss.driver.api.core.ConsistencyLevel;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.*;
import com.payneteasy.atomiccache.cassandra.impl.model.AtomicCacheRecordState;
import com.payneteasy.atomiccache.cassandra.impl.model.InsertResult;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class InsertStatement {

    private final CqlSession        session;
    private final PreparedStatement stmt;

    public InsertStatement(CqlSession aSession, String aTableName, Duration rowTtl) {
        session = aSession;

        stmt = session.prepare(
              "insert into " + aTableName
              + "\n          (key, state     , started_fetching_at, fetch_id )"
              + "\n   values (?  , 'FETCHING', ?                  , ?        )"
              + "\n   if not exists using ttl " + rowTtl.getSeconds()
        );
    }

    public InsertResult insert(String key, Instant aStartFetchingAt, UUID fetchId) {
        ResultSet rs = session.execute(
                stmt.boundStatementBuilder()
                        .setString  (0, key)
                        .setInstant (1, aStartFetchingAt)
                        .setUuid    (2, fetchId)
                        .setSerialConsistencyLevel(ConsistencyLevel.LOCAL_SERIAL)
                        .setConsistencyLevel(ConsistencyLevel.LOCAL_QUORUM)
                        .build()
        );

        if (rs.wasApplied()) {
            return InsertResult.builder()
                    .wasApplied(true)
                    .build();
        }

        Row row = Objects.requireNonNull(rs.one(), () -> "No any row for key " + key);

        return InsertResult.builder()
                .wasApplied(false)
                .key             ( row.getString("key"))
                .fetchId         ( row.getUuid("fetch_id"))
                .value           ( row.getString("value"))
                .state           (AtomicCacheRecordState.fromString(row.getString("state")))
                .startFetchingAt ( row.getInstant("started_fetching_at"))
                .build();
    }
}
