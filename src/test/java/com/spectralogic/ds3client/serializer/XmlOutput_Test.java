/*
 * ******************************************************************************
 *   Copyright 2014 Spectra Logic Corporation. All Rights Reserved.
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

import com.spectralogic.ds3client.models.Ds3Object;
import com.spectralogic.ds3client.models.ListBucketResult;
import com.spectralogic.ds3client.models.MasterObjectList;
import com.spectralogic.ds3client.models.Objects;
import com.spectralogic.ds3client.serializer.XmlOutput;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class XmlOutput_Test {

    @Test
    public void singleList() throws IOException {
        final String xmlResponse = "<masterobjectlist><objects><object name='file1' size='256'/><object name='file2' size='1202'/><object name='file3' size='2523'/></objects></masterobjectlist>";

        final MasterObjectList masterObjectList = XmlOutput.fromXml(xmlResponse, MasterObjectList.class);
        assertThat(masterObjectList, is(notNullValue()));
        assertThat(masterObjectList.getObjects(), is(notNullValue()));
        final List<Objects> objectsList = masterObjectList.getObjects();
        assertThat(objectsList.size(), is(1));
        final Objects objects = objectsList.get(0);
        final List<Ds3Object> objectList = objects.getObject();
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
        assertThat(result.getContentsList().get(0).getSize(), is(674570l));
    }
}
