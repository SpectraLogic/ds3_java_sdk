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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static com.spectralogic.ds3client.utils.Guard.isMapNullOrEmpty;

public class JobPartTrackerImpl implements JobPartTracker {

    static private final Logger LOG = LoggerFactory.getLogger(JobPartTrackerImpl.class);
    private final Map<String, ObjectPartTracker> trackers;

    public JobPartTrackerImpl(final Map<String, ObjectPartTracker> trackers) {
        this.trackers = trackers;
    }

    public void completePart(final String key, final ObjectPart objectPart) {
        if (isMapNullOrEmpty(this.trackers) || !this.trackers.containsKey(key)) {
            throw new IllegalArgumentException("Trackers does not contain specified Object Part Tracker: " + key);
        }
        trackers.get(key).completePart(objectPart);
    }

    public boolean containsPart(final String key, final ObjectPart objectPart) {
        if (isMapNullOrEmpty(this.trackers) || !this.trackers.containsKey(key)) {
            throw new IllegalArgumentException("Trackers does not contain specified Object Part Tracker: " + key);
        }
        return trackers.get(key).containsPart(objectPart);
    }

    public JobPartTracker attachDataTransferredListener(final DataTransferredListener listener) {
        if (this.trackers == null) {
            LOG.error("Trackers is null: cannot attach Data Transferred Listener");
            return this;
        }
        for (final ObjectPartTracker tracker : this.trackers.values()) {
            tracker.attachDataTransferredListener(listener);
        }
        return this;
    }

    public JobPartTracker attachObjectCompletedListener(final ObjectCompletedListener listener) {
        if (this.trackers == null) {
            LOG.error("Trackers is null: cannot attach Object Completed Listener");
            return this;
        }
        for (final ObjectPartTracker tracker : this.trackers.values()) {
            tracker.attachObjectCompletedListener(listener);
        }
        return this;
    }

    public void removeDataTransferredListener(final DataTransferredListener listener) {
        if (this.trackers == null) {
            LOG.error("Trackers is null: cannot remove Data Transferred Listener");
            return;
        }
        for (final ObjectPartTracker tracker : this.trackers.values()) {
            tracker.removeDataTransferredListener(listener);
        }
    }

    public void removeObjectCompletedListener(final ObjectCompletedListener listener) {
        if (this.trackers == null) {
            LOG.error("Trackers is null: cannot remove Object Completed Listener");
            return;
        }
        for (final ObjectPartTracker tracker : this.trackers.values()) {
            tracker.removeObjectCompletedListener(listener);
        }
    }
}
