package com.atomiccache.test.cache.guava;

import com.atomiccache.api.cache.IAtomicCache;
import com.atomiccache.guava.AtomicCacheGuavaBuilder;
import com.atomiccache.test.cache.example.model.ExampleKey;
import com.atomiccache.test.cache.example.model.ExampleValue;

public class GuavaInstance {

    public static final IAtomicCache<ExampleKey, ExampleValue> GUAVA_CACHE;

    static {
        GUAVA_CACHE = new AtomicCacheGuavaBuilder<ExampleKey, ExampleValue>()
                .build();
    }

}
