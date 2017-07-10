/*
 * ******************************************************************************
 *   Copyright 2014-2017 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3client.helpers.strategy.blobstrategy;

import com.spectralogic.ds3client.models.Objects;

import java.util.Collection;

/**
 * When aggregating jobs from more than one process, it is possible that one process will see a
 * {@link com.spectralogic.ds3client.models.MasterObjectList}
 * that contains blobs defined in the other process.  To prevent one process from trying to transfer
 * blobs defined in another process, we apply a filter to master object lists to eliminate blobs not
 * originally defined in a particular process.  The ChunkFilter interface implements the behavior
 * in a {@link NullMasterObjectListFilter} filter that decides what chunks to include in the resultant
 * master object list.
 */
public interface ChunkFilter {
    /**
     * Filter out chunks containing blobs not originally defined in a job a process creates.
     * @param chunksFromMasterObjectList The chunks in either a master object list returned from a call to
     *                                   {@link com.spectralogic.ds3client.helpers.Ds3ClientHelpers.Job#startWriteJob(TransferStrategy)},
     *                                   {@link com.spectralogic.ds3client.helpers.Ds3ClientHelpers.Job#startReadJob(TransferStrategy)},
     *                                   or one its variants or from {@link com.spectralogic.ds3client.Ds3Client#getJobChunksReadyForClientProcessingSpectraS3(GetJobChunksReadyForClientProcessingSpectraS3Request)}
     * @return Chunks containing blobs originally defined in a particular process.
     */
    Iterable<Objects> apply(final Collection<Objects> chunksFromMasterObjectList);
}
