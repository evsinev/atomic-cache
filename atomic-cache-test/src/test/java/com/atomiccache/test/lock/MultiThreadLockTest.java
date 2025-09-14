package com.atomiccache.test.lock;

import com.atomiccache.api.lock.IAtomicCacheLock;
import com.atomiccache.api.lock.IAtomicLock;
import com.atomiccache.test.cache.example.model.ExampleKey;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class MultiThreadLockTest extends BaseLockTest {

    private static final Logger LOG = LoggerFactory.getLogger( MultiThreadLockTest.class );

    public MultiThreadLockTest(IAtomicCacheLock<ExampleKey> atomicCacheLock) {
        super(atomicCacheLock);
    }

    @Test
    public void multiThreadedLocking() throws InterruptedException {
        int threadCount = 5;
        int sleepMs     = 500;

        try (ExecutorService executor = Executors.newFixedThreadPool(threadCount)) {
            CountDownLatch  latch    = new CountDownLatch(threadCount);

            for (int i = 0; i < threadCount; i++) {
                final int threadId = i;
                executor.execute(() -> {
                    ExampleKey key = new ExampleKey(1, "type-1");

                    try (IAtomicLock _lock = atomicCacheLock.lock(key, Duration.ofSeconds(1))) {
                        LOG.debug("Thread {} acquired lock on key: type-{}", threadId, threadId);
                        Thread.sleep(500); // Simulate some work
                    } catch (Exception e) {
                        LOG.error("Thread {} failed to acquire lock.", threadId, e);
                    } finally {
                        LOG.debug("Thread {} released lock.", threadId);
                        latch.countDown();
                    }
                });
            }

            long startedTime = System.nanoTime();
            assertThat(latch.await(threadCount * 2 * sleepMs, MILLISECONDS)).isTrue();
            long endTime = System.nanoTime();

            assertThat(endTime - startedTime)
                    .isGreaterThanOrEqualTo(MILLISECONDS.toNanos((threadCount - 1) * sleepMs));
        }

    }


}
