/*
 * ******************************************************************************
 *   Copyright 2014-2017 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3client.utils;

import org.junit.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class SafeString_Manipulation_Test {

    @Test
    public void safeToStringIntTest() {
        final int myInt = 5;
        assertThat(SafeStringManipulation.safeToString(myInt), is("5"));
    }

    @Test
    public void safeToStringIntegerTest() {
        final Integer nullInt = null;
        assertThat(SafeStringManipulation.safeToString(nullInt), is(nullValue()));

        final Integer myInt = 5;
        assertThat(SafeStringManipulation.safeToString(myInt), is("5"));
    }

    @Test
    public void safeToStringBooleanTest() {
        final boolean myBoolean = true;
        assertThat(SafeStringManipulation.safeToString(myBoolean), is("true"));
    }

    @Test
    public void safeToStringDoubleTest() {
        final double myDouble = 2.1;
        assertThat(SafeStringManipulation.safeToString(myDouble), is("2.1"));

        final Double nullDouble = null;
        assertThat(SafeStringManipulation.safeToString(nullDouble), is(nullValue()));

        final Double nullableDouble = 2.2;
        assertThat(SafeStringManipulation.safeToString(nullableDouble), is("2.2"));
    }

    @Test
    public void safeToStringLongTest() {
        final long myLong = 3;
        assertThat(SafeStringManipulation.safeToString(myLong), is("3"));

        final Long nullLong = null;
        assertThat(SafeStringManipulation.safeToString(nullLong), is(nullValue()));

        final Long nullableLong = 4L;
        assertThat(SafeStringManipulation.safeToString(nullableLong), is("4"));
    }

    @Test
    public void safeToStringStringTest() {
        final String nullString = null;
        assertThat(SafeStringManipulation.safeToString(nullString), is(nullValue()));

        final String myString = "my string";
        assertThat(SafeStringManipulation.safeToString(myString), is("my string"));
    }

    @Test
    public void safeToStringDateTest() {
        final Date nullDate = null;
        assertThat(SafeStringManipulation.safeToString(nullDate), is(nullValue()));

        final Date myDate = new Date(123456789L);
        assertThat(SafeStringManipulation.safeToString(myDate), is("123456789"));
    }

    private enum TestEnum {
        ENUM_ONE, ENUM_TWO, ENUM_THREE
    }

    @Test
    public void safeToStringEnumTest() {
        final TestEnum nullEnum = null;
        assertThat(SafeStringManipulation.safeToString(nullEnum), is(nullValue()));

        final TestEnum myEnum = TestEnum.ENUM_TWO;
        assertThat(SafeStringManipulation.safeToString(myEnum), is("ENUM_TWO"));
    }

    @Test
    public void safeUrlEscapeTest() {
        assertThat(SafeStringManipulation.safeUrlEscape(null), is(nullValue()));
        assertThat(SafeStringManipulation.safeUrlEscape(""), is(""));
        assertThat(SafeStringManipulation.safeUrlEscape("one2three+four"), is("one2three%2Bfour"));
        assertThat(SafeStringManipulation.safeUrlEscape("one two three"), is("one%20two%20three"));
    }

    @Test
    public void safeQueryParamEscapeTest() {
        assertThat(SafeStringManipulation.safeQueryParamEscape(null), is(nullValue()));
        assertThat(SafeStringManipulation.safeQueryParamEscape(""), is(""));
        assertThat(SafeStringManipulation.safeQueryParamEscape("one2three+four"), is("one2three%2Bfour"));
        assertThat(SafeStringManipulation.safeQueryParamEscape("one two three"), is("one%20two%20three"));
        assertThat(SafeStringManipulation.safeQueryParamEscape("one&two&three"), is("one%26two%26three"));
    }
}
