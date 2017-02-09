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

package com.spectralogic.ds3client.helpers.channels;

import com.google.common.collect.ImmutableMultimap;
import com.spectralogic.ds3client.models.BulkObject;
import com.spectralogic.ds3client.models.common.Range;
import com.spectralogic.ds3client.utils.ByteArraySeekableByteChannel;
import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class RangedSeekableByteChannel_Test {

    @Test
    public void multipleRangesWithMultipleBlobs() throws IOException {
        final String firstPartialBlob = "First blob text";
        final String secondPartialBlob = "Second Blob text";

        final ByteBuffer firstBlobData = ByteBuffer.wrap(firstPartialBlob.getBytes(Charset.forName("UTF-8")));
        final ByteBuffer secondBlobData = ByteBuffer.wrap(secondPartialBlob.getBytes(Charset.forName("UTF-8")));

        final ImmutableMultimap.Builder<BulkObject, Range> builder = ImmutableMultimap.builder();

        final BulkObject blob1 = new BulkObject();
        blob1.setName("obj1.txt");
        blob1.setLength(50);
        blob1.setInCache(true);
        blob1.setOffset(0);

        final BulkObject blob2 = new BulkObject();
        blob2.setName("obj1.txt");
        blob2.setLength(50);
        blob2.setInCache(true);
        blob2.setOffset(50);

        builder.put(blob1, Range.byLength(0, firstBlobData.limit()));
        builder.put(blob2, Range.byLength(50, secondBlobData.limit()));

        final ImmutableMultimap<BulkObject, Range> ranges = builder.build();

        final ByteArraySeekableByteChannel channel = new ByteArraySeekableByteChannel();
        try (final RangedSeekableByteChannel rangedChannel = new RangedSeekableByteChannel(channel, ranges, "name")) {

            rangedChannel.position(50).write(secondBlobData);
            rangedChannel.position(0).write(firstBlobData);

            final String message = channel.toString();
            assertThat(message, is(firstPartialBlob + secondPartialBlob));

        } catch (final IllegalStateException e) {
            fail("This should not return an error");
        }
    }

    @Test
    public void multipleRangesNotOnBlobBoundryWithMultipleBlobs() throws IOException {
        final String firstPartialBlob = "First blob text";
        final String secondPartialBlob = "Second Blob text";

        final ByteBuffer firstBlobData = ByteBuffer.wrap(firstPartialBlob.getBytes(Charset.forName("UTF-8")));
        final ByteBuffer secondBlobData = ByteBuffer.wrap(secondPartialBlob.getBytes(Charset.forName("UTF-8")));

        final ImmutableMultimap.Builder<BulkObject, Range> builder = ImmutableMultimap.builder();

        final BulkObject blob1 = new BulkObject();
        blob1.setName("obj1.txt");
        blob1.setLength(50);
        blob1.setInCache(true);
        blob1.setOffset(0);

        final BulkObject blob2 = new BulkObject();
        blob2.setName("obj1.txt");
        blob2.setLength(50);
        blob2.setInCache(true);
        blob2.setOffset(50);

        builder.put(blob1, Range.byLength(5, firstBlobData.limit()));
        builder.put(blob2, Range.byLength(60, secondBlobData.limit()));

        final ImmutableMultimap<BulkObject, Range> ranges = builder.build();

        final ByteArraySeekableByteChannel channel = new ByteArraySeekableByteChannel();
        try (final RangedSeekableByteChannel rangedChannel = new RangedSeekableByteChannel(channel, ranges, "name")) {

            rangedChannel.position(50).write(secondBlobData);
            rangedChannel.position(0).write(firstBlobData);

            final String message = channel.toString();
            assertThat(message, is(firstPartialBlob + secondPartialBlob));

        } catch (final IllegalStateException e) {
            fail("This should not return an error");
        }
    }

    @Test(expected = IllegalStateException.class)
    public void seekToInvalidPosition() throws IOException {
        final ImmutableMultimap.Builder<BulkObject, Range> builder = ImmutableMultimap.builder();

        final BulkObject blob1 = new BulkObject();
        blob1.setName("obj1.txt");
        blob1.setLength(20);
        blob1.setInCache(true);
        blob1.setOffset(0);

        final BulkObject blob2 = new BulkObject();
        blob2.setName("obj1.txt");
        blob2.setLength(20);
        blob2.setInCache(true);
        blob2.setOffset(20);

        builder.put(blob1, Range.byPosition(5, 15));
        builder.put(blob2, Range.byPosition(20, 10));

        final ImmutableMultimap<BulkObject, Range> ranges = builder.build();

        final ByteArraySeekableByteChannel channel = new ByteArraySeekableByteChannel();
        try (final RangedSeekableByteChannel rangedChannel = new RangedSeekableByteChannel(channel, ranges, "name")) {
            rangedChannel.position(17);
        }
    }
}
