package com.spectralogic.ds3client.helpers.events;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ConcurrentEventRunner implements EventRunner {

    private final static Executor EVENTS_EXECUTOR = Executors.newFixedThreadPool(4);
    @Override
    public void emitEvent(final Runnable runnable) {
        EVENTS_EXECUTOR.execute(runnable);
    }
}
