/*
 * ****************************************************************************
 *    Copyright 2014-2016 Spectra Logic Corporation. All Rights Reserved.
 *    Licensed under the Apache License, Version 2.0 (the "License"). You may not use
 *    this file except in compliance with the License. A copy of the License is located at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *    or in the "license" file accompanying this file.
 *    This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 *    CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *    specific language governing permissions and limitations under the License.
 *  ****************************************************************************
 */

// This code is auto-generated, do not modify

package com.spectralogic.ds3client.commands.spectrads3;

import com.google.common.collect.ImmutableMap;
import com.spectralogic.ds3client.MockedWebResponse;
import com.spectralogic.ds3client.commands.parsers.GetJobToReplicateSpectraS3ResponseParser;
import com.spectralogic.ds3client.networking.WebResponse;
import org.junit.Test;

import java.io.IOException;

public class GetPutJobToReplicate_Test {

    @Test
    public void getPutJobToReplicate_ProcessResponse_Test() throws IOException {
        final String responsePayload = "Some response payload";
        final ImmutableMap<String, String> emptyMap = ImmutableMap.of();
        final WebResponse webResponse = new MockedWebResponse(responsePayload, 200, emptyMap);
        final GetJobToReplicateSpectraS3Response response = new GetJobToReplicateSpectraS3ResponseParser()
                .startResponse(webResponse);
        response.getStringResult();
    }
}
