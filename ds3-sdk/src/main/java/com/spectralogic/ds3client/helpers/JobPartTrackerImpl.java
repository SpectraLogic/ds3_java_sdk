package com.spectralogic.ds3client.helpers;

import java.util.Map;

public class JobPartTrackerImpl implements JobPartTracker {

    private final Map<String, ObjectPartTracker> trackers;

    public JobPartTrackerImpl(final Map<String, ObjectPartTracker> trackers) {
        this.trackers = trackers;
    }

    public void completePart(final String key, final ObjectPart objectPart) {
        trackers.get(key).completePart(objectPart);
    }

    public boolean containsPart(final String key, final ObjectPart objectPart) {
        return trackers.get(key).containsPart(objectPart);
    }

    public JobPartTracker attachDataTransferredListener(final DataTransferredListener listener) {
        for (final ObjectPartTracker tracker : this.trackers.values()) {
            tracker.attachDataTransferredListener(listener);
        }
        return this;
    }

    public JobPartTracker attachObjectCompletedListener(final ObjectCompletedListener listener) {
        for (final ObjectPartTracker tracker : this.trackers.values()) {
            tracker.attachObjectCompletedListener(listener);
        }
        return this;
    }

    public void removeDataTransferredListener(final DataTransferredListener listener) {
        for (final ObjectPartTracker tracker : this.trackers.values()) {
            tracker.removeDataTransferredListener(listener);
        }
    }

    public void removeObjectCompletedListener(final ObjectCompletedListener listener) {
        for (final ObjectPartTracker tracker : this.trackers.values()) {
            tracker.removeObjectCompletedListener(listener);
        }
    }
}
