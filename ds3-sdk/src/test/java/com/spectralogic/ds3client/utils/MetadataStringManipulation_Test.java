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

import java.util.regex.Pattern;

import static com.spectralogic.ds3client.utils.MetadataStringManipulation.toDecodedString;
import static com.spectralogic.ds3client.utils.MetadataStringManipulation.toEncodedString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class MetadataStringManipulation_Test {

    private static final String STRING_WITH_SPACES = "String With Spaces";
    private static final String STRING_WITH_SYMBOLS = "1234567890-!@#$%^&*()_+`~[]\\{}|;':\"./<>?\u03C0\u221E\u03CA\u03D5\u03E0";

    private static final Pattern ENCODED_PATTERN = Pattern.compile("[a-zA-Z0-9!#$&'*\\-.~\\^_`|,=%]*");

    /*
     * Verifies that an encoded string only contains header safe characters
     */
    private static boolean isEncodedSafeChars(final String str) {
        return ENCODED_PATTERN.matcher(str).matches();
    }

    @Test
    public void encodedPattern_Test() {
        assertTrue(isEncodedSafeChars(""));
        assertTrue(isEncodedSafeChars("abc123"));
        assertTrue(isEncodedSafeChars("!#$&'*-.~^_`|,=%"));

        assertFalse(isEncodedSafeChars(" "));
        assertFalse(isEncodedSafeChars("{"));
        assertFalse(isEncodedSafeChars("}"));
        assertFalse(isEncodedSafeChars("["));
        assertFalse(isEncodedSafeChars("]"));
        assertFalse(isEncodedSafeChars("<"));
        assertFalse(isEncodedSafeChars(">"));
        assertFalse(isEncodedSafeChars("@"));
        assertFalse(isEncodedSafeChars(";"));
        assertFalse(isEncodedSafeChars(":"));
        assertFalse(isEncodedSafeChars("\\"));
        assertFalse(isEncodedSafeChars("\""));
        assertFalse(isEncodedSafeChars("/"));
        assertFalse(isEncodedSafeChars("?"));
        assertFalse(isEncodedSafeChars("+"));

        assertFalse(isEncodedSafeChars("\u221E"));
        assertFalse(isEncodedSafeChars("\u03C0"));
        assertFalse(isEncodedSafeChars("\u03CA"));
        assertFalse(isEncodedSafeChars("\u03D5"));
        assertFalse(isEncodedSafeChars("\u03E0"));

    }

    @Test
    public void encodeDecode_NullString_Test() {
        assertThat(toEncodedString(null), is(nullValue()));
        assertThat(toDecodedString(null), is(nullValue()));
    }

    @Test
    public void encodeDecode_WithSpaces_Test() {
        final String encoded = toEncodedString(STRING_WITH_SPACES);
        assertTrue(isEncodedSafeChars(encoded));
        assertThat(toDecodedString(encoded), is(STRING_WITH_SPACES));
    }

    @Test
    public void encodeDecode_WithSymbols_Test() {
        final String encoded = toEncodedString(STRING_WITH_SYMBOLS);
        assertTrue(isEncodedSafeChars(encoded));
        assertThat(toDecodedString(encoded), is(STRING_WITH_SYMBOLS));
    }

    @Test
    public void encodeDecode_SpaceAndPlus_Test() {
        final String plusSpace = " +";
        final String encoded = toEncodedString(plusSpace);
        assertTrue(isEncodedSafeChars(encoded));
        assertThat(toDecodedString(encoded), is(plusSpace));
    }

    @Test
    public void encodeDecode_Range_Test() {
        final String range = "Range=bytes=0-10,110-120";
        final String encoded = toEncodedString(range);
        assertTrue(isEncodedSafeChars(encoded));

        assertThat(encoded, is(range));
        assertThat(toDecodedString(range), is(range));
    }

    @Test
    public void decodedDecode_LowerCase_Test() {
        final String encodedToLower = toEncodedString(STRING_WITH_SYMBOLS).toLowerCase();
        assertTrue(isEncodedSafeChars(encodedToLower));
        assertThat(toDecodedString(encodedToLower), is(STRING_WITH_SYMBOLS));
    }

    @Test
    public void encodeDecode_DoubleEncoding_Test() {
        final String encoded1 = toEncodedString(STRING_WITH_SYMBOLS);
        final String encoded2 = toEncodedString(encoded1);
        assertTrue(isEncodedSafeChars(encoded1));
        assertTrue(isEncodedSafeChars(encoded2));

        final String decoded1 = toDecodedString(encoded2);
        final String decoded2 = toDecodedString(decoded1);
        assertThat(decoded1, is(encoded1));
        assertThat(decoded2, is(STRING_WITH_SYMBOLS));
    }
}
