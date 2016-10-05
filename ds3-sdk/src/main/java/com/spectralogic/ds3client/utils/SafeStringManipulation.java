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

import com.google.common.escape.Escaper;
import com.google.common.net.PercentEscaper;
import com.spectralogic.ds3client.commands.interfaces.Ds3Request;

import java.util.Date;

/**
 * Safely converts a type to a string. This is used when adding
 * objects to query params.
 */
public final class SafeStringManipulation {

    private static final String DS3_URL_PATH_FRAGMENT_SAFE_CHARS =
            "-._~" +        // Google escaper URL_PATH_OTHER_SAFE_CHARS_LACKING_PLUS
            "!$'()*,&=" +   // removed ; (so it will be escaped) and added / (so it will not)
            "@:/";          // Their urlFragmentEscaper uses URL_PATH_OTHER_SAFE_CHARS_LACKING_PLUS + "+/?"+

    private static final Escaper DS3_URL_FRAGMENT_ESCAPER =
            new PercentEscaper(DS3_URL_PATH_FRAGMENT_SAFE_CHARS, false);

    private SafeStringManipulation() {
        //pass
    }

    public static <T> String safeUrlEscape(final T obj) {
        if (obj == null) {
            return null;
        }
        return getDs3Escaper().escape(safeToString(obj));
    }

    public static <T> String safeToString(final T obj) {
        if (obj == null) {
            return null;
        }
        if(obj.getClass().isPrimitive()) {
            return String.valueOf(obj);
        }
        if (obj instanceof Date) {
            return Long.toString(((Date) obj).getTime());
        }
        return obj.toString();
    }
    public static Escaper getDs3Escaper() {
        // escaped characters in DS3 path and query parameter value segments
        return DS3_URL_FRAGMENT_ESCAPER;
    }

    public static String getEscapedRequestPath(final Ds3Request request) {
        if (request == null || request.getPath() == null){
            return "";
        }
        return getDs3Escaper().escape(request.getPath());
    }

}
