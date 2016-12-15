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

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Locale;

import static com.spectralogic.ds3client.utils.MetadataStringManipulation.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class MetadataStringManipulation_Test {

    private static final String STRING_WITH_SPACES = "String With Spaces";
    private static final String ENCODED_STRING_WITH_SPACES = "String%20With%20Spaces";

    private static final String STRING_WITH_SYMBOLS = "1234567890-!@#$%^&*()_+`~[]\\{}|;':\"./<>?∞πϊφϠ";
    private static final String ENCODED_STRING_WITH_SYMBOLS = "1234567890-!%40#$%25^&*%28%29_%2B`~%5B%5D%5C%7B%7D|%3B'%3A%22.%2F%3C%3E%3F%C3%A2%CB%86%C5%BE%C3%8F%E2%82%AC%C3%8F%C5%A0%C3%8F%E2%80%A0%C3%8F%C2%A0";

    private static final String UTF8_STRING_WITH_SPACES = toUtf8String(STRING_WITH_SPACES);
    private static final String UTF8_ENCODED_STRING_WITH_SPACES = toUtf8String(ENCODED_STRING_WITH_SPACES);

    private static final String UTF8_STRING_WITH_SYMBOLS = toUtf8String(STRING_WITH_SYMBOLS);
    private static final String UTF8_ENCODED_STRING_WITH_SYMBOLS = toUtf8String(ENCODED_STRING_WITH_SYMBOLS);

    @Test
    public void difficultSymbols_Test() {
        final String difficultSymbols = "∞πϊφϠ";
        final String encoded = toEncodedString(difficultSymbols);
        final String decoded = toDecodedString(encoded);

        System.out.println("***Test: difficultSymbols_Test");
        System.out.println("   original: " + difficultSymbols);
        System.out.println("      bytes: " + Arrays.toString(difficultSymbols.getBytes(StandardCharsets.UTF_8)));
        System.out.println("   encoded:  " + encoded);
        System.out.println("      bytes: " + Arrays.toString(encoded.getBytes(StandardCharsets.UTF_8)));
        System.out.println("   decoded:  " + decoded);
        System.out.println("      bytes: " + Arrays.toString(decoded.getBytes(StandardCharsets.UTF_8)));
        assertThat(decoded, is(difficultSymbols));
    }

    @Test
    public void toEscapedString_Null_Test() {
        final String nullString = null;
        assertThat(toEncodedString(nullString), is(nullValue()));
    }

    @Test
    public void toEscapedString_Spaces_Test() {
        final String expected = UTF8_ENCODED_STRING_WITH_SPACES;
        System.out.println("***Test: toEscapedString_Spaces_Test");
        System.out.println("   was:      " + toEncodedString(STRING_WITH_SPACES));
        System.out.println("   expected: " + expected);
        assertThat(toEncodedString(STRING_WITH_SPACES), is(expected));
    }

    @Test
    public void toEscapedString_Symbols_Test() {
        final String expected = UTF8_ENCODED_STRING_WITH_SYMBOLS;
        System.out.println("***Test: toEscapedString_Symbols_Test");
        System.out.println("   was:      " + toEncodedString(STRING_WITH_SYMBOLS));
        System.out.println("   expected: " + expected);
        assertThat(toEncodedString(STRING_WITH_SYMBOLS), is(expected));
    }

    @Test
    public void toDecodedString_Null_Test() {
        final String nullString = null;
        assertThat(toDecodedString(nullString), is(nullValue()));
    }

    @Test
    public void toDecodedString_Spaces_Test() {
        final String expected = UTF8_STRING_WITH_SPACES;
        System.out.println("***Test: toDecodedString_Spaces_Test");
        System.out.println("   was:      " + toDecodedString(ENCODED_STRING_WITH_SPACES));
        System.out.println("   expected: " + expected);
        assertThat(toDecodedString(ENCODED_STRING_WITH_SPACES), is(expected));
    }

    @Test
    public void toDecodedString_Symbols_Test() {
        final String expected = UTF8_STRING_WITH_SYMBOLS;
        System.out.println("***Test: toDecodedString_Symbols_Test");
        System.out.println("   was:      " + toDecodedString(ENCODED_STRING_WITH_SYMBOLS));
        System.out.println("   expected: " + expected);
        assertThat(toDecodedString(ENCODED_STRING_WITH_SYMBOLS), is(expected));
    }

    @Test
    public void encodeAndDecode_Symbols_Test() {
        final String expected = UTF8_STRING_WITH_SYMBOLS;
        System.out.println("***Test: encodeAndDecode_Symbols_Test");
        System.out.println("   was:      " + toDecodedString(toEncodedString(STRING_WITH_SYMBOLS)));
        System.out.println("   expected: " + expected);
        assertThat(toDecodedString(toEncodedString(STRING_WITH_SYMBOLS)), is(expected));
    }

    @Test
    public void encodeAndDecode_EncodedSymbols_Test() {
        final String expected = UTF8_ENCODED_STRING_WITH_SYMBOLS;
        System.out.println("***Test: encodeAndDecode_EncodedSymbols_Test");
        System.out.println("   was:      " + toDecodedString(toEncodedString(ENCODED_STRING_WITH_SYMBOLS)));
        System.out.println("   expected: " + expected);
        assertThat(toDecodedString(toEncodedString(ENCODED_STRING_WITH_SYMBOLS)), is(expected));
    }

    @Test
    public void encodeAndDecode_SpaceAndPlus_Test() {
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
        final String expected = UTF8_STRING_WITH_SYMBOLS;
        System.out.println("***Test: toDecodedString_LowerCase_Test");
        System.out.println("   was:      " + toDecodedString(ENCODED_STRING_WITH_SYMBOLS.toLowerCase()));
        System.out.println("   expected: " + expected);
        assertThat(toDecodedString(ENCODED_STRING_WITH_SYMBOLS.toLowerCase()), is(expected));
    }

    @Test
    public void encodeAndDecode_DoubleEscaping_Test() {
        final String expected = UTF8_ENCODED_STRING_WITH_SYMBOLS;
        final String result = toDecodedString(toEncodedString(ENCODED_STRING_WITH_SYMBOLS));
        System.out.println("***Test: encodeAndDecode_DoubleEscaping_Test");
        System.out.println("   was:      " + result);
        System.out.println("   expected: " + expected);
        assertThat(result, is(expected));
    }
}
