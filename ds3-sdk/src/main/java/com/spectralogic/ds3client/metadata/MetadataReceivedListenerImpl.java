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

import com.spectralogic.ds3client.helpers.MetadataReceivedListener;
import com.spectralogic.ds3client.metadata.interfaces.MetadataRestore;
import com.spectralogic.ds3client.networking.Metadata;

import java.io.IOException;

public class MetadataReceivedListenerImpl implements MetadataReceivedListener {
    private String localFilePath = null;

    public MetadataReceivedListenerImpl(final String localFilePath) {
        this.localFilePath = localFilePath;
    }

    @Override
    public void metadataReceived(final String filename, final Metadata metadata) throws IOException, InterruptedException {
        final String actualFilePath = MetaDataUtil.getRealFilePath(localFilePath, filename);
        restoreMetaData(actualFilePath, metadata);
    }

    /**
     * Restore the metadata to local file
     *
     * @param objectName name of the file to be restored
     * @param metadata   metadata which needs to be set on local file
     */
    private void restoreMetaData(final String objectName, final Metadata metadata) throws IOException, InterruptedException {
        //get metadatarestore on the basis of os
        final MetadataRestore metadataRestore = new MetadataRestoreFactory().getOSSpecificMetadataRestore(metadata, objectName);
        //restore os name
        metadataRestore.restoreOSName();
        //restore user and owner based on OS
        metadataRestore.restoreUserAndOwner();
        //restore creation and modified time based on OS
        metadataRestore.restoreFileTimes();
        //restore permissions based on OS
        metadataRestore.restorePermissions();
    }


}
