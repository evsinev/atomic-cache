package com.atomiccache.cassandra.impl.cache.model;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(makeFinal = true, level = PRIVATE)
@Builder
public class InsertResult {
    boolean                wasApplied;
    AtomicCacheRecordState state;
    String                 key;
    String                  value;
    Instant                 startFetchingAt;
    UUID                    fetchId;

    public boolean hasFetchedValue() {
        return !isWasApplied() && getState() == AtomicCacheRecordState.FETCHED;
    }
}
