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

import org.junit.Test;

import static com.spectralogic.ds3client.utils.MetadataStringManipulation.toDecodedString;
import static com.spectralogic.ds3client.utils.MetadataStringManipulation.toEncodedString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class MetadataStringManipulation_Test {

    private static final String STRING_WITH_SPACES = "String With Spaces";
    private static final String STRING_WITH_ENCODED_SPACES = "String%20With%20Spaces";

    private static final String STRING_WITH_SYMBOLS = "1234567890-!@#$%^&*()_+`~[]\\{}|;':\"./<>?∞πϊφϠ";
    private static final String STRING_WITH_ENCODED_SYMBOLS = "1234567890-!%40#$%25^&*%28%29_%2B`~%5B%5D%5C%7B%7D|%3B'%3A%22.%2F%3C%3E%3F%C3%A2%CB%86%C5%BE%C3%8F%E2%82%AC%C3%8F%C5%A0%C3%8F%E2%80%A0%C3%8F%C2%A0";

    @Test
    public void toEscapedString_Null_Test() {
        final String nullString = null;
        assertThat(toEncodedString(nullString), is(nullValue()));
    }

    @Test
    public void toEscapedString_Spaces_Test() {
        assertThat(toEncodedString(STRING_WITH_SPACES), is(STRING_WITH_ENCODED_SPACES));
    }

    @Test
    public void toEscapedString_Symbols_Test() {
        assertThat(toEncodedString(STRING_WITH_SYMBOLS), is(STRING_WITH_ENCODED_SYMBOLS));
    }

    @Test
    public void toDecodedString_Null_Test() {
        final String nullString = null;
        assertThat(toDecodedString(nullString), is(nullValue()));
    }

    @Test
    public void toDecodedString_Spaces_Test() {
        assertThat(toDecodedString(STRING_WITH_ENCODED_SPACES), is(STRING_WITH_SPACES));
    }

    @Test
    public void toDecodedString_Symbols_Test() {
        assertThat(toDecodedString(STRING_WITH_ENCODED_SYMBOLS), is(STRING_WITH_SYMBOLS));
    }

    @Test
    public void escapeAndDecode_SpaceAndPlus_Test() {
        final String decode = " +";
        final String encoded = "%20%2B";
        assertThat(toEncodedString(decode), is(encoded));
        assertThat(toDecodedString(encoded), is(decode));
    }

    @Test
    public void encodeAndDecode_Range_Test() {
        final String range = "Range=bytes=0-10,110-120";
        assertThat(toEncodedString(range), is(range));
        assertThat(toDecodedString(range), is(range));
    }

    @Test
    public void toDecodedString_LowerCase_Test() {
        assertThat(toDecodedString(STRING_WITH_ENCODED_SYMBOLS.toLowerCase()), is(STRING_WITH_SYMBOLS));
    }

    @Test
    public void escapeAndDecode_DoubleEscaping_Test() {
        final String result = toDecodedString(toEncodedString(STRING_WITH_ENCODED_SYMBOLS));
        assertThat(result, is(STRING_WITH_ENCODED_SYMBOLS));
    }
}
