package com.atomiccache.test.map.example;

import com.atomiccache.api.map.IAtomicCacheMap;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class AtomicCacheMapNaiveImpl<K, V> implements IAtomicCacheMap<K, V> {

    private final ConcurrentHashMap<K, V> map = new ConcurrentHashMap<>();

    @Override
    public Optional<V> get(@NonNull K key) {
        return Optional.ofNullable(map.get(key));
    }

    @Override
    public Optional<V> getAndPut(@NonNull K key, @NonNull V value, Duration aTtl) {
        V previousValue = map.put(key, value);
        return Optional.ofNullable(previousValue);
    }

    @Override
    public Optional<V> getAndRemove(@NonNull K key) {
        return Optional.ofNullable(map.remove(key));
    }

    @Override
    public void put(@NonNull K key, @NonNull V value, Duration aTtl) {
        map.put(key, value);
    }

    @Override
    public void remove(@NonNull K key) {
        map.remove(key);
    }
}
