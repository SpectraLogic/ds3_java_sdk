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

import org.junit.Test;

import java.security.InvalidParameterException;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class ObjectPartTrackerImpl_Test {
    @Test
    public void invalidPartsFail() {
        try {
            new ObjectPartTrackerImpl("foo", Arrays.asList(
                new ObjectPart(0L, 100L),
                new ObjectPart(99L, 100L)
            ));
            fail();
        } catch (final InvalidParameterException e) {
        }
    }

    @Test
    public void containsPartWorks() {
        final ObjectPartTracker tracker = new ObjectPartTrackerImpl("foo", Arrays.asList(new ObjectPart(0L, 100L), new ObjectPart(100L, 100L)));
        assertThat(tracker.containsPart(new ObjectPart(100L, 100L)), is(true));
        assertThat(tracker.containsPart(new ObjectPart(100L, 50L)), is(false));
        assertThat(tracker.containsPart(new ObjectPart(150L, 50L)), is(false));
    }
}
