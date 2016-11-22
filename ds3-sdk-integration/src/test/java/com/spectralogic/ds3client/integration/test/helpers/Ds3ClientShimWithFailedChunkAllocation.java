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

package com.spectralogic.ds3client.integration.test.helpers;

import com.spectralogic.ds3client.Ds3ClientImpl;
import com.spectralogic.ds3client.MockedWebResponse;
import com.spectralogic.ds3client.commands.spectrads3.AllocateJobChunkSpectraS3Request;
import com.spectralogic.ds3client.commands.spectrads3.AllocateJobChunkSpectraS3Response;
import com.spectralogic.ds3client.commands.spectrads3.GetJobChunksReadyForClientProcessingSpectraS3Request;
import com.spectralogic.ds3client.commands.spectrads3.GetJobChunksReadyForClientProcessingSpectraS3Response;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class Ds3ClientShimWithFailedChunkAllocation extends Ds3ClientShim {
    public Ds3ClientShimWithFailedChunkAllocation(final Ds3ClientImpl ds3ClientImpl)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
    {
        super(ds3ClientImpl);
    }

    @Override
    public AllocateJobChunkSpectraS3Response allocateJobChunkSpectraS3(final AllocateJobChunkSpectraS3Request request)
            throws IOException
    {
        final AllocateJobChunkSpectraS3Response allocateJobChunkSpectraS3Response =
                new AllocateJobChunkSpectraS3Response(new MockedWebResponse("A response", 307, makeFailingResponseHeaders()));

        return allocateJobChunkSpectraS3Response;
    }

    private Map<String, String> makeFailingResponseHeaders() {
        final Map<String, String> headers = new HashMap<>();
        headers.put("content-NONE", "text/xml");
        headers.put("Retry-After", "1");

        return headers;
    }

    @Override
    public GetJobChunksReadyForClientProcessingSpectraS3Response getJobChunksReadyForClientProcessingSpectraS3(final GetJobChunksReadyForClientProcessingSpectraS3Request request)
            throws IOException
    {
        final GetJobChunksReadyForClientProcessingSpectraS3Response getJobChunksReadyForClientProcessingSpectraS3Response =
                new GetJobChunksReadyForClientProcessingSpectraS3Response(new MockedWebResponse("A response", 307, makeFailingResponseHeaders()));

        return getJobChunksReadyForClientProcessingSpectraS3Response;
    }
}
