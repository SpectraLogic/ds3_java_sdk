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

package com.spectralogic.ds3client.commands;

import com.spectralogic.ds3client.models.common.Range;
import com.spectralogic.ds3client.utils.ByteArraySeekableByteChannel;
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
               new ByteArraySeekableByteChannel(0),
               UUID.randomUUID().toString(),
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
                new ByteArraySeekableByteChannel(0),
                UUID.randomUUID().toString(),
                0);

        request.withByteRanges(Range.byPosition(0, 100), Range.byPosition(150, 200));

        final List<String> rangeHeader = new ArrayList<>();
        rangeHeader.addAll(request.getHeaders().get("Range"));

        assertThat(rangeHeader, is(notNullValue()));
        assertThat(rangeHeader.size(), is(1));
        assertThat(rangeHeader.get(0), is("bytes=0-100,150-200"));
    }
}
