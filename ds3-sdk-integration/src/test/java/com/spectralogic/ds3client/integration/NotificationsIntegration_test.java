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

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.CreateBucketRequest;
import com.spectralogic.ds3client.commands.spectrads3.notifications.*;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.serializer.XmlProcessingException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.SignatureException;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class NotificationsIntegration_test {

    private static Ds3Client client;

    @BeforeClass
    public static void startup() {
        client = Util.fromEnv();
    }

    @AfterClass
    public static void teardown() throws IOException {
        client.close();
    }

    @Test
    public void objectCompletionRegistration() throws IOException, SignatureException {
        final CreateObjectCachedNotificationRegistrationSpectraS3Response response = client.createObjectCachedNotificationRegistrationSpectraS3(
                new CreateObjectCachedNotificationRegistrationSpectraS3Request("192.168.56.101"));
        assertThat(response, is(notNullValue()));
        assertThat(response.getS3ObjectCachedNotificationRegistrationResult(), is(notNullValue()));

        final UUID registrationId = response.getS3ObjectCachedNotificationRegistrationResult().getId();

        final GetObjectCachedNotificationRegistrationSpectraS3Response getResponse = client
                .getObjectCachedNotificationRegistrationSpectraS3(
                        new GetObjectCachedNotificationRegistrationSpectraS3Request(registrationId));
        assertThat(getResponse, is(notNullValue()));
        assertThat(getResponse.getS3ObjectCachedNotificationRegistrationResult(), is(notNullValue()));
        assertThat(getResponse.getS3ObjectCachedNotificationRegistrationResult().getId(), is(notNullValue()));

        assertThat(client.deleteObjectCachedNotificationRegistrationSpectraS3(
                new DeleteObjectCachedNotificationRegistrationSpectraS3Request(registrationId)), is(notNullValue()));
    }

    @Test
    public void jobCompletionRegistration() throws IOException, SignatureException {
        final CreateJobCompletedNotificationRegistrationSpectraS3Response response = client.createJobCompletedNotificationRegistrationSpectraS3(
                new CreateJobCompletedNotificationRegistrationSpectraS3Request("192.168.56.101/other"));
        assertThat(response, is(notNullValue()));
        assertThat(response.getJobCompletedNotificationRegistrationResult(), is(notNullValue()));

        final UUID registrationId = response.getJobCompletedNotificationRegistrationResult().getId();

        final GetJobCompletedNotificationRegistrationSpectraS3Response getResponse = client
                .getJobCompletedNotificationRegistrationSpectraS3(
                        new GetJobCompletedNotificationRegistrationSpectraS3Request(registrationId));
        assertThat(getResponse, is(notNullValue()));
        assertThat(getResponse.getJobCompletedNotificationRegistrationResult(), is(notNullValue()));
        assertThat(getResponse.getJobCompletedNotificationRegistrationResult().getId(), is(notNullValue()));

        assertThat(client.deleteJobCompletedNotificationRegistrationSpectraS3(new DeleteJobCompletedNotificationRegistrationSpectraS3Request(registrationId)), is(notNullValue()));
    }

    @Test
    public void jobCreateRegistration() throws IOException, SignatureException {
        final CreateJobCreatedNotificationRegistrationSpectraS3Response response = client
                .createJobCreatedNotificationRegistrationSpectraS3(
                        new CreateJobCreatedNotificationRegistrationSpectraS3Request("192.168.56.101/other"));
        assertThat(response, is(notNullValue()));
        assertThat(response.getJobCreatedNotificationRegistrationResult(), is(notNullValue()));

        final UUID registrationId = response.getJobCreatedNotificationRegistrationResult().getId();

        final GetJobCreatedNotificationRegistrationSpectraS3Response getResponse = client
                .getJobCreatedNotificationRegistrationSpectraS3(
                        new GetJobCreatedNotificationRegistrationSpectraS3Request(registrationId));
        assertThat(getResponse, is(notNullValue()));
        assertThat(getResponse.getJobCreatedNotificationRegistrationResult(), is(notNullValue()));
        assertThat(getResponse.getJobCreatedNotificationRegistrationResult().getId(), is(notNullValue()));

        assertThat(client.deleteJobCreatedNotificationRegistrationSpectraS3(
                new DeleteJobCreatedNotificationRegistrationSpectraS3Request(registrationId)), is(notNullValue()));
    }

    @Test
    public void objectLostRegistration() throws IOException, SignatureException {
        final CreateObjectLostNotificationRegistrationSpectraS3Response response = client
                .createObjectLostNotificationRegistrationSpectraS3(
                        new CreateObjectLostNotificationRegistrationSpectraS3Request("192.168.56.101/other"));
        assertThat(response, is(notNullValue()));
        assertThat(response.getS3ObjectLostNotificationRegistrationResult(), is(notNullValue()));

        final UUID registrationId = response.getS3ObjectLostNotificationRegistrationResult().getId();

        final GetObjectLostNotificationRegistrationSpectraS3Response getResponse = client
                .getObjectLostNotificationRegistrationSpectraS3(
                        new GetObjectLostNotificationRegistrationSpectraS3Request(registrationId));
        assertThat(getResponse, is(notNullValue()));
        assertThat(getResponse.getS3ObjectLostNotificationRegistrationResult(), is(notNullValue()));
        assertThat(getResponse.getS3ObjectLostNotificationRegistrationResult().getId(), is(notNullValue()));

        assertThat(client.deleteObjectLostNotificationRegistrationSpectraS3(
                new DeleteObjectLostNotificationRegistrationSpectraS3Request(registrationId)), is(notNullValue()));
    }

    @Test
    public void objectPersistedRegistration() throws IOException, SignatureException, URISyntaxException, XmlProcessingException {
        final String bucketName = "test_bucket";

        try {
            client.createBucket(new CreateBucketRequest(bucketName));
            final Ds3ClientHelpers.Job job = Util.getLoadJob(client, bucketName, Util.RESOURCE_BASE_NAME);

            final CreateObjectPersistedNotificationRegistrationSpectraS3Response response = client
                    .createObjectPersistedNotificationRegistrationSpectraS3(
                            new CreateObjectPersistedNotificationRegistrationSpectraS3Request("192.168.56.101/other")
                                    .withJobId(job.getJobId()));
            assertThat(response, is(notNullValue()));
            assertThat(response.getS3ObjectPersistedNotificationRegistrationResult(), is(notNullValue()));

            job.transfer(new ResourceObjectPutter(Util.RESOURCE_BASE_NAME));

            final UUID registrationId = response.getS3ObjectPersistedNotificationRegistrationResult().getId();

            final GetObjectPersistedNotificationRegistrationSpectraS3Response getResponse = client
                    .getObjectPersistedNotificationRegistrationSpectraS3(
                            new GetObjectPersistedNotificationRegistrationSpectraS3Request(registrationId));
            assertThat(getResponse, is(notNullValue()));
            assertThat(getResponse.getS3ObjectPersistedNotificationRegistrationResult(), is(notNullValue()));
            assertThat(getResponse.getS3ObjectPersistedNotificationRegistrationResult().getId(), is(notNullValue()));

            assertThat(client.deleteObjectPersistedNotificationRegistrationSpectraS3(
                    new DeleteObjectPersistedNotificationRegistrationSpectraS3Request(registrationId)), is(notNullValue()));
        } finally {
            Util.deleteAllContents(client, bucketName);
        }
    }

    @Test
    public void partitionFailureRegistration() throws IOException, SignatureException {
        final CreateTapePartitionFailureNotificationRegistrationSpectraS3Response response = client
                .createTapePartitionFailureNotificationRegistrationSpectraS3(
                        new CreateTapePartitionFailureNotificationRegistrationSpectraS3Request("192.168.56.101/other"));
        assertThat(response, is(notNullValue()));
        assertThat(response.getTapePartitionFailureNotificationRegistrationResult(), is(notNullValue()));

        final UUID registrationId = response.getTapePartitionFailureNotificationRegistrationResult().getId();

        final GetTapePartitionFailureNotificationRegistrationSpectraS3Response getResponse = client
                .getTapePartitionFailureNotificationRegistrationSpectraS3(
                        new GetTapePartitionFailureNotificationRegistrationSpectraS3Request(registrationId));
        assertThat(getResponse, is(notNullValue()));
        assertThat(getResponse.getTapePartitionFailureNotificationRegistrationResult(), is(notNullValue()));
        assertThat(getResponse.getTapePartitionFailureNotificationRegistrationResult().getId(), is(notNullValue()));

        assertThat(client.deleteTapePartitionFailureNotificationRegistrationSpectraS3(
                new DeleteTapePartitionFailureNotificationRegistrationSpectraS3Request(registrationId)), is(notNullValue()));
    }

    @Test
    public void tapeFailureRegistration() throws IOException, SignatureException {
        final CreateTapeFailureNotificationRegistrationSpectraS3Response response = client
                .createTapeFailureNotificationRegistrationSpectraS3(
                        new CreateTapeFailureNotificationRegistrationSpectraS3Request("192.168.56.101/other"));
        assertThat(response, is(notNullValue()));
        assertThat(response.getTapeFailureNotificationRegistrationResult(), is(notNullValue()));

        final UUID registrationId = response.getTapeFailureNotificationRegistrationResult().getId();

        final GetTapeFailureNotificationRegistrationSpectraS3Response getResponse = client
                .getTapeFailureNotificationRegistrationSpectraS3(
                        new GetTapeFailureNotificationRegistrationSpectraS3Request(registrationId));
        assertThat(getResponse, is(notNullValue()));
        assertThat(getResponse.getTapeFailureNotificationRegistrationResult(), is(notNullValue()));
        assertThat(getResponse.getTapeFailureNotificationRegistrationResult().getId(), is(notNullValue()));

        assertThat(client.deleteTapeFailureNotificationRegistrationSpectraS3(
                new DeleteTapeFailureNotificationRegistrationSpectraS3Request(registrationId)), is(notNullValue()));
    }
}
