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

import com.spectralogic.ds3client.networking.WebResponse;
import java.io.IOException;
import com.spectralogic.ds3client.models.S3ObjectList;
import java.io.InputStream;
import com.spectralogic.ds3client.serializer.XmlOutput;
import com.spectralogic.ds3client.commands.interfaces.AbstractResponse;
import java.util.List;

public class GetObjectsDetailsSpectraS3Response extends AbstractResponse {

    private S3ObjectList s3ObjectListResult;
    private Integer pagingTruncated;
    private Integer pagingTotalResultCount;

    public GetObjectsDetailsSpectraS3Response(final WebResponse response) throws IOException {
        super(response);
    }

    @Override
    protected void processResponse() throws IOException {
        try {
            this.checkStatusCode(200);

            switch (this.getStatusCode()) {
            case 200:
                try (final InputStream content = getResponse().getResponseStream()) {
                    this.s3ObjectListResult = XmlOutput.fromXml(content, S3ObjectList.class);
                    this.pagingTruncated = parseIntHeader("page-truncated");
                    this.pagingTotalResultCount = parseIntHeader("total-result-count");
                }
                break;
            default:
                assert false : "checkStatusCode should have made it impossible to reach this line.";
            }
        } finally {
            this.getResponse().close();
        }
    }

    private Integer parseIntHeader(final String key) {
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

    public Integer getPagingTruncated() {
        return pagingTruncated;
    }

    public Integer getPagingTotalResultCount() {
        return pagingTotalResultCount;
    }

    public S3ObjectList getS3ObjectListResult() {
        return this.s3ObjectListResult;
    }

}