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

import com.google.common.base.Preconditions;
import com.spectralogic.ds3client.helpers.DataTransferredListener;

public class DataTransferredObserver extends AbstractObserver<Long> {
    public DataTransferredObserver(final DataTransferredListener dataTransferredListener) {
        super(new UpdateStrategy<Long>() {
            @Override
            public void update(final Long eventData) {
                dataTransferredListener.dataTransferred(eventData);
            }
        });

        Preconditions.checkNotNull(dataTransferredListener, "dataTransferredListener may not be null.");
    }

    public DataTransferredObserver(final UpdateStrategy<Long> updateStrategy) {
        super(updateStrategy);
    }
}
