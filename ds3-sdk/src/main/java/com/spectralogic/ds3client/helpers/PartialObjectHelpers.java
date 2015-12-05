package com.spectralogic.ds3client.helpers;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.spectralogic.ds3client.models.Range;
import com.spectralogic.ds3client.models.bulk.BulkObject;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.models.bulk.Objects;
import com.spectralogic.ds3client.models.bulk.PartialDs3Object;
import com.spectralogic.ds3client.utils.Guard;

import java.util.List;

final class PartialObjectHelpers {
    private PartialObjectHelpers() {
       //pass
    }

    public static ImmutableMultimap<String, Range> getPartialObjectsRanges(final List<Ds3Object> objects) {
        final ImmutableMultimap.Builder<String, Range> builder = ImmutableMultimap.builder();

        for (final Ds3Object obj : objects) {
            if (obj instanceof PartialDs3Object) {
                final PartialDs3Object partialDs3Object = (PartialDs3Object) obj;
                builder.put(partialDs3Object.getName(), partialDs3Object.getRange());
            }
        }
        return builder.build();
    }

    public static ImmutableMultimap<BulkObject, Range> mapRangesToBlob(final List<Objects> chunks, final ImmutableMultimap<String, Range> partialObjects) {
        final ImmutableMultimap.Builder<BulkObject, Range> builder = ImmutableMultimap.builder();

        for (final Objects chunk : chunks) {
            for (final BulkObject blob : chunk.getObjects()) {
                final ImmutableCollection<Range> ranges = partialObjects.get(blob.getName());

                if (Guard.isNullOrEmpty(ranges)) continue;

                final ImmutableList<Range> rangesForBlob = getRangesForBlob(blob, ranges);

                builder.putAll(blob, rangesForBlob);
            }
        }

        return builder.build();
    }

    private static ImmutableList<Range> getRangesForBlob(final BulkObject object, final ImmutableCollection<Range> ranges) {
        final ImmutableList.Builder<Range> builder = ImmutableList.builder();

        final long start = object.getOffset();
        final long end = start + object.getLength() - 1;

        for (final Range range : ranges) {
            final long rangeStart = range.getStart();
            final long rangeEnd = range.getEnd();

            if (rangeStart > end || rangeEnd < start) continue;
            if (rangeStart >= start && rangeEnd <= end) {
                builder.add(range);
            } else if (rangeStart < start) {
                builder.add(Range.byPosition(start, rangeEnd));
            }
            else {
                builder.add(Range.byPosition(rangeStart, end));
            }
        }

        return builder.build();
    }
}
