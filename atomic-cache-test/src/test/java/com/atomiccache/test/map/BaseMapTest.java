package com.atomiccache.test.map;

import com.atomiccache.api.map.IAtomicCacheMap;
import com.atomiccache.guava.AtomicCacheMapGuavaBuilder;
import com.atomiccache.test.cache.example.model.ExampleKey;
import com.atomiccache.test.cache.example.model.ExampleValue;
import com.atomiccache.test.map.example.AtomicCacheMapNaiveImpl;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.List;

import static com.atomiccache.test.cache.cassandra.CassandraInstance.CASSANDRA_CACHE_MAP;

public abstract class BaseMapTest {


    final IAtomicCacheMap<ExampleKey, ExampleValue> map;

    public BaseMapTest(IAtomicCacheMap<ExampleKey, ExampleValue> map) {
        this.map = map;
    }

    @Parameterized.Parameters
    public static Collection<IAtomicCacheMap<ExampleKey, ExampleValue>> parameters() {
        return List.of(
                new AtomicCacheMapNaiveImpl<>()
                , new AtomicCacheMapGuavaBuilder<ExampleKey, ExampleValue>().build()
                , CASSANDRA_CACHE_MAP
        );
    }

}
