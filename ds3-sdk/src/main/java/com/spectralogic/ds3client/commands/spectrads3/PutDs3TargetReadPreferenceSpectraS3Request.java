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

// This code is auto-generated, do not modify
package com.spectralogic.ds3client.commands.spectrads3;

import com.spectralogic.ds3client.HttpVerb;
import com.spectralogic.ds3client.commands.AbstractRequest;
import com.google.common.net.UrlEscapers;
import com.spectralogic.ds3client.models.TargetReadPreference;
import java.util.UUID;

public class PutDs3TargetReadPreferenceSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String bucketId;

    private final TargetReadPreference readPreference;

    private final UUID targetId;

    // Constructor
    
    public PutDs3TargetReadPreferenceSpectraS3Request(final String bucketId, final TargetReadPreference readPreference, final UUID targetId) {
        this.bucketId = bucketId;
        this.readPreference = readPreference;
        this.targetId = targetId;
                this.getQueryParams().put("bucket_id", bucketId);
        this.getQueryParams().put("read_preference", readPreference.toString());
        this.getQueryParams().put("target_id", targetId.toString());
    }


    @Override
    public HttpVerb getVerb() {
        return HttpVerb.POST;
    }

    @Override
    public String getPath() {
        return "/_rest_/ds3_target_read_preference";
    }
    
    public String getBucketId() {
        return this.bucketId;
    }


    public TargetReadPreference getReadPreference() {
        return this.readPreference;
    }


    public UUID getTargetId() {
        return this.targetId;
    }

}