/*
 * ******************************************************************************
 *   Copyright 2014-2015 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3client.models.bulk;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import java.util.ArrayList;

public enum Priority {
    CRITICAL,
    VERY_HIGH,
    HIGH,
    NORMAL,
    LOW,
    BACKGROUND,
    MINIMIZED_DUE_TO_TOO_MANY_RETRIES;

    public static String valuesString() {
        final ArrayList<Priority> list = Lists.newArrayList(Priority.values());
        return Joiner.on(", ").join(Lists.transform(list, new Function<Priority, String>() {
            @Override
            public String apply(final Priority input) {
	        return input.toString().toLowerCase();
            }
        }));
    }
}
