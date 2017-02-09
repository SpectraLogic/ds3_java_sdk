/*
 * ****************************************************************************
 *    Copyright 2014-2017 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3client.exceptions;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class AggregateException_Test {

    @Test
    public void basicAggregate() {
        final Exception e1 = new Exception("first exception");
        final Exception e2 = new Exception("second exception");

        final List<Throwable> exceptionList = new ArrayList<>();
        exceptionList.add(e1);
        exceptionList.add(e2);

        final AggregateException aggregateException = new AggregateException(exceptionList);

        final Iterator<Throwable> exceptionIter = aggregateException.getExceptions().iterator();

        assertTrue(exceptionIter.hasNext());
        assertThat(exceptionIter.next(), is(notNullValue()));
        assertTrue(exceptionIter.hasNext());
        assertThat(exceptionIter.next(), is(notNullValue()));
        assertFalse(exceptionIter.hasNext());
    }

    @Test
    public void repeatIteration() {
        final Exception e1 = new Exception("first exception");

        final List<Throwable> exceptionList = new ArrayList<>();
        exceptionList.add(e1);

        final AggregateException aggregateException = new AggregateException(exceptionList);

        final Iterator<Throwable> exceptionIter = aggregateException.getExceptions().iterator();

        assertTrue(exceptionIter.hasNext());
        assertThat(exceptionIter.next(), is(notNullValue()));
        assertFalse(exceptionIter.hasNext());

        final Iterator<Throwable> repeatIter = aggregateException.getExceptions().iterator();
        assertTrue(repeatIter.hasNext());
        assertThat(repeatIter.next(), is(notNullValue()));
        assertFalse(repeatIter.hasNext());

    }
}
