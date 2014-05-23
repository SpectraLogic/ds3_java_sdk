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
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.spectralogic.ds3client.models.JobInfo;
import com.spectralogic.ds3client.networking.WebResponse;
import com.spectralogic.ds3client.serializer.XmlOutput;

public class GetJobListResponse extends AbstractResponse {
    private List<JobInfo> jobs;
    
    public GetJobListResponse(final WebResponse response) throws IOException {
        super(response);
    }
    
    @Override
    protected void processResponse() throws IOException {
        this.checkStatusCode(200);
        final StringWriter writer = new StringWriter();
        IOUtils.copy(this.getResponse().getResponseStream(), writer, UTF8);
        this.jobs = XmlOutput.fromXml(writer.toString(), InternalJobListResult.class).getJobs();
    }

    public List<JobInfo> getJobs() {
        return this.jobs;
        
    }
    
    private static class InternalJobListResult {
        @JsonProperty("Job")
        private List<JobInfo> jobs;
        
        public List<JobInfo> getJobs() {
            return this.jobs;
        }
    }
}
