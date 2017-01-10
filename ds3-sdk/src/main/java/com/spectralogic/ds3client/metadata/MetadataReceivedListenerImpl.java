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

// This code is auto-generated, do not modify
package com.spectralogic.ds3client.metadata;

import com.spectralogic.ds3client.helpers.MetadataReceivedListener;
import com.spectralogic.ds3client.metadata.interfaces.MetaDataRestore;
import com.spectralogic.ds3client.metadata.interfaces.MetaDataRestoreListener;
import com.spectralogic.ds3client.networking.Metadata;
import com.spectralogic.ds3client.utils.MetaDataUtil;

public class MetadataReceivedListenerImpl implements MetadataReceivedListener {
    private String localFilePath = null;
    private final MetaDataRestoreListener metaDataRestoreListener;

    public MetadataReceivedListenerImpl(final String localFilePath ,final MetaDataRestoreListener metaDataRestoreListener) {
        this.localFilePath = localFilePath;
        this.metaDataRestoreListener = metaDataRestoreListener;
    }

    @Override
    public void metadataReceived(final String filename, final Metadata metadata) {
        final String actualFilePath = MetaDataUtil.getRealFilePath(localFilePath, filename);
        restoreMetaData(actualFilePath, metadata);
    }

    /**
     * Restore the metadata to local file
     *
     * @param objectName name of the file to be restored
     * @param metadata   metadata which needs to be set on local file
     */
    private void restoreMetaData(final String objectName, final Metadata metadata) {
        final String osName = System.getProperty("os.name");
        //get metadatarestore on the basis of os
        final MetaDataRestore metaDataRestore = new MetaDataRestoreFactory().getOSSpecificMetadataRestore(metadata, objectName, osName,metaDataRestoreListener);
        //restore os name
        metaDataRestore.restoreOSName();
        //restore user and owner based on OS
        metaDataRestore.restoreUserAndOwner();
        //restore creation and modified time based on OS
        metaDataRestore.restoreFileTimes();
        //restore permissions based on OS
        metaDataRestore.restorePermissions();
        if (metaDataRestore instanceof WindowsMetaDataRestore) {
            ((WindowsMetaDataRestore) metaDataRestore).restoreFlags();
        }


    }


}
