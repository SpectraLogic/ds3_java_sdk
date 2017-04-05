/*
 * ******************************************************************************
 *   Copyright 2014-2017 Spectra Logic Corporation. All Rights Reserved.
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

import com.spectralogic.ds3client.models.BulkObject;
import com.spectralogic.ds3client.models.Objects;

import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;

public class JobState {
    private final AtomicInteger objectsRemaining;
    private final JobPartTracker partTracker;

    public JobState(final Collection<Objects> filteredChunks,final JobPartTracker partTracker) {
        this.objectsRemaining = new AtomicInteger(getObjectCount(filteredChunks));
        this.partTracker = partTracker.attachObjectCompletedListener(new ObjectCompletedListenerImpl());
    }
    
    public boolean hasObjects() {
        return this.objectsRemaining.get() > 0;
    }

    private static int getObjectCount(final Collection<Objects> chunks) {
        final HashSet<String> result = new HashSet<>();
        for (final Objects chunk : chunks) {
            for (final BulkObject bulkObject : chunk.getObjects()) {
                result.add(bulkObject.getName());
            }
        }
        return result.size();
    }

    private final class ObjectCompletedListenerImpl implements ObjectCompletedListener {
        @Override
        public void objectCompleted(final String name) {
            JobState.this.objectsRemaining.decrementAndGet();
        }
    }

    @Override
    public String toString() {
        return "JobState{" +
                "objectsRemaining=" + objectsRemaining +
                ", partTracker=" + partTracker +
                '}';
    }
}
