package com.atomiccache.cassandra.impl.cache.dao;

import com.atomiccache.cassandra.impl.cache.model.InsertResult;
import com.atomiccache.cassandra.impl.cache.model.SelectResult;
import com.atomiccache.cassandra.impl.cache.model.UpdateStartFetchingResult;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface ICassandraDao {

    InsertResult insert(String keyText, Instant now, UUID uuid);

    Optional<SelectResult> select(String keyText);

    void setError(String keyText, String aErrorMessage);

    void setValue(String keyText, @NonNull String aValue);

    UpdateStartFetchingResult updateStartedFetchingAt(String keyText, Instant now, UUID aPreviousFetchId, UUID aNewFetchId);
}
