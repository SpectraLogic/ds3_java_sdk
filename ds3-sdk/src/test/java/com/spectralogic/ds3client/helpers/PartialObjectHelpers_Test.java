package com.spectralogic.ds3client.helpers;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Lists;
import com.spectralogic.ds3client.models.Range;
import com.spectralogic.ds3client.models.bulk.BulkObject;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.models.bulk.Objects;
import com.spectralogic.ds3client.models.bulk.PartialDs3Object;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;
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

        List<BulkObject> blobs = Lists.newArrayList();
        blobs.add(new BulkObject("obj1.txt", 100, true, 0));
        blobs.add(new BulkObject("obj2.txt", 100, true, 0));

        final Objects chunk1 =  new Objects();
        chunk1.setObjects(blobs);

        blobs = Lists.newArrayList();
        blobs.add(new BulkObject("obj2.txt", 200, true, 100));

        final Objects chunk2 = new Objects();
        chunk2.setObjects(blobs);

        final List<Objects> chunks = Lists.newArrayList();
        chunks.add(chunk1);
        chunks.add(chunk2);

        final ImmutableMultimap<BulkObject, Range> blobToRangeMapping = PartialObjectHelpers.mapRangesToBlob(chunks, ranges);

        assertFalse(blobToRangeMapping.isEmpty());
        assertThat(blobToRangeMapping.size(), is(4));

        final ImmutableCollection<Range> obj1Blob = blobToRangeMapping.get(new BulkObject("obj1.txt", 100, true, 0));
        assertThat(obj1Blob, is(notNullValue()));
        assertThat(obj1Blob.size(), is(1));

        Range range = obj1Blob.asList().get(0);
        assertThat(range.getStart(), is(0L));
        assertThat(range.getEnd(), is(99L));

        final ImmutableCollection<Range> obj2Blob1 = blobToRangeMapping.get(new BulkObject("obj2.txt", 100, true, 0));
        assertThat(obj2Blob1, is(notNullValue()));
        assertThat(obj2Blob1.size(), is(2));

        range = obj2Blob1.asList().get(0);
        assertThat(range.getStart(), is(0L));
        assertThat(range.getEnd(), is(4L));

        range = obj2Blob1.asList().get(1);
        assertThat(range.getStart(), is(10L));
        assertThat(range.getEnd(), is(99L));

        final ImmutableCollection<Range> obj2Blob2 = blobToRangeMapping.get(new BulkObject("obj2.txt", 200, true, 100));
        assertThat(obj2Blob2, is(notNullValue()));
        assertThat(obj2Blob2.size(), is(1));

        range = obj2Blob2.asList().get(0);
        assertThat(range.getStart(), is(100L));
        assertThat(range.getEnd(), is(209L));
    }
}
