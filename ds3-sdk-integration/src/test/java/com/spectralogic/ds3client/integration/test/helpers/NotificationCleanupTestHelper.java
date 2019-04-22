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

package com.spectralogic.ds3client.integration.test.helpers;

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.spectrads3.notifications.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

/**
 * This class provides utilities for cleaning up notifications in testing
 */
public class NotificationCleanupTestHelper {

    final private static Logger LOG = LoggerFactory.getLogger(ABMTestHelper.class);

    /**
     * Deletes an Object Cached Notification Registration with the specified id and logs
     * an error if it is not deleted as expected.
     */
    public static void deleteObjectCachedNotification(final UUID id, final Ds3Client client) {
        if (id == null) {
            //This might not be an error if this function is called as part of cleanup code
            LOG.debug("Notification id is null");
            return;
        }
        try {
            client.deleteObjectCachedNotificationRegistrationSpectraS3(
                    new DeleteObjectCachedNotificationRegistrationSpectraS3Request(id));
        } catch (final IOException e) {
            LOG.error("Object Cached Notification Registration was not deleted as expected: " + id.toString());
        }
    }

    /**
     * Deletes a Job Completed Notification Registration with the specified id and logs
     * an error if it is not deleted as expected.
     */
    public static void deleteJobCompletedNotification(final UUID id, final Ds3Client client) {
        if (id == null) {
            //This might not be an error if this function is called as part of cleanup code
            LOG.debug("Notification id is null");
            return;
        }
        try {
            client.deleteJobCompletedNotificationRegistrationSpectraS3(
                    new DeleteJobCompletedNotificationRegistrationSpectraS3Request(id));
        } catch (final IOException e) {
            LOG.error("Job Completed Notification Registration was not deleted as expected: " + id.toString());
        }
    }

    /**
     * Deletes a Job Created Notification with the specified id and logs an error if
     * it is not deleted as expected
     */
    public static void deleteJobCreatedNotification(final UUID id, final Ds3Client client) {
        if (id == null) {
            //This might not be an error if this function is called as part of cleanup code
            LOG.debug("Notification id is null");
            return;
        }
        try {
            client.deleteJobCreatedNotificationRegistrationSpectraS3(
                    new DeleteJobCreatedNotificationRegistrationSpectraS3Request(id));
        } catch (final IOException e) {
            LOG.error("Job Created Notification Registration was not deleted as expected: " + id.toString());
        }
    }

    /**
     * Deletes an Object Lost Notification with the specified id and logs an error if
     * it is not deleted as expected
     */
    public static void deleteObjectLostNotification(final UUID id, final Ds3Client client) {
        if (id == null) {
            //This might not be an error if this function is called as part of cleanup code
            LOG.debug("Notification id is null");
            return;
        }
        try {
            client.deleteObjectLostNotificationRegistrationSpectraS3(
                    new DeleteObjectLostNotificationRegistrationSpectraS3Request(id));
        } catch (final IOException e) {
            LOG.error("Object Lost Notification Registration was not deleted as expected: " + id.toString());
        }
    }

    /**
     * Deletes an Object Persisted Notification with the specified id and logs an error
     * if it is not deleted as expected
     */
    public static void deleteObjectPersistedNotification(final UUID id, final Ds3Client client) {
        if (id == null) {
            //This might not be an error if this function is called as part of cleanup code
            LOG.debug("Notification id is null");
            return;
        }
        try {
            client.deleteObjectPersistedNotificationRegistrationSpectraS3(
                    new DeleteObjectPersistedNotificationRegistrationSpectraS3Request(id));
        } catch (final IOException e) {
            LOG.error("Object Persisted Notification Registration was not deleted as expected: " + id.toString());
        }
    }

    /**
     * Deletes a Tape Partition Failure Notification with the specified id and logs an
     * error if it is not deleted as expected
     */
    public static void deleteTapePartitionFailureNotification(final UUID id, final Ds3Client client) {
        if (id == null) {
            //This might not be an error if this function is called as part of cleanup code
            LOG.debug("Notification id is null");
            return;
        }
        try {
            client.deleteTapePartitionFailureNotificationRegistrationSpectraS3(
                    new DeleteTapePartitionFailureNotificationRegistrationSpectraS3Request(id));
        } catch (final IOException e) {
            LOG.error("Tape Partition Failure Notification Registration was not deleted as expected: " + id.toString());
        }
    }

    /**
     * Deletes a Tape Failure Notification with the specified id and logs an error
     * if it is not deleted as expected
     */
    public static void deleteTapeFailureNotification(final UUID id, final Ds3Client client) {
        if (id == null) {
            //This might not be an error if this function is called as part of cleanup code
            LOG.debug("Notification id is null");
            return;
        }
        try {
            client.deleteTapeFailureNotificationRegistrationSpectraS3(
                    new DeleteTapeFailureNotificationRegistrationSpectraS3Request(id));
        } catch (final IOException e) {
            LOG.error("Tape Failure Notification Registration was not deleted as expected: " + id.toString());
        }
    }
}
