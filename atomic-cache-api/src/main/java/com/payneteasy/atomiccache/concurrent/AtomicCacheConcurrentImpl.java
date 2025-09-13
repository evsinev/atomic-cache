package com.payneteasy.atomiccache.concurrent;

import com.payneteasy.atomiccache.api.IAtomicCacheFetch;
import com.payneteasy.atomiccache.api.IAtomicCache;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.concurrent.ConcurrentHashMap;

public class AtomicCacheConcurrentImpl<K, V> implements IAtomicCache<K, V> {

    private final ConcurrentHashMap<K, V> map = new ConcurrentHashMap<>();


    @Override
    public V getOrFetch(@NonNull K aKey, @NonNull IAtomicCacheFetch<K, V> aFetch) {
        return map.computeIfAbsent(aKey, key -> {
            try {
                return aFetch.fetchValue(aKey);
            } catch (Exception e) {
                throw new IllegalStateException("Cannot fetch value for key " + key, e);
            }
        });
    }

    @Override
    public void close() {

    }
}
