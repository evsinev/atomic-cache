package com.atomiccache.test.cache.example;

import com.atomiccache.api.cache.IAtomicCacheFetch;
import com.atomiccache.test.cache.example.model.ExampleKey;
import com.atomiccache.test.cache.example.model.ExampleValue;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class ExampleFetch implements IAtomicCacheFetch<ExampleKey, ExampleValue> {

    private static final Logger LOG = LoggerFactory.getLogger( ExampleFetch.class );

    private final AtomicInteger counter = new AtomicInteger();

    @Override
    public @NonNull ExampleValue fetchValue(@NonNull ExampleKey key) throws Exception {
        counter.incrementAndGet();
        LOG.debug("Sleeping 1 seconds. key: {}", key);
        Thread.sleep(250);
        return new ExampleValue("name-" + key.getId(), key.getType());
    }

    public int getCounter() {
        return counter.get();
    }
}
