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

import com.spectralogic.ds3client.models.BlobApiBean;
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
                new BlobApiBean(null, false, true, 10L, "foo", 0L, null, 1),
                new BlobApiBean(null, false, true, 13L, "bar", 0L, null, 1),
                new BlobApiBean(null, true, true, 11L, "foo", 10L, null, 1),
                new BlobApiBean(null, true, true, 12L, "baz", 0L, null, 1)
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
