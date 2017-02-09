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

/**
 * An UnrecoverableIOException is used to classify IOExceptions into one of 2 types:
 * those that should result in retrying a data transfer, such as a failure in an HTTP GET or PUT;
 * and those that should <b>not</b> result in retrying a data transfer, such as a failure writing to
 * or reading from a file.  During an HTTP PUT or GET that involves transferring data to or from
 * a file, file system failures are considered non-recoverable, where network-related failures
 * are considered recoverable.
 */
public class UnrecoverableIOException extends IOException {
    public UnrecoverableIOException() {
        super();
    }

    public UnrecoverableIOException(final String message) {
        super(message);
    }

    public UnrecoverableIOException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UnrecoverableIOException(final Throwable cause) {
        super(cause);
    }
}
