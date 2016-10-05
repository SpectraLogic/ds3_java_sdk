/*
 * ****************************************************************************
 *    Copyright 2014-2016 Spectra Logic Corporation. All Rights Reserved.
 *    Licensed under the Apache License, Version 2.0 (the "License"). You may not use
 *    this file except in compliance with the License. A copy of the License is located at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *    or in the "license" file accompanying this file.
 *    This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 *    CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *    specific language governing permissions and limitations under the License.
 *  ****************************************************************************
 */

package com.spectralogic.ds3client.helpers;

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.Ds3ClientImpl;
import com.spectralogic.ds3client.integration.Util;
import com.spectralogic.ds3client.integration.test.helpers.Ds3ClientShim;
import com.spectralogic.ds3client.integration.test.helpers.TempStorageIds;
import com.spectralogic.ds3client.integration.test.helpers.TempStorageUtil;
import com.spectralogic.ds3client.models.ChecksumType;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.utils.ResourceUtils;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static com.spectralogic.ds3client.integration.Util.deleteAllContents;

public class JobImpl_Test {
    private static final Logger LOG = LoggerFactory.getLogger(JobImpl_Test.class);

    private static final Ds3Client client = Util.fromEnv();
    private static final Ds3ClientHelpers HELPERS = Ds3ClientHelpers.wrap(client);
    private static final String BUCKET_NAME = "Get_Job_Management_Test";
    private static final String TEST_ENV_NAME = "GetJobManagement_Test";
    private static TempStorageIds envStorageIds;
    private static UUID dataPolicyId;

    @BeforeClass
    public static void startup() throws Exception {
        dataPolicyId = TempStorageUtil.setupDataPolicy(TEST_ENV_NAME, false, ChecksumType.Type.MD5, client);
        envStorageIds = TempStorageUtil.setup(TEST_ENV_NAME, dataPolicyId, client);
        setupBucket(dataPolicyId);
    }

    @AfterClass
    public static void teardown() throws IOException {
        try {
            deleteAllContents(client, BUCKET_NAME);
        } finally {
            TempStorageUtil.teardown(TEST_ENV_NAME, envStorageIds, client);
            client.close();
        }
    }

    /**
     * Creates the test bucket with the specified data policy to prevent cascading test failure
     * when there are multiple data policies
     */
    private static void setupBucket(final UUID dataPolicy) {
        try {
            HELPERS.ensureBucketExists(BUCKET_NAME, dataPolicy);
        } catch (final Exception e) {
            LOG.error("Setting up test environment failed: " + e.getMessage());
        }
    }

