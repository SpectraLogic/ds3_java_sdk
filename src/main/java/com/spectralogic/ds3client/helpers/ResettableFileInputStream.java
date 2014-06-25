/*
 * ******************************************************************************
 *   Copyright 2014 Spectra Logic Corporation. All Rights Reserved.
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

import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * To better handle 307 redirects on HTTP PUTs the InputStream needs to be seekable.
 * This class wraps a FileInputStream and makes it seekable using the underlying FileChannel.
 *
 * To use it simply pass the FileInputStream into the constructor for this, and then pass this to the PutObjectRequest.
 *
 * <pre>
 *     {@code
 *     final FileInputStream fileStream = new FileInputStream("filename");
 *
 *     final ResettableFileInputStream resettableStream = new ResettableFileInputStream(fileStream);
 *
 *     final PutObjectRequest request = new PutObjectRequest("bucketName", "filename", jobId, resettableStream);
 *     }
 *
 *
 * </pre>
 */
public class ResettableFileInputStream extends FilterInputStream {
    private long markLocation = 0;
    private long markReadLimit = 0;

    public ResettableFileInputStream(final FileInputStream fileInputStream) {
        super(fileInputStream);
    }

    @Override
    public boolean markSupported() {
        return true;
    }

    @Override
    public void mark(final int readlimit) {
        try {
            this.markLocation = this.getChannel().position();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
        this.markReadLimit = readlimit;
    }

    @Override
    public void reset() throws IOException {
        final FileChannel channel = this.getChannel();
        if (channel.position() > this.markLocation + this.markReadLimit) {
            this.markLocation = 0;
            this.markReadLimit = 0;
        }
        channel.position(this.markLocation);
    }

    private FileChannel getChannel() {
        return ((FileInputStream)this.in).getChannel();
    }
}
