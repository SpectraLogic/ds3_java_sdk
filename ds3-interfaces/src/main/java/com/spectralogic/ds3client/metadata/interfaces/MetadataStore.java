/*
 * ******************************************************************************
 *   Copyright 2014-2017 Spectra Logic Corporation. All Rights Reserved.
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
package com.spectralogic.ds3client.metadata.interfaces;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public interface MetadataStore {
    /**
     * @param attr basic file attributes
     */
    void saveCreationTimeMetaData(final BasicFileAttributes attr);


    /**
     * @param attr basic file attributes
     */
    void saveAccessTimeMetaData(final BasicFileAttributes attr);

    /**
     * @param attr basic file attributes
     */
    void saveLastModifiedTime(final BasicFileAttributes attr);


    /**
     * save os meta data and return os name
     *
     * @return os name
     */
     String saveOSMetaData(final String osName);

    /**
     *Storing metadata based on OS
     *
     * @param file  path of local file
     * @param attrs FileAttributes
     */
    void saveOSSpecificMetadata(final Path file , BasicFileAttributes attrs) throws IOException;


}
