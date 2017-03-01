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

/**
 * An exception specialized to let us filter on the specific circumstance
 * when reading metadata from or writing metadata to a file on Windows
 * fails.
 */
public class WindowsMetadataException extends RuntimeException {
    public WindowsMetadataException() { }

    public WindowsMetadataException(final String failureMessage) {
        super(failureMessage);
    }

    public WindowsMetadataException(final String failureMessage, final Throwable cause) {
        super(failureMessage, cause);
    }

    public WindowsMetadataException(final Throwable cause) {
        super(cause);
    }

    protected WindowsMetadataException(final String failureMessage, final Throwable cause,
                                       final boolean enableSuppression, final boolean writableStackTrace)
    {
        super(failureMessage, cause, enableSuppression, writableStackTrace);
    }
}
