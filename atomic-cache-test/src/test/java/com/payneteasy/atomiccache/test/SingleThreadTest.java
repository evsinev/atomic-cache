package com.payneteasy.atomiccache.test;

import com.payneteasy.atomiccache.api.IAtomicCache;
import com.payneteasy.atomiccache.example.ExampleFetch;
import com.payneteasy.atomiccache.example.model.ExampleKey;
import com.payneteasy.atomiccache.example.model.ExampleValue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class SingleThreadTest extends BaseTest {

    public SingleThreadTest(IAtomicCache<ExampleKey, ExampleValue> update) {
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
