/*
 * ******************************************************************************
 *   Copyright 2014-2016 Spectra Logic Corporation. All Rights Reserved.
 *   Licensed under the Apache License, Version 2.0 (the "License"). You may not use
 *   this file except in compliance with the License. A copy of the License is located at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   or in the "license" file accompanying this file.
 *   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 *   CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 * ****************************************************************************
 */

package com.spectralogic.ds3client.helpers;

import java.util.Map;

public class JobPartTrackerImpl implements JobPartTracker {

    private final Map<String, ObjectPartTracker> trackers;

    public JobPartTrackerImpl(final Map<String, ObjectPartTracker> trackers) {
        if (trackers == null) {
            throw new IllegalArgumentException("The trackers parameter cannot be null");
        }
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

    @Override
    public String toString() {
        return "JobPartTrackerImpl{" +
                "trackers=" + trackers.keySet() +
                '}';
    }
}
