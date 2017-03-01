/*
 * ******************************************************************************
 *   Copyright 2014-2016 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3client.metadata;

import com.google.common.collect.ImmutableList;
import com.spectralogic.ds3client.exceptions.AggregateException;
import com.spectralogic.ds3client.helpers.FailureEventListener;
import com.spectralogic.ds3client.helpers.MetadataReceivedListener;
import com.spectralogic.ds3client.helpers.events.FailureEvent;
import com.spectralogic.ds3client.metadata.interfaces.MetadataRestore;
import com.spectralogic.ds3client.networking.Metadata;
import com.spectralogic.ds3client.utils.Guard;
import com.spectralogic.ds3client.utils.StringExtensions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MetadataReceivedListenerImpl implements MetadataReceivedListener {

    private final static Logger LOG = LoggerFactory.getLogger(MetadataReceivedListenerImpl.class);

    private final String localFilePath;
    private final FailureEventListener failureEventListener;
    private final String httpEndpoint;

    public MetadataReceivedListenerImpl(final String localFilePath) {
        this(localFilePath, null, null);
    }

    public MetadataReceivedListenerImpl(final String localFilePath,
                                        final FailureEventListener failureEventListener,
                                        final String httpEndpoint)
    {
        this.localFilePath = localFilePath;
        this.failureEventListener = failureEventListener;
        this.httpEndpoint = httpEndpoint;
    }

    @Override
    public void metadataReceived(final String filename, final Metadata metadata) {
        try {
            final String actualFilePath = MetaDataUtil.getRealFilePath(localFilePath, filename);
            restoreMetaData(actualFilePath, metadata);
        } catch (final Throwable t) {
            if (failureEventListener != null) {
                failureEventListener.onFailure(FailureEvent.builder()
                        .doingWhat(FailureEvent.FailureActivity.RestoringMetadata)
                        .withCausalException(t)
                        .withObjectNamed(filename)
                        .usingSystemWithEndpoint(StringExtensions.getStringOrDefault(httpEndpoint, " "))
                        .build());
            }
        }
    }

    /**
     * Restore the metadata to local file
     *
     * @param objectName name of the file to be restored
     * @param metadata   metadata which needs to be set on local file
     */
    private void restoreMetaData(final String objectName, final Metadata metadata) throws IOException, InterruptedException {

        final ImmutableList.Builder<Throwable> exceptionBuilder = ImmutableList.builder();

        //get metadatarestore on the basis of os
        final MetadataRestore metadataRestore = new MetadataRestoreFactory().getOSSpecificMetadataRestore(metadata, objectName);
        //restore os name
        metadataRestore.restoreOSName();

        //restore user and owner based on OS
        try {
            metadataRestore.restoreUserAndOwner();
        } catch (final Throwable t) {
             LOG.error("Could not restore owner and owner information", t);
             exceptionBuilder.add(t);
        }

        //restore creation and modified time based on OS
        try {
            metadataRestore.restoreFileTimes();
        } catch (final Throwable t) {
            LOG.error("Could not restore the file times", t);
            exceptionBuilder.add(t);
        }

        //restore permissions based on OS
        try {
            metadataRestore.restorePermissions();
        } catch (final Throwable t) {
            LOG.error("Could not restore the file permissions", t);
            exceptionBuilder.add(t);
        }

        final ImmutableList<Throwable> exceptions = exceptionBuilder.build();
        if (!Guard.isNullOrEmpty(exceptions)) {
            throw new AggregateException(exceptions);
        }
    }
}
