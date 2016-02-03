/*
 * ******************************************************************************
 *   Copyright 2014-2015 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3client.serializer;

import com.google.common.collect.ImmutableList;
import com.spectralogic.ds3client.models.*;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.models.bulk.Ds3ObjectList;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;

public class XmlOutput_Test {

    @Test
    public void singleList() throws IOException {
        final String xmlResponse = "<MasterObjectList BucketName=\"lib\" JobId=\"9652a41a-218a-4158-af1b-064ab9e4ef71\" Priority=\"NORMAL\" RequestType=\"PUT\" StartDate=\"2014-07-29T16:08:39.000Z\"><Nodes><Node EndPoint=\"FAILED_TO_DETERMINE_DATAPATH_IP_ADDRESS\" HttpPort=\"80\" HttpsPort=\"443\" Id=\"b18ee082-1352-11e4-945e-080027ebeb6d\"/></Nodes><Objects ChunkId=\"cfa3153f-57de-41c7-b1fb-f30fa4154232\" ChunkNumber=\"0\"><Object Name=\"file2\" InCache=\"false\" Length=\"1202\" Offset=\"0\"/><Object Name=\"file1\" InCache=\"false\" Length=\"256\" Offset=\"0\"/><Object Name=\"file3\" InCache=\"false\" Length=\"2523\" Offset=\"0\"/></Objects></MasterObjectList>";

        final JobWithChunksApiBean masterObjectList = XmlOutput.fromXml(xmlResponse, JobWithChunksApiBean.class);
        assertThat(masterObjectList, is(notNullValue()));
        assertThat(masterObjectList.getObjects(), is(notNullValue()));
        final List<JobChunkApiBean> objectsList = masterObjectList.getObjects();
        assertThat(objectsList.size(), is(1));
        final JobChunkApiBean objects = objectsList.get(0);
        final List<BulkObject> objectList = objects.getObjects();
        assertThat(objectList.size(), is(3));
    }

    @Test
    public void bucketList() throws IOException {
        final String xmlResponse = "<ListBucketResult xmlns=\"http://s3.amazonaws.com/doc/2006-03-01/\"><Name>remoteTest16</Name><Prefix/><Marker/><MaxKeys>1000</MaxKeys><IsTruncated>false</IsTruncated><Contents><Key>user/hduser/gutenberg/20417.txt.utf-8</Key><LastModified>2014-01-03T13:26:47.000Z</LastModified><ETag>NOTRETURNED</ETag><Size>674570</Size><StorageClass>STANDARD</StorageClass><Owner><ID>ryan</ID><DisplayName>ryan</DisplayName></Owner></Contents><Contents><Key>user/hduser/gutenberg/5000.txt.utf-8</Key><LastModified>2014-01-03T13:26:47.000Z</LastModified><ETag>NOTRETURNED</ETag><Size>1423803</Size><StorageClass>STANDARD</StorageClass><Owner><ID>ryan</ID><DisplayName>ryan</DisplayName></Owner></Contents><Contents><Key>user/hduser/gutenberg/4300.txt.utf-8</Key><LastModified>2014-01-03T13:26:47.000Z</LastModified><ETag>NOTRETURNED</ETag><Size>1573150</Size><StorageClass>STANDARD</StorageClass><Owner><ID>ryan</ID><DisplayName>ryan</DisplayName></Owner></Contents></ListBucketResult>";

        final ListBucketResult result = XmlOutput.fromXml(xmlResponse, ListBucketResult.class);
        assertThat(result, is(notNullValue()));
        assertThat(result.getName(), is("remoteTest16"));
        assertThat(result.getContentsList(), is(notNullValue()));
        assertThat(result.getContentsList().size(), is(3));
        assertThat(result.getContentsList().get(0).getSize(), is(674570L));
    }

    @Test
    public void bucketListWithEmptyContents() throws IOException {
        final String xmlResponse = "<ListBucketResult xmlns=\"http://s3.amazonaws.com/doc/2006-03-01/\"><Name>remoteTest16</Name><Prefix/><Marker/><MaxKeys>1000</MaxKeys><IsTruncated>false</IsTruncated></ListBucketResult>";

        final ListBucketResult result = XmlOutput.fromXml(xmlResponse, ListBucketResult.class);
        assertThat(result, is(notNullValue()));
        assertThat(result.getContentsList(), is(notNullValue()));
    }

    @Test
    public void toXmlWithNoFilter() throws XmlProcessingException {
        final String expectedString = "<Objects><Object Name=\"file1\" Size=\"12\"/><Object Name=\"file2\" Size=\"5022\"/></Objects>";
        final List<Ds3Object> objectList = ImmutableList.of(new Ds3Object("file1", 12), new Ds3Object("file2", 5022)).asList();
        final Ds3ObjectList ds3ObjectList = new Ds3ObjectList(objectList);
        final String result = XmlOutput.toXml(ds3ObjectList, true);

        assertThat(result, is(expectedString));
    }

    @Test
    public void toXmlWithFilter() throws XmlProcessingException {
        final String expectedString = "<Objects><Object Name=\"file1\"/><Object Name=\"file2\"/></Objects>";
        final List<Ds3Object> objectList = ImmutableList.of(new Ds3Object("file1", 12), new Ds3Object("file2", 5022)).asList();
        final Ds3ObjectList ds3ObjectList = new Ds3ObjectList(objectList);
        final String result = XmlOutput.toXml(ds3ObjectList, false);

        assertThat(result, is(expectedString));
    }

    @Test
    public void toXmlWithPriority() throws XmlProcessingException {
        final String expectedString = "<Objects Priority=\"HIGH\"><Object Name=\"file1\" Size=\"12\"/><Object Name=\"file2\" Size=\"5022\"/></Objects>";
        final List<Ds3Object> objectList = ImmutableList.of(new Ds3Object("file1", 12), new Ds3Object("file2", 5022)).asList();
        final Ds3ObjectList ds3ObjectList = new Ds3ObjectList(objectList);
        ds3ObjectList.setPriority(BlobStoreTaskPriority.HIGH);
        final String result = XmlOutput.toXml(ds3ObjectList, true);

        assertThat(result, is(expectedString));
    }

    @Test(expected = java.io.IOException.class)
    public void fromXmlWithInvalidElementThrowsExceptionInDevBuild() throws IOException {
        assumeFalse(XmlOutput.isProductionBuild());

        final String xmlResponse = "<Object Name=\"file1\" InCache=\"false\" Length=\"256\" Offset=\"0\" TheAnswerToEverything=\"42\" />";
        XmlOutput.fromXml(xmlResponse, com.spectralogic.ds3client.models.bulk.BulkObject.class);
    }

    @Test
    public void fromXmlWithInvalidElementIgnoredInProductionBuild() throws IOException {
        assumeTrue(XmlOutput.isProductionBuild());

        final String xmlResponse = "<Object Name=\"file1\" InCache=\"false\" Length=\"256\" Offset=\"0\" TheAnswerToEverything=\"42\" />";
        XmlOutput.fromXml(xmlResponse, com.spectralogic.ds3client.models.bulk.BulkObject.class);
    }
}
