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

package com.spectralogic.ds3client.helpers;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;
import com.spectralogic.ds3client.models.common.Range;
import com.spectralogic.ds3client.utils.Guard;

import java.util.ArrayList;
import java.util.List;

final class RangeHelper {
    private RangeHelper() {}

    static ImmutableCollection<Range> replaceRange(final ImmutableCollection<Range> existingRanges,
                                                   final long numBytesTransferred,
                                                   final long intendedNumBytesToTransfer)
    {
        Preconditions.checkState(numBytesTransferred >= 0, "numBytesTransferred must be >= 0.");
        Preconditions.checkState(intendedNumBytesToTransfer > 0, "intendedNumBytesToTransfer must be > 0.");
        Preconditions.checkState(intendedNumBytesToTransfer > numBytesTransferred, "intendedNumBytesToTransfer must be > numBytesTransferred");

        if (Guard.isNullOrEmpty(existingRanges)) {
            return ImmutableList.of(Range.byLength(numBytesTransferred, intendedNumBytesToTransfer - numBytesTransferred));
        }

        final List<Range> newRanges = new ArrayList<>();

        final UnmodifiableIterator<Range> existingRangesIterator = existingRanges.iterator();

        long previousAccumulatedBytesInRanges = 0;
        long currentAccumulatedBytesInRanges = existingRanges.iterator().next().getLength();

        while (existingRangesIterator.hasNext()) {
            final Range existingRange = existingRangesIterator.next();

            if (numBytesTransferred < currentAccumulatedBytesInRanges) {
                final Range firstNewRange = Range.byPosition(existingRange.getStart() - previousAccumulatedBytesInRanges  + numBytesTransferred, existingRange.getEnd());
                newRanges.add(firstNewRange);

                addRemainingRanges(existingRangesIterator, newRanges);
                break;
            }

            previousAccumulatedBytesInRanges += existingRange.getLength();
            currentAccumulatedBytesInRanges += existingRange.getLength();
        }

        return ImmutableList.copyOf(newRanges);
    }

    static void addRemainingRanges(final UnmodifiableIterator<Range> existingRangesIterator, final List<Range> newRanges) {
        while (existingRangesIterator.hasNext()) {
            newRanges.add(existingRangesIterator.next());
        }
    }

    static long transferSizeForRanges(final ImmutableCollection<Range> existingRanges) {
        long result = 0;

        if (Guard.isNullOrEmpty(existingRanges)) {
            return result;
        }

        for (final Range range : existingRanges) {
            result += range.getLength();
        }

        return result;
    }
}
