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

package com.spectralogic.ds3client.models.bulk;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.util.List;
import java.util.UUID;

public class MasterObjectList {

    @JsonProperty("Objects")
    private List<Objects> objects;

    @JsonProperty("Nodes")
    @JacksonXmlElementWrapper
    private List<Node> nodes;

    @JsonProperty("BucketName")
    private String bucketName;

    @JsonProperty("JobId")
    private UUID jobId;

    @JsonProperty("Priority")
    private Priority priority;

    @JsonProperty("RequestType")
    private RequestType requestType;

    @JsonProperty("StartDate")
    private String startDate;

    public MasterObjectList() {}

    public List<Objects> getObjects() {
        return this.objects;
    }
    public void setObjects(final List<Objects> objects) {
        this.objects = objects;
    }

    public UUID getJobId() {
        return this.jobId;
    }
    public void setJobId(final UUID jobId) {
        this.jobId = jobId;
    }

    @Override
    public String toString() {
        return this.objects.toString();
    }
}
