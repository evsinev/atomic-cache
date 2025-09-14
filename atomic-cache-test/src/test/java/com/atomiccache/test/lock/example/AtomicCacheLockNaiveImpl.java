package com.atomiccache.test.lock.example;

import com.atomiccache.api.lock.IAtomicCacheLock;
import com.atomiccache.api.lock.IAtomicLock;

import java.time.Duration;
import java.util.concurrent.locks.ReentrantLock;

public class AtomicCacheLockNaiveImpl<K> implements IAtomicCacheLock<K> {

    private final ReentrantLock lock = new ReentrantLock();

    @Override
    public IAtomicLock lock(K aKey, Duration aDuration) {
        lock.lock();
        return new AtomicLockNaiveImpl<>(lock);
    }
}
