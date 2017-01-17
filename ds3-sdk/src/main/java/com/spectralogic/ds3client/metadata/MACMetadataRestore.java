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

import com.spectralogic.ds3client.metadata.interfaces.MetadataRestoreListener;
import com.spectralogic.ds3client.networking.Metadata;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.spectralogic.ds3client.metadata.interfaces.MetadataKeyConstants.KEY_ACCESS_TIME;
import static com.spectralogic.ds3client.metadata.interfaces.MetadataKeyConstants.KEY_CREATION_TIME;
import static com.spectralogic.ds3client.metadata.interfaces.MetadataKeyConstants.KEY_LAST_MODIFIED_TIME;

class MACMetadataRestore extends PosixMetadataRestore {
    private static final Logger LOG = LoggerFactory.getLogger(MACMetadataRestore.class);

    public MACMetadataRestore(final Metadata metadata, final String filePath, final String localOS, final MetadataRestoreListener metadataRestoreListener) {
        super(metadata, filePath, localOS, metadataRestoreListener);
    }

    /**
     * Return date in specified format.
     *
     * @param milliSeconds Date in milliseconds
     * @param dateFormat   Date format
     * @return String representing date in specified format
     */
    public static String getDate(final long milliSeconds, final String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        final SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    @Override
    public void restoreFileTimes() {
        String creationTime = null;
        if (metadata.get(KEY_CREATION_TIME).size() > 0) {
            creationTime = metadata.get(KEY_CREATION_TIME).get(0);
        }
        String accessTime = null;
        if (metadata.get(KEY_ACCESS_TIME).size() > 0) {
            accessTime = metadata.get(KEY_ACCESS_TIME).get(0);
        }
        String modifiedTime = null;
        if (metadata.get(KEY_LAST_MODIFIED_TIME).size() > 0) {
            modifiedTime = metadata.get(KEY_LAST_MODIFIED_TIME).get(0);
        }
        if (modifiedTime != null && creationTime != null && accessTime != null) {
            restoreCreationTimeMAC(objectName, creationTime);
            restoreModifiedTimeMAC(objectName, modifiedTime);
        }

    }

    /**
     * Restore creation time in mac only if target creation is before current creation time
     *
     * @param objectName   path of the object where we need to restore
     * @param creationTime creation time got from server
     */
    private void restoreCreationTimeMAC(final String objectName, final String creationTime) {
        try {
            final ProcessBuilder processBuilder = new ProcessBuilder("touch", "-t", getDate(Long.parseLong(creationTime), "YYYYMMddHHmm"), objectName);
            final Process process = processBuilder.start();
            //Wait to get exit value
            final int exitValue = process.waitFor();
            if(exitValue != 0) {
                LOG.error("Unable to restore creation time::"+exitValue);
                metadataRestoreListener.metadataRestoreFailed("Unable to restore creation time ::"+exitValue);
            }
        } catch (final Exception e) {
            LOG.error("Unable to restore creation time", e);
            metadataRestoreListener.metadataRestoreFailed("Unable to restore creation time ::"+e.getMessage());
        }

    }

    /**
     * Restore modified time in case of MAC using touch command
     *
     * @param objectName   path of the object where we need to restore
     * @param modifiedTime modified time need to restore
     */
    private void restoreModifiedTimeMAC(final String objectName, final String modifiedTime) {
        try {
            final ProcessBuilder processBuilder = new ProcessBuilder("touch", "-mt", getDate(Long.parseLong(modifiedTime), "YYYYMMddHHmm"), objectName);
            final Process process = processBuilder.start();
            //Wait to get exit value
            final int exitValue = process.waitFor();
            if(exitValue != 0) {
                LOG.error("Unable to restore modified time::"+exitValue);
                metadataRestoreListener.metadataRestoreFailed("Unable to restore creation time ::"+exitValue);
            }
        } catch (final Exception e) {
            LOG.error("Unable to restore modified time", e);
            metadataRestoreListener.metadataRestoreFailed("Unable to restore modified time ::"+e.getMessage());
        }

    }
}
