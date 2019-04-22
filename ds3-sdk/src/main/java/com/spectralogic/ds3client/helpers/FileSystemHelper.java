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

package com.spectralogic.ds3client.helpers;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

public interface FileSystemHelper {
    boolean pathIsDirectory(final Path path);

    boolean pathObjectExists(final Path path);

    boolean pathIsWritable(final Path path) throws IOException;

    long getAvailableFileSpace(final Path path) throws IOException;

    /**
     * Determine if the file system directory specified in the destinationDirectory parameter
     * has enough storage space to contain the objects listed in the parameter objectNames contained in
     * the bucket specified in the parameter buckName.  You can use this method prior to starting a read
     * job to ensure that your file system has enough storage space to contain the objects you wish to
     * retrieve.
     *
     * @param helpers              An instance of the Ds3ClientHelpers interface that has most likely
     *                             wrapped an instance of Ds3Client.
     * @param bucketName           The Black Pearl bucket containing the objects you wish to retrieve.
     * @param objectNames          The names of the objects you wish to retrieve.
     * @param destinationDirectory The file system directory in you intend to store retrieved objects.
     * @return {@link ObjectStorageSpaceVerificationResult}
     */
    ObjectStorageSpaceVerificationResult objectsFromBucketWillFitInDirectory(final Ds3ClientHelpers helpers,
                                                                             final String bucketName,
                                                                             final Collection<String> objectNames,
                                                                             final Path destinationDirectory);
}
