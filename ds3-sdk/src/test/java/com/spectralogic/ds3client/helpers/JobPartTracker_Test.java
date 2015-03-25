/*
 * ******************************************************************************
 *   Copyright 2014-2015 Spectra Logic Corporation. All Rights Reserved.
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

import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class JobPartTracker_Test {
    @Test
    public void trackerCallsTrackers() {
        final MockObjectPartTracker fooTracker = new MockObjectPartTracker("foo", new ArrayList<>(Arrays.asList(false, true)));
        final MockObjectPartTracker barTracker = new MockObjectPartTracker("bar", new ArrayList<>(Arrays.asList(true, false)));
        final Map<String, ObjectPartTracker> trackers = new HashMap<>();
        trackers.put("foo", fooTracker);
        trackers.put("bar", barTracker);
        final JobPartTracker jobPartTracker = new JobPartTracker(trackers);

        final ObjectPart[] partsRemoved = {
            new ObjectPart(10, 11),
            new ObjectPart(12, 13)
        };
        jobPartTracker.completePart("foo", partsRemoved[0]);
        jobPartTracker.completePart("foo", partsRemoved[1]);

        final ObjectPart[] partsChecked = {
            new ObjectPart(14, 15),
            new ObjectPart(16, 17)
        };
        assertThat(jobPartTracker.containsPart("foo", partsChecked[0]), is(false));
        assertThat(jobPartTracker.containsPart("foo", partsChecked[1]), is(true));

        assertThat(fooTracker.getPartsRemoved(), is((Collection<ObjectPart>)Arrays.asList(partsRemoved)));
        assertThat(fooTracker.getPartsChecked(), is((Collection<ObjectPart>)Arrays.asList(partsChecked)));
        
        assertThat(barTracker.getPartsRemoved().isEmpty(), is(true));
        assertThat(barTracker.getPartsChecked().isEmpty(), is(true));
    }

    @Test
    public void TrackerEventsForward()
    {
        final MockObjectPartTracker fooTracker = new MockObjectPartTracker("foo", Arrays.asList(false, true));
        final MockObjectPartTracker barTracker = new MockObjectPartTracker("bar", Arrays.asList(true, false));
        final Map<String, ObjectPartTracker> trackers = new HashMap<>();
        trackers.put("foo", fooTracker);
        trackers.put("bar", barTracker);

        final List<Long> sizes = new ArrayList<>();
        final List<String> objects = new ArrayList<>();

        final JobPartTracker jobPartTracker = new JobPartTracker(trackers);
        jobPartTracker.attachDataTransferredListener(new DataTransferredListener() {
            @Override
            public void dataTransferred(final long size) {
                sizes.add(size);
            }
        });
        jobPartTracker.attachObjectCompletedListener(new ObjectCompletedListener() {
            @Override
            public void objectCompleted(final String name) {
                objects.add(name);
            }
        });

        fooTracker.onDataTransferred(10);
        barTracker.onDataTransferred(11);
        fooTracker.onDataTransferred(12);

        barTracker.onCompleted();
        fooTracker.onCompleted();

        assertThat(sizes, is(Arrays.asList(10L, 11L, 12L)));
        assertThat(objects, is(Arrays.asList("bar", "foo")));
    }

    private static class MockObjectPartTracker implements ObjectPartTracker {
        private final String objectName;
        private final List<Boolean> _containsPartResponses;
        private final List<DataTransferredListener> dataTransferredListeners = new ArrayList<>();
        private final List<ObjectCompletedListener> objectCompletedListeners = new ArrayList<>();
        private final List<ObjectPart> _partsRemoved = new ArrayList<>();
        private final List<ObjectPart> _partsChecked = new ArrayList<>();

        public MockObjectPartTracker(final String objectName, final List<Boolean> containsPartResponses) {
            this.objectName = objectName;
            this._containsPartResponses = containsPartResponses;
        }

        public Collection<ObjectPart> getPartsRemoved() {
            return this._partsRemoved;
        }

        public Collection<ObjectPart> getPartsChecked() {
            return this._partsChecked;
        }

        public void onDataTransferred(final long size)
        {
            for (final DataTransferredListener listener : this.dataTransferredListeners) {
                listener.dataTransferred(size);
            }
        }

        public void onCompleted()
        {
            for (final ObjectCompletedListener listener : this.objectCompletedListeners) {
                listener.objectCompleted(this.objectName);
            }
        }

        @Override
        public ObjectPartTracker attachDataTransferredListener(final DataTransferredListener listener) {
            this.dataTransferredListeners.add(listener);
            return this;
        }

        @Override
        public ObjectPartTracker attachObjectCompletedListener(final ObjectCompletedListener listener) {
            this.objectCompletedListeners.add(listener);
            return this;
        }

        @Override
        public void completePart(final ObjectPart partToRemove)
        {
            this._partsRemoved.add(partToRemove);
        }

        @Override
        public boolean containsPart(final ObjectPart part)
        {
            this._partsChecked.add(part);
            return this._containsPartResponses.remove(0);
        }
    }
}
