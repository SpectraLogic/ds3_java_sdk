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

package com.spectralogic.ds3client.helpers.strategy.transferstrategy;

import com.google.common.base.Preconditions;
import com.spectralogic.ds3client.helpers.FailureEventListener;
import com.spectralogic.ds3client.helpers.events.FailureEvent;

/**
 * This class provides the ability to emit an event when we have encountered an exception
 * during a blob transfer.
 */
public class FailureEventObserver extends AbstractObserver<FailureEvent> {
    /**
     * @param failureEventListener An instance of {@link FailureEventListener} wrapped in an event updater.
     */
    public FailureEventObserver(final FailureEventListener failureEventListener) {
        super(new UpdateStrategy<FailureEvent>() {
            @Override
            public void update(final FailureEvent eventData) {
                failureEventListener.onFailure(eventData);
            }
        });

        Preconditions.checkNotNull(failureEventListener, "failureEventListener may not be null.");
    }

    /**
     * @param updateStrategy An interface whose implementation determines how to handle a failure
     *                       event.
     */
    public FailureEventObserver(final UpdateStrategy<FailureEvent> updateStrategy) {
        super(updateStrategy);
    }
}
