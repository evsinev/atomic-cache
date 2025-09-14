package com.atomiccache.cassandra.impl.lock;

import org.slf4j.Logger;

import java.time.Duration;

public class ThreadCondition {

    private final Duration sleep;
    private final Logger   log;

    public ThreadCondition(Duration sleep, Logger aLogger) {
        this.sleep = sleep;
        log        = aLogger;
    }

    public boolean awaitCanRun() {
        if (Thread.currentThread().isInterrupted()) {
            return false;
        }

        try {
            Thread.sleep(sleep.toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
            log.debug("Interrupted sleep", e);
        }

        return !Thread.currentThread().isInterrupted();
    }
}
