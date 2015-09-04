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

package com.spectralogic.ds3client.commands;

import com.spectralogic.ds3client.HttpVerb;

public class DeleteFolderRequest extends AbstractRequest {

    private final String folderName;
    private final String bucketName;

    public DeleteFolderRequest(final String bucketName, final String folderName) {
        this.bucketName = bucketName;
        this.folderName = folderName;
        getQueryParams().put("bucketId", this.bucketName);
        getQueryParams().put("recursive", null);
    }

    public String getBucket() { return this.bucketName; }

    public String getFolder() { return this.folderName; }

    @Override
    public String getPath() { return "/_rest_/folder/" + this.folderName; }

    @Override
    public HttpVerb getVerb() { return HttpVerb.DELETE; }
}
