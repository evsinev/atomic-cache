package com.atomiccache.test.lock.example;

import com.atomiccache.api.lock.IAtomicLock;

import java.util.concurrent.locks.Lock;

public class AtomicLockNaiveImpl<K> implements IAtomicLock {
    private final Lock lock;

    public AtomicLockNaiveImpl(Lock lock) {
        this.lock = lock;
    }
    @Override
    public void unlock() {
        lock.unlock();
    }
}
