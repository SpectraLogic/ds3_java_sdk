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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PerformanceUtils {

    static private final Logger LOG = LoggerFactory.getLogger(PerformanceUtils.class);

    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public static void logMbps(final long startTime, final long endTime, final long totalBytes, final String objName, final boolean isPutCommand) {
        final double time = (endTime - startTime == 0)? 1.0: (endTime - startTime)/1000D;
        final double content = convertBytesToMegaBytes(totalBytes);
        final double mbps = content / time;

        final String messagePrefix = getMessagePrefix(isPutCommand);
        
        LOG.info("{}", String.format("%s %s statistics: Length (%.03f MB), Time (%.03f sec), MBps (%.03f)", messagePrefix, objName, content, time, mbps));
    }

    /**
     * Logs the current status of a get/put command
     */
    public static void logMbpsStatus(
            final long startTime,
            final long curTime,
            final long curBytes,
            final String objName,
            final boolean isPutCommand) {
        final double time = (curTime - startTime == 0)? 0.0: (curTime - startTime)/1000D;
        final double curMegaBytes = convertBytesToMegaBytes(curBytes);
        final String messagePrefix = getMessagePrefix(isPutCommand);

        LOG.info("{}", String.format("%s %s status: Transferred (%.03f MB), Time (%.03f sec)", messagePrefix, objName, curMegaBytes, time));
    }

    /**
     * Creates the message prefix for the specified type of command
     */
    private static String getMessagePrefix(final boolean isPutCommand) {
        if (isPutCommand) {
            return  "Putting";
        }
        return  "Getting";
    }

    /**
     * Converts bytes to megabytes
     */
    private static double convertBytesToMegaBytes(final long bytes) {
        return bytes/1024D/1024D;
    }
}
