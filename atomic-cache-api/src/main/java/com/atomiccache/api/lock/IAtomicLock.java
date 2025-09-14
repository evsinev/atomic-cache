package com.atomiccache.api.lock;

import java.io.Closeable;

public interface IAtomicLock extends Closeable {

    void unlock();

    @Override
    default void close() {
        unlock();
    }
}
