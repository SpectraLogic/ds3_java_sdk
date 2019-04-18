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

package com.spectralogic.ds3client.models;

import com.google.common.collect.ImmutableList;
import com.spectralogic.ds3client.models.multipart.CompleteMultipartUpload;
import com.spectralogic.ds3client.models.multipart.Part;
import com.spectralogic.ds3client.serializer.XmlOutput;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class ModelParsing_Test {

    @Test
    public void listBucketResultParseTest() throws IOException {
        final String input = "<ListBucketResult><Contents><ETag>test</ETag><Key>movies/movie1.mov</Key><LastModified/>" +
                "<Owner><DisplayName>jason</DisplayName><ID>04531ac9-6639-4bee-8c09-9c8f0fbdbdcb</ID></Owner>" +
                "<Size>0</Size><StorageClass/></Contents><Contents><ETag/><Key>movies/movie2.mov</Key><LastModified/>" +
                "<Owner><DisplayName>jason</DisplayName><ID>04531ac9-6639-4bee-8c09-9c8f0fbdbdcb</ID></Owner>" +
                "<Size>0</Size><StorageClass/></Contents><CreationDate>2016-01-21T01:14:32.000Z</CreationDate>" +
                "<Delimiter>/</Delimiter><IsTruncated>true</IsTruncated><Marker>movies/cover.jpg</Marker>" +
                "<MaxKeys>2</MaxKeys><Name>bucket1</Name><NextMarker>movies/movie2.mov</NextMarker>" +
                "<Prefix>movies/</Prefix></ListBucketResult>";

        final ListBucketResult result = XmlOutput.fromXml(input, ListBucketResult.class);
        assertThat(result.getName(), is("bucket1"));
        assertThat(result.getObjects().size(), is(2));
        assertThat(result.getObjects().get(0).getETag(), is("test"));
        assertThat(result.getObjects().get(0).getKey(), is("movies/movie1.mov"));
        assertThat(result.getObjects().get(1).getETag(), is(nullValue()));
    }

    @Test
    public void listBucketResultCommonPrefixesTest() throws IOException {
        final String input = "<ListBucketResult>" +
                "<CommonPrefixes><Prefix>movies/</Prefix></CommonPrefixes>" +
                "<CommonPrefixes><Prefix>scores/</Prefix></CommonPrefixes>" +
                "<Contents><ETag/><Key>music.mp3</Key><LastModified/><Owner><DisplayName>jason</DisplayName>" +
                "<ID>04531ac9-6639-4bee-8c09-9c8f0fbdbdcb</ID></Owner><Size>0</Size><StorageClass/></Contents>" +
                "<Contents><ETag/><Key>song.mp3</Key><LastModified/><Owner><DisplayName>jason</DisplayName>" +
                "<ID>04531ac9-6639-4bee-8c09-9c8f0fbdbdcb</ID></Owner><Size>0</Size><StorageClass/></Contents>" +
                "<CreationDate>2016-01-21T01:14:32.000Z</CreationDate><Delimiter>/</Delimiter>" +
                "<IsTruncated>false</IsTruncated><Marker/><MaxKeys>1000</MaxKeys><Name>bucket1</Name><NextMarker/>" +
                "<Prefix/></ListBucketResult>";

        final ListBucketResult result = XmlOutput.fromXml(input, ListBucketResult.class);
        assertThat(result.getCommonPrefixes().size(), is(2));
        assertThat(result.getCommonPrefixes().get(0).getPrefix(), is("movies/"));
        assertThat(result.getCommonPrefixes().get(1).getPrefix(), is("scores/"));
    }

    @Test
    public void completeMultipartUploadParseTest() throws IOException {
        final String input = "<CompleteMultipartUpload>" +
                "<Part><PartNumber>1</PartNumber><ETag>a54357aff0632cce46d942af68356b38</ETag></Part>" +
                "<Part><PartNumber>2</PartNumber><ETag>0c78aef83f66abc1fa1e8477f296d394</ETag></Part>" +
                "<Part><PartNumber>3</PartNumber><ETag>8e86fe4f25cc4ddca48cc5fdcb4adb1c</ETag></Part>" +
                "</CompleteMultipartUpload>";

        final CompleteMultipartUpload result = XmlOutput.fromXml(input, CompleteMultipartUpload.class);
        assertThat(result.getParts().size(), is(3));
        assertThat(result.getParts().get(0).getPartNumber(), is(1));
        assertThat(result.getParts().get(0).geteTag(), is("a54357aff0632cce46d942af68356b38"));
        assertThat(result.getParts().get(1).getPartNumber(), is(2));
        assertThat(result.getParts().get(1).geteTag(), is("0c78aef83f66abc1fa1e8477f296d394"));
        assertThat(result.getParts().get(2).getPartNumber(), is(3));
        assertThat(result.getParts().get(2).geteTag(), is("8e86fe4f25cc4ddca48cc5fdcb4adb1c"));
    }

    @Test
    public void completeMultipartUploadToStringTest() {
        final String expected = "<CompleteMultipartUpload>" +
                "<Part><PartNumber>1</PartNumber><ETag>a54357aff0632cce46d942af68356b38</ETag></Part>" +
                "<Part><PartNumber>2</PartNumber><ETag>0c78aef83f66abc1fa1e8477f296d394</ETag></Part>" +
                "</CompleteMultipartUpload>";

        final Part part1 = new Part();
        part1.setPartNumber(1);
        part1.seteTag("a54357aff0632cce46d942af68356b38");

        final Part part2 = new Part();
        part2.setPartNumber(2);
        part2.seteTag("0c78aef83f66abc1fa1e8477f296d394");

        final ImmutableList<Part> parts = ImmutableList.of(part1, part2);
        final CompleteMultipartUpload input = new CompleteMultipartUpload();
        input.setParts(parts.asList());

        final String result = XmlOutput.toXml(input);

        assertThat(result, is(expected));
    }
}
