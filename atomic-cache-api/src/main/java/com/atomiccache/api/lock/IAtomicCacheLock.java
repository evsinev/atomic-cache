package com.atomiccache.api.lock;

import java.time.Duration;

public interface IAtomicCacheLock<K> {

    IAtomicLock lock(K aKey, Duration aDuration);

}
