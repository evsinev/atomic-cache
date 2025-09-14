package com.atomiccache.guava.impl;

import com.atomiccache.api.map.IAtomicCacheMap;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.time.Duration;
import java.util.Optional;

public class AtomicCacheMapGuavaImpl<K, V> implements IAtomicCacheMap<K, V> {

    private final Cache<K, V> cache;

    public AtomicCacheMapGuavaImpl(int aMaxSize, Duration aExpireAfterWrite) {
        cache = CacheBuilder.newBuilder()
                .maximumSize(aMaxSize)
                .expireAfterWrite(aExpireAfterWrite)
                .build();
    }

    @Override
    public Optional<V> get(@NonNull K key) {
        return Optional.ofNullable(cache.getIfPresent(key));
    }

    @Override
    public Optional<V> getAndPut(@NonNull K key, @NonNull V value, Duration aTtl) {
        V previousValue = cache.getIfPresent(key);
        cache.put(key, value);
        return Optional.ofNullable(previousValue);
    }

    @Override
    public Optional<V> getAndRemove(@NonNull K key) {
        V previousValue = cache.getIfPresent(key);
        cache.invalidate(key);
        return Optional.ofNullable(previousValue);
    }

    @Override
    public void put(@NonNull K key, @NonNull V value, Duration aTtl) {
        cache.put(key, value);
    }

    @Override
    public void remove(@NonNull K key) {
        cache.invalidate(key);
    }
}
