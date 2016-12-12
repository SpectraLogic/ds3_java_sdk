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

import com.google.common.base.Preconditions;
import com.spectralogic.ds3client.models.Contents;
import com.spectralogic.ds3client.utils.Guard;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

class FileSystemHelperImpl implements FileSystemHelper {
    public boolean pathIsDirectory(final Path path) {
        Preconditions.checkNotNull(path, "path must not be null.");

        return Files.isDirectory(path);
    }

    public boolean pathObjectExists(final Path path) {
        Preconditions.checkNotNull(path, "path must not be null.");

        return Files.exists(path);
    }

    public boolean pathIsWritable(final Path path) {
        Preconditions.checkNotNull(path, "path must not be null.");

        return Files.isWritable(path);
    }

    public long getAvailableFileSpace(final Path path) throws IOException {
        Preconditions.checkNotNull(path, "path must not be null.");

        return Files.getFileStore(path).getUsableSpace();
    }

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
    public ObjectStorageSpaceVerificationResult objectsFromBucketWillFitInDirectory(final Ds3ClientHelpers helpers,
                                                                                    final String bucketName,
                                                                                    final Collection<String> objectNames,
                                                                                    final Path destinationDirectory)
    {
        Preconditions.checkNotNull(helpers, "helpers may not be null.");
        Guard.throwOnNullOrEmptyString(bucketName, "bucketName must have a non-empty value.");
        Preconditions.checkNotNull(objectNames, "objectNames may not be null.");
        Preconditions.checkNotNull(destinationDirectory, "destinationDirectory may not be null.");

        long requiredSpace = 0;
        long availableSpace = 0;

        if ( ! pathObjectExists(destinationDirectory)) {
            final IOException ioException = null;
            return new ObjectStorageSpaceVerificationResult(ObjectStorageSpaceVerificationResult.VerificationStatus.PathDoesNotExist,
                    requiredSpace, availableSpace, ioException);
        }

        if ( ! pathIsDirectory(destinationDirectory)) {
            final IOException ioException = null;
            return new ObjectStorageSpaceVerificationResult(ObjectStorageSpaceVerificationResult.VerificationStatus.PathIsNotADirectory,
                    requiredSpace, availableSpace, ioException);
        }

        if ( ! pathIsWritable(destinationDirectory)) {
            final IOException ioException = null;
            return new ObjectStorageSpaceVerificationResult(ObjectStorageSpaceVerificationResult.VerificationStatus.PathLacksAccess,
                    requiredSpace, availableSpace, ioException);
        }

        try {
            helpers.ensureBucketExists(bucketName);
        } catch (final IOException e) {
            final IOException ioException = null;
            return new ObjectStorageSpaceVerificationResult(ObjectStorageSpaceVerificationResult.VerificationStatus.BucketDoesNotExist,
                    requiredSpace, availableSpace, ioException);
        }

        try {
            requiredSpace = getRequiredSpaceForObjects(helpers, bucketName, objectNames);
            availableSpace = getAvailableFileSpace(destinationDirectory);
        } catch (final IOException e) {
            return new ObjectStorageSpaceVerificationResult(ObjectStorageSpaceVerificationResult.VerificationStatus.CaughtIOException,
                    requiredSpace, availableSpace, e);
        }

        final ObjectStorageSpaceVerificationResult.VerificationStatus verificationStatus = availableSpace > requiredSpace ?
                ObjectStorageSpaceVerificationResult.VerificationStatus.OK : ObjectStorageSpaceVerificationResult.VerificationStatus.PathLacksSufficientStorageSpace;

        final IOException ioException = null;
        return new ObjectStorageSpaceVerificationResult(verificationStatus, requiredSpace, availableSpace, ioException);
    }

    private long getRequiredSpaceForObjects(final Ds3ClientHelpers helpers,
                                            final String bucketName,
                                            final Collection<String> objectNames)
                                            throws IOException
    {
        long result = 0;

        final Map<String, Long> objectSizeMap = new HashMap<>();

        final Iterable<Contents> bucketContents = helpers.listObjects(bucketName);

        for (final Contents bucketContent : bucketContents) {
            objectSizeMap.put(bucketContent.getKey(), bucketContent.getSize());
        }

        // Of the objects in the bucket, keep the information about only those in objectNames
        objectSizeMap.keySet().retainAll(objectNames);

        for (final long objectSize : objectSizeMap.values()) {
            result += objectSize;
        }

        return result;
    }
}
