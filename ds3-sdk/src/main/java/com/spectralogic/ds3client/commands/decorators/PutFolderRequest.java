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

package com.spectralogic.ds3client.commands.decorators;

import com.spectralogic.ds3client.commands.PutObjectRequest;
import com.spectralogic.ds3client.exceptions.FolderNameMissingTrailingForwardSlash;

import java.util.UUID;

/**
 * Decorates the {@link com.spectralogic.ds3client.commands.PutObjectRequest} and is used to
 * ensure the correct creation of a folder using the Put Object command.
 *
 * A folder is created by putting an object with no content, of zero size, and with the
 * content-length=0.  Also, the name of the folder must end with a forward slash '/'.
 */
public class PutFolderRequest {

    private final PutObjectRequest putObjectRequest;

    public PutFolderRequest(final String bucketName, final String folderName, final UUID job) {
        validateNameEndsWithSlash(folderName);
        putObjectRequest = new PutObjectRequest(bucketName, folderName, job, 0, 0, null);
        putObjectRequest.getHeaders().put("Content-Length", "0");
    }

    private static void validateNameEndsWithSlash(final String folderName) {
        if (!folderName.endsWith("/")) {
            throw new FolderNameMissingTrailingForwardSlash(folderName);
        }
    }

    public PutObjectRequest getPutObjectRequest() {
        return putObjectRequest;
    }
}
