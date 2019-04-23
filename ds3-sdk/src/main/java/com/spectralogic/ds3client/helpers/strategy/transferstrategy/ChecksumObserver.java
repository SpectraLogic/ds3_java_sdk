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
import com.spectralogic.ds3client.helpers.ChecksumListener;

/**
 * This class provides the ability to emit an event when we have computed the checksum value for a blob to be
 * transferred to a Black Pearl.
 */
public class ChecksumObserver extends AbstractObserver<ChecksumEvent> {

    /**
     * @param checksumListener An instance of {@link ChecksumListener} wrapped in an event updater.
     */
    public ChecksumObserver(final ChecksumListener checksumListener) {
        super(new UpdateStrategy<ChecksumEvent>() {
                  @Override
                  public void update(final ChecksumEvent eventData) {
                      checksumListener.value(eventData.getBlob(), eventData.getChecksumType(), eventData.getChecksum());
                  }
              });

        Preconditions.checkNotNull(checksumListener, "checksumListener may not be null.");
    }

    /**
     * @param updateStrategy The interface whose implementation determines how to handle emitting an event
     *                       when we compute the checksum for a blob.
     */
    public ChecksumObserver(final UpdateStrategy<ChecksumEvent> updateStrategy) {
        super(updateStrategy);
    }
}
