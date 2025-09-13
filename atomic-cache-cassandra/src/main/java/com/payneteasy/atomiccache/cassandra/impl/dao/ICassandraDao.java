package com.payneteasy.atomiccache.cassandra.impl.dao;

import com.payneteasy.atomiccache.cassandra.impl.model.InsertResult;
import com.payneteasy.atomiccache.cassandra.impl.model.SelectResult;
import com.payneteasy.atomiccache.cassandra.impl.model.UpdateStartFetchingResult;
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
