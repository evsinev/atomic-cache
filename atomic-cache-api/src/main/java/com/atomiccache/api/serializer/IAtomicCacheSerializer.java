package com.atomiccache.api.serializer;

public interface IAtomicCacheSerializer<K, V>
        extends
        IAtomicCacheKeySerializer<K>,
        IAtomicCacheValueSerializer<V>,
        IAtomicCacheValueDeserializer<V>
{
}
