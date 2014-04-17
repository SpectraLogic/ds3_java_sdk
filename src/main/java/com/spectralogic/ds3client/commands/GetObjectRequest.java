/*
 * ******************************************************************************
 *   Copyright 2014 Spectra Logic Corporation. All Rights Reserved.
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
import org.apache.http.entity.ContentType;

public class GetObjectRequest extends AbstractRequest{

    private final String bucketName;
    private final String objectName;

    /**
     * We plan to mark this deprecated to encourage users to use the constructor that tacks a job Id.
     * We will still need this method for single Put operations, but the preferred method is to use
     * the put request in the context of a bulk request.
     */
    public GetObjectRequest(final String bucketName, final String objectName) {
       this.bucketName = bucketName;
        this.objectName = objectName;
    }

    @Override
    public String getPath() {
        return "/"+ bucketName + "/" + objectName;
    }


    @Override
    public ContentType getContentType() {
        return ContentType.APPLICATION_OCTET_STREAM;
    }

    @Override
    public HttpVerb getVerb() {
        return HttpVerb.GET;
    }
}
