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


import com.google.common.collect.ImmutableMap;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.metadata.interfaces.MetadataStore;
import com.spectralogic.ds3client.metadata.interfaces.MetadataStoreListener;
import com.spectralogic.ds3client.utils.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFileAttributes;
import java.util.Map;

/**
 * Implementation of MetaDataAcess Interface
 * Used to store meta data on Server
 */
public class MetadataAccessImpl implements Ds3ClientHelpers.MetadataAccess {
    static private final Logger LOG = LoggerFactory.getLogger(MetadataAccessImpl.class);
    private final Map<String, Path> fileMapper;
    private final MetadataStoreListener metadataStoreListener;

    public MetadataAccessImpl(final Map<String, Path> fileMapper , final MetadataStoreListener metadataStoreListener) {
        this.fileMapper = fileMapper;
        this.metadataStoreListener = metadataStoreListener;
    }

    @Override
    public Map<String, String> getMetadataValue(final String filename) {
        try {
            final Path file = fileMapper.get(filename);
            return storeMetaData(file).build();
        } catch (final Exception e) {
            LOG.error("failed to store Metadata", e);
            metadataStoreListener.onMetadataFailed("Unable to get MetaData"+e.getMessage());
            return null;
        }
    }

    /**
     * @param file local path of file
     * @return map builder containing the data to be stored on server
     */
    private ImmutableMap.Builder<String, String> storeMetaData(final Path file) {
        final ImmutableMap.Builder<String, String> metadata = new ImmutableMap.Builder<>();
        new ImmutableMap.Builder<String, String>();
        try {
            //get metadata store based on os type
            final MetadataStore metadataStore = new MetadataStoreFactory().getOsSpecificMetadataStore(metadata,metadataStoreListener);
            metadataStore.saveOSMetaData(MetaDataUtil.getOS());

            final BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);

            PosixFileAttributes attrPosix = null;

            if ( !Platform.isWindows()) {
                attrPosix = Files.readAttributes(file, PosixFileAttributes.class);
            }

            metadataStore.saveCreationTimeMetaData(attr);
            metadataStore.saveAccessTimeMetaData(attr);
            metadataStore.saveLastModifiedTime(attr);

            if(attrPosix != null) {
                metadataStore.saveOSSpecificMetadata(file, attrPosix);
            } else {
                metadataStore.saveOSSpecificMetadata(file, attr);
            }

        } catch (final IOException ioe) {
            LOG.error("unable to get metadata", ioe);
            metadataStoreListener.onMetadataFailed("Unable to get MetaData"+ioe.getMessage());
        }
        return metadata;
    }

}