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

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Maps.EntryTransformer;
import com.spectralogic.ds3client.models.bulk.BulkObject;

import java.util.Collection;
import java.util.HashMap;

class JobPartTrackerFactory {
    public static JobPartTracker buildPartTracker(final Iterable<BulkObject> objects) {
        final ArrayListMultimap<String, ObjectPart> multimap = ArrayListMultimap.create();
        for (final BulkObject bulkObject : objects) {
            multimap.put(bulkObject.getName(), new ObjectPart(bulkObject.getOffset(), bulkObject.getLength()));
        }
        return new JobPartTracker(new HashMap<>(Maps.transformEntries(
            multimap.asMap(),
            new BuildObjectPartTrackerFromObjectPartGroup()
        )));
    }

    private static final class BuildObjectPartTrackerFromObjectPartGroup
            implements EntryTransformer<String, Collection<ObjectPart>, ObjectPartTracker> {
        @Override
        public ObjectPartTracker transformEntry(final String key, final Collection<ObjectPart> value) {
            return new ObjectPartTrackerImpl(key, value);
        }
    }
}
