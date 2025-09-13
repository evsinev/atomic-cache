package com.payneteasy.atomiccache.test;

import com.payneteasy.atomiccache.api.IAtomicCache;
import com.payneteasy.atomiccache.cassandra.CassandraInstance;
import com.payneteasy.atomiccache.concurrent.AtomicCacheConcurrentImpl;
import com.payneteasy.atomiccache.example.model.ExampleKey;
import com.payneteasy.atomiccache.example.model.ExampleValue;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.List;

public abstract class BaseTest {

    final IAtomicCache<ExampleKey, ExampleValue> cache;

    public BaseTest(IAtomicCache<ExampleKey, ExampleValue> cache) {
        this.cache = cache;
    }

    @Parameterized.Parameters
    public static Collection<IAtomicCache<ExampleKey, ExampleValue>> parameters() {
        return List.of(
                  new AtomicCacheConcurrentImpl<>()
                , CassandraInstance.CASSANDRA_CACHE
        );
    }

}
