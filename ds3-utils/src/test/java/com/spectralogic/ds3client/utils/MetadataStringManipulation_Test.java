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

import java.util.regex.Pattern;

import static com.spectralogic.ds3client.utils.MetadataStringManipulation.toDecodedString;
import static com.spectralogic.ds3client.utils.MetadataStringManipulation.toEncodedKeyString;
import static com.spectralogic.ds3client.utils.MetadataStringManipulation.toEncodedValueString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class MetadataStringManipulation_Test {

    private static final String STRING_WITH_SPACES = "String With Spaces";
    private static final String STRING_WITH_SYMBOLS = "1234567890-!@#$%^&*()_+`~[]\\{}|;':\"./<>?∞πϊφϠ";
    private static final String STRING_WITH_SYMBOLS_UNICODE = "1234567890-!@#$%^&*()_+`~[]\\{}|;':\"./<>?\u03C0\u221E\u03CA\u03D5\u03E0";

    private static final Pattern KEY_ENCODED_PATTERN = Pattern.compile("[a-zA-Z0-9!#$&'*-.~^_`|%]*");

    private static final Pattern VALUE_ENCODED_PATTERN = Pattern.compile("[a-zA-Z0-9!#$&'*-.~^_`|%()<>@,;:\"/\\\\\\[\\]?={}]*");

    /*
     * Verifies that an encoded string only contains key header safe characters
     */
    private static boolean isEncodedSafeKeyChars(final String str) {
        return KEY_ENCODED_PATTERN.matcher(str).matches();
    }

    /*
     * Verifies that an encoded string only contains value header safe characters
     */
    private static boolean isEncodedSafeValueChars(final String str) {
        return VALUE_ENCODED_PATTERN.matcher(str).matches();
    }

    @Test
    public void encodedKeyPattern_Test() {
        assertTrue(isEncodedSafeKeyChars(""));
        assertTrue(isEncodedSafeKeyChars("abc123"));
        assertTrue(isEncodedSafeKeyChars("!#$&'*-.~^_`|%"));

        assertFalse(isEncodedSafeKeyChars(" "));
        assertFalse(isEncodedSafeKeyChars("{"));
        assertFalse(isEncodedSafeKeyChars("}"));
        assertFalse(isEncodedSafeKeyChars("["));
        assertFalse(isEncodedSafeKeyChars("]"));
        assertFalse(isEncodedSafeKeyChars("<"));
        assertFalse(isEncodedSafeKeyChars(">"));
        assertFalse(isEncodedSafeKeyChars("@"));
        assertFalse(isEncodedSafeKeyChars(";"));
        assertFalse(isEncodedSafeKeyChars(":"));
        assertFalse(isEncodedSafeKeyChars("\\"));
        assertFalse(isEncodedSafeKeyChars("\""));
        assertFalse(isEncodedSafeKeyChars("/"));
        assertFalse(isEncodedSafeKeyChars("?"));

        assertFalse(isEncodedSafeKeyChars("\u221E"));
        assertFalse(isEncodedSafeKeyChars("\u03C0"));
        assertFalse(isEncodedSafeKeyChars("\u03CA"));
        assertFalse(isEncodedSafeKeyChars("\u03D5"));
        assertFalse(isEncodedSafeKeyChars("\u03E0"));
    }

    @Test
    public void encodedValuePattern_Test() {
        assertTrue(isEncodedSafeValueChars(""));
        assertTrue(isEncodedSafeValueChars("abc123"));
        assertTrue(isEncodedSafeValueChars("!#$&'*-.~^_`|,=%"));
        assertTrue(isEncodedSafeValueChars("{"));
        assertTrue(isEncodedSafeValueChars("}"));
        assertTrue(isEncodedSafeValueChars("<"));
        assertTrue(isEncodedSafeValueChars(">"));
        assertTrue(isEncodedSafeValueChars("@"));
        assertTrue(isEncodedSafeValueChars(";"));
        assertTrue(isEncodedSafeValueChars(":"));
        assertTrue(isEncodedSafeValueChars("\""));
        assertTrue(isEncodedSafeValueChars("/"));
        assertTrue(isEncodedSafeValueChars("?"));
        assertTrue(isEncodedSafeValueChars("["));
        assertTrue(isEncodedSafeValueChars("]"));
        assertTrue(isEncodedSafeValueChars("\\"));

        assertFalse(isEncodedSafeValueChars(" "));

        assertFalse(isEncodedSafeValueChars("\u221E"));
        assertFalse(isEncodedSafeValueChars("\u03C0"));
        assertFalse(isEncodedSafeValueChars("\u03CA"));
        assertFalse(isEncodedSafeValueChars("\u03D5"));
        assertFalse(isEncodedSafeValueChars("\u03E0"));
    }

    @Test
    public void encodeDecode_NullString_Test() {
        assertThat(toEncodedKeyString(null), is(nullValue()));
        assertThat(toEncodedValueString(null), is(nullValue()));
        assertThat(toDecodedString(null), is(nullValue()));
    }

    @Test
    public void encodeDecode_WithSpaces_Test() {
        final String encodedKey = toEncodedKeyString(STRING_WITH_SPACES);
        assertTrue(isEncodedSafeKeyChars(encodedKey));
        assertThat(toDecodedString(encodedKey), is(STRING_WITH_SPACES));

        final String encodedValue = toEncodedValueString(STRING_WITH_SPACES);
        assertTrue(isEncodedSafeValueChars(encodedValue));
        assertThat(toDecodedString(encodedValue), is(STRING_WITH_SPACES));
    }

    @Test
    public void encodeDecode_WithSymbols_Test() {
        final String encodedKey = toEncodedKeyString(STRING_WITH_SYMBOLS);
        assertTrue(isEncodedSafeKeyChars(encodedKey));
        assertThat(toDecodedString(encodedKey), is(STRING_WITH_SYMBOLS));

        final String encodedValue = toEncodedValueString(STRING_WITH_SYMBOLS);
        assertTrue(isEncodedSafeValueChars(encodedValue));
        assertThat(toDecodedString(encodedValue), is(STRING_WITH_SYMBOLS));
    }

    @Test
    public void encodeDecode_WithUnicodeSymbols_Test() {
        final String encodedKey = toEncodedKeyString(STRING_WITH_SYMBOLS_UNICODE);
        assertTrue(isEncodedSafeKeyChars(encodedKey));
        assertThat(toDecodedString(encodedKey), is(STRING_WITH_SYMBOLS_UNICODE));

        final String encodedValue = toEncodedValueString(STRING_WITH_SYMBOLS_UNICODE);
        assertTrue(isEncodedSafeValueChars(encodedValue));
        assertThat(toDecodedString(encodedValue), is(STRING_WITH_SYMBOLS_UNICODE));
    }

    @Test
    public void encodeDecode_SpaceAndPlus_Test() {
        final String plusSpace = " +";

        final String encodedKey = toEncodedKeyString(plusSpace);
        assertTrue(isEncodedSafeKeyChars(encodedKey));
        assertThat(toDecodedString(encodedKey), is(plusSpace));

        final String encodedValue = toEncodedValueString(plusSpace);
        assertTrue(isEncodedSafeValueChars(encodedValue));
        assertThat(toDecodedString(encodedValue), is(plusSpace));
    }

    @Test
    public void encodeDecode_Range_Test() {
        final String range = "Range=bytes=0-10,110-120";

        final String encodedValue = toEncodedValueString(range);
        assertTrue(isEncodedSafeValueChars(encodedValue));
        assertThat(encodedValue, is(range));
        assertThat(toDecodedString(range), is(range));
    }

    @Test
    public void decodedDecode_LowerCase_Test() {
        final String encodedToLowerKey = toEncodedKeyString(STRING_WITH_SYMBOLS).toLowerCase();
        assertTrue(isEncodedSafeKeyChars(encodedToLowerKey));
        assertThat(toDecodedString(encodedToLowerKey), is(STRING_WITH_SYMBOLS));

        final String encodedToLowerValue = toEncodedValueString(STRING_WITH_SYMBOLS).toLowerCase();
        assertTrue(isEncodedSafeValueChars(encodedToLowerValue));
        assertThat(toDecodedString(encodedToLowerValue), is(STRING_WITH_SYMBOLS));
    }

    @Test
    public void encodeDecode_DoubleEncoding_Test() {
        final String encoded1Key = toEncodedKeyString(STRING_WITH_SYMBOLS);
        final String encoded2Key = toEncodedKeyString(encoded1Key);
        assertTrue(isEncodedSafeKeyChars(encoded1Key));
        assertTrue(isEncodedSafeKeyChars(encoded2Key));

        final String decoded1Key = toDecodedString(encoded2Key);
        final String decoded2Key = toDecodedString(decoded1Key);
        assertThat(decoded1Key, is(encoded1Key));
        assertThat(decoded2Key, is(STRING_WITH_SYMBOLS));

        final String encoded1Value = toEncodedValueString(STRING_WITH_SYMBOLS);
        final String encoded2Value = toEncodedValueString(encoded1Value);
        assertTrue(isEncodedSafeValueChars(encoded1Value));
        assertTrue(isEncodedSafeValueChars(encoded2Value));

        final String decoded1Value = toDecodedString(encoded2Value);
        final String decoded2Value = toDecodedString(decoded1Value);
        assertThat(decoded1Value, is(encoded1Value));
        assertThat(decoded2Value, is(STRING_WITH_SYMBOLS));
    }
}
