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

import com.spectralogic.ds3client.exceptions.Ds3NoMoreRetriesException;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A subclass of {@link ChunkAttemptRetryBehavior} that will retry an uncompleted
 * chunk operation at most {@code maxAttempts} before giving up.
 */
public class MaxChunkAttemptsRetryBehavior implements ChunkAttemptRetryBehavior {
    private final int maxAttempts;
    private final AtomicInteger currentAttempt = new AtomicInteger(0);

    public MaxChunkAttemptsRetryBehavior(final int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    @Override
    public void invoke() throws IOException {
        if (currentAttempt.incrementAndGet() > maxAttempts) {
            throw new Ds3NoMoreRetriesException(maxAttempts);
        }
    }

    @Override
    public void reset() {
        currentAttempt.set(0);
    }
}
