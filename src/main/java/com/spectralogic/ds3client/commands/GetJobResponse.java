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

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.spectralogic.ds3client.models.Ds3Object;
import com.spectralogic.ds3client.models.JobInfo;
import com.spectralogic.ds3client.models.JobObjects;
import com.spectralogic.ds3client.networking.WebResponse;
import com.spectralogic.ds3client.serializer.XmlOutput;

public class GetJobResponse extends AbstractResponse {
    private static final String STATE_IN_CACHE = "IN_CACHE";
    
    private JobInfo jobInfo;
    private List<JobObjects> objectsList;

    public GetJobResponse(final WebResponse response) throws IOException {
        super(response);
    }
    
    public JobInfo getJobInfo() {
        return this.jobInfo;
    }

    public List<JobObjects> getObjectsList() {
        return this.objectsList;
    }

    @Override
    protected void processResponse() throws IOException {
        final StringWriter writer = new StringWriter();
        IOUtils.copy(this.getResponse().getResponseStream(), writer, UTF8);
        final InternalJobResult internalResult = XmlOutput.fromXml(writer.toString(), InternalJobResult.class);
        
        this.jobInfo = convertInternalToExternalJobInfo(internalResult);
        this.objectsList = convertInternalToExternalObjectListList(internalResult.getObjectListList());
    }

    private static JobInfo convertInternalToExternalJobInfo(final InternalJobResult internalJobResult) {
        return new JobInfo(
            internalJobResult.getBucketName(),
            internalJobResult.getStartDate(),
            internalJobResult.getJobId(),
            internalJobResult.getPriority(),
            internalJobResult.getRequestType()
        );
    }

    private static List<JobObjects> convertInternalToExternalObjectListList(final List<InternalObjectList> internalListList) {
        final ArrayList<JobObjects> externalListList = new ArrayList<>();
        for (final InternalObjectList internalList : internalListList) {
            externalListList.add(convertInternalToExternalObjectList(internalList));
        }
        return externalListList;
    }

    private static JobObjects convertInternalToExternalObjectList(final InternalObjectList internalList) {
        final JobObjects externalList = new JobObjects();
        
        externalList.setServerId(internalList.getServerId());
        
        final List<Ds3Object> objects = new ArrayList<>();
        final List<Ds3Object> objectsInCache = new ArrayList<>();
        for (final InternalObject internalObject : internalList.getObjects()) {
            (
                internalObject.getState().equals(STATE_IN_CACHE)
                    ? objectsInCache
                    : objects
            ).add(new Ds3Object(internalObject.getName(), internalObject.getSize()));
        }
        externalList.setObject(objects);
        externalList.setObjectsInCache(objectsInCache);
        
        return externalList;
    }

    
    @JsonIgnoreProperties("ChunkNumber")
    private static class InternalObjectList {
        @JsonProperty("Object")
        private List<InternalObject> objects;
        
        @JsonProperty("ServerId")
        private String serverId;
        
        public List<InternalObject> getObjects() {
            return this.objects;
        }
        
        public String getServerId() {
            return this.serverId;
        }
    }
    
    private static class InternalJobResult extends JobInfo {
        @JsonProperty("Objects")
        private List<InternalObjectList> objectListList;
        
        public List<InternalObjectList> getObjectListList() {
            return this.objectListList;
        }
    }
    
    private static class InternalObject {
        @JsonProperty("Name")
        private String name;
        
        @JsonProperty("Size")
        private long size;
        
        @JsonProperty("State")
        private String state;
        
        public String getName() {
            return this.name;
        }
        
        public long getSize() {
            return this.size;
        }
        
        public String getState() {
            return this.state;
        }
    }
}
