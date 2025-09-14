package com.atomiccache.api.map;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.time.Duration;
import java.util.Optional;

public interface IAtomicCacheMap<K, V> {

    /**
     * K - key
     */
    Optional<V> get(@NonNull K key);

    Optional<V> getAndPut(@NonNull K key, @NonNull V value, Duration aTtl);

    void put(@NonNull K key, @NonNull V value, Duration aTtl);

    Optional<V> getAndRemove(@NonNull K key);

    void remove(@NonNull K key);

    default void close() {}
}