    @Test
    /**
     * This test verifies that object completed callback handler are added to the correct set.
     * There are 2 sets: 1 for events we create to let us know when to close channels we have opened;
     * and 1 for events clients have registered.  It's important that we fire the events that close
     * channels before calling client events, so that clients can rely on the channels having been
     * closed.
     */
    public void testWriteObjectCompletionEventsPopulateCorrectJobPartTracker()
            throws IOException, URISyntaxException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, NoSuchFieldException, ClassNotFoundException
    {
        final Ds3ClientShim ds3ClientShim = new Ds3ClientShim((Ds3ClientImpl) client);

        long bookSize = -1;

        final String DIR_NAME = "largeFiles/";
        final String[] FILE_NAMES = new String[]{"lesmis-copies.txt"};

        final List<String> bookTitles = new ArrayList<>();
        final List<Ds3Object> objects = new ArrayList<>();
        for (final String book : FILE_NAMES) {
            final Path objPath = ResourceUtils.loadFileResource(DIR_NAME + book);
            bookSize = Files.size(objPath);
            final Ds3Object obj = new Ds3Object(book, bookSize);

            bookTitles.add(book);
            objects.add(obj);
        }

        final int maxNumBlockAllocationRetries = 1;
        final int maxNumObjectTransferAttempts = 3;
        final Ds3ClientHelpers ds3ClientHelpers = Ds3ClientHelpers.wrap(ds3ClientShim,
                maxNumBlockAllocationRetries,
                maxNumObjectTransferAttempts);

        final Ds3ClientHelpers.Job writeJob = ds3ClientHelpers.startWriteJob(BUCKET_NAME, objects);

        final Interator interator = new Interator();

        writeJob.attachObjectCompletedListener(new ObjectCompletedListener() {
            private int numCompletedObjects = 0;

            @Override
            public void objectCompleted(final String name) {
                interator.increment();
                assertTrue(bookTitles.contains(name));
                assertEquals(1, ++numCompletedObjects);
            }
        });

        // Check that the client has one callback registered
        final Field partTrackerField = writeJob.getClass().getSuperclass().getDeclaredField("jobPartTracker");
        partTrackerField.setAccessible(true);
        final JobImpl.JobPartTrackerDecorator jobPartTrackerDecorator = (JobImpl.JobPartTrackerDecorator)partTrackerField.get(writeJob);

        final Field clientJobPartTrackerField = jobPartTrackerDecorator.getClass().getDeclaredField("clientJobPartTracker");
        clientJobPartTrackerField.setAccessible(true);
        final JobPartTrackerImpl clientJobPartTrackerImpl = (JobPartTrackerImpl)clientJobPartTrackerField.get(jobPartTrackerDecorator);

        final Field clientTackersField = clientJobPartTrackerImpl.getClass().getDeclaredField("trackers");
        clientTackersField.setAccessible(true);
        final Map<String, ObjectPartTracker> clientTrackers = (Map<String, ObjectPartTracker>)clientTackersField.get(clientJobPartTrackerImpl);
        final ObjectPartTrackerImpl clientObjectPartTrackerImpl = (ObjectPartTrackerImpl)clientTrackers.get(FILE_NAMES[0]);

        final Field clientObjectCompletedListenersField = clientObjectPartTrackerImpl.getClass().getDeclaredField("objectCompletedListeners");
        clientObjectCompletedListenersField.setAccessible(true);
        final Set<ObjectCompletedListener> clientObjectCompletedListeners = (Set<ObjectCompletedListener>)clientObjectCompletedListenersField.get(clientObjectPartTrackerImpl);

        assertEquals(1, clientObjectCompletedListeners.size());

        // Check that we have no internal callbacks registered.  Registering internal callbacks doesn't happen until you
        // call transfer

        final Field internalJobPartTrackerField = jobPartTrackerDecorator.getClass().getDeclaredField("internalJobPartTracker");
        internalJobPartTrackerField.setAccessible(true);
        final JobPartTrackerImpl internalJobPartTrackerImpl = (JobPartTrackerImpl)internalJobPartTrackerField.get(jobPartTrackerDecorator);

        final Field internalTackersField = internalJobPartTrackerImpl.getClass().getDeclaredField("trackers");
        internalTackersField.setAccessible(true);
        final Map<String, ObjectPartTracker> internalTrackers = (Map<String, ObjectPartTracker>)internalTackersField.get(internalJobPartTrackerImpl);
        final ObjectPartTrackerImpl internalObjectPartTrackerImpl = (ObjectPartTrackerImpl)internalTrackers.get(FILE_NAMES[0]);

        final Field internalObjectCompletedListenersField = internalObjectPartTrackerImpl.getClass().getDeclaredField("objectCompletedListeners");
        internalObjectCompletedListenersField.setAccessible(true);
        final Set<ObjectCompletedListener> internalObjectCompletedListeners = (Set<ObjectCompletedListener>)internalObjectCompletedListenersField.get(internalObjectPartTrackerImpl);

        assertEquals(0, internalObjectCompletedListeners.size());

        // trigger the callback

        jobPartTrackerDecorator.completePart(FILE_NAMES[0], new ObjectPart(0, bookSize));

        assertEquals(1, interator.getValue());
    }

    private static class Interator {
        private int intValue = 0;

        private int increment() {
            return ++intValue;
        }

        private int getValue() {
            return intValue;
        }
    }
}
