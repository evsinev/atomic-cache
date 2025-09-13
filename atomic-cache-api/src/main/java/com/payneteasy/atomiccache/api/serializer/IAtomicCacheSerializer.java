package com.payneteasy.atomiccache.api.serializer;

public interface IAtomicCacheSerializer<K, P>
        extends
        IAtomicCacheKeySerializer<K>,
        IAtomicCacheValueSerializer<P>,
        IAtomicCacheValueDeserializer<P>
{
}
