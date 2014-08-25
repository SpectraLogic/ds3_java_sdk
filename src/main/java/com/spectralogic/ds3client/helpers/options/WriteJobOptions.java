package com.spectralogic.ds3client.helpers.options;

import com.spectralogic.ds3client.models.bulk.Priority;
import com.spectralogic.ds3client.models.bulk.WriteOptimization;

public class WriteJobOptions {
    private Priority priority;
    private WriteOptimization writeOptimization;

    private WriteJobOptions() {
        this.priority = null;
        this.writeOptimization = null;
    }

    public static WriteJobOptions create() {
        return new WriteJobOptions();
    }

    public WriteJobOptions withWriteOptimization(final WriteOptimization writeOptimization) {
        this.writeOptimization = writeOptimization;
        return this;
    }

    public WriteOptimization getWriteOptimization() {
        return writeOptimization;
    }

    public void setWriteOptimization(final WriteOptimization writeOptimization) {
        this.writeOptimization = writeOptimization;
    }

    public WriteJobOptions withPriority(final Priority priority) {
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
