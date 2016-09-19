package com.spectralogic.ds3client;

import com.spectralogic.ds3client.helpers.events.EventRunner;

public class SingleThreadedEventRunner implements EventRunner {
    @Override
    public void emitEvent(final Runnable runnable) {
        runnable.run();
    }
}
