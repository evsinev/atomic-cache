package com.atomiccache.test.lock;

import com.atomiccache.api.lock.IAtomicCacheLock;
import com.atomiccache.guava.AtomicCacheLockGuavaStripedBuilder;
import com.atomiccache.test.cache.example.model.ExampleKey;
import com.atomiccache.test.lock.example.AtomicCacheLockNaiveImpl;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.List;

import static com.atomiccache.test.cache.cassandra.CassandraInstance.CASSANDRA_CACHE_LOCK;

public abstract class BaseLockTest {

    private static final IAtomicCacheLock<ExampleKey> GUAVA_LOCK = new AtomicCacheLockGuavaStripedBuilder<ExampleKey>()
            .build();

    final IAtomicCacheLock<ExampleKey> atomicCacheLock;

    public BaseLockTest(IAtomicCacheLock<ExampleKey> atomicCacheLock) {
        this.atomicCacheLock = atomicCacheLock;
    }

    @Parameterized.Parameters
    public static Collection<IAtomicCacheLock<ExampleKey>> parameters() {
        return List.of(
                new AtomicCacheLockNaiveImpl<>()
                , GUAVA_LOCK
                , CASSANDRA_CACHE_LOCK
        );
    }

}
