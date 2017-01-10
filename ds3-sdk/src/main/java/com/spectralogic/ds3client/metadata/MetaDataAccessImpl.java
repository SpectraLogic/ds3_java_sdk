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

// This code is auto-generated, do not modify
package com.spectralogic.ds3client.metadata;


import com.google.common.collect.ImmutableMap;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.metadata.interfaces.MetaDataStore;
import com.spectralogic.ds3client.metadata.interfaces.MetaDataStoreListner;
import com.spectralogic.ds3client.utils.MetaDataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFileAttributes;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of MetaDataAcess Interface
 * Used to store meta data on Server
 */
public class MetaDataAccessImpl implements Ds3ClientHelpers.MetadataAccess {
    static private final Logger LOG = LoggerFactory.getLogger(MetaDataAccessImpl.class);
    private final Map<String, Path> fileMapper;
    private final MetaDataStoreListner metaDataStoreListner;

    public MetaDataAccessImpl(final Map<String, Path> fileMapper ,final MetaDataStoreListner metaDataStoreListner) {
        this.fileMapper = fileMapper;
        this.metaDataStoreListner = metaDataStoreListner;
    }

    @Override
    public Map<String, String> getMetadataValue(final String filename) {
        try {
            final Path file = fileMapper.get(filename);
            return storeMetaData(file).build();
        } catch (final Exception e) {
            LOG.error("failed to store Metadata", e);
            metaDataStoreListner.onMetaDataFailed("Unable to get MetaData"+e.getMessage());
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
            //get local os name
            final String localOSName = MetaDataUtil.getOS();
            final Set<String> setFileAttributes = MetaDataUtil.getSupportedFileAttributes(file);
            //get metadata store based on os type
            final MetaDataStore metaDataStore = new MetaDataStoreFactory().getOsSpecificMetadataStore(localOSName, metadata);
            metaDataStore.saveOSMetaData(localOSName);
            for (final String fileAttributeType : setFileAttributes) {
                switch (fileAttributeType) {
                    case "basic":
                        final BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);
                        metaDataStore.saveCreationTimeMetaData(attr);
                        metaDataStore.saveAccessTimeMetaData(attr);
                        metaDataStore.saveLastModifiedTime(attr);
                        if (metaDataStore instanceof WindowsMetaDataStore) {
                            ((WindowsMetaDataStore) metaDataStore).saveWindowsfilePermissions(file);
                            ((WindowsMetaDataStore) metaDataStore).saveWindowsDescriptors(file);
                            ((WindowsMetaDataStore) metaDataStore).saveFlagMetaData(file);

                        }
                        break;
                    case "posix":
                        final PosixFileAttributes attrPosix = Files.readAttributes(file, PosixFileAttributes.class);
                        if (metaDataStore instanceof PosixMetaDataStore) {
                            ((PosixMetaDataStore) metaDataStore).saveUserId(file);
                            ((PosixMetaDataStore) metaDataStore).saveGroupId(file);
                            ((PosixMetaDataStore) metaDataStore).saveModeMetaData(file);
                            ((PosixMetaDataStore) metaDataStore).saveOwnerNameMetaData(attrPosix);
                            ((PosixMetaDataStore) metaDataStore).saveGroupNameMetaData(attrPosix);
                            ((PosixMetaDataStore) metaDataStore).savePosixPermssionsMeta(attrPosix);
                        }
                        break;
                }
            }
        } catch (final IOException ioe) {
            LOG.error("unable to get metadata", ioe);
            metaDataStoreListner.onMetaDataFailed("Unable to get MetaData"+ioe.getMessage());
        }
        return metadata;
    }

}