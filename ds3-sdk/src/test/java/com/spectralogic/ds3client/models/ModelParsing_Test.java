package com.spectralogic.ds3client.models;

import com.spectralogic.ds3client.serializer.XmlOutput;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class ModelParsing_Test {

    @Test
    public void listBucketResult_Parse_Test() throws IOException {
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
    public void listBucketResult_CommonPrefixes_Test() throws IOException {
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
}
