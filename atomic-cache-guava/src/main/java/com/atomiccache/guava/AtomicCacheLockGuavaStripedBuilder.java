package com.atomiccache.guava;

import com.atomiccache.api.lock.IAtomicCacheLock;
import com.atomiccache.guava.impl.AtomicCacheLockGuavaStripedImpl;

public class AtomicCacheLockGuavaStripedBuilder<K> {

    private int maxSize = 100;

    public AtomicCacheLockGuavaStripedBuilder<K> maxSize(int maxSize) {
        this.maxSize = maxSize;
        return this;
    }

    public IAtomicCacheLock<K> build() {
        return new AtomicCacheLockGuavaStripedImpl<>(maxSize);
    }
}
