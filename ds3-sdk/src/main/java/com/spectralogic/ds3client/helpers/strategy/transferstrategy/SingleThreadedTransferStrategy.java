/*
 * ****************************************************************************
 *    Copyright 2014-2017 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3client.helpers.strategy.transferstrategy;

import com.spectralogic.ds3client.helpers.JobState;
import com.spectralogic.ds3client.helpers.events.FailureEvent;
import com.spectralogic.ds3client.helpers.strategy.blobstrategy.BlobStrategy;
import com.spectralogic.ds3client.models.MasterObjectList;

import java.util.concurrent.Executors;

/**
 * An implementation of {@link TransferStrategy} that runs transfers in a single-threaded executor.
 */
public class SingleThreadedTransferStrategy extends AbstractTransferStrategy {
    public SingleThreadedTransferStrategy(final BlobStrategy blobStrategy,
                                          final JobState jobState,
                                          final EventDispatcher eventDispatcher,
                                          final MasterObjectList masterObjectList,
                                          final FailureEvent.FailureActivity failureActivity)
    {
        super(blobStrategy,
              jobState,
              Executors.newSingleThreadExecutor(),
              eventDispatcher,
              masterObjectList,
              failureActivity);
    }
}
