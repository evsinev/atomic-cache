package com.atomiccache.test.cache;

import com.atomiccache.api.cache.IAtomicCache;
import com.atomiccache.test.cache.cassandra.CassandraInstance;
import com.atomiccache.test.cache.example.AtomicCacheNaiveImpl;
import com.atomiccache.test.cache.example.model.ExampleKey;
import com.atomiccache.test.cache.example.model.ExampleValue;
import com.atomiccache.test.cache.guava.GuavaInstance;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.List;

public abstract class BaseCacheTest {

    final IAtomicCache<ExampleKey, ExampleValue> cache;

    public BaseCacheTest(IAtomicCache<ExampleKey, ExampleValue> cache) {
        this.cache = cache;
    }

    @Parameterized.Parameters
    public static Collection<IAtomicCache<ExampleKey, ExampleValue>> parameters() {
        return List.of(
                new AtomicCacheNaiveImpl<>()
                , GuavaInstance.GUAVA_CACHE
                , CassandraInstance.CASSANDRA_CACHE
        );
    }

}
