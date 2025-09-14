package com.atomiccache.guava.impl;

import com.atomiccache.api.cache.IAtomicCache;
import com.atomiccache.api.cache.IAtomicCacheFetch;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.time.Duration;
import java.util.concurrent.ExecutionException;

public class AtomicCacheGuavaImpl<K, V> implements IAtomicCache<K, V> {

    private final Cache<K, V> cache;

    public AtomicCacheGuavaImpl(int aMaxSize, Duration aExpireAfterWrite) {
        cache = CacheBuilder.newBuilder()
                .maximumSize(aMaxSize)
                .expireAfterWrite(aExpireAfterWrite)
                .build();
    }

    @Override
    public V getOrFetch(@NonNull K key, @NonNull IAtomicCacheFetch<K, V> aFetch) {
        try {
            return cache.get(key, () -> aFetch.fetchValue(key));
        } catch (ExecutionException e) {
            throw new IllegalStateException("Cannot get value for key " + key, e);
        }
    }
}
