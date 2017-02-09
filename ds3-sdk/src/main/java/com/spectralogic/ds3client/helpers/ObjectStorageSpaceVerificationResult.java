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

package com.spectralogic.ds3client.helpers;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

/**
 * An object that holds the result of calling {@link Ds3ClientHelpers#objectsFromBucketWillFitInDirectory(String, Collection, Path)}
 */
public class ObjectStorageSpaceVerificationResult {
    public enum VerificationStatus {
        /**
         * Objects will fit in intended directory.
         */
        OK,

        /**
         * The path intended to store objects is not a directory.
         */
        PathIsNotADirectory,

        /**
         * The path intended to store objects does not exist, so its storage capacity is unknown.
         */
        PathDoesNotExist,

        /**
         * The path intended to store objects is not writable.
         */
        PathLacksAccess,

        /**
         * The path intended to store objects lacks the capacity to store all the objects.
         */
        PathLacksSufficientStorageSpace,

        /**
         * The bucket from which you wish to retrieve objects does not exist.
         */
        BucketDoesNotExist,

        /**
         * Caught an IOException from the file system or communicating with Black Pearl
         */
        CaughtIOException
    }

    private final VerificationStatus verificationStatus;
    private final long requiredSpace;
    private final long availableSpace;
    private final boolean containsSufficientSpace;
    private final IOException ioException;

    public ObjectStorageSpaceVerificationResult(final VerificationStatus verificationStatus,
                                                final long requiredSpace,
                                                final long availableSpace,
                                                final IOException ioException)
    {
        this.requiredSpace = requiredSpace;
        this.availableSpace = availableSpace;
        this.containsSufficientSpace = (availableSpace > requiredSpace);
        this.verificationStatus = verificationStatus;
        this.ioException = ioException;
    }

    /**
     * @return The status object describing the ability of a path to contain a collection of objects.
     */
    public VerificationStatus getVerificationStatus() {
        return verificationStatus;
    }

    /**
     * @return The space required, in bytes, to store the requested objects.
     */
    public long getRequiredSpace() {
        return requiredSpace;
    }

    /**
     * @return The space available, in bytes, available in a destination path.
     */
    public long getAvailableSpace() {
        return availableSpace;
    }

    /**
     * When calling {@link Ds3ClientHelpers#objectsFromBucketWillFitInDirectory(String, Collection, Path)}, you can
     * quickly determine the result using code like the following...
     *
     * <pre>
     *     <code>
     *         if (helpers.objectsFromBucketWillFitInDirectory(bucketName, objectsToRetrieve, directory)).containsSufficientSpace() {
     *             // put read job here
     *         }
     *     </code>
     * </pre>
     * @return
     */
    public boolean containsSufficientSpace() {
        return containsSufficientSpace;
    }

    /**
     * @return Any IOException generated as a result of calling
     * {@link Ds3ClientHelpers#objectsFromBucketWillFitInDirectory(String, Collection, Path)}.  Null if no
     * IOException was generated.
     */
    public IOException getIoException() {
        return ioException;
    }

    @Override
    public String toString() {
        return "ObjectStorageSpaceVerificationResult { " +
                "verificationStatus = " + verificationStatus +
                ", requiredSpace = " + requiredSpace +
                ", availableSpace = " + availableSpace +
                ", containsSufficientSpace = " + containsSufficientSpace +
                ", ioException = " + ioException +
                '}';
    }
}
