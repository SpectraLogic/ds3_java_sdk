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

public class MultiMapImpl<K, V> implements MultiMap<K, V> {

    private final Multimap<K, V> multimap;

    public MultiMapImpl(final Multimap<K, V> multimap) {
        this.multimap = multimap;
    }

    @Override
    public Collection<Map.Entry<K, Collection<V>>> entrySet() {
        return multimap.asMap().entrySet();
    }
}
