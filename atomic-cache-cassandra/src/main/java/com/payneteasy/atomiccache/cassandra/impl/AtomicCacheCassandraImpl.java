package com.payneteasy.atomiccache.cassandra.impl;

import com.payneteasy.atomiccache.api.IAtomicCacheFetch;
import com.payneteasy.atomiccache.api.IAtomicCache;
import com.payneteasy.atomiccache.api.serializer.IAtomicCacheKeySerializer;
import com.payneteasy.atomiccache.api.serializer.IAtomicCacheValueDeserializer;
import com.payneteasy.atomiccache.api.serializer.IAtomicCacheValueSerializer;
import com.payneteasy.atomiccache.cassandra.impl.dao.ICassandraDao;
import com.payneteasy.atomiccache.cassandra.impl.model.InsertResult;
import com.payneteasy.atomiccache.cassandra.impl.model.SelectResult;
import com.payneteasy.atomiccache.cassandra.impl.model.UpdateStartFetchingResult;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import static com.payneteasy.atomiccache.cassandra.impl.model.AtomicCacheRecordState.ERROR;
import static com.payneteasy.atomiccache.cassandra.impl.model.AtomicCacheRecordState.FETCHED;

public class AtomicCacheCassandraImpl<K, V> implements IAtomicCache<K, V> {

    private static final Logger LOG = LoggerFactory.getLogger( AtomicCacheCassandraImpl.class );

    private static final int INITIAL_GUARD = 3;

    private final IAtomicCacheKeySerializer<K>     keySerializer;
    private final IAtomicCacheValueDeserializer<V> valueDeserializer;
    private final IAtomicCacheValueSerializer<V>   valueSerializer;

    private final ICassandraDao dao;

    private final Duration maxLockWait        ;
    private final Duration maxLockFallbackTime;
    private final Duration sleepBetweenChecks;

    public AtomicCacheCassandraImpl(IAtomicCacheKeySerializer<K> keySerializer, IAtomicCacheValueDeserializer<V> valueDeserializer, IAtomicCacheValueSerializer<V> valueSerializer, ICassandraDao dao, Duration maxLockWait, Duration maxLockFallbackTime, Duration sleepBetweenChecks) {
        this.keySerializer       = keySerializer;
        this.valueDeserializer   = valueDeserializer;
        this.valueSerializer     = valueSerializer;
        this.dao                 = dao;
        this.maxLockWait         = maxLockWait;
        this.maxLockFallbackTime = maxLockFallbackTime;
        this.sleepBetweenChecks  = sleepBetweenChecks;
    }

    @Override
    public V getOrFetch(@NonNull K aKey, @NonNull IAtomicCacheFetch<K, V> aFetch) {
        String keyText = keySerializer.serializeKey(aKey);
        MDC.put("key", keyText);
        try {
            LOG.debug("getOrFetch()");

            InsertResult insertResult = dao.insert(keyText, Instant.now(), UUID.randomUUID());

            // use previous result
            if (insertResult.hasFetchedValue()) {
                return valueDeserializer.deserializeValueFromText(insertResult.getValue());
            }

            // fetch for the first time
            if (insertResult.isWasApplied()) {
                return fetchResult(aKey, aFetch);
            }

            // fetching state is too old
            UUID previousFetchId = insertResult.getFetchId();

            if (Instant.now().minus(maxLockFallbackTime).isAfter(insertResult.getStartFetchingAt())) {
                LOG.debug("Started fetching time is tool old {}", insertResult.getStartFetchingAt());
                return fetchResultAgain(aKey, previousFetchId, aFetch, INITIAL_GUARD);
            }

            if (insertResult.getState() == ERROR) {
                return fetchResultAgain(aKey, previousFetchId, aFetch, INITIAL_GUARD);
            }

            return fetchLoop(aKey, aFetch, keyText, previousFetchId, INITIAL_GUARD);
        } finally {
            MDC.remove("key");
        }
    }

    private V fetchLoop(K aKey, IAtomicCacheFetch<K, V> aFetch, String keyText, UUID previousFetchId, int aGuard) {
        LOG.atDebug()
                .addKeyValue("previous_fetch_id", previousFetchId)
                .addKeyValue("guard", aGuard)
                .log("fetchInCycle()");

        // wait for result from another fetcher
        long endTime = System.currentTimeMillis() + maxLockWait.toMillis();
        while (System.currentTimeMillis() <= endTime) {
            SelectResult selectResult = dao.select(keyText).orElseThrow(() -> new IllegalStateException("No any results found for key " + keyText));
            if (selectResult.getState() == FETCHED) {
                return valueDeserializer.deserializeValueFromText(selectResult.getValue());
            }

            // another fetcher has error
            if (selectResult.getState() == ERROR) {
                return fetchResultAgain(aKey, previousFetchId, aFetch, aGuard);
            }

            try {
                //noinspection BusyWait
                Thread.sleep(sleepBetweenChecks.toMillis());
            } catch (InterruptedException e) {
                LOG.warn("Interrupted while waiting for fetch result. Skipping.");
            }
        }

        // fetching is too long. fetch once again
        return fetchResultAgain(aKey, previousFetchId, aFetch, aGuard);
    }

    private V fetchResultAgain(K aKey, UUID aFetchId, @NonNull IAtomicCacheFetch<K, V> aFetch, int aGuard) {
        LOG.atDebug()
                .addKeyValue("aFetchId", aFetchId)
                .addKeyValue("guard", aGuard)
                .log("fetchResultAgain()");

        aGuard--;
        if (aGuard <= 0) {
            throw new IllegalStateException("Guard exhausted");
        }
        String keyText = keySerializer.serializeKey(aKey);

        UpdateStartFetchingResult result  = dao.updateStartedFetchingAt(keyText, Instant.now(), aFetchId, UUID.randomUUID());

        // another fetcher start fetching. wait for it
        if (!result.isApplied()) {
            return fetchLoop(aKey, aFetch, keyText, result.getFetchId(), aGuard);
        }

        return fetchResult(aKey, aFetch);
    }

    private V fetchResult(K aKey, IAtomicCacheFetch<K, V> aFetch) {
        LOG.atDebug()
                .log("fetchResult()");

        String keyText = keySerializer.serializeKey(aKey);

        // fetch result
        V value;
        try {
            LOG.debug("Fetching value ...");
            value = aFetch.fetchValue(aKey);
            LOG.debug("Fetched value {}", value);

        } catch (Exception e) {
            // cannot fetch value
            dao.setError(keyText, "Cannot fetch value: " + e);
            throw new IllegalStateException("Cannot fetch value for key " + aKey, e);
        }

        //noinspection ConstantValue
        if (value == null) {
            dao.setError(keyText, "fetchValue() returned null value");
            throw new IllegalStateException("fetchValue() returned null value for key " + aKey);
        }

        dao.setValue(keyText, valueSerializer.serializeValue(value));

        return value;
    }

    @Override
    public void close() {

    }
}
