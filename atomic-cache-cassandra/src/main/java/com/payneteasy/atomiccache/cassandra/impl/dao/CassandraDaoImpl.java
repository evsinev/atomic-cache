package com.payneteasy.atomiccache.cassandra.impl.dao;

import com.datastax.oss.driver.api.core.CqlSession;
import com.payneteasy.atomiccache.cassandra.impl.dao.stmt.*;
import com.payneteasy.atomiccache.cassandra.impl.model.InsertResult;
import com.payneteasy.atomiccache.cassandra.impl.model.SelectResult;
import com.payneteasy.atomiccache.cassandra.impl.model.UpdateStartFetchingResult;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class CassandraDaoImpl implements ICassandraDao {

    private static final Logger LOG = LoggerFactory.getLogger( CassandraDaoImpl.class );

    private final InsertStatement              insertStatement;
    private final SelectStatement              selectStatement;
    private final SetErrorStatement            setErrorStatement;
    private final SetValueStatement            setValueStatement;
    private final UpdateStartFetchingStatement updateStartFetchingStatement;

    public CassandraDaoImpl(CqlSession session, String aTableName, Duration rowTtl) {
        insertStatement              = new InsertStatement(session, aTableName, rowTtl);
        selectStatement              = new SelectStatement(session, aTableName);
        setErrorStatement            = new SetErrorStatement(session, aTableName);
        setValueStatement            = new SetValueStatement(session, aTableName);
        updateStartFetchingStatement = new UpdateStartFetchingStatement(session, aTableName);
    }

    @Override
    public InsertResult insert(String keyText, Instant now, UUID aFetchId) {
        LOG.atDebug()
                .addKeyValue("started_fetching_at", now)
                .addKeyValue("fetch_id", aFetchId)
                .log("insert()");

        InsertResult result = insertStatement.insert(keyText, now, aFetchId);
        LOG.atDebug()
                .addKeyValue("was_applied", result.isWasApplied())
                .addKeyValue("state", result.getState())
                .addKeyValue("started_fetching_at", result.getStartFetchingAt())
                .addKeyValue("value", result.getValue())
                .addKeyValue("fetch_id", result.getFetchId())
                .log("insert() result");

        return result;
    }

    @Override
    public Optional<SelectResult> select(String keyText) {
        Optional<SelectResult> result = selectStatement.select(keyText);
        if (result.isPresent()) {
            LOG.atDebug()
                .addKeyValue("state", result.get().getState())
                .addKeyValue("value", result.get().getValue())
                .log("select()");
        } else {
            LOG.atDebug()
                    .log("select() returned no result");
        }
        return result;
    }

    @Override
    public void setError(String keyText, String aErrorMessage) {
        LOG.atDebug()
                .addKeyValue("error_message", aErrorMessage)
                .log("setError()");
        setErrorStatement.setError(keyText, aErrorMessage);
    }

    @Override
    public void setValue(String keyText, @NonNull String aValue) {
        LOG.atDebug()
                .addKeyValue("value", aValue)
                .log("setValue()");
         setValueStatement.setValue(keyText, aValue);
    }

    @Override
    public UpdateStartFetchingResult updateStartedFetchingAt(String keyText, Instant now, UUID aPreviousFetchId, UUID aNewFetchId) {
        LOG.atDebug()
                .addKeyValue("started_fetching_at", now)
                .addKeyValue("previous_fetch_id", aPreviousFetchId)
                .addKeyValue("new_fetch_id", aNewFetchId)
                .log("updateStartedFetchingAt()");
        UpdateStartFetchingResult result = updateStartFetchingStatement.updateStartedFetchingAt(keyText, now, aPreviousFetchId, aNewFetchId);
        LOG.atDebug()
                .addKeyValue("was_applied", result.isApplied())
                .addKeyValue("fetch_id", result.getFetchId())
                .log("updateStartedFetchingAt() result");

        return result;
    }
}
