package com.payneteasy.atomiccache.cassandra;

import com.payneteasy.atomiccache.api.IAtomicCache;
import com.payneteasy.atomiccache.example.model.ExampleKey;
import com.payneteasy.atomiccache.example.model.ExampleValue;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CassandraTest {

    @Test
    public void test() {
        IAtomicCache<ExampleKey, ExampleValue> cache = CassandraInstance.CASSANDRA_CACHE;

        ExampleValue value = cache.getOrFetch(new ExampleKey(1, "TOKEN"), key -> new ExampleValue("token-1", "ACCESS_TOKEN"));
        assertThat(value).isNotNull();
    }
}
