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

package com.spectralogic.ds3client.exceptions;

import java.io.IOException;

public class ContentLengthNotMatchException extends IOException {
    private final String fileName;
    private final long contentLength;
    private final long totalBytes;
    public ContentLengthNotMatchException(final String fileName, final long contentLength, final long totalBytes) {
        super(String.format("The Content length for %s (%d) not match the number of byte read (%d)", fileName, contentLength, totalBytes));

        this.fileName = fileName;
        this.contentLength = contentLength;
        this.totalBytes = totalBytes;
    }

    public String getFileName() {
        return this.fileName;
    }

    public long getContentLength() {
        return this.contentLength;
    }

    public long getTotalBytes() {
        return this.totalBytes;
    }
}
