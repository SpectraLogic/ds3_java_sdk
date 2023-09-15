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

package com.spectralogic.ds3client.metadata;


import com.google.common.collect.ImmutableMap;
import com.spectralogic.ds3client.helpers.FailureEventListener;
import com.spectralogic.ds3client.helpers.MetadataAccess;
import com.spectralogic.ds3client.helpers.events.FailureEvent;
import com.spectralogic.ds3client.metadata.interfaces.MetadataStore;
import com.spectralogic.ds3client.utils.Platform;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFileAttributes;
import java.util.Map;

import com.spectralogic.ds3client.utils.StringExtensions;

/**
 * Implementation of MetaDataAcess Interface
 * Used to store meta data on Server
 */
public class MetadataAccessImpl implements MetadataAccess {
    private final ImmutableMap<String, Path> fileMapper;
    private final FailureEventListener failureEventListener;
    private final String httpEndpoint;

    public MetadataAccessImpl(final ImmutableMap<String, Path> fileMapper) {
        this(fileMapper, null, null);
    }

    public MetadataAccessImpl(final ImmutableMap<String, Path> fileMapper,
                              final FailureEventListener failureEventListener,
                              final String httpEndpoint)
    {
        this.fileMapper = fileMapper;
        this.failureEventListener = failureEventListener;
        this.httpEndpoint = httpEndpoint;
    }

    @Override
    public Map<String, String> getMetadataValue(final String filename) {
        final Path file = fileMapper.get(filename);
        try {
            return storeMetaData(file);
        } catch (final Throwable t) {
            if (failureEventListener != null) {
                failureEventListener.onFailure(FailureEvent.builder()
                        .withObjectNamed(filename)
                        .withCausalException(t)
                        .doingWhat(FailureEvent.FailureActivity.RecordingMetadata)
                        .usingSystemWithEndpoint(StringExtensions.getStringOrDefault(httpEndpoint, " "))
                        .build());
            }
        }

        return ImmutableMap.of();
    }

    /**
     * @param file local path of file
     * @return map builder containing the data to be stored on server
     */
    private static ImmutableMap<String, String> storeMetaData(final Path file) throws IOException {
        final ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();

        //get metadata store based on os type
        final MetadataStore metadataStore = MetadataStoreFactory.getOsSpecificMetadataStore(builder);
        metadataStore.saveOSMetaData(MetaDataUtil.getOS());

        final BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);

        PosixFileAttributes attrPosix = null;

        if (!Platform.isWindows()) {
            attrPosix = Files.readAttributes(file, PosixFileAttributes.class);
        }

        metadataStore.saveCreationTimeMetaData(attr);
        metadataStore.saveAccessTimeMetaData(attr);
        metadataStore.saveLastModifiedTime(attr);

        if (attrPosix != null) {
            metadataStore.saveOSSpecificMetadata(file, attrPosix);
        } else {
            metadataStore.saveOSSpecificMetadata(file, attr);
        }

        return builder.build();
    }

}
