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

package com.spectralogic.ds3client.diagnostics;

import com.google.common.collect.ImmutableList;
import com.spectralogic.ds3client.models.CacheFilesystemInformation;
import com.spectralogic.ds3client.models.Tape;

import java.util.Optional;

/**
 * Contains the result of running all diagnostic in {@link Ds3Diagnostics#runDiagnostics()}
 */
public class Ds3DiagnosticsFullResult implements Ds3DiagnosticsResult {

    /**
     * Contains a list of {@link Ds3DiagnosticSummary} which acts as a summary
     * for which diagnostics reported potential issues
     * */
    private final Optional<ImmutableList<Ds3DiagnosticSummary>> summary;

    /** Contains the result of {@link Ds3Diagnostics#getCacheNearCapacity()} */
    private final Optional<ImmutableList<CacheFilesystemInformation>> cacheNearCapacity;

    /** Contains the result of {@link Ds3Diagnostics#getOfflineTapes()} */
    private final Optional<ImmutableList<Tape>> offlineTapes;

    public Ds3DiagnosticsFullResult(
            final Optional<ImmutableList<CacheFilesystemInformation>> cacheNearCapacity,
            final Optional<ImmutableList<Tape>> offlineTapes,
            Optional<ImmutableList<Ds3DiagnosticSummary>> summary) {
        this.cacheNearCapacity = cacheNearCapacity;
        this.offlineTapes = offlineTapes;
        this.summary = summary;
    }

    public Optional<ImmutableList<CacheFilesystemInformation>> getCacheNearCapacity() {
        return cacheNearCapacity;
    }

    public Optional<ImmutableList<Tape>> getOfflineTapes() {
        return offlineTapes;
    }

    public Optional<ImmutableList<Ds3DiagnosticSummary>> getSummary() {
        return summary;
    }
}
