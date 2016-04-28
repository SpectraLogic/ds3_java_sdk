package com.spectralogic.ds3client.commands;

import com.spectralogic.ds3client.models.common.Range;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class GetObjectRequest_Test {

    @Test
    public void singleRangeFormat() {
       final GetObjectRequest request = new GetObjectRequest(
               "bucketName",
               "objectName",
               null,
               UUID.randomUUID(),
               0);

        request.withByteRanges(Range.byPosition(0, 100));

        final List<String> rangeHeader = new ArrayList<>();
        rangeHeader.addAll(request.getHeaders().get("Range"));

        assertThat(rangeHeader, is(notNullValue()));
        assertThat(rangeHeader.size(), is(1));
        assertThat(rangeHeader.get(0), is("bytes=0-100"));
    }

    @Test
    public void multipleRangeFormat() {
        final GetObjectRequest request = new GetObjectRequest(
                "bucketName",
                "objectName",
                null,
                UUID.randomUUID(),
                0);

        request.withByteRanges(Range.byPosition(0, 100), Range.byPosition(150, 200));

        final List<String> rangeHeader = new ArrayList<>();
        rangeHeader.addAll(request.getHeaders().get("Range"));

        assertThat(rangeHeader, is(notNullValue()));
        assertThat(rangeHeader.size(), is(1));
        assertThat(rangeHeader.get(0), is("bytes=0-100,150-200"));
    }
}
