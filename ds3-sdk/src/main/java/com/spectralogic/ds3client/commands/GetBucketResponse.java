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


import com.spectralogic.ds3client.models.ListBucketResult;
import com.spectralogic.ds3client.networking.WebResponse;
import com.spectralogic.ds3client.serializer.XmlOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class GetBucketResponse extends AbstractResponse {
    private final static Logger LOG = LoggerFactory.getLogger(GetBucketResponse.class);
    private ListBucketResult result;

    public GetBucketResponse(final WebResponse response) throws IOException {
        super(response);

    }

    public ListBucketResult getResult() {
        return this.result;
    }

    @Override
    protected void processResponse() throws IOException {
        try (final WebResponse response = this.getResponse();
             final InputStream contentStream = response.getResponseStream()) {
            this.checkStatusCode(200);
            LOG.debug("Starting bucket xml parsing");
            this.result = XmlOutput.fromXml(contentStream, ListBucketResult.class);
            LOG.debug("Finished bucket xml parsing");
        }
    }
}
