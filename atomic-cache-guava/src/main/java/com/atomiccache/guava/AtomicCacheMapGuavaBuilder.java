package com.atomiccache.guava;

import com.atomiccache.api.map.IAtomicCacheMap;
import com.atomiccache.guava.impl.AtomicCacheMapGuavaImpl;

import java.time.Duration;

public class AtomicCacheMapGuavaBuilder<K, V> {

    private int      maxSize          = 10_000;
    private Duration expireAfterWrite = Duration.ofHours(1);

    public AtomicCacheMapGuavaBuilder<K, V> maxSize(int maxSize) {
        this.maxSize = maxSize;
        return this;
    }

    public AtomicCacheMapGuavaBuilder<K, V> expireAfterWrite(Duration expireAfterWrite) {
        this.expireAfterWrite = expireAfterWrite;
        return this;
    }

    public IAtomicCacheMap<K, V> build() {
        return new AtomicCacheMapGuavaImpl<>(
                maxSize
                , expireAfterWrite
        );
    }

}
