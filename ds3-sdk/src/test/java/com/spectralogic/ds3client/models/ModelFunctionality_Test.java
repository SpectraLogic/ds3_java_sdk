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

package com.spectralogic.ds3client.models;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ModelFunctionality_Test {

    @Test
    public void bulkObjectEqualsEmptyObjects_Test() {
        final BulkObject obj1 = new BulkObject();
        final BulkObject obj2 = new BulkObject();

        assertThat(obj1.equals(obj2), is(true));
    }

    @Test
    public void bulkObjectNullableEquals_Test() {
        assertThat(BulkObject.nullableEquals(null, null), is(true));
        assertThat(BulkObject.nullableEquals("1", null), is(false));
        assertThat(BulkObject.nullableEquals("1", "1"), is(true));
        assertThat(BulkObject.nullableEquals("1", 1), is(false));
    }
}
