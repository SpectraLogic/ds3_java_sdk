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
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.models.CacheFilesystemInformation;
import com.spectralogic.ds3client.models.Tape;

import java.io.IOException;
import java.util.Optional;

/**
 * A wrapper around the {@link com.spectralogic.ds3client.Ds3Client} which performs diagnostics.
 */
public abstract class Ds3Diagnostics {

    /** The amount of file cache utilization that is considered near capacity */
    protected static final double CACHE_UTILIZATION_NEAR_CAPACITY_LEVEL = 0.95;

    /**
     * Wraps the given {@link com.spectralogic.ds3client.Ds3ClientImpl} with diagnostics methods.
     * @param client An instance of {@link com.spectralogic.ds3client.Ds3Client}, usually gotten from a call to
     *               {@link com.spectralogic.ds3client.Ds3ClientBuilder}
     * @return An instance of {@link com.spectralogic.ds3client.Ds3Client} wrapped with diagnostics methods.
     */
    public static Ds3Diagnostics wrap(final Ds3Client client) {
        return new Ds3DiagnosticsImpl(client);
    }

    /**
     * Retrieves the {@link CacheFilesystemInformation} for all cache that are near capacity.
     * A cache is determined near capacity if the current utilization is at or exceeds
     * {@link #CACHE_UTILIZATION_NEAR_CAPACITY_LEVEL}. If no file systems are near capacity,
     * than an empty {@link Optional} is returned.
     * @throws com.spectralogic.ds3client.exceptions.NoCacheFileSystemException If no cache file systems are found
     * @throws IOException
     */
    public abstract Optional<ImmutableList<CacheFilesystemInformation>> getCacheNearCapacity() throws IOException;

    /**
     * Retrieves the {@link Tape} for all tapes with status of OFFLINE. If no tapes are offline,
     * than an empty {@link Optional} is returned.
     * @throws IOException
     */
    public abstract Optional<ImmutableList<Tape>> getOfflineTapes() throws IOException;

    /**
     * Runs all diagnostics and returns the combined result of:
     *   {@link #getCacheNearCapacity()},
     *   {@link #getOfflineTapes()}
     * @throws IOException
     */
    public abstract Ds3DiagnosticsResult runDiagnostics() throws IOException;
}
