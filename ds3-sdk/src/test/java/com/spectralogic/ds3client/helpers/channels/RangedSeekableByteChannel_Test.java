package com.spectralogic.ds3client.helpers.channels;

import com.google.common.collect.ImmutableMultimap;
import com.spectralogic.ds3client.models.BlobApiBean;
import com.spectralogic.ds3client.models.Range;
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

        final ImmutableMultimap.Builder<BlobApiBean, Range> builder = ImmutableMultimap.builder();

        final BlobApiBean blob1 = new BlobApiBean(null, true, true, 50, "obj1.txt", 0, null, 1);
        final BlobApiBean blob2 =new BlobApiBean(null, true, true, 50, "obj1.txt", 50, null, 1);

        builder.put(blob1, Range.byLength(0, firstBlobData.limit()));
        builder.put(blob2, Range.byLength(50, secondBlobData.limit()));

        final ImmutableMultimap<BlobApiBean, Range> ranges = builder.build();

        final ByteArraySeekableByteChannel channel = new ByteArraySeekableByteChannel();
        try (final RangedSeekableByteChannel rangedChannel = new RangedSeekableByteChannel(channel, ranges)) {

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

        final ImmutableMultimap.Builder<BlobApiBean, Range> builder = ImmutableMultimap.builder();

        final BlobApiBean blob1 = new BlobApiBean(null, true, true, 50, "obj1.txt", 0, null, 1);
        final BlobApiBean blob2 =new BlobApiBean(null, true, true, 50, "obj1.txt", 50, null, 1);

        builder.put(blob1, Range.byLength(5, firstBlobData.limit()));
        builder.put(blob2, Range.byLength(60, secondBlobData.limit()));

        final ImmutableMultimap<BlobApiBean, Range> ranges = builder.build();

        final ByteArraySeekableByteChannel channel = new ByteArraySeekableByteChannel();
        try (final RangedSeekableByteChannel rangedChannel = new RangedSeekableByteChannel(channel, ranges)) {

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
        final ImmutableMultimap.Builder<BlobApiBean, Range> builder = ImmutableMultimap.builder();

        final BlobApiBean blob1 = new BlobApiBean(null, true, true, 20, "obj1.txt", 0, null, 1);
        final BlobApiBean blob2 =new BlobApiBean(null, true, true, 20, "obj1.txt", 20, null, 1);

        builder.put(blob1, Range.byPosition(5, 15));
        builder.put(blob2, Range.byPosition(20, 10));

        final ImmutableMultimap<BlobApiBean, Range> ranges = builder.build();

        final ByteArraySeekableByteChannel channel = new ByteArraySeekableByteChannel();
        try (final RangedSeekableByteChannel rangedChannel = new RangedSeekableByteChannel(channel, ranges)) {
            rangedChannel.position(17);
        }
    }
}
