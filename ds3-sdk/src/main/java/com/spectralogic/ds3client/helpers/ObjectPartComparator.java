/*
 * ******************************************************************************
 *   Copyright 2014 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3client.helpers;

import java.util.Comparator;

public class ObjectPartComparator implements Comparator<ObjectPart> {
    private static final ObjectPartComparator instance = new ObjectPartComparator();
    
    public static ObjectPartComparator instance() {
        return instance;
    }

    private ObjectPartComparator() {
        // Use singleton
    }

    @Override
    public int compare(final ObjectPart o1, final ObjectPart o2) {
        return intOf(signum(o1.getOffset() - o2.getOffset()));
    }
    
    private static long signum(final long value) {
        return value < 0 ? -1 : (value == 0 ? 0 : 1);
    }
    
    private static int intOf(final long value) {
        return (int)value;
    }
}
