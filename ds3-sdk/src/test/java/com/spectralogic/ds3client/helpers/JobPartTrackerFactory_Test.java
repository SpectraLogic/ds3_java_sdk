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

import com.spectralogic.ds3client.SingleThreadedEventRunner;
import com.spectralogic.ds3client.helpers.events.Events;
import com.spectralogic.ds3client.models.BulkObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class JobPartTrackerFactory_Test {

    @BeforeClass
    public static void setup() {
        Events.setEventRunner(new SingleThreadedEventRunner());
    }

    @AfterClass
    public static void teardown() {
        Events.setEventRunner(Events.DEFAULT_EVENT_RUNNER);
    }

    @Test
    public void createdTrackerTracksParts() {
        final BulkObject part1 = new BulkObject();
        part1.setName("foo");
        part1.setLength(10L);
        part1.setInCache(false);
        part1.setOffset(0L);

        final BulkObject part2 = new BulkObject();
        part2.setName("bar");
        part2.setLength(13L);
        part2.setInCache(false);
        part2.setOffset(0L);

        final BulkObject part3 = new BulkObject();
        part3.setName("foo");
        part3.setLength(11L);
        part3.setInCache(true);
        part3.setOffset(10L);

        final BulkObject part4 = new BulkObject();
        part4.setName("baz");
        part4.setLength(12L);
        part4.setInCache(true);
        part4.setOffset(0L);

        final JobPartTracker tracker = JobPartTrackerFactory.buildPartTracker(Arrays.asList(
                part1, part2, part3, part4
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
