package com.atomiccache.cassandra;

import com.atomiccache.api.map.IAtomicCacheMap;
import com.atomiccache.api.serializer.*;
import com.atomiccache.cassandra.impl.map.AtomicCacheMapCassandraImpl;
import com.atomiccache.cassandra.impl.map.CassandraMapDaoImpl;
import com.datastax.oss.driver.api.core.CqlSession;

import java.util.Objects;

public class AtomicCacheMapCassandraBuilder<K, V> {

    private IAtomicCacheKeySerializer<K>     keySerializer;
    private IAtomicCacheValueSerializer<V>   valueSerializer;
    private IAtomicCacheValueDeserializer<V> valueDeserializer;
    private String                           tableName;
    private CqlSession                       session;

    public AtomicCacheMapCassandraBuilder<K, V> serializer(IAtomicCacheSerializer<K, V> serializer) {
        keySerializer = serializer;
        valueSerializer = serializer;
        valueDeserializer = serializer;
        return this;
    }

    public AtomicCacheMapCassandraBuilder<K, V> keySerializer(IAtomicCacheKeySerializer<K> keySerializer) {
        this.keySerializer = keySerializer;
        return this;
    }

    public AtomicCacheMapCassandraBuilder<K, V> valueSerializer(IAtomicCacheValueSerializer<V> valueSerializer) {
        this.valueSerializer = valueSerializer;
        return this;
    }

    public AtomicCacheMapCassandraBuilder<K, V> valueDeserializer(IAtomicCacheValueDeserializer<V> valueDeserializer) {
        this.valueDeserializer = valueDeserializer;
        return this;
    }

    public AtomicCacheMapCassandraBuilder<K, V> tableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public AtomicCacheMapCassandraBuilder<K, V> session(CqlSession session) {
        this.session = session;
        return this;
    }

    public IAtomicCacheMap<K, V> build() {
        Objects.requireNonNull(session   , "No session");
        Objects.requireNonNull(tableName , "No table name");

        return new AtomicCacheMapCassandraImpl<>(
                  new AtomicCacheSerializerContainer<>(keySerializer, valueDeserializer, valueSerializer)
                , new CassandraMapDaoImpl(session, tableName)
        );
    }

}
