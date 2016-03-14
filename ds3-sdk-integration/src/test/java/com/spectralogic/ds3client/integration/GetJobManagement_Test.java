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

package com.spectralogic.ds3client.integration;

import com.google.common.collect.Lists;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.*;
import com.spectralogic.ds3client.commands.spectrads3.*;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.helpers.options.ReadJobOptions;
import com.spectralogic.ds3client.integration.test.helpers.TempStorageIds;
import com.spectralogic.ds3client.integration.test.helpers.TempStorageUtil;
import com.spectralogic.ds3client.models.*;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.serializer.XmlProcessingException;
import com.spectralogic.ds3client.utils.ResourceUtils;
import org.junit.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.channels.Channels;
import java.nio.channels.SeekableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SignatureException;
import java.util.UUID;

import static com.spectralogic.ds3client.integration.Util.RESOURCE_BASE_NAME;
import static com.spectralogic.ds3client.integration.Util.deleteAllContents;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class GetJobManagement_Test {

    private static final Ds3Client client = Util.fromEnv();
    private static final Ds3ClientHelpers HELPERS = Ds3ClientHelpers.wrap(client);
    private static final String BUCKET_NAME = "Get_Job_Management_Test";
    private static final String TEST_ENV_NAME = "GetJobManagement_Test";
    private static TempStorageIds envStorageIds;

    @BeforeClass
    public static void startup() throws Exception {
        final UUID dataPolicyId = TempStorageUtil.setupDataPolicy(TEST_ENV_NAME, true, ChecksumType.Type.MD5, client);
        envStorageIds = TempStorageUtil.setup(TEST_ENV_NAME, dataPolicyId, client);
        HELPERS.ensureBucketExists(BUCKET_NAME);
        putBeowulf();
    }

    @AfterClass
    public static void teardown() throws IOException, SignatureException {
        deleteAllContents(client, BUCKET_NAME);
        TempStorageUtil.teardown(TEST_ENV_NAME, envStorageIds, client);
        client.close();
    }

    private static void waitForObjectToBeInCache(final UUID jobId) throws Exception {
        long cachedSize = 0;
        int cycles = 0;
        while (cachedSize == 0) {
            Thread.sleep(500);
            final MasterObjectList mol = client.getJobSpectraS3(new GetJobSpectraS3Request(jobId)).getMasterObjectListResult();
            cachedSize = mol.getCachedSizeInBytes();
            cycles++;
            if (cycles > 20) {
                throw new Exception("Failed to upload object to cache in 10 seconds");
            }
        }
    }

    private static void putBeowulf() throws Exception {
        final String book1 = "beowulf.txt";
        final Path objPath1 = ResourceUtils.loadFileResource(RESOURCE_BASE_NAME + book1);
        final Ds3Object obj = new Ds3Object(book1, Files.size(objPath1));
        final Ds3ClientHelpers.Job job = HELPERS.startWriteJob(BUCKET_NAME, Lists
                .newArrayList(obj));
        final UUID jobId = job.getJobId();
        final SeekableByteChannel book1Channel = new ResourceObjectPutter(RESOURCE_BASE_NAME).buildChannel(book1);
        client.putObject(new PutObjectRequest(BUCKET_NAME, book1, book1Channel, jobId, 0, Files.size(objPath1)));
        waitForObjectToBeInCache(jobId);
    }

    @SuppressWarnings("deprecation")
    @Test
    public void nakedS3Get() throws IOException, SignatureException, XmlProcessingException,
            URISyntaxException, InterruptedException {

        final WritableByteChannel writeChannel = Channels.newChannel(new OutputStream() {
            @Override
            public void write(final int b) throws IOException {
            }
        });
        final GetObjectResponse getObjectResponse = client.getObject(
                new GetObjectRequest(BUCKET_NAME, "beowulf.txt", writeChannel));

        assertThat(getObjectResponse.getStatusCode(), is(200));
    }

    @Test
    public void createReadJob() throws IOException, SignatureException, InterruptedException, XmlProcessingException, URISyntaxException {

        final Ds3ClientHelpers.Job readJob = HELPERS.startReadJob(BUCKET_NAME, Lists.newArrayList(
                new Ds3Object("beowulf.txt", 10)));

        final GetJobSpectraS3Response jobSpectraS3Response = client
                .getJobSpectraS3(new GetJobSpectraS3Request(readJob.getJobId()));

        assertThat(jobSpectraS3Response.getStatusCode(), is(200));
    }

    @Test
    public void createReadJobWithPriorityOption() throws IOException, SignatureException,
            InterruptedException, XmlProcessingException, URISyntaxException {

        final Ds3ClientHelpers.Job readJob = HELPERS.startReadJob(BUCKET_NAME, Lists.newArrayList(
                new Ds3Object("beowulf.txt", 10)), ReadJobOptions.create().withPriority(Priority.LOW));
        final GetJobSpectraS3Response jobSpectraS3Response = client
                .getJobSpectraS3(new GetJobSpectraS3Request(readJob.getJobId()));

        assertThat(jobSpectraS3Response.getMasterObjectListResult().getPriority(), is(Priority.LOW));
    }

    @Ignore("Ignore pending implementation of withName option")
    @Test
    public void createReadJobWithNameOption() throws IOException, SignatureException, XmlProcessingException,
            URISyntaxException, InterruptedException {

        //TODO: REMOVE COMMENT BELOW AND VERIFY SYNTAX FOR JOB NAME
        final Ds3ClientHelpers.Job readJob = HELPERS.startReadJob(BUCKET_NAME, Lists.newArrayList(
                new Ds3Object("beowulf.txt", 10))/*, ReadJobOptions.create().withName("test_job")*/);
        final GetJobSpectraS3Response jobSpectraS3Response = client
                .getJobSpectraS3(new GetJobSpectraS3Request(readJob.getJobId()));

        assertThat(jobSpectraS3Response.getMasterObjectListResult().getName(), is("test_job"));
    }
}