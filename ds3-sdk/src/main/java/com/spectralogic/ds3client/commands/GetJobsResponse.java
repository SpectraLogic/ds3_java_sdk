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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.spectralogic.ds3client.models.bulk.JobInfo;
import com.spectralogic.ds3client.networking.WebResponse;
import com.spectralogic.ds3client.serializer.XmlOutput;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;

public class GetJobsResponse extends AbstractResponse {
    private List<JobInfo> jobs;

    public GetJobsResponse(final WebResponse response) throws IOException {
        super(response);
    }

    @Override
    protected void processResponse() throws IOException {
        try (final WebResponse response = this.getResponse()) {
            checkStatusCode(200);
            this.jobs = parseJobs(response).getJobs();
        }
    }

    public List<JobInfo> getJobs() {
        return this.jobs;
    }
    
    private static Jobs parseJobs(final WebResponse webResponse) throws IOException {
        try (final InputStream content = webResponse.getResponseStream();
             final StringWriter writer = new StringWriter()) {
            IOUtils.copy(content, writer, UTF8);
            return XmlOutput.fromXml(writer.toString(), Jobs.class);
        }
    }
    
    private static class Jobs {
        @JsonProperty("Job")
        private List<JobInfo> jobs;

        public List<JobInfo> getJobs() {
            return jobs;
        }
    }
}
