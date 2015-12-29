/*
 * ******************************************************************************
 *   Copyright 2014-2015 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3client.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

public class IOUtils {

    static private final Logger LOG = LoggerFactory.getLogger(IOUtils.class);

    public static void copy(
        final InputStream inputStream,
        final OutputStream outputStream,
        final int bufferSize,
        final String path)
            throws IOException {
        final byte[] buffer = new byte[bufferSize];
        int len;
        long totalBytes = 0;

        final long startTime = getStartTime();
        while ((len = inputStream.read(buffer)) != -1) {
            totalBytes += len;
            outputStream.write(buffer, 0, len);
        }
        final long endTime = getEndTime();
        logMbps(startTime, endTime, totalBytes, path, true);
    }

    public static void copy(
        final InputStream inputStream,
        final WritableByteChannel writableByteChannel,
        final int bufferSize,
        final String objName)
            throws IOException {
        final byte[] buffer = new byte[bufferSize];
        final ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
        int len;
        long totalBytes = 0;

        final long startTime = getStartTime();
        while ((len = inputStream.read(buffer)) != -1) {
            totalBytes += len;
            byteBuffer.position(0);
            byteBuffer.limit(len);
            writableByteChannel.write(byteBuffer);
        }
        final long endTime = getEndTime();
        logMbps(startTime, endTime, totalBytes, objName, false);
    }

    private static long getEndTime() {
        return System.currentTimeMillis();
    }

    private static long getStartTime() {
        return System.currentTimeMillis();
    }

    private static void logMbps(final long startTime, final long endTime, final long totalBytes, final String objName, final boolean isPutCommand) {
        final double time = (endTime - startTime == 0)? 1.0: (endTime - startTime)/1000D;
        final double content = totalBytes/1024D/1024D;
        final double mbps = content / time;

        if (isPutCommand) {
            LOG.info(String.format("Putting %s statistics: Length (%.03f MB), Time (%.03f sec), Mbps (%.03f)", objName, content, time, mbps));
        }
        else {
            LOG.info(String.format("Getting %s statistics: Length (%.03f MB), Time (%.03f sec), Mbps (%.03f)", objName, content, time, mbps));
        }
    }
}

