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
import com.spectralogic.ds3client.helpers.WaitingForChunksListener;

/**
 * This class emits an event when chunks we are creating or retrieving from a Black Pearl
 * are not yet available. The event reports the number of seconds we will wait before
 * trying again.
 */
public class WaitingForChunksObserver extends AbstractObserver<Integer> {
    public WaitingForChunksObserver(final WaitingForChunksListener waitingForChunksListener) {
        super(new UpdateStrategy<Integer>() {
            @Override
            public void update(final Integer eventData) {
                waitingForChunksListener.waiting(eventData);
            }
        });

        Preconditions.checkNotNull(waitingForChunksListener, "waitingForChunksListener may not be null.");
    }

    public WaitingForChunksObserver(final UpdateStrategy<Integer> updateStrategy) {
        super(updateStrategy);
    }
}
