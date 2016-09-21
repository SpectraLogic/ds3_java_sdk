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

import com.google.common.collect.Multimap;

import java.util.Collection;
import java.util.Map;

public final class Guard {

    private Guard() {
        //pass
    }

    public static boolean isNullOrEmpty(final Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isStringNullOrEmpty(final String str) {
        return str == null || str.isEmpty();
    }

    public static boolean isNotNullAndNotEmpty(final Collection<?> collection) {
        return !isNullOrEmpty(collection);
    }

    public static boolean isMultiMapNullOrEmpty(final Multimap<?, ?> multimap) {
        return multimap == null || multimap.isEmpty();
    }

    public static boolean isMapNullOrEmpty(final Map<?, ?> map) {
        return map == null || map.isEmpty();
    }
}
