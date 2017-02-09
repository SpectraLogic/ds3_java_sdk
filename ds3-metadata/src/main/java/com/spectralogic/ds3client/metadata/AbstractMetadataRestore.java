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

package com.spectralogic.ds3client.metadata;

import com.spectralogic.ds3client.metadata.interfaces.MetadataRestore;
import com.spectralogic.ds3client.networking.Metadata;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.FileTime;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.spectralogic.ds3client.metadata.MetadataKeyConstants.KEY_ACCESS_TIME;
import static com.spectralogic.ds3client.metadata.MetadataKeyConstants.KEY_CREATION_TIME;
import static com.spectralogic.ds3client.metadata.MetadataKeyConstants.KEY_LAST_MODIFIED_TIME;
import static com.spectralogic.ds3client.metadata.MetadataKeyConstants.KEY_OS;

abstract class AbstractMetadataRestore implements MetadataRestore {
    protected String storedOS;
    protected Metadata metadata;
    protected String objectName;
    protected String localOS;

    @Override
    public void restoreFileTimes() throws IOException, InterruptedException {
        String creationTime = null;
        if(metadata.get(KEY_CREATION_TIME).size()>0) {
            creationTime = metadata.get(KEY_CREATION_TIME).get(0);
        }

        String accessTime = null;
        if(metadata.get(KEY_ACCESS_TIME).size()>0) {
            accessTime = metadata.get(KEY_ACCESS_TIME).get(0);
        }

        String modifiedTime = null;
        if(metadata.get(KEY_LAST_MODIFIED_TIME).size()>0) {
            modifiedTime = metadata.get(KEY_LAST_MODIFIED_TIME).get(0);
        }

        if (modifiedTime != null && creationTime != null && accessTime != null) {
            final String modifiedTimes = modifiedTime;
            final String creationTimes = creationTime;
            final String accessTimes = accessTime;
            final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

            executorService.schedule(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    setFileTimes(objectName, creationTimes, modifiedTimes, accessTimes);
                    return null;
                }
            }, 10, TimeUnit.SECONDS);
        }
    }

    @Override
    public void restoreOSName() {
        if (metadata.get(KEY_OS).size() > 0) {
            storedOS = metadata.get(KEY_OS).get(0);
        }
    }
    /**
     * Restore the creation time , access time and modified time to local
     *
     * @param filePath path of local file
     * @param creationTime creation time got from server
     * @param lastModifiedTime modified time got from server
     * @param lastAccessedTime last aceess time got from server
     */
    private void setFileTimes(final String filePath,
                              final String creationTime,
                              final String lastModifiedTime,
                              final String lastAccessedTime)
        throws IOException
    {
        final BasicFileAttributeView attributes = Files.getFileAttributeView(Paths.get(filePath), BasicFileAttributeView.class);
        final FileTime timeCreation = FileTime.fromMillis(Long.parseLong(creationTime));
        final FileTime timeModified = FileTime.fromMillis(Long.parseLong(lastModifiedTime));
        final FileTime timeAccessed = FileTime.fromMillis(Long.parseLong(lastAccessedTime));
        attributes.setTimes(timeModified, timeAccessed, timeCreation);
    }
}
