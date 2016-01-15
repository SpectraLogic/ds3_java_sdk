package com.spectralogic.ds3client.helpers.util;

import com.google.common.collect.*;
import com.spectralogic.ds3client.models.BlobApiBean;
import com.spectralogic.ds3client.models.JobChunkApiBean;
import com.spectralogic.ds3client.models.Range;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.models.bulk.PartialDs3Object;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

public class PartialObjectHelpers_Test {

    @Test
    public void getRanges() {
        final List<Ds3Object> objects = Lists.newArrayList();
        objects.add(new Ds3Object("obj1.txt"));
        objects.add(new PartialDs3Object("obj2.txt", Range.byLength(10, 200)));
        final ImmutableMultimap<String, Range> ranges = PartialObjectHelpers.getPartialObjectsRanges(objects);
        assertFalse(ranges.isEmpty());
        assertThat(ranges.size(), is(1));
        final ImmutableList<Range> obj2Ranges = ImmutableList.copyOf(ranges.get("obj2.txt"));
        assertThat(obj2Ranges.size(), is(1));
        final Range range = obj2Ranges.get(0);
        assertThat(range, is(notNullValue()));
        assertThat(range.getStart(), is(10L));
        assertThat(range.getEnd(), is(209L));
    }

    @Test
    public void getMultipleRanges() {
        final List<Ds3Object> objects = Lists.newArrayList();
        objects.add(new PartialDs3Object("obj2.txt", Range.byLength(0, 5)));
        objects.add(new PartialDs3Object("obj2.txt", Range.byLength(10, 200)));
        objects.add(new PartialDs3Object("obj2.txt", Range.byLength(300, 100)));
        final ImmutableMultimap<String, Range> ranges = PartialObjectHelpers.getPartialObjectsRanges(objects);
        assertFalse(ranges.isEmpty());
        assertThat(ranges.size(), is(3));
        final ImmutableList<Range> obj2Ranges = ranges.get("obj2.txt").asList();
        assertThat(obj2Ranges.size(), is(3));
        Range range = obj2Ranges.get(0);
        assertThat(range, is(notNullValue()));
        assertThat(range.getStart(), is(0L));
        assertThat(range.getEnd(), is(4L));

        range = obj2Ranges.get(1);
        assertThat(range, is(notNullValue()));
        assertThat(range.getStart(), is(10L));
        assertThat(range.getEnd(), is(209L));

        range = obj2Ranges.get(2);
        assertThat(range, is(notNullValue()));
        assertThat(range.getStart(), is(300L));
        assertThat(range.getEnd(), is(399L));
    }

    @Test
    public void multipleObjectsWithRanges() {
        final List<Ds3Object> objects = Lists.newArrayList();
        objects.add(new PartialDs3Object("obj2.txt", Range.byLength(0, 5)));
        objects.add(new PartialDs3Object("obj2.txt", Range.byLength(10, 200)));
        objects.add(new PartialDs3Object("obj1.txt", Range.byLength(0, 100)));
        final ImmutableMultimap<String, Range> ranges = PartialObjectHelpers.getPartialObjectsRanges(objects);
        assertFalse(ranges.isEmpty());
        assertThat(ranges.size(), is(3));
        final ImmutableList<Range> obj1Ranges = ranges.get("obj1.txt").asList();
        assertThat(obj1Ranges.size(), is(1));

        Range range = obj1Ranges.get(0);
        assertThat(range, is(notNullValue()));
        assertThat(range.getStart(), is(0L));
        assertThat(range.getEnd(), is(99L));

        final ImmutableList<Range> obj2Ranges = ranges.get("obj2.txt").asList();
        assertThat(obj2Ranges.size(), is(2));

        range = obj2Ranges.get(0);
        assertThat(range, is(notNullValue()));
        assertThat(range.getStart(), is(0L));
        assertThat(range.getEnd(), is(4L));

        range = obj2Ranges.get(1);
        assertThat(range, is(notNullValue()));
        assertThat(range.getStart(), is(10L));
        assertThat(range.getEnd(), is(209L));
    }

