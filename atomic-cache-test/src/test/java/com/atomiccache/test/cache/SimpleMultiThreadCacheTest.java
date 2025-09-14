package com.atomiccache.test.cache;

import com.atomiccache.api.cache.IAtomicCache;
import com.atomiccache.test.cache.example.ExampleFetch;
import com.atomiccache.test.cache.example.model.ExampleKey;
import com.atomiccache.test.cache.example.model.ExampleValue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class SimpleMultiThreadCacheTest extends BaseCacheTest {

    public SimpleMultiThreadCacheTest(IAtomicCache<ExampleKey, ExampleValue> update) {
        super(update);
    }

    @Test
    public void multi_thread() throws InterruptedException {
        ExampleFetch fetch = new ExampleFetch();

        List<Thread>                     threads = new ArrayList<>();
        ArrayBlockingQueue<ExampleValue> queue   = new ArrayBlockingQueue<>(20);
        for (int i = 0; i < 10; i++) {
            threads.add(new Thread(() -> {
                ExampleValue value = cache.getOrFetch(new ExampleKey(1, "type-1"), fetch);
                queue.add(value);
                assertThat(value)
                        .isNotNull()
                        .isEqualTo(new ExampleValue("name-1", "type-1"));
            }));
        }

        threads.forEach(Thread::start);

        for (Thread thread : threads) {
            thread.join(Duration.ofSeconds(2));
        }

        assertThat(fetch.getCounter()).isEqualTo(1);

        assertThat(queue.size()).isEqualTo(10);
        for (ExampleValue value : queue) {
            assertThat(value).isEqualTo(new ExampleValue("name-1", "type-1"));
        }
    }

}
