package com.atomiccache.api.cache;

import org.checkerframework.checker.nullness.qual.NonNull;

public interface IAtomicCache<K, V> {

    /**
     * K - key
     * P - payload
     */
    V getOrFetch(@NonNull K key, @NonNull IAtomicCacheFetch<K, V> aFetch);

    default void close() {}
}
