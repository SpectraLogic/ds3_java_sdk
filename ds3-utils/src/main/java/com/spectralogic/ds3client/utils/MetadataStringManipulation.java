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

import com.google.common.escape.Escaper;
import com.google.common.net.PercentEscaper;
import org.apache.commons.codec.CharEncoding;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * Percent encodes and decodes strings. The encoding conforms to the HTTP header key requirements.
 * Headers keys must contain only printable US-ASCII characters that are non-separators.
 */
public final class MetadataStringManipulation {

    /**
     * List of printable US-ASCII characters that do not need percent encoding.
     * Following the spec https://tools.ietf.org/html/rfc2616#page-31 for Message Headers
     */
    private static final String HTTP_HEADER_KEY_SAFE_CHARS = "!#$&'*-.~^_`|";
    private static final String HTTP_HEADER_VALUE_SAFE_CHARS = "()<>@,;:\"/\\[]?={}" + HTTP_HEADER_KEY_SAFE_CHARS;


    private static final Escaper HTTP_HEADER_KEY_ESCAPER =
            new PercentEscaper(HTTP_HEADER_KEY_SAFE_CHARS, false);

    private static final Escaper HTTP_HEADER_VALUE_ESCAPER =
            new PercentEscaper(HTTP_HEADER_VALUE_SAFE_CHARS, false);

    private MetadataStringManipulation() {
        //pass
    }

    /**
     * Percent encodes non-alpha-numeric characters within the specified string
     * excluding the symbols listed in {@link #HTTP_HEADER_KEY_SAFE_CHARS} using UTF-8
     */
    public static String toEncodedKeyString(final String str) {
        if (str == null) {
            return null;
        }

        final String strUtf8 = new String(str.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        return getMetadataKeyEscaper().escape(strUtf8);
    }


    /**
     * Percent encodes non-alpha-numeric characters within the specified string
     * excluding the symbols listed in {@link #HTTP_HEADER_VALUE_SAFE_CHARS} using UTF-8
     */
    public static String toEncodedValueString(final String str) {
        if (str == null) {
            return null;
        }

        final String strUtf8 = new String(str.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        return getMetadataValueEscaper().escape(strUtf8);
    }

    /**
     * Decodes a percent encoded string
     */
    public static String toDecodedString(final String str) {
        if (str == null) {
            return null;
        }
        try {
            return URLDecoder.decode(str, CharEncoding.UTF_8);
        } catch (final UnsupportedEncodingException e) {
            //Should not happen
            throw new RuntimeException("Could not decode string: " + str, e);
        }
    }

    private static Escaper getMetadataKeyEscaper() {
        return HTTP_HEADER_KEY_ESCAPER;
    }

    private static Escaper getMetadataValueEscaper() {
        return HTTP_HEADER_VALUE_ESCAPER;
    }
}
