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
import com.spectralogic.ds3client.commands.DeleteObjectRequest;
import com.spectralogic.ds3client.integration.Util;
import com.spectralogic.ds3client.integration.test.helpers.Ds3ClientShim;
import com.spectralogic.ds3client.integration.test.helpers.TempStorageIds;
import com.spectralogic.ds3client.integration.test.helpers.TempStorageUtil;
import com.spectralogic.ds3client.models.ChecksumType;
import com.spectralogic.ds3client.models.Contents;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.utils.ResourceUtils;
import com.spectralogic.ds3client.IntValue;

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

    private static final String DIR_NAME = "largeFiles/";
    private static final String[] FILE_NAMES = new String[]{"lesmis-copies.txt"};

    private long bookSize = -1;

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

    /**
     * This test verifies that object completed callback handler are added to the correct set.
     * There are 2 sets: 1 for events we create to let us know when to close channels we have opened;
     * and 1 for events clients have registered.  It's important that we fire the events that close
     * channels before calling client events, so that clients can rely on the channels having been
     * closed.
     * @throws IOException
     * @throws URISyntaxException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchFieldException
     * @throws ClassNotFoundException
     */
    @Test
    public void testWriteObjectCompletionEventsPopulateCorrectJobPartTracker()
            throws IOException, URISyntaxException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, NoSuchFieldException, ClassNotFoundException
    {
        final IntValue intValue = new IntValue();

        final List<String> bookTitles = new ArrayList<>();

        final ObjectCompletedListener objectCompletedListener = new ObjectCompletedListener() {
            private int numCompletedObjects = 0;

            @Override
            public void objectCompleted(final String name) {
                intValue.increment();
                assertTrue(bookTitles.contains(name));
                assertEquals(1, ++numCompletedObjects);
            }
        };

        // This is used just to make sure the callback handler ends up in the right place.
        final DataTransferredListener dataTransferredListener = new DataTransferredListener() {
            @Override
            public void dataTransferred(final long size) {

            }
        };

        try {
            final JobImplJobPartDecoratorPair jobImplJobPartDecoratorPair = createJobPartDecoratorAndEnsureObjectPartTrackersPopulated(objectCompletedListener,
                    dataTransferredListener, bookTitles);

            // trigger the callback

            jobImplJobPartDecoratorPair.getJobPartTrackerDecorator().completePart(FILE_NAMES[0], new ObjectPart(0, bookSize));

            assertEquals(1, intValue.getValue());
        } finally {
            deleteBigFileFromBlackPearlBucket();
        }
    }

    private JobImplJobPartDecoratorPair createJobPartDecoratorAndEnsureObjectPartTrackersPopulated
            (
                    final ObjectCompletedListener objectCompletedListener,
                    final DataTransferredListener dataTransferredListener,
                    final List<String> bookTitles
            )
            throws IOException, URISyntaxException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, NoSuchFieldException, ClassNotFoundException
    {
        final List<Ds3Object> objects = new ArrayList<>();
        for (final String book : FILE_NAMES) {
            final Path objPath = ResourceUtils.loadFileResource(DIR_NAME + book);
            bookSize = Files.size(objPath);
            final Ds3Object obj = new Ds3Object(book, bookSize);
            bookTitles.add(book);
            objects.add(obj);
        }

        final Ds3ClientShim ds3ClientShim = new Ds3ClientShim((Ds3ClientImpl) client);

        final int maxNumBlockAllocationRetries = 1;
        final int maxNumObjectTransferAttempts = 3;
        final Ds3ClientHelpers ds3ClientHelpers = Ds3ClientHelpers.wrap(ds3ClientShim,
                maxNumBlockAllocationRetries,
                maxNumObjectTransferAttempts);

        final Ds3ClientHelpers.Job writeJob = ds3ClientHelpers.startWriteJob(BUCKET_NAME, objects);

        writeJob.attachObjectCompletedListener(objectCompletedListener);

        writeJob.attachDataTransferredListener(dataTransferredListener);
        return getJobImplJobPartDecoratorAndEnsureObjectPartTrackersPopulated(writeJob);
    }

    private JobImplJobPartDecoratorPair getJobImplJobPartDecoratorAndEnsureObjectPartTrackersPopulated(final Ds3ClientHelpers.Job job)
            throws NoSuchFieldException, IllegalAccessException
    {
        // Check that the client has one callback registered
        final Field partTrackerField = job.getClass().getSuperclass().getDeclaredField("jobPartTracker");
        partTrackerField.setAccessible(true);
        final JobImpl.JobPartTrackerDecorator jobPartTrackerDecorator = (JobImpl.JobPartTrackerDecorator) partTrackerField.get(job);

        final Field clientJobPartTrackerField = jobPartTrackerDecorator.getClass().getDeclaredField("clientJobPartTracker");
        clientJobPartTrackerField.setAccessible(true);
        final JobPartTrackerImpl clientJobPartTrackerImpl = (JobPartTrackerImpl) clientJobPartTrackerField.get(jobPartTrackerDecorator);

        final Field clientTackersField = clientJobPartTrackerImpl.getClass().getDeclaredField("trackers");
        clientTackersField.setAccessible(true);
        final Map<String, ObjectPartTracker> clientTrackers = (Map<String, ObjectPartTracker>) clientTackersField.get(clientJobPartTrackerImpl);
        final ObjectPartTrackerImpl clientObjectPartTrackerImpl = (ObjectPartTrackerImpl) clientTrackers.get(FILE_NAMES[0]);

        final Field clientObjectCompletedListenersField = clientObjectPartTrackerImpl.getClass().getDeclaredField("objectCompletedListeners");
        clientObjectCompletedListenersField.setAccessible(true);
        final Set<ObjectCompletedListener> clientObjectCompletedListeners = (Set<ObjectCompletedListener>) clientObjectCompletedListenersField.get(clientObjectPartTrackerImpl);

        assertEquals(1, clientObjectCompletedListeners.size());

        // Data transfer listeners
        final Field clientDataTransferListenersField = clientObjectPartTrackerImpl.getClass().getDeclaredField("dataTransferredListeners");
        clientDataTransferListenersField.setAccessible(true);
        final Set<DataTransferredListener> clientDataTransferredListeners = (Set<DataTransferredListener>) clientDataTransferListenersField.get(clientObjectPartTrackerImpl);

        assertEquals(1, clientDataTransferredListeners.size());

        // Check that we have no internal callbacks registered.  Registering internal callbacks doesn't happen until you
        // call transfer

        final Field internalJobPartTrackerField = jobPartTrackerDecorator.getClass().getDeclaredField("internalJobPartTracker");
        internalJobPartTrackerField.setAccessible(true);
        final JobPartTrackerImpl internalJobPartTrackerImpl = (JobPartTrackerImpl) internalJobPartTrackerField.get(jobPartTrackerDecorator);

        final Field internalTackersField = internalJobPartTrackerImpl.getClass().getDeclaredField("trackers");
        internalTackersField.setAccessible(true);
        final Map<String, ObjectPartTracker> internalTrackers = (Map<String, ObjectPartTracker>) internalTackersField.get(internalJobPartTrackerImpl);
        final ObjectPartTrackerImpl internalObjectPartTrackerImpl = (ObjectPartTrackerImpl) internalTrackers.get(FILE_NAMES[0]);

        final Field internalObjectCompletedListenersField = internalObjectPartTrackerImpl.getClass().getDeclaredField("objectCompletedListeners");
        internalObjectCompletedListenersField.setAccessible(true);
        final Set<ObjectCompletedListener> internalObjectCompletedListeners = (Set<ObjectCompletedListener>) internalObjectCompletedListenersField.get(internalObjectPartTrackerImpl);

        assertEquals(0, internalObjectCompletedListeners.size());

        // Data transfer listeners
        final Field internalDataTransferListenersField = internalObjectPartTrackerImpl.getClass().getDeclaredField("dataTransferredListeners");
        internalDataTransferListenersField.setAccessible(true);
        final Set<DataTransferredListener> internalDataTransferredListeners = (Set<DataTransferredListener>) internalDataTransferListenersField.get(internalObjectPartTrackerImpl);

        assertEquals(0, internalDataTransferredListeners.size());

        return new JobImplJobPartDecoratorPair(jobPartTrackerDecorator, (JobImpl) job);
    }

    private final class JobImplJobPartDecoratorPair {
        private final JobImpl.JobPartTrackerDecorator jobPartTrackerDecorator;
        private final JobImpl job;

        private JobImplJobPartDecoratorPair(final JobImpl.JobPartTrackerDecorator jobPartTrackerDecorator,
                                            final JobImpl job) {
            this.jobPartTrackerDecorator = jobPartTrackerDecorator;
            this.job = job;
        }

        public JobImpl.JobPartTrackerDecorator getJobPartTrackerDecorator() {
            return jobPartTrackerDecorator;
        }

        public JobImpl getJob() {
            return job;
        }
    }

    private void deleteBigFileFromBlackPearlBucket() throws IOException {
        final Ds3ClientHelpers helpers = Ds3ClientHelpers.wrap(client);

        final Iterable<Contents> objects = helpers.listObjects(BUCKET_NAME);
        for (final Contents contents : objects) {
            if (contents.getKey().equals(FILE_NAMES[0])) {
                client.deleteObject(new DeleteObjectRequest(BUCKET_NAME, contents.getKey()));
            }
        }
    }

    /**
     * This test verifies that there is 1 object completed callback handler registered in the internal job part
     * tracker.  This callback handler does not exist until you call transfer on the write job.
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws URISyntaxException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchFieldException
     */
    @Test
    public void testWriteObjectCompletionPopulatesInternalJobPartTrackers()
            throws NoSuchMethodException, IOException, ClassNotFoundException, URISyntaxException, IllegalAccessException, InvocationTargetException, NoSuchFieldException
    {
        final IntValue intValue = new IntValue();

        final List<String> bookTitles = new ArrayList<>();

        final ObjectCompletedListener objectCompletedListener = new ObjectCompletedListener() {
            private int numCompletedObjects = 0;

            @Override
            public void objectCompleted(final String name) {
                intValue.increment();
                assertTrue(bookTitles.contains(name));
                assertEquals(1, ++numCompletedObjects);
            }
        };

        final DataTransferredListener dataTransferredListener = new DataTransferredListener() {
            @Override
            public void dataTransferred(final long size) {

            }
        };

        final JobImplJobPartDecoratorPair jobImplJobPartDecoratorPair = createJobPartDecoratorAndEnsureObjectPartTrackersPopulated(objectCompletedListener,
                dataTransferredListener, bookTitles);

        final Path dirPath = ResourceUtils.loadFileResource(DIR_NAME);

        try {
            jobImplJobPartDecoratorPair.getJob().transfer(new FileObjectPutter(dirPath));

            final Field partTrackerField = jobImplJobPartDecoratorPair.getJob().getClass().getSuperclass().getDeclaredField("jobPartTracker");
            partTrackerField.setAccessible(true);
            final JobImpl.JobPartTrackerDecorator jobPartTrackerDecorator = (JobImpl.JobPartTrackerDecorator)partTrackerField.get(jobImplJobPartDecoratorPair.getJob());

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

            assertEquals(1, internalObjectCompletedListeners.size());
        } finally {
            deleteBigFileFromBlackPearlBucket();
        }
    }

    @Test
    public void testWriteObjectCompletionFiresInternalHandlersFirst()
            throws NoSuchMethodException, IOException, ClassNotFoundException, URISyntaxException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {
        final IntValue intValue = new IntValue();

        final List<String> bookTitles = new ArrayList<>();

        final ObjectCompletedListener objectCompletedListener = new ObjectCompletedListener() {
            private int numCompletedObjects = 0;

            @Override
            public void objectCompleted(final String name) {
                intValue.increment();
                assertTrue(bookTitles.contains(name));
                assertEquals(1, ++numCompletedObjects);
            }
        };

        final DataTransferredListener dataTransferredListener = new DataTransferredListener() {
            @Override
            public void dataTransferred(final long size) {

            }
        };

        final JobImplJobPartDecoratorPair jobImplJobPartDecoratorPair = createJobPartDecoratorAndEnsureObjectPartTrackersPopulated(objectCompletedListener,
                dataTransferredListener, bookTitles);

        final Path dirPath = ResourceUtils.loadFileResource(DIR_NAME);

        try {
            final JobPartDecoratorInterceptor jobPartDecoratorInterceptor = new JobPartDecoratorInterceptor(jobImplJobPartDecoratorPair);
            jobPartDecoratorInterceptor.transfer(new FileObjectPutter(dirPath));
            assertTrue(jobPartDecoratorInterceptor.bothHandlersFired());
        } finally {
            deleteBigFileFromBlackPearlBucket();
        }
    }

    private static final class JobPartDecoratorInterceptor implements JobPartTracker {
        private final JobImplJobPartDecoratorPair jobImplJobPartDecoratorPair;
        private final ObjectCompletedCallbackTracker objectCompletedCallbackTracker;

        private JobPartDecoratorInterceptor(final JobImplJobPartDecoratorPair jobImplJobPartDecoratorPair)
                throws NoSuchFieldException, IllegalAccessException
        {
            this.jobImplJobPartDecoratorPair = jobImplJobPartDecoratorPair;
            this.objectCompletedCallbackTracker = new ObjectCompletedCallbackTracker();

            // Replace the job part trackers in JobPartTrackerDecorator with JobPartTrackerInterceptor
            final JobImpl.JobPartTrackerDecorator jobPartTrackerDecorator = jobImplJobPartDecoratorPair.getJobPartTrackerDecorator();

            final Field internalJobPartTrackerField = jobPartTrackerDecorator.getClass().getDeclaredField("internalJobPartTracker");
            internalJobPartTrackerField.setAccessible(true);
            final JobPartTrackerInterceptor internalJobPartTrackerInterceptor = new JobPartTrackerInterceptor(
                    (JobPartTracker)internalJobPartTrackerField.get(jobPartTrackerDecorator),
                    objectCompletedCallbackTracker.getInternalObjectCompletedHandler());
            internalJobPartTrackerField.set(jobPartTrackerDecorator, internalJobPartTrackerInterceptor);

            final Field clientJobPartTrackerField = jobPartTrackerDecorator.getClass().getDeclaredField("clientJobPartTracker");
            clientJobPartTrackerField.setAccessible(true);
            final JobPartTrackerInterceptor clientJobPartTrackerInterceptor = new JobPartTrackerInterceptor(
                    (JobPartTracker)clientJobPartTrackerField.get(jobPartTrackerDecorator),
                    objectCompletedCallbackTracker.getClientObjectCompletedHandler());
            clientJobPartTrackerField.set(jobPartTrackerDecorator, clientJobPartTrackerInterceptor);
        }

        @Override
        public void completePart(final String key, final ObjectPart objectPart) {
            jobImplJobPartDecoratorPair.getJobPartTrackerDecorator().completePart(key, objectPart);
        }

        @Override
        public boolean containsPart(final String key, final ObjectPart objectPart) {
            return jobImplJobPartDecoratorPair.getJobPartTrackerDecorator().containsPart(key, objectPart);
        }

        @Override
        public JobPartTracker attachDataTransferredListener(final DataTransferredListener listener) {
            return jobImplJobPartDecoratorPair.getJobPartTrackerDecorator().attachDataTransferredListener(listener);
        }

        @Override
        public JobPartTracker attachObjectCompletedListener(final ObjectCompletedListener listener) {
            return jobImplJobPartDecoratorPair.getJobPartTrackerDecorator().attachObjectCompletedListener(listener);
        }

        @Override
        public void removeDataTransferredListener(final DataTransferredListener listener) {
            jobImplJobPartDecoratorPair.getJobPartTrackerDecorator().removeDataTransferredListener(listener);
        }

        @Override
        public void removeObjectCompletedListener(final ObjectCompletedListener listener) {
            jobImplJobPartDecoratorPair.getJobPartTrackerDecorator().removeObjectCompletedListener(listener);
        }

        private void transfer(final Ds3ClientHelpers.ObjectChannelBuilder channelBuilder) throws IOException {
            jobImplJobPartDecoratorPair.getJob().transfer(channelBuilder);
        }

        private boolean bothHandlersFired() {
            return objectCompletedCallbackTracker.bothHandlersFired();
        }
    }

    private static class ObjectCompletedCallbackTracker {
        private final BooleanValue internalObjectCompletedHandlerCalled = new BooleanValue();
        private final BooleanValue clientObjectCompletedHandlerCalled = new BooleanValue();

        private final ObjectCompletedCallbackRecorder clientObjectCompletedHandler = new ObjectCompletedCallbackRecorder() {
            @Override
            public void onObjectCompletedCalled() {
                clientObjectCompletedHandlerCalled.setValue(true);

                if ( ! internalObjectCompletedHandlerCalled.getValue()) {
                    throw new IllegalStateException("Client object completed handler called before closing channels.");
                }
            }
        };

        private final ObjectCompletedCallbackRecorder internalObjectCompletedHandler = new ObjectCompletedCallbackRecorder() {
            @Override
            public void onObjectCompletedCalled() {
                internalObjectCompletedHandlerCalled.setValue(true);

                if (clientObjectCompletedHandlerCalled.getValue()) {
                    throw new IllegalStateException("Client object completed handler called before closing channels.");
                }
            }
        };

        public ObjectCompletedCallbackRecorder getClientObjectCompletedHandler() {
            return clientObjectCompletedHandler;
        }

        public ObjectCompletedCallbackRecorder getInternalObjectCompletedHandler() {
            return internalObjectCompletedHandler;
        }

        public boolean bothHandlersFired() {
            return internalObjectCompletedHandlerCalled.getValue() && clientObjectCompletedHandlerCalled.getValue();
        }
    }

    private static class BooleanValue {
        private boolean value = false;

        private boolean getValue() {
            return this.value;
        }

        private boolean setValue(final boolean value) {
            this.value = value;
            return getValue();
        }
    }

    private interface ObjectCompletedCallbackRecorder {
        void onObjectCompletedCalled();
    }

    private static final class JobPartTrackerInterceptor implements JobPartTracker {
        private final JobPartTracker jobPartTracker;
        private final ObjectCompletedCallbackRecorder objectCompletedCallbackRecorder;

        private JobPartTrackerInterceptor(final JobPartTracker jobPartTracker, final ObjectCompletedCallbackRecorder objectCompletedCallbackRecorder) {
            this.jobPartTracker = jobPartTracker;
            this.objectCompletedCallbackRecorder = objectCompletedCallbackRecorder;
        }

        @Override
        public void completePart(final String key, final ObjectPart objectPart) {
            objectCompletedCallbackRecorder.onObjectCompletedCalled();
            jobPartTracker.completePart(key, objectPart);
        }

        @Override
        public boolean containsPart(final String key, final ObjectPart objectPart) {
            return jobPartTracker.containsPart(key, objectPart);
        }

        @Override
        public JobPartTracker attachDataTransferredListener(final DataTransferredListener listener) {
            return jobPartTracker.attachDataTransferredListener(listener);
        }

        @Override
        public JobPartTracker attachObjectCompletedListener(final ObjectCompletedListener listener) {
            return jobPartTracker.attachObjectCompletedListener(listener);
        }

        @Override
        public void removeDataTransferredListener(final DataTransferredListener listener) {
            jobPartTracker.removeDataTransferredListener(listener);
        }

        @Override
        public void removeObjectCompletedListener(final ObjectCompletedListener listener) {
            jobPartTracker.removeObjectCompletedListener(listener);
        }
    }

    @Test
    public void testWriteObjectRemovesEventsFromCorrectPartTracker()
            throws IOException, URISyntaxException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, NoSuchFieldException, ClassNotFoundException
    {
        final IntValue intValue = new IntValue();

        final List<String> bookTitles = new ArrayList<>();

        final ObjectCompletedListener objectCompletedListener = new ObjectCompletedListener() {
            private int numCompletedObjects = 0;

            @Override
            public void objectCompleted(final String name) {
                intValue.increment();
                assertTrue(bookTitles.contains(name));
                assertEquals(1, ++numCompletedObjects);
            }
        };

        // This is used just to make sure the callback handler ends up in the right place.
        final DataTransferredListener dataTransferredListener = new DataTransferredListener() {
            @Override
            public void dataTransferred(final long size) {

            }
        };

        try {
            final JobImplJobPartDecoratorPair jobImplJobPartDecoratorPair = createJobPartDecoratorAndEnsureObjectPartTrackersPopulated(objectCompletedListener,
                    dataTransferredListener, bookTitles);

            jobImplJobPartDecoratorPair.getJob().removeObjectCompletedListener(objectCompletedListener);

            final JobImpl job = jobImplJobPartDecoratorPair.getJob();

            final Field partTrackerField = job.getClass().getSuperclass().getDeclaredField("jobPartTracker");
            partTrackerField.setAccessible(true);
            final JobImpl.JobPartTrackerDecorator jobPartTrackerDecorator = (JobImpl.JobPartTrackerDecorator) partTrackerField.get(job);

            final Field clientJobPartTrackerField = jobPartTrackerDecorator.getClass().getDeclaredField("clientJobPartTracker");
            clientJobPartTrackerField.setAccessible(true);
            final JobPartTrackerImpl clientJobPartTrackerImpl = (JobPartTrackerImpl) clientJobPartTrackerField.get(jobPartTrackerDecorator);

            final Field clientTackersField = clientJobPartTrackerImpl.getClass().getDeclaredField("trackers");
            clientTackersField.setAccessible(true);
            final Map<String, ObjectPartTracker> clientTrackers = (Map<String, ObjectPartTracker>) clientTackersField.get(clientJobPartTrackerImpl);
            final ObjectPartTrackerImpl clientObjectPartTrackerImpl = (ObjectPartTrackerImpl) clientTrackers.get(FILE_NAMES[0]);

            final Field clientObjectCompletedListenersField = clientObjectPartTrackerImpl.getClass().getDeclaredField("objectCompletedListeners");
            clientObjectCompletedListenersField.setAccessible(true);
            final Set<ObjectCompletedListener> clientObjectCompletedListeners = (Set<ObjectCompletedListener>) clientObjectCompletedListenersField.get(clientObjectPartTrackerImpl);

            assertEquals(0, clientObjectCompletedListeners.size());

            // Data transfer listeners
            job.removeDataTransferredListener(dataTransferredListener);
            final Field clientDataTransferListenersField = clientObjectPartTrackerImpl.getClass().getDeclaredField("dataTransferredListeners");
            clientDataTransferListenersField.setAccessible(true);
            final Set<DataTransferredListener> clientDataTransferredListeners = (Set<DataTransferredListener>) clientDataTransferListenersField.get(clientObjectPartTrackerImpl);

            assertEquals(0, clientDataTransferredListeners.size());

        } finally {
            deleteBigFileFromBlackPearlBucket();
        }
    }
}
