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
package com.spectralogic.ds3client.helpers;

import com.google.common.collect.ImmutableList;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.models.JobNode;
import com.spectralogic.ds3client.models.Objects;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ChunkTransferrer_Test {

    @Test
    public void nullJobNode() throws IOException {
        final Ds3Client client = mock(Ds3Client.class);

        final ChunkTransferrer chunkTransferrer = new ChunkTransferrer(null, client, null, 1);
        final ImmutableList.Builder<Objects> objectsBuilder = ImmutableList.builder();

        final Objects objects = new Objects();
        objects.setChunkId(UUID.randomUUID());
        objects.setChunkNumber(0);
        objectsBuilder.add(objects);

        chunkTransferrer.transferChunks(new ArrayList<JobNode>(), objectsBuilder.build());

        verify(client, times(0)).newForNode((JobNode) any());
    }
}
