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

package com.spectralogic.ds3client.utils;

import org.apache.http.ConnectionClosedException;
import com.spectralogic.ds3client.helpers.UnrecoverableIOException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class IOUtils {
    private static final Logger LOG = LoggerFactory.getLogger(IOUtils.class);

    public static long copy(
        final InputStream inputStream,
        final OutputStream outputStream,
        final int bufferSize,
        final String objName,
        final boolean isPutCommand)
            throws IOException {
        final byte[] buffer = new byte[bufferSize];
        int len;
        long totalBytes = 0;

        final long startTime = PerformanceUtils.getCurrentTime();
        long statusUpdateTime = startTime;
        while ((len = inputStream.read(buffer)) != -1) {
            totalBytes += len;
            outputStream.write(buffer, 0, len);

            final long curTime = PerformanceUtils.getCurrentTime();
            if (statusUpdateTime <= curTime) {
                PerformanceUtils.logMbpsStatus(startTime, curTime, totalBytes, objName, isPutCommand);
                statusUpdateTime += 60000D; //Only logs status once a minute
            }
        }

       return totalBytes;
    }

    public static long copy(
        final InputStream inputStream,
        final WritableByteChannel writableByteChannel,
        final int bufferSize,
        final String objName,
        final boolean isPutCommand)
            throws IOException {
        final byte[] buffer = new byte[bufferSize];
        final ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
        int len;
        long totalBytes = 0;

        final long startTime = PerformanceUtils.getCurrentTime();
        long statusUpdateTime = startTime;

        try {
            while ((len = inputStream.read(buffer)) != -1) {
                totalBytes += len;

                try {
                    byteBuffer.position(0);
                    byteBuffer.limit(len);
                    writableByteChannel.write(byteBuffer);
                } catch (final Throwable t) {
                    throw new UnrecoverableIOException(t);
                }

                final long curTime = PerformanceUtils.getCurrentTime();
                if (statusUpdateTime <= curTime) {
                    PerformanceUtils.logMbpsStatus(startTime, curTime, totalBytes, objName, isPutCommand);
                    statusUpdateTime += 60000D; //Only logs status once a minute
                }
            }
        } catch (final ConnectionClosedException e) {
            LOG.error("Connection closed trying to copy from stream to channel.", e);
        }

        return totalBytes;
    }
}

