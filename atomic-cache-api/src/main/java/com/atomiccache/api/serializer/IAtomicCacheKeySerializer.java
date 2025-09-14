package com.atomiccache.api.serializer;

import org.checkerframework.checker.nullness.qual.NonNull;

public interface IAtomicCacheKeySerializer<K> {

    @NonNull
    String serializeKey(@NonNull K key);

}
