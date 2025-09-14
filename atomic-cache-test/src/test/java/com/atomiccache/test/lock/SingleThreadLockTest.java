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

@RunWith(Parameterized.class)
public class SingleThreadLockTest extends BaseLockTest {

    private static final Logger LOG = LoggerFactory.getLogger( SingleThreadLockTest.class );

    public SingleThreadLockTest(IAtomicCacheLock<ExampleKey> atomicCacheLock) {
        super(atomicCacheLock);
    }

    @Test
    public void test() {
        try (IAtomicLock lock = atomicCacheLock.lock(new ExampleKey(1, "type-1"), Duration.ofSeconds(1))) {
            LOG.debug( "Locked" );
        }
    }
}
