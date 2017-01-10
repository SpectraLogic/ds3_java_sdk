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
package com.spectralogic.ds3client.utils;

import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.Set;

public class MetaDataUtil {

    public static Set<String> getSupportedFileAttributes(final Path file) {
        final FileSystem store = file.getFileSystem();
        return store.supportedFileAttributeViews();
    }


    /**
     * @return name of Operting System in String
     */

    public static String getOS() {
        return System.getProperty("os.name");
    }


    /**
     * Get the actual file path from the objectName received from BP
     *
     * @param localFilePath :path of local directory
     * @param filename      : name of the file with logical folder
     * @return actualFilePath
     */
    public static String getRealFilePath(final String localFilePath, final String filename) {
        final String filePath = localFilePath + "/" + filename;
        String fName = filePath;
        while (true) {
            final File file = new File(fName);
            if (file.exists()) {
                break;
            } else {
                final File parentFile = new File(file.getParent());
                fName = parentFile.getParent() + "/" + file.getName();
            }
        }
        return fName;
    }

}
