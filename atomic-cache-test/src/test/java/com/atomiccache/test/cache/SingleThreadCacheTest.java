package com.atomiccache.test.cache;

import com.atomiccache.api.cache.IAtomicCache;
import com.atomiccache.test.cache.example.ExampleFetch;
import com.atomiccache.test.cache.example.model.ExampleKey;
import com.atomiccache.test.cache.example.model.ExampleValue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class SingleThreadCacheTest extends BaseCacheTest {

    public SingleThreadCacheTest(IAtomicCache<ExampleKey, ExampleValue> update) {
        super(update);
    }

    @Test
    public void single_thread() {
        ExampleFetch fetch = new ExampleFetch();

        assertThat(cache.getOrFetch(new ExampleKey(2, "type-1"), fetch))
                .isNotNull()
                .isEqualTo(new ExampleValue("name-2", "type-1"));

        assertThat(cache.getOrFetch(new ExampleKey(2, "type-1"), fetch))
                .isNotNull()
                .isEqualTo(new ExampleValue("name-2", "type-1"));

        assertThat(fetch.getCounter()).isEqualTo(1);
    }

}
