package com.payneteasy.atomiccache.api;

import org.checkerframework.checker.nullness.qual.NonNull;

public interface IAtomicCache<K, V> {

    /**
     * K - key
     * P - payload
     */
    V getOrFetch(@NonNull K key, @NonNull IAtomicCacheFetch<K, V> aFetch);

    void close();
}
