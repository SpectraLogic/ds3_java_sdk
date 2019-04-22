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

package com.spectralogic.ds3client.commands.parsers.interfaces;

import com.spectralogic.ds3client.commands.interfaces.Ds3Response;
import com.spectralogic.ds3client.models.ChecksumType;
import com.spectralogic.ds3client.networking.WebResponse;

import java.io.IOException;
import java.util.List;

import static com.spectralogic.ds3client.commands.parsers.utils.ResponseParserUtils.determineChecksumType;
import static com.spectralogic.ds3client.commands.parsers.utils.ResponseParserUtils.getFirstHeaderValue;

public abstract class AbstractResponseParser<T extends Ds3Response> {

    public abstract T parseXmlResponse(final WebResponse response) throws IOException;

    private WebResponse response;
    private String checksum;
    private ChecksumType.Type checksumType;

    public T response(final WebResponse response) throws IOException {
        this.response = response;
        if (response != null) {
            this.checksumType = determineChecksumType(this.response.getHeaders());
            if (this.checksumType != null) {
                this.checksum = getFirstHeaderValue(this.response.getHeaders(), "content-" + checksumType.toString().replace("_", "").toLowerCase());
            } else {
                this.checksum = null;
            }
        }
        else {
            this.checksum = null;
            this.checksumType = ChecksumType.Type.NONE;
        }
        return parseXmlResponse(response);
    }

    public  WebResponse getResponse() {
        return this.response;
    }

    public int getStatusCode() {
        return this.response.getStatusCode();
    }

    protected Integer parseIntHeader(final String key) {
        final List<String> list = getResponse().getHeaders().get(key);
        switch (list.size()) {
            case 0:
                return null;
            case 1:
                return Integer.parseInt(list.get(0));
            default:
                throw new IllegalArgumentException("Response has more than one header value for " + key);
        }
    }

    public String getChecksum() {
        return checksum;
    }

    public ChecksumType.Type getChecksumType() {
        return checksumType;
    }
}