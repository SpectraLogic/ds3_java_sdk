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

package com.spectralogic.ds3client.helpers.events;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 *
 * A asynchronous event runner that will execute events in order, but does not
 * guarantee events complete in order.
 *
 */
public class ConcurrentEventRunner implements EventRunner {

    private final static int DEFAULT_EXECUTOR_SIZE = 4;

    private final static Executor EVENTS_EXECUTOR = Executors.newFixedThreadPool(DEFAULT_EXECUTOR_SIZE);
    @Override
    public void emitEvent(final Runnable runnable) {
        EVENTS_EXECUTOR.execute(runnable);
    }
}
