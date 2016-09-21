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

package com.spectralogic.ds3client.helpers.channels;

import com.google.common.collect.*;
import com.spectralogic.ds3client.helpers.TruncateNotAllowedException;
import com.spectralogic.ds3client.models.BulkObject;
import com.spectralogic.ds3client.models.common.Range;
import com.spectralogic.ds3client.utils.Guard;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.util.HashMap;
import java.util.Map;

public class RangedSeekableByteChannel implements SeekableByteChannel {

    private final SeekableByteChannel byteChannel;
    private final ImmutableMultimap<BulkObject, Range> ranges;
    private final ImmutableMap<BulkObject, Long> blobSizes;
    private final ImmutableMap<BulkObject, Long> startingOffsetForBlob;
    private final long size;
    private final String name;

    private long position;
    private boolean open;

    public static RangedSeekableByteChannel wrap(final SeekableByteChannel byteChannel, final ImmutableMultimap<BulkObject, Range> ranges, final String name) throws IOException {
        return new RangedSeekableByteChannel(byteChannel, ranges, name);
    }

    public RangedSeekableByteChannel(final SeekableByteChannel byteChannel, final ImmutableMultimap<BulkObject, Range> ranges, final String name) throws IOException {
        this.byteChannel = byteChannel;
        this.ranges = ranges;
        this.position = 0;
        this.open = true;
        this.name = name;
        this.size = getSize(byteChannel.size(), ranges);
        this.blobSizes = computesBlobSize(ranges);
        this.startingOffsetForBlob = computeRealBlobOffset(this.blobSizes);
    }

    private static ImmutableMap<BulkObject,Long> computeRealBlobOffset(final ImmutableMap<BulkObject, Long> blobLengths) {
        if (Guard.isMapNullOrEmpty(blobLengths)) {
            return ImmutableMap.of();
        }
        final Map<BulkObject, Long> realOffsets = new HashMap<>();

        final ImmutableSortedSet<BulkObject> bulkObjects = ImmutableSortedSet.copyOf(BlobComparator.create(), blobLengths.keySet());
        final ImmutableList<BulkObject> sortedList = ImmutableList.copyOf(bulkObjects);

        final int listLength = sortedList.size();

        realOffsets.put(sortedList.get(0), 0L);

        for(int i = 1; i < listLength; i++) {
            // get previous real offset, and add it's size, that's the current channels 'real' offset
            final BulkObject previous = sortedList.get(i-1);

            final long previousOffset = realOffsets.get(previous);
            final long previousSize = blobLengths.get(previous);

            realOffsets.put(sortedList.get(i), previousOffset + previousSize);
        }

        return ImmutableMap.copyOf(realOffsets);
    }

    private static ImmutableMap<BulkObject, Long> computesBlobSize(final ImmutableMultimap<BulkObject, Range> ranges) {
        if (Guard.isMultiMapNullOrEmpty(ranges)) {
            return ImmutableMap.of();
        }

        final ImmutableMap.Builder<BulkObject, Long> builder = ImmutableMap.builder();

        for (final BulkObject blob : ranges.keySet()) {
            builder.put(blob, sizeOfBulkRange(ranges.get(blob)));
        }

        return builder.build();
    }

    private static long getSize(final long channelSize, final ImmutableMultimap<BulkObject, Range> ranges) {
        if (Guard.isMultiMapNullOrEmpty(ranges)) {
            return channelSize;
        } else {
            return sizeOfBulkRange(ranges.values());
        }
    }

    private static long sizeOfBulkRange(final ImmutableCollection<Range> ranges) {
        long rangeSize = 0;
        for (final Range range : ranges) {
            rangeSize += range.getLength();
        }
        return rangeSize;
    }

    @Override
    public int read(final ByteBuffer dst) throws IOException {
        checkClosed();
        final int bytesRead = byteChannel.read(dst);
        this.position += bytesRead;
        return bytesRead;
    }

    @Override
    public int write(final ByteBuffer src) throws IOException {
        checkClosed();
        final int bytesWritten = byteChannel.write(src);
        this.position += bytesWritten;
        return bytesWritten;
    }

    @Override
    public long position() throws IOException {
        checkClosed();
        return this.position;
    }

    @Override
    public SeekableByteChannel position(final long newPosition) throws IOException {
        checkClosed();
        if (!checkRange(newPosition)) {
            throw new IllegalStateException("The requested position is outside the acceptable ranges for this stream");
        }
        this.position = newPosition;

        final BulkObject blob = getBlobFromPosition(ranges, newPosition);
        if (blob == null) {
            byteChannel.position(newPosition);
        } else {
            final long offsetInBlob = newPosition - blob.getOffset();
            final long blobOffset = startingOffsetForBlob.get(blob);

            byteChannel.position(blobOffset + offsetInBlob);
        }

        return this;
    }

    private boolean checkRange(final long position) {
        if (Guard.isMultiMapNullOrEmpty(ranges)) return true;  // this means we are reading from the stream and we do not need any range checks

        final BulkObject blob = getBlobFromPosition(ranges, position);
        if (blob == null) return false; // this means that the position we are seeking to is not in any of the blobs that we know about

        // Check to make sure that the position we are seeking to is within the size of the ranges contained in a blob
        final long blobSize = blobSizes.get(blob);
        return blob.getOffset() <= position && position < blob.getOffset() + blobSize;
    }

    private static BulkObject getBlobFromPosition(final ImmutableMultimap<BulkObject, Range> blobs, final long position) {
        if (Guard.isMultiMapNullOrEmpty(blobs)) {
            return null;
        }

        for (final BulkObject bulkObject : blobs.keySet()) {
            if (bulkObject.getOffset() <= position && position < bulkObject.getOffset() + bulkObject.getLength()) {
                return bulkObject;
            }
        }
        return null;
    }

    @Override
    public long size() throws IOException {
        checkClosed();
        return this.size;
    }

    @Override
    public SeekableByteChannel truncate(final long size) throws IOException {
        throw new TruncateNotAllowedException();
    }

    @Override
    public boolean isOpen() {
        return this.open;
    }

    @Override
    public void close() throws IOException {
        try {
            this.byteChannel.close();
        } finally {
            this.open = false;
        }
    }

    private void checkClosed() {
        if (!this.open) {
            throw new IllegalStateException("Object " + name + " already closed");
        }
    }
}
