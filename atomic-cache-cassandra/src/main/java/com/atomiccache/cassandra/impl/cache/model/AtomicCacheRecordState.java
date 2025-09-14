package com.atomiccache.cassandra.impl.cache.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum AtomicCacheRecordState {

    FETCHING, FETCHED, ERROR;

    private static final Logger LOG = LoggerFactory.getLogger( AtomicCacheRecordState.class );

    public static AtomicCacheRecordState fromString(String state) {
        if (state == null) {
            LOG.error( "Cannot parse <null> as state" );
            return ERROR;
        }

        switch (state) {
            case "FETCHING": return FETCHING;
            case "FETCHED": return FETCHED;
            case "ERROR": return ERROR;
            default:
                LOG.error( "Cannot parse '{}' as state", state );
                return ERROR;
        }
    }
}
