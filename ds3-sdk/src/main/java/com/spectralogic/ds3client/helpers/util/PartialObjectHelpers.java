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

package com.spectralogic.ds3client.helpers.util;

import com.google.common.collect.*;
import com.spectralogic.ds3client.models.BulkObject;
import com.spectralogic.ds3client.models.Objects;
import com.spectralogic.ds3client.models.common.Range;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.models.bulk.PartialDs3Object;
import com.spectralogic.ds3client.utils.Guard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PartialObjectHelpers {
    private PartialObjectHelpers() {
       //pass
    }

    public static ImmutableMultimap<String, Range> getPartialObjectsRanges(final Iterable<Ds3Object> objects) {
        final ImmutableMultimap.Builder<String, Range> builder = ImmutableMultimap.builder();

        for (final Ds3Object obj : objects) {
            if (obj instanceof PartialDs3Object) {
                final PartialDs3Object partialDs3Object = (PartialDs3Object) obj;
                builder.put(partialDs3Object.getName(), partialDs3Object.getRange());
            }
        }
        return builder.build();
    }

    public static ImmutableMap<String, ImmutableMultimap<BulkObject, Range>> mapRangesToBlob(
            final List<Objects> chunks,
            final ImmutableMultimap<String, Range> partialObjects) {

        final Map<String, ImmutableMultimap.Builder<BulkObject, Range>> objectMapperBuilders = new HashMap<>();

        for (final Objects chunk : chunks) {
            for (final BulkObject blob : chunk.getObjects()) {
                final ImmutableCollection<Range> ranges = partialObjects.get(blob.getName());

                if (Guard.isNullOrEmpty(ranges)) continue;

                final ImmutableList<Range> rangesForBlob = getRangesForBlob(blob, ranges);
                final ImmutableList<Range> nonDuplicateRanges = dedupRanges(rangesForBlob);
                getMultiMapBuilder(objectMapperBuilders, blob.getName()).putAll(blob, nonDuplicateRanges);
            }
        }

        final ImmutableMap.Builder<String, ImmutableMultimap<BulkObject, Range>> builder = ImmutableMap.builder();

        for (final Map.Entry<String, ImmutableMultimap.Builder<BulkObject, Range>> entry : objectMapperBuilders.entrySet()) {
            builder.put(entry.getKey(), entry.getValue().build());
        }

        return builder.build();
    }

    static ImmutableList<Range> dedupRanges(final ImmutableList<Range> rangesForBlob) {

        final ImmutableList.Builder<Range> builder = ImmutableList.builder();
        final ImmutableSortedSet<Range> sortedRanges = ImmutableSortedSet.copyOf(rangesForBlob);

        Range currentRange = null;
        Range nextRange;

        for (final Range sortedRange : sortedRanges) {
            nextRange = sortedRange;
            if (currentRange == null) {
                // This will only be called on the first iteration of the loop
                currentRange = nextRange;
                continue;
            }

            // If the currentRange ends after the nextRange starts, combine the ranges
            if (currentRange.getEnd() >= nextRange.getStart()) {
                final long start = Math.min(currentRange.getStart(), nextRange.getStart());
                final long end = Math.max(currentRange.getEnd(), nextRange.getEnd());
                currentRange = Range.byPosition(start, end);
                // Do not add the range here.  The next range could also intersect.
                // Continue looking until there is a range that does not intersect.
                continue;
            }
            builder.add(currentRange);
            currentRange = nextRange;
        }

        // We need to make sure to add the last item in the list
        if (currentRange != null) {
            builder.add(currentRange);
        }

        // Return the ranges sorted
        return Ordering.natural().immutableSortedCopy(builder.build());
    }

    private static ImmutableMultimap.Builder<BulkObject, Range> getMultiMapBuilder(
            final Map<String, ImmutableMultimap.Builder<BulkObject, Range>> mapper, final String file) {
        if (mapper.containsKey(file)) {
            return mapper.get(file);
        } else {
            final ImmutableMultimap.Builder<BulkObject, Range> builder = ImmutableMultimap.builder();
            mapper.put(file, builder);
            return builder;
        }
    }

    private static ImmutableList<Range> getRangesForBlob(
            final BulkObject object,
            final ImmutableCollection<Range> ranges) {
        final ImmutableList.Builder<Range> builder = ImmutableList.builder();

        final long start = object.getOffset();
        final long end = start + object.getLength() - 1;

        for (final Range range : ranges) {
            final long rangeStart = range.getStart();
            final long rangeEnd = range.getEnd();

            if (rangeStart > end || rangeEnd < start) continue;
            if (rangeStart >= start && rangeEnd <= end) {
                builder.add(range);
            } else {
                builder.add(Range.byPosition(Math.max(rangeStart, start), Math.min(end, rangeEnd)));
            }
        }

        return builder.build();
    }
}
