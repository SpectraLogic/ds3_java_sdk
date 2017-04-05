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

package com.spectralogic.ds3client.helpers.strategy.transferstrategy;

import com.spectralogic.ds3client.models.BulkObject;

/**
 * This class provides the ability to emit an event when a blob transfer has completed.
 */
public class BlobTransferredEventObserver extends AbstractObserver<BulkObject> {
    /**
     * @param updateStrategy The interface whose implementation determines how to handle emitting an event
     *                       when a blob transfer completes.
     */
    public BlobTransferredEventObserver(final UpdateStrategy<BulkObject> updateStrategy) {
        super(updateStrategy);
    }
}
