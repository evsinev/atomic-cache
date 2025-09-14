package com.atomiccache.test.map;

import com.atomiccache.api.map.IAtomicCacheMap;
import com.atomiccache.test.cache.example.model.ExampleKey;
import com.atomiccache.test.cache.example.model.ExampleValue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.time.Duration;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class SingleThreadMapTest extends BaseMapTest {

    public SingleThreadMapTest(IAtomicCacheMap<ExampleKey, ExampleValue> map) {
        super(map);
    }

    @Test
    public void test() {
        ExampleKey   key_1   = new ExampleKey(1, "type-1");
        ExampleValue value_1 = new ExampleValue("name-1", "type-1");
        Duration     ttl     = Duration.ofSeconds(30);

        {
            Optional<ExampleValue> value = map.get(key_1);
            assertThat(value.isPresent()).isFalse();
        }

        {
            Optional<ExampleValue> maybePrevValue = map.getAndPut(key_1, value_1, ttl);
            assertThat(maybePrevValue.isPresent()).isFalse();
        }

        {
            Optional<ExampleValue> value = map.get(key_1);
            assertThat(value.isPresent()).isTrue();
            assertThat(value.get()).isEqualTo(value_1);
        }

        {
            Optional<ExampleValue> maybePrevValue = map.getAndPut(key_1, value_1, ttl);
            assertThat(maybePrevValue.isPresent()).isTrue();
            assertThat(maybePrevValue.get()).isEqualTo(value_1);
        }

        {
            Optional<ExampleValue> remove = map.getAndRemove(key_1);
            assertThat(remove.isPresent()).isTrue();
            assertThat(remove.get()).isEqualTo(value_1);
        }

        {
            Optional<ExampleValue> remove = map.getAndRemove(key_1);
            assertThat(remove.isEmpty()).isTrue();
        }

        {
            Optional<ExampleValue> value = map.get(key_1);
            assertThat(value.isPresent()).isFalse();
        }
    }
}
