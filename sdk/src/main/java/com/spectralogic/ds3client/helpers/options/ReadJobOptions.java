package com.spectralogic.ds3client.helpers.options;

import com.spectralogic.ds3client.models.bulk.Priority;

public class ReadJobOptions {

    private Priority priority;

    private ReadJobOptions() {}

    public static ReadJobOptions create() {
        return new ReadJobOptions();
    }

    public ReadJobOptions withPriority(final Priority priority) {
        this.priority = priority;
        return this;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(final Priority priority) {
        this.priority = priority;
    }
}
