/*
 * ******************************************************************************
 *   Copyright 2014-2019 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3client.helpers.channelbuilders;

import com.google.common.collect.Lists;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.spectrads3.GetObjectDetailsSpectraS3Request;
import com.spectralogic.ds3client.commands.spectrads3.GetObjectDetailsSpectraS3Response;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.integration.Util;
import com.spectralogic.ds3client.integration.test.helpers.TempStorageIds;
import com.spectralogic.ds3client.integration.test.helpers.TempStorageUtil;
import com.spectralogic.ds3client.models.ChecksumType;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import org.junit.*;

import java.io.*;
import java.net.URISyntaxException;
import java.util.UUID;

import static com.spectralogic.ds3client.integration.Util.deleteAllContents;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ObjectStreamBuilder_Test {

    private static final Ds3Client client = Util.fromEnv();
    private static final Ds3ClientHelpers HELPERS = Ds3ClientHelpers.wrap(client);
    private static final String TEST_ENV_NAME = "object_stream_builder_test";
    private static final String BUCKET_NAME = "object_stream_builder_bucket";
    private static final String OBJ_NAME = "stream_object_test.txt";
    private static final String OBJ_CONTENT = "Content of stream object";
    private static final byte[] OBJ_BYTES = OBJ_CONTENT.getBytes();
    private static TempStorageIds envStorageIds;
    private static UUID envDataPolicyId;

    @BeforeClass
    public static void startup() throws IOException {
        envDataPolicyId = TempStorageUtil.setupDataPolicy(TEST_ENV_NAME, false, ChecksumType.Type.MD5, client);
        envStorageIds = TempStorageUtil.setup(TEST_ENV_NAME, envDataPolicyId, client);
    }

    @AfterClass
    public static void teardown() throws IOException {
        TempStorageUtil.teardown(TEST_ENV_NAME, envStorageIds, client);
        client.close();
    }

    @Before
    public void beforeRunningTestMethod() throws IOException {
        //Create a bucket and transfer object to BP using ObjectInputStreamBuilder
        HELPERS.ensureBucketExists(BUCKET_NAME, envDataPolicyId);

        final Ds3Object obj = new Ds3Object(OBJ_NAME, OBJ_BYTES.length);
        final Ds3ClientHelpers.Job job = HELPERS.startWriteJob(BUCKET_NAME, Lists.newArrayList(obj));

        job.transfer(new ObjectInputStreamBuilder() {

            @Override
            public InputStream buildInputStream(final String key) {
                return new ByteArrayInputStream(OBJ_BYTES);
            }
        });
    }

    @After
    public void afterRunningTestMethod() throws IOException {
        deleteAllContents(client, BUCKET_NAME);
    }

    @Test
    public void objectInputStreamBuilderTest() throws IOException, URISyntaxException {
        //Verify that the object was transferred correctly to the BP in beforeRunningTestMethod
        final GetObjectDetailsSpectraS3Request objectDetailsRequest =
                new GetObjectDetailsSpectraS3Request(OBJ_NAME, BUCKET_NAME);
        final GetObjectDetailsSpectraS3Response objectDetailsResponse = client
                .getObjectDetailsSpectraS3(objectDetailsRequest);

        assertThat(objectDetailsResponse.getS3ObjectResult().getName(), is(OBJ_NAME));
    }

    @Test
    public void objectOutputStreamBuilderTest() throws IOException, URISyntaxException {
        //Retrieve the object on the BP using ObjectOutputStreamBuilder and verify contents
        final Ds3Object obj = new Ds3Object(OBJ_NAME, OBJ_BYTES.length);
        final Ds3ClientHelpers.Job job = HELPERS.startReadJob(BUCKET_NAME, Lists.newArrayList(obj));

        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        job.transfer(new ObjectOutputStreamBuilder() {

            @Override
            public OutputStream buildOutputStream(final String key) throws IOException {
                return stream;
            }
        });
        assertThat(stream.toByteArray(), is(OBJ_BYTES));
    }
}
