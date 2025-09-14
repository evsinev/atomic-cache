package com.atomiccache.guava;

import com.atomiccache.api.cache.IAtomicCache;
import com.atomiccache.guava.impl.AtomicCacheGuavaImpl;

import java.time.Duration;

public class AtomicCacheGuavaBuilder<K, V> {

    private int      maxSize          = 10_000;
    private Duration expireAfterWrite = Duration.ofHours(1);

    public AtomicCacheGuavaBuilder<K, V> maxSize(int maxSize) {
        this.maxSize = maxSize;
        return this;
    }

    public AtomicCacheGuavaBuilder<K, V> expireAfterWrite(Duration expireAfterWrite) {
        this.expireAfterWrite = expireAfterWrite;
        return this;
    }

    public IAtomicCache<K, V> build() {
        return new AtomicCacheGuavaImpl<>(
                maxSize
                , expireAfterWrite
        );
    }

}
