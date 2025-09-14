package com.atomiccache.guava.impl;

import com.atomiccache.api.lock.IAtomicCacheLock;
import com.atomiccache.api.lock.IAtomicLock;
import com.google.common.util.concurrent.Striped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.locks.Lock;

public class AtomicCacheLockGuavaStripedImpl<K> implements IAtomicCacheLock<K> {

    private static final Logger LOG = LoggerFactory.getLogger( AtomicCacheLockGuavaStripedImpl.class );

    private final Striped<Lock> striped;

    public AtomicCacheLockGuavaStripedImpl(int aSize) {
        striped = Striped.lazyWeakLock(aSize);
    }

    @Override
    public IAtomicLock lock(K aKey, Duration aDuration) {
        Lock lock = striped.get(aKey);
        LOG.debug("Locking key: {}, lock: {}", aKey, lock);
        lock.lock();
        return lock::unlock;
    }
}
