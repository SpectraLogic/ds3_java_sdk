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

package com.spectralogic.ds3client.models;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;
import java.util.UUID;

public class MasterObjectList {
    private List<Objects> objects;

    @JacksonXmlProperty
    private UUID jobid;

    public MasterObjectList() {}

    public List<Objects> getObjects() {
        return objects;
    }

    public void setObjects(final List<Objects> objects) {
        this.objects = objects;
    }

    public String toString() {
        return objects.toString();
    }

    public UUID getJobid() {
        return jobid;
    }

    public void setJobid(final UUID jobid) {
        this.jobid = jobid;
    }
}
