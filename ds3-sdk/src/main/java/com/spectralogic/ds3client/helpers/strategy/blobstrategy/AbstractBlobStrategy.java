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

package com.spectralogic.ds3client.helpers.strategy.blobstrategy;

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.helpers.strategy.transferstrategy.BlobTransferredEventObserver;
import com.spectralogic.ds3client.helpers.strategy.transferstrategy.EventDispatcher;
import com.spectralogic.ds3client.helpers.strategy.transferstrategy.UpdateStrategy;
import com.spectralogic.ds3client.models.BulkObject;
import com.spectralogic.ds3client.models.MasterObjectList;

public abstract class AbstractBlobStrategy implements BlobStrategy {

    private final Ds3Client client;
    private final MasterObjectList masterObjectList;
    private final EventDispatcher eventDispatcher;
    private final ChunkAttemptRetryBehavior retryBehavior;
    private final ChunkAttemptRetryDelayBehavior chunkAttemptRetryDelayBehavior;

    public AbstractBlobStrategy(final Ds3Client client,
                                final MasterObjectList masterObjectList,
                                final EventDispatcher eventDispatcher,
                                final ChunkAttemptRetryBehavior retryBehavior,
                                final ChunkAttemptRetryDelayBehavior chunkAttemptRetryDelayBehavior)
    {
        this.client = client;
        this.masterObjectList = masterObjectList;
        this.eventDispatcher = eventDispatcher;
        this.retryBehavior = retryBehavior;
        this.chunkAttemptRetryDelayBehavior = chunkAttemptRetryDelayBehavior;

        eventDispatcher.attachBlobTransferredEventObserver(new BlobTransferredEventObserver(new UpdateStrategy<BulkObject>() {
            @Override
            public void update(final BulkObject eventData) {
                blobCompleted(eventData);
            }
        }));
    }

    /**
     * Emit an event when a blob is transferred.
     * @param bulkObject The transferred blob.
     */
    public abstract void blobCompleted(final BulkObject bulkObject);

    public Ds3Client client() {
        return client;
    }

    public EventDispatcher eventDispatcher() {
        return eventDispatcher;
    }

    public MasterObjectList masterObjectList() {
        return masterObjectList;
    }

    public ChunkAttemptRetryBehavior retryBehavior() {
        return retryBehavior;
    }

    public ChunkAttemptRetryDelayBehavior chunkAttemptRetryDelayBehavior() {
        return chunkAttemptRetryDelayBehavior;
    }
}
