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

package com.spectralogic.ds3client.utils;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Guard_Test {

    @Test
    public void testCollectionNull() {
        assertTrue(Guard.isNullOrEmpty(null));
        assertFalse(Guard.isNotNullAndNotEmpty(null));
    }

    @Test
    public void testCollectionEmpty() {
        final List<String> data = Lists.newArrayList();
        assertTrue(Guard.isNullOrEmpty(data));
        assertFalse(Guard.isNotNullAndNotEmpty(data));
    }

    @Test
    public void testNotEmptyCollection() {
        final List<String> data = Lists.newArrayList("hi");
        assertFalse(Guard.isNullOrEmpty(data));
        assertTrue(Guard.isNotNullAndNotEmpty(data));
    }
}
