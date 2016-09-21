/*
 * ****************************************************************************
 *    Copyright 2014-2016 Spectra Logic Corporation. All Rights Reserved.
 *    Licensed under the Apache License, Version 2.0 (the "License"). You may not use
 *    this file except in compliance with the License. A copy of the License is located at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *    or in the "license" file accompanying this file.
 *    This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 *    CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *    specific language governing permissions and limitations under the License.
 *  ****************************************************************************
 */

package com.spectralogic.ds3client.helpers;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ObjectPartComparator_Test {
    @Test
    public void compare() {
        final List<ObjectPart> objects = Arrays.asList(
            new ObjectPart(3L, 2L),
            new ObjectPart(0L, 2L),
            new ObjectPart(2L, 1L),
            new ObjectPart(0L, 1L)
        );
        Collections.sort(objects, ObjectPartComparator.instance());
        final List<ObjectPart> expected = Arrays.asList(
            new ObjectPart(0L, 1L),
            new ObjectPart(0L, 2L),
            new ObjectPart(2L, 1L),
            new ObjectPart(3L, 2L)
        );
        for (int i = 0; i < expected.size(); i++)
        {
            final ObjectPart current = objects.get(i);
            final ObjectPart expectedObj = expected.get(i);
            assertThat(current.getOffset(), is(expectedObj.getOffset()));
            assertThat(current.getOffset(), is(expectedObj.getOffset()));
        }
    }
}
