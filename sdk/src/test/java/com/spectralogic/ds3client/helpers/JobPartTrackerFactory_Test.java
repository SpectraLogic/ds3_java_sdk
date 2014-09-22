/*
 * ******************************************************************************
 *   Copyright 2014 Spectra Logic Corporation. All Rights Reserved.
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

import com.spectralogic.ds3client.models.bulk.BulkObject;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class JobPartTrackerFactory_Test {
    @Test
    public void createdTrackerTracksParts() {
        final JobPartTracker tracker = JobPartTrackerFactory.buildPartTracker(Arrays.asList(
            new BulkObject("foo", 10L, false, 0L),
            new BulkObject("bar", 13L, false, 0L),
            new BulkObject("foo", 11L, true, 10L),
            new BulkObject("baz", 12L, true, 0L)
        ));

        final List<Long> transfers = new ArrayList<>();
        final List<String> objects = new ArrayList<>();
        tracker.attachDataTransferredListener(new DataTransferredListener() {
            @Override
            public void dataTransferred(final long size) {
                transfers.add(size);
            }
        });
        tracker.attachObjectCompletedListener(new ObjectCompletedListener() {
            @Override
            public void objectCompleted(final String name) {
                objects.add(name);
            }
        });

        tracker.completePart("baz", new ObjectPart(0L, 12L));
        tracker.completePart("foo", new ObjectPart(10L, 11L));
        tracker.completePart("bar", new ObjectPart(0L, 13L));
        tracker.completePart("foo", new ObjectPart(0L, 10L));

        assertThat(transfers, is(Arrays.asList(12L, 11L, 13L, 10L)));
        assertThat(objects, is(Arrays.asList("baz", "bar", "foo")));
    }
}
