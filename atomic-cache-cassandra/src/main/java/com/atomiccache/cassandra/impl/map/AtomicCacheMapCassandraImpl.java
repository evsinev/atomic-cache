package com.atomiccache.cassandra.impl.map;

import com.atomiccache.api.map.IAtomicCacheMap;
import com.atomiccache.api.serializer.IAtomicCacheSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Optional;

public class AtomicCacheMapCassandraImpl<K, V> implements IAtomicCacheMap<K, V> {

    private static final Logger LOG = LoggerFactory.getLogger( AtomicCacheMapCassandraImpl.class );

    private final IAtomicCacheSerializer<K, V> serializer;

    private final CassandraMapDaoImpl dao;


    public AtomicCacheMapCassandraImpl(IAtomicCacheSerializer<K, V> serializer, CassandraMapDaoImpl dao) {
        this.serializer = serializer;
        this.dao        = dao;
    }


    @Override
    public Optional<V> get(@NonNull K key) {
        return dao.select(serializer.serializeKey(key))
                .map(serializer::deserializeValueFromText);
    }

    @Override
    public Optional<V> getAndPut(@NonNull K key, @NonNull V value, Duration aTtl) {
        Optional<V> previous = get(key);
        dao.put(serializer.serializeKey(key), serializer.serializeValue(value), aTtl);
        return previous;
    }

    @Override
    public void put(@NonNull K key, @NonNull V value, Duration aTtl) {
        dao.put(serializer.serializeKey(key), serializer.serializeValue(value), aTtl);
    }


    @Override
    public Optional<V> getAndRemove(@NonNull K key) {
        Optional<V> previous = get(key);
        dao.removeKey(serializer.serializeKey(key));
        return previous;
    }

    @Override
    public void remove(@NonNull K key) {
        dao.removeKey(serializer.serializeKey(key));
    }

}
