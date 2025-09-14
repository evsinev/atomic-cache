package com.atomiccache.api.serializer;

public interface IAtomicCacheSerializer<K, P>
        extends
        IAtomicCacheKeySerializer<K>,
        IAtomicCacheValueSerializer<P>,
        IAtomicCacheValueDeserializer<P>
{
}
