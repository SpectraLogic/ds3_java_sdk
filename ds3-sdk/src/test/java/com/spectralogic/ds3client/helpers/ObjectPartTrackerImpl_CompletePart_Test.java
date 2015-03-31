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

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class ObjectPartTrackerImpl_CompletePart_Test {
    private final String name;
    private final boolean shouldSucceed;
    private final Collection<ObjectPart> parts;

    public ObjectPartTrackerImpl_CompletePart_Test(final String name, final Boolean shouldSucceed, final Collection<ObjectPart> parts) {
        this.name = name;
        this.shouldSucceed = shouldSucceed;
        this.parts = parts;
    }
    
    @Parameters(name = "{0} should succeed == {1}")
    public static Collection<Object[]> testCases() {
        return Arrays.asList(
            new Object[] { "entire part", true, Arrays.asList(
                new ObjectPart(100L, 100L),
                new ObjectPart(0L, 100L),
                new ObjectPart(200L, 100L)
            ) }, 
            new Object[] { "first then second part", true, Arrays.asList(
                new ObjectPart(100L, 75L),
                new ObjectPart(175L, 25L),
                new ObjectPart(0L, 100L),
                new ObjectPart(200L, 100L)
            ) },
            new Object[] { "second then first part", true, Arrays.asList(
                new ObjectPart(175L, 25L),
                new ObjectPart(100L, 75L),
                new ObjectPart(0L, 100L),
                new ObjectPart(200L, 100L)
            ) },
            new Object[] { "middle then first then second part", true, Arrays.asList(
                new ObjectPart(125L, 50L),
                new ObjectPart(100L, 25L),
                new ObjectPart(175L, 25L),
                new ObjectPart(0L, 100L),
                new ObjectPart(200L, 100L)
            ) },
            new Object[] { "completely before", false, Arrays.asList(new ObjectPart(0L, 99L)) },
            new Object[] { "just before", false, Arrays.asList(new ObjectPart(0L, 100L)) },
            new Object[] { "overlapping before", false, Arrays.asList(new ObjectPart(50L, 100L)) },
            new Object[] { "overlapping both", false, Arrays.asList(new ObjectPart(50L, 200L)) },
            new Object[] { "overlapping after", false, Arrays.asList(new ObjectPart(150L, 100L)) },
            new Object[] { "just after", false, Arrays.asList(new ObjectPart(200L, 100L)) },
            new Object[] { "completely after", false, Arrays.asList(new ObjectPart(201L, 99L)) }
        );
    }
    
    @Test
    public void completePartFiresExpectedEvents() {
        if (this.shouldSucceed) {
            checkSuccess();
        } else {
            checkFailure();
        }
    }

    private void checkSuccess() {
        final ObjectPartTracker tracker = new ObjectPartTrackerImpl(this.name, Arrays.asList(
            new ObjectPart(0L, 100L),
            new ObjectPart(100L, 100L),
            new ObjectPart(200L, 100L)
        ));
        final List<Object> events = new ArrayList<>();
        tracker.attachDataTransferredListener(new DataTransferredListener() {
            @Override
            public void dataTransferred(final long size) {
                events.add(size);
            }
        });
        tracker.attachObjectCompletedListener(new ObjectCompletedListener() {
            @Override
            public void objectCompleted(final String name) {
                events.add(name);
            }
        });
        for (final ObjectPart part : this.parts) {
            tracker.completePart(part);
        }
        assertThat(events.toArray(), is(equalTo(buildExpectedEvents().toArray())));
    }

    private Collection<Object> buildExpectedEvents() {
        final Collection<Object> expectedEvents = new ArrayList<>();
        for (final ObjectPart part : this.parts) {
            expectedEvents.add(part.getLength());
        }
        expectedEvents.add(this.name);
        return expectedEvents;
    }

    private void checkFailure() {
        final ObjectPartTracker tracker = new ObjectPartTrackerImpl(this.name, Arrays.asList(new ObjectPart(100L, 100L)));
        try {
            tracker.completePart(this.parts.iterator().next());
            Assert.fail();
        } catch (final IllegalStateException e) {
        }
    }
}
