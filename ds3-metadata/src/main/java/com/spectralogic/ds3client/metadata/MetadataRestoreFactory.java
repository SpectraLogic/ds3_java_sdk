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

import com.spectralogic.ds3client.metadata.interfaces.MetadataRestore;
import com.spectralogic.ds3client.networking.Metadata;
import com.spectralogic.ds3client.utils.Platform;


public class MetadataRestoreFactory {
    public MetadataRestore getOSSpecificMetadataRestore(final Metadata metadata, final String filePath) {
        final String localOS = MetaDataUtil.getOS();
        
        if (Platform.isWindows()) {
            return new WindowsMetadataRestore(metadata, filePath, localOS);
        } else if (Platform.isMac()) {
            return new MACMetadataRestore(metadata, filePath, localOS);
        } else {
            return new PosixMetadataRestore(metadata, filePath, localOS);
        }
    }
}
