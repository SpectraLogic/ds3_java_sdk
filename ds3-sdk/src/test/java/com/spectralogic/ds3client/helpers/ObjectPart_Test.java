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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class ObjectPart_Test {
    @Test
    public void assignsFields() {
        final ObjectPart objectPart = new ObjectPart(10L, 11L);
        assertThat(objectPart.getOffset(), is(10L));
        assertThat(objectPart.getLength(), is(11L));
        assertThat(objectPart.getEnd(), is(20L));
    }

    @Test
    public void equalsWorks() {
        final ObjectPart original = new ObjectPart(10L, 11L);
        final Object other1 = new ObjectPart(10L, 11L);
        final Object other2 = new ObjectPart(11L, 11L);
        final Object other3 = new ObjectPart(10L, 12L);
        final Object other4 = new ObjectPart(15L, 15L);
        final Object other5 = "foo";
        assertThat(original, is(original));
        assertThat(original, is(other1));
        assertThat(original, is(not(other2)));
        assertThat(original, is(not(other3)));
        assertThat(original, is(not(other4)));
        assertThat(original, is(not(other5)));
    }
}
