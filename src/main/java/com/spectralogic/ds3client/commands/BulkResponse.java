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

import com.spectralogic.ds3client.models.MasterObjectList;
import com.spectralogic.ds3client.serializer.XmlOutput;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public abstract class BulkResponse extends AbstractResponse {
    private MasterObjectList result;
    public BulkResponse(final CloseableHttpResponse response) throws IOException {
        super(response);
    }

    public MasterObjectList getResult() {
        return result;
    }

    @Override
    protected void processResponse() throws IOException {
        checkStatusCode(200);
        final CloseableHttpResponse response = getResponse();

        try(final StringWriter writer = new StringWriter();
            final InputStream content = response.getEntity().getContent()){

            IOUtils.copy(content, writer, UTF8);

            result = XmlOutput.fromXml(writer.toString(), MasterObjectList.class);
        }
    }
}
