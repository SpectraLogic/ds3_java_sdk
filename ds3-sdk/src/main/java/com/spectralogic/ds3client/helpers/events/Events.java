package com.spectralogic.ds3client.helpers.events;

public final class Events {
    public static final EventRunner DEFAULT_EVENT_RUNNER = new ConcurrentEventRunner();

    private static EventRunner runner = DEFAULT_EVENT_RUNNER;

    public static void emitEvent(final Runnable runnable) {
        runner.emitEvent(runnable);
    }

    public static void setEventRunner(final EventRunner eventRunner) {
        runner = eventRunner;
    }
}
