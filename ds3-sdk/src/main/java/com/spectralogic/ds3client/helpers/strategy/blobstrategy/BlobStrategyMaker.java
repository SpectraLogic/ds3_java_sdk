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

package com.spectralogic.ds3client.helpers.strategy.blobstrategy;

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.helpers.strategy.transferstrategy.EventDispatcher;
import com.spectralogic.ds3client.models.MasterObjectList;

/**
 * An interface used as an intermediate when building a {@link com.spectralogic.ds3client.helpers.strategy.transferstrategy.TransferStrategy}
 * instance as part of getting a transfer strategy from a {@link com.spectralogic.ds3client.helpers.strategy.transferstrategy.TransferStrategyBuilder}.
 */
public interface BlobStrategyMaker {
    BlobStrategy makeBlobStrategy(final Ds3Client client,
                                  final MasterObjectList masterObjectList,
                                  final EventDispatcher eventDispatcher);
}
