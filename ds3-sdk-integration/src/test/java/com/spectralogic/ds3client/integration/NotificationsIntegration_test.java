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
import com.spectralogic.ds3client.commands.PutBucketRequest;
import com.spectralogic.ds3client.commands.notifications.*;
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
        final NotificationResponse response = client.createObjectCachedNotification(new CreateObjectCachedNotificationRequest("192.168.56.101"));
        assertThat(response, is(notNullValue()));
        assertThat(response.getRegistration(), is(notNullValue()));

        final UUID registrationId = response.getRegistration().getId();

        final NotificationResponse getResponse = client.getObjectCachedNotification(new GetObjectCachedNotificationRequest(registrationId));
        assertThat(getResponse, is(notNullValue()));
        assertThat(getResponse.getRegistration(), is(notNullValue()));
        assertThat(getResponse.getRegistration().getId(), is(notNullValue()));

        assertThat(client.deleteObjectCachedNotification(new DeleteObjectCachedNotificationRequest(registrationId)), is(notNullValue()));

    }

    @Test
    public void jobCompletionRegistration() throws IOException, SignatureException {
        final NotificationResponse response = client.createJobCompletedNotification(new CreateJobCompletedNotificationRequest("192.168.56.101/other"));
        assertThat(response, is(notNullValue()));
        assertThat(response.getRegistration(), is(notNullValue()));

        final UUID registrationId = response.getRegistration().getId();

        final NotificationResponse getResponse = client.getJobCompletedNotification(new GetJobCompletedNotificationRequest(registrationId));
        assertThat(getResponse, is(notNullValue()));
        assertThat(getResponse.getRegistration(), is(notNullValue()));
        assertThat(getResponse.getRegistration().getId(), is(notNullValue()));

        assertThat(client.deleteJobCompleteNotification(new DeleteJobCompletedNotificationRequest(registrationId)), is(notNullValue()));
    }

    @Test
    public void jobCreateRegistration() throws IOException, SignatureException {
        final NotificationResponse response = client.createJobCreatedNotification(new CreateJobCreatedNotificationRequest("192.168.56.101/other"));
        assertThat(response, is(notNullValue()));
        assertThat(response.getRegistration(), is(notNullValue()));

        final UUID registrationId = response.getRegistration().getId();

        final NotificationResponse getResponse = client.getJobCreatedNotification(new GetJobCreatedNotificationRequest(registrationId));
        assertThat(getResponse, is(notNullValue()));
        assertThat(getResponse.getRegistration(), is(notNullValue()));
        assertThat(getResponse.getRegistration().getId(), is(notNullValue()));

        assertThat(client.deleteJobCreatedNotification(new DeleteJobCreatedNotificationRequest(registrationId)), is(notNullValue()));
    }

    @Test
    public void objectLostRegistration() throws IOException, SignatureException {
        final NotificationResponse response = client.createObjectLostNotification(new CreateObjectLostNotificationRequest("192.168.56.101/other"));
        assertThat(response, is(notNullValue()));
        assertThat(response.getRegistration(), is(notNullValue()));

        final UUID registrationId = response.getRegistration().getId();

        final NotificationResponse getResponse = client.getObjectLostNotification(new GetObjectLostNotificationRequest(registrationId));
        assertThat(getResponse, is(notNullValue()));
        assertThat(getResponse.getRegistration(), is(notNullValue()));
        assertThat(getResponse.getRegistration().getId(), is(notNullValue()));

        assertThat(client.deleteObjectLostNotification(new DeleteObjectLostNotificationRequest(registrationId)), is(notNullValue()));
    }

    @Test
    public void objectPersistedRegistration() throws IOException, SignatureException, URISyntaxException, XmlProcessingException {
        final String bucketName = "test_bucket";

        try {
            client.putBucket(new PutBucketRequest(bucketName));
            final Ds3ClientHelpers.Job job = Util.getLoadJob(client, bucketName, Util.RESOURCE_BASE_NAME);

            final NotificationResponse response = client.createObjectPersistedNotification(new CreateObjectPersistedNotificationRequest("192.168.56.101/other", job.getJobId()));
            assertThat(response, is(notNullValue()));
            assertThat(response.getRegistration(), is(notNullValue()));

            job.transfer(new ResourceObjectPutter(Util.RESOURCE_BASE_NAME));

            final UUID registrationId = response.getRegistration().getId();

            final NotificationResponse getResponse = client.getObjectPersistedNotification(new GetObjectPersistedNotificationRequest(registrationId));
            assertThat(getResponse, is(notNullValue()));
            assertThat(getResponse.getRegistration(), is(notNullValue()));
            assertThat(getResponse.getRegistration().getId(), is(notNullValue()));

            assertThat(client.deleteObjectPersistedNotification(new DeleteObjectPersistedNotificationRequest(registrationId)), is(notNullValue()));
        } finally {
            Util.deleteAllContents(client, bucketName);
        }
    }

    @Test
    public void partitionFailureRegistration() throws IOException, SignatureException {
        final NotificationResponse response = client.createPartitionFailureNotification(new CreatePartitionFailureNotificationRequest("192.168.56.101/other"));
        assertThat(response, is(notNullValue()));
        assertThat(response.getRegistration(), is(notNullValue()));

        final UUID registrationId = response.getRegistration().getId();

        final NotificationResponse getResponse = client.getPartitionFailureNotification(new GetPartitionFailureNotificationRequest(registrationId));
        assertThat(getResponse, is(notNullValue()));
        assertThat(getResponse.getRegistration(), is(notNullValue()));
        assertThat(getResponse.getRegistration().getId(), is(notNullValue()));

        assertThat(client.deletePartitionFailureNotification(new DeletePartitionFailureNotificationRequest(registrationId)), is(notNullValue()));
    }

    @Test
    public void tapeFailureRegistration() throws IOException, SignatureException {
        final NotificationResponse response = client.createTapeFailureNotification(new CreateTapeFailureNotificationRequest("192.168.56.101/other"));
        assertThat(response, is(notNullValue()));
        assertThat(response.getRegistration(), is(notNullValue()));

        final UUID registrationId = response.getRegistration().getId();

        final NotificationResponse getResponse = client.getTapeFailureNotification(new GetTapeFailureNotificationRequest(registrationId));
        assertThat(getResponse, is(notNullValue()));
        assertThat(getResponse.getRegistration(), is(notNullValue()));
        assertThat(getResponse.getRegistration().getId(), is(notNullValue()));

        assertThat(client.deleteTapeFailureNotification(new DeleteTapeFailureNotificationRequest(registrationId)), is(notNullValue()));
    }
}
