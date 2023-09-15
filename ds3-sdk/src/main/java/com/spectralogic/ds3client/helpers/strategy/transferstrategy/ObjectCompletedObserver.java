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
import com.spectralogic.ds3client.helpers.ObjectCompletedListener;

/**
 * An {@link Observer} that enables sending event when a DS3 object transfer completes.
 */
public class ObjectCompletedObserver extends AbstractObserver<String> {
    /**
     * @param objectCompletedListener An {@link ObjectCompletedListener} wrapped in an event
     *                                updater.
     */
    public ObjectCompletedObserver(final ObjectCompletedListener objectCompletedListener) {
        super(new StringUpdateStrategy(objectCompletedListener));

        Preconditions.checkNotNull(objectCompletedListener, "objectCompletedListener may not be null");
    }

    /**
     * @param updateStrategy An {@link UpdateStrategy} whose implementation determines how to handle a DS3 object transfer
     *                       completion.
     */
    public ObjectCompletedObserver(final UpdateStrategy<String> updateStrategy) {
        super(updateStrategy);
    }

    private static class StringUpdateStrategy implements UpdateStrategy<String> {
        private final ObjectCompletedListener objectCompletedListener;

        public StringUpdateStrategy(final ObjectCompletedListener objectCompletedListener) {
            this.objectCompletedListener = objectCompletedListener;
        }

        @Override
        public void update(final String eventData) {
            objectCompletedListener.objectCompleted(eventData);
        }
    }
}
