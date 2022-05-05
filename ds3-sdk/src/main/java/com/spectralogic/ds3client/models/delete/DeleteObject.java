/*
 * ******************************************************************************
 *   Copyright 2014-2019 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3client.models.delete;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

@JsonInclude( JsonInclude.Include.NON_NULL )
public class DeleteObject {

    @JsonProperty("Key")
    private String key;

    @JsonProperty("VersionId")
    private UUID versionId;

    public DeleteObject(final String key, final UUID versionId) {
        this.key = key;
        this.versionId = versionId;
    }

    public DeleteObject(final String key) {
        this(key, null);
    }

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }


    public UUID getVersionId()
    {
        return versionId;
    }


    public void setVersionId( final UUID versionId )
    {
        this.versionId = versionId;
    }
}