    @Test
    public void testBasicBlobToRange() {
        final List<Ds3Object> objects = Lists.newArrayList();
        objects.add(new PartialDs3Object("obj2.txt", Range.byLength(0, 5)));
        objects.add(new PartialDs3Object("obj2.txt", Range.byLength(10, 200)));
        objects.add(new PartialDs3Object("obj1.txt", Range.byLength(0, 100)));
        final ImmutableMultimap<String, Range> ranges = PartialObjectHelpers.getPartialObjectsRanges(objects);

        List<BlobApiBean> blobs = Lists.newArrayList();
        blobs.add(new BlobApiBean(null, true, true, 100, "obj1.txt", 0, null, 1));
        blobs.add(new BlobApiBean(null, true, true, 100, "obj2.txt", 0, null, 1));

        final JobChunkApiBean chunk1 =  new JobChunkApiBean(null, 1, null, blobs);
        chunk1.setObjects(blobs);

        blobs = Lists.newArrayList();
        blobs.add(new BlobApiBean(null, true, true, 200, "obj2.txt", 100, null, 1));

        final JobChunkApiBean chunk2 = new JobChunkApiBean(null, 0, null, blobs);
        chunk2.setObjects(blobs);

        final List<JobChunkApiBean> chunks = Lists.newArrayList();
        chunks.add(chunk1);
        chunks.add(chunk2);

        final ImmutableMap<String, ImmutableMultimap<BlobApiBean, Range>> blobToRangeMapping = PartialObjectHelpers.mapRangesToBlob(chunks, ranges);

        assertFalse(blobToRangeMapping.isEmpty());
        assertThat(blobToRangeMapping.size(), is(2));

        final ImmutableCollection<Range> obj1Blob = blobToRangeMapping.get("obj1.txt").get(new BlobApiBean(null, true, true, 100, "obj1.txt", 0, null, 1));
        assertThat(obj1Blob, is(notNullValue()));
        assertThat(obj1Blob.size(), is(1));

        Range range = obj1Blob.asList().get(0);
        assertThat(range.getStart(), is(0L));
        assertThat(range.getEnd(), is(99L));

        final ImmutableCollection<Range> obj2Blob1 = blobToRangeMapping.get("obj2.txt").get(new BlobApiBean(null, true, true, 100, "obj2.txt", 0, null, 1));
        assertThat(obj2Blob1, is(notNullValue()));
        assertThat(obj2Blob1.size(), is(2));

        range = obj2Blob1.asList().get(0);
        assertThat(range.getStart(), is(0L));
        assertThat(range.getEnd(), is(4L));

        range = obj2Blob1.asList().get(1);
        assertThat(range.getStart(), is(10L));
        assertThat(range.getEnd(), is(99L));

        final ImmutableCollection<Range> obj2Blob2 = blobToRangeMapping.get("obj2.txt").get(new BlobApiBean(null, true, true, 200, "obj2.txt",100, null, 1));
        assertThat(obj2Blob2, is(notNullValue()));
        assertThat(obj2Blob2.size(), is(1));

        range = obj2Blob2.asList().get(0);
        assertThat(range.getStart(), is(100L));
        assertThat(range.getEnd(), is(209L));
    }

    @Test
    public void combineTwoOverlappingRanges() {
        final ImmutableList<Range> ranges = PartialObjectHelpers.dedupRanges(ImmutableList.of(Range.byPosition(5, 10), Range.byPosition(9, 14)));

        assertThat(ranges.size(), is(1));
        final Range range = ranges.get(0);
        assertThat(range.getStart(), is(5L));
        assertThat(range.getEnd(), is(14L));
    }

    @Test
    public void combineThreeOverlappingRanges() {
        final ImmutableList<Range> ranges = PartialObjectHelpers.dedupRanges(ImmutableList.of(Range.byPosition(5, 10), Range.byPosition(9, 14), Range.byPosition(11, 20)));

        assertThat(ranges.size(), is(1));
        final Range range = ranges.get(0);
        assertThat(range.getStart(), is(5L));
        assertThat(range.getEnd(), is(20L));
    }

    @Test
    public void combineThreeOverlappingRangesInRandomOrder() {
        final ImmutableList<Range> ranges = PartialObjectHelpers.dedupRanges(ImmutableList.of(Range.byPosition(11, 20), Range.byPosition(5, 10), Range.byPosition(9, 14)));

        assertThat(ranges.size(), is(1));
        final Range range = ranges.get(0);
        assertThat(range.getStart(), is(5L));
        assertThat(range.getEnd(), is(20L));
    }

    @Test
    public void checkSorted() {
        final ImmutableList<Range> ranges = PartialObjectHelpers.dedupRanges(ImmutableList.of(Range.byPosition(11, 20), Range.byPosition(5, 10)));

        assertThat(ranges.size(), is(2));
        Range range = ranges.get(0);
        assertThat(range.getStart(), is(5L));
        assertThat(range.getEnd(), is(10L));

        range = ranges.get(1);
        assertThat(range.getStart(), is(11L));
        assertThat(range.getEnd(), is(20L));
    }

    @Test
    public void singleRangeValue() {
        final ImmutableList<Range> ranges = PartialObjectHelpers.dedupRanges(ImmutableList.of(Range.byPosition(5, 20)));

        assertThat(ranges.size(), is(1));
        final Range range = ranges.get(0);
        assertThat(range.getStart(), is(5L));
        assertThat(range.getEnd(), is(20L));
    }

    @Test
    public void multiBlobSingleRangeMapping() {

        final List<JobChunkApiBean> objects = Lists.newArrayList();
        final JobChunkApiBean objs1 = new JobChunkApiBean(null, 0, null, Lists.newArrayList(
                new BlobApiBean(null, true, true, 10, "obj1.txt", 0, null, 1)));
        final JobChunkApiBean objs2 = new JobChunkApiBean(null, 0, null, Lists.newArrayList(
                new BlobApiBean(null, true, true, 10, "obj1.txt", 10, null, 1)));
        final JobChunkApiBean objs3 = new JobChunkApiBean(null, 0, null, Lists.newArrayList(
                new BlobApiBean(null, true, true, 10, "obj1.txt", 20, null, 1)));

        objects.add(objs1);
        objects.add(objs2);
        objects.add(objs3);

        final ImmutableMap<String, ImmutableMultimap<BlobApiBean, Range>> ranges = PartialObjectHelpers.mapRangesToBlob(objects, ImmutableMultimap.of("obj1.txt", Range.byLength(0, 30)));
        assertThat(ranges.size(), is(1));
        final ImmutableMultimap<BlobApiBean, Range> object = ranges.get("obj1.txt");
        for (final BlobApiBean obj : object.keySet()) {
            assertThat(object.get(obj).size(), is(1));
            if (obj.getOffset() == 10) {
                final Range range = object.get(obj).asList().get(0);
                assertThat(range.getStart(), is(10L));
                assertThat(range.getLength(), is(10L));
                assertThat(range.getEnd(), is(19L));
            }
        }
    }
}
