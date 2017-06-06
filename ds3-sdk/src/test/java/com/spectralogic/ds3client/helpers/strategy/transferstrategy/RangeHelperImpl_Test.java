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

package com.spectralogic.ds3client.helpers.strategy.transferstrategy;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;
import com.spectralogic.ds3client.models.common.Range;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RangeHelperImpl_Test {
    @Test
    public void testReplacingValueInFirstRange() {
        final ImmutableCollection<Range> existingRanges = makeSomeRanges();

        final long intendedNumBytesToTransfer = numBytesToTransfer(existingRanges, existingRanges.size());

        final long numBytesTransferred = 7510;

        final ImmutableCollection<Range> newRanges = RangeHelper.replaceRange(existingRanges, numBytesTransferred, intendedNumBytesToTransfer);

        assertEquals(existingRanges.size(), newRanges.size());
        assertEquals(existingRanges.iterator().next().getLength() - numBytesTransferred, newRanges.iterator().next().getLength());
        assertEquals(numBytesTransferred + existingRanges.iterator().next().getStart(), newRanges.iterator().next().getStart());

        final UnmodifiableIterator<Range> existingRangesIterator = existingRanges.iterator();
        existingRangesIterator.next();

        final UnmodifiableIterator<Range> newRangesIterator = newRanges.iterator();
        newRangesIterator.next();

        while (existingRangesIterator.hasNext()) {
            final Range existingRange = existingRangesIterator.next();
            final Range newRange = newRangesIterator.next();

            assertEquals(existingRange.getStart(), newRange.getStart());
            assertEquals(existingRange.getLength(), newRange.getLength());
        }
    }

    private ImmutableCollection<Range> makeSomeRanges() {
        final Range firstRange = Range.byLength(10, 10000);
        final Range secondRange = Range.byLength(20000, 15000);
        final Range thirdRange = Range.byLength(40000, 20000);
        return ImmutableList.<Range>builder()
                .add(firstRange)
                .add(secondRange)
                .add(thirdRange)
                .build();
    }

    private long numBytesToTransfer(final ImmutableCollection<Range> ranges, final int numRanges) {
        long result = 0;
        int currentRange = 0;

        for (final Range range : ranges) {
            if (++currentRange > numRanges) {
                break;
            }

            result += range.getLength();
        }

        return result;
    }

    @Test
    public void testReplacingValueInSecondRange() {
        final ImmutableCollection<Range> existingRanges = makeSomeRanges();

        final long intendedNumBytesToTransfer = numBytesToTransfer(existingRanges, existingRanges.size());
        final long numBytesTransferred = 10010;


        final ImmutableCollection<Range> newRanges = RangeHelper.replaceRange(existingRanges, numBytesTransferred, intendedNumBytesToTransfer);

        assertEquals(2, newRanges.size());

        final UnmodifiableIterator<Range> existingRangesIterator = existingRanges.iterator();
        existingRangesIterator.next();

        assertEquals(existingRangesIterator.next().getLength() + existingRanges.iterator().next().getLength() - numBytesTransferred, newRanges.iterator().next().getLength());

        final long firstRangeLength = existingRanges.iterator().next().getLength();
        final long secondRangeStart = newRanges.iterator().next().getStart();

        assertEquals(firstRangeLength + numBytesTransferred, secondRangeStart);

        final UnmodifiableIterator<Range> newRangesIterator = newRanges.iterator();
        newRangesIterator.next();

        while (existingRangesIterator.hasNext()) {
            final Range existingRange = existingRangesIterator.next();
            final Range newRange = newRangesIterator.next();

            assertEquals(existingRange.getStart(), newRange.getStart());
            assertEquals(existingRange.getLength(), newRange.getLength());
        }
    }

    @Test
    public void testReplacingValueInThirdRange() {
        final ImmutableCollection<Range> existingRanges = makeSomeRanges();

        final long intendedNumBytesToTransfer = numBytesToTransfer(existingRanges, existingRanges.size());
        final long numBytesTransferred = 27000;

        final ImmutableCollection<Range> newRanges = RangeHelper.replaceRange(existingRanges, numBytesTransferred, intendedNumBytesToTransfer);

        assertEquals(1, newRanges.size());

        final UnmodifiableIterator<Range> existingRangesIterator = existingRanges.iterator();
        existingRangesIterator.next();

        final long lengthOf2ndRange = existingRangesIterator.next().getLength();
        final long expectedStartIn3rdRange = lengthOf2ndRange + numBytesTransferred;

        assertEquals(expectedStartIn3rdRange, newRanges.iterator().next().getStart());
        assertEquals(existingRangesIterator.next().getEnd(), newRanges.iterator().next().getEnd());
    }

    @Test
    public void testReplacingValueWhenWeTransferExactlyFirstRange() {
        final ImmutableCollection<Range> existingRanges = makeSomeRanges();

        final long intendedNumBytesToTransfer = numBytesToTransfer(existingRanges, existingRanges.size());
        final long numBytesTransferred = existingRanges.iterator().next().getLength();

        final ImmutableCollection<Range> newRanges = RangeHelper.replaceRange(existingRanges, numBytesTransferred, intendedNumBytesToTransfer);

        assertEquals(2, newRanges.size());

        final UnmodifiableIterator<Range> existingRangesIterator = existingRanges.iterator();
        existingRangesIterator.next();

        final UnmodifiableIterator<Range> newRangesIterator = newRanges.iterator();

        while (existingRangesIterator.hasNext()) {
            final Range existingRange = existingRangesIterator.next();
            final Range newRange = newRangesIterator.next();

            assertEquals(existingRange.getStart(), newRange.getStart());
            assertEquals(existingRange.getLength(), newRange.getLength());
        }
    }

    @Test
    public void testReplacingValueWhenWeTransferExactly1st2Ranges() {
        final ImmutableCollection<Range> existingRanges = makeSomeRanges();

        final long intendedNumBytesToTransfer = numBytesToTransfer(existingRanges, existingRanges.size());
        final long numBytesTransferred = numBytesToTransfer(existingRanges, 2);

        final ImmutableCollection<Range> newRanges = RangeHelper.replaceRange(existingRanges, numBytesTransferred, intendedNumBytesToTransfer);

        assertEquals(1, newRanges.size());

        final UnmodifiableIterator<Range> existingRangesIterator = existingRanges.iterator();
        existingRangesIterator.next();
        existingRangesIterator.next();

        final UnmodifiableIterator<Range> newRangesIterator = newRanges.iterator();

        while (existingRangesIterator.hasNext()) {
            final Range existingRange = existingRangesIterator.next();
            final Range newRange = newRangesIterator.next();

            assertEquals(existingRange.getStart(), newRange.getStart());
            assertEquals(existingRange.getLength(), newRange.getLength());
        }
    }

    @Test
    public void testReplacingValueWhenWeTransfer0Bytes() {
        final ImmutableCollection<Range> existingRanges = makeSomeRanges();

        final long intendedNumBytesToTransfer = numBytesToTransfer(existingRanges, existingRanges.size());
        final long numBytesTransferred = 0;

        final ImmutableCollection<Range> newRanges = RangeHelper.replaceRange(existingRanges, numBytesTransferred, intendedNumBytesToTransfer);

        assertEquals(3, newRanges.size());

        final UnmodifiableIterator<Range> existingRangesIterator = existingRanges.iterator();

        final UnmodifiableIterator<Range> newRangesIterator = newRanges.iterator();

        while (existingRangesIterator.hasNext()) {
            final Range existingRange = existingRangesIterator.next();
            final Range newRange = newRangesIterator.next();

            assertEquals(existingRange.getStart(), newRange.getStart());
            assertEquals(existingRange.getLength(), newRange.getLength());
        }
    }
}
