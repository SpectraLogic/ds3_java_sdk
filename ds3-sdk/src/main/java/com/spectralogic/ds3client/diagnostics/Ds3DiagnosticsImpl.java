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
import com.spectralogic.ds3client.commands.spectrads3.GetCacheStateSpectraS3Request;
import com.spectralogic.ds3client.commands.spectrads3.GetCacheStateSpectraS3Response;
import com.spectralogic.ds3client.commands.spectrads3.GetTapesSpectraS3Request;
import com.spectralogic.ds3client.commands.spectrads3.GetTapesSpectraS3Response;
import com.spectralogic.ds3client.exceptions.NoCacheFileSystemException;
import com.spectralogic.ds3client.models.CacheFilesystemInformation;
import com.spectralogic.ds3client.models.Tape;
import com.spectralogic.ds3client.models.TapeState;
import com.spectralogic.ds3client.utils.Guard;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class Ds3DiagnosticsImpl extends Ds3Diagnostics {

    private final Ds3Client client;

    public Ds3DiagnosticsImpl(final Ds3Client client) {
        this.client = client;
    }

    @Override
    public Optional<ImmutableList<CacheFilesystemInformation>> getCacheNearCapacity() throws IOException {
        final GetCacheStateSpectraS3Response response = client
                .getCacheStateSpectraS3(new GetCacheStateSpectraS3Request());
        final List<CacheFilesystemInformation> fileSystemsInfo = response.getCacheInformationResult().getFilesystems();
        if (Guard.isNullOrEmpty(fileSystemsInfo)) {
            throw new NoCacheFileSystemException();
        }
        final ImmutableList.Builder<CacheFilesystemInformation> builder = ImmutableList.builder();
        for (final CacheFilesystemInformation fileSystemInfo : fileSystemsInfo) {
            final double percentUtilization = fileSystemInfo.getUsedCapacityInBytes() / fileSystemInfo.getAvailableCapacityInBytes();
            if (percentUtilization >= CACHE_UTILIZATION_NEAR_CAPACITY_LEVEL) {
                builder.add(fileSystemInfo);
            }
        }
        final ImmutableList<CacheFilesystemInformation> fileSystemsNearCapacity = builder.build();
        if (Guard.isNullOrEmpty(fileSystemsNearCapacity)) {
            return Optional.empty();
        }
        return Optional.of(fileSystemsNearCapacity);
    }

    @Override
    public Optional<ImmutableList<Tape>> getOfflineTapes() throws IOException {
        final GetTapesSpectraS3Request getTapesRequest = new GetTapesSpectraS3Request().withState(TapeState.OFFLINE);
        final GetTapesSpectraS3Response getTapesResponse = client.getTapesSpectraS3(getTapesRequest);
        final List<Tape> offlineTapes = getTapesResponse.getTapeListResult().getTapes();
        if (Guard.isNullOrEmpty(offlineTapes)) {
            return Optional.empty();
        }
        return Optional.of(ImmutableList.copyOf(offlineTapes));
    }

    @Override
    public Ds3DiagnosticsResult runDiagnostics() throws IOException {
        final ImmutableList.Builder<Ds3DiagnosticSummary> builderSummary = ImmutableList.builder();

        final Optional<ImmutableList<CacheFilesystemInformation>> cacheNearCapacity = getCacheNearCapacity();
        if (cacheNearCapacity.isPresent()) {
            builderSummary.add(Ds3DiagnosticSummary.CACHE_NEAR_CAPACITY);
        }

        final Optional<ImmutableList<Tape>> offlineTapes = getOfflineTapes();
        if (offlineTapes.isPresent()) {
            builderSummary.add(Ds3DiagnosticSummary.OFFLINE_TAPES);
        }

        final ImmutableList<Ds3DiagnosticSummary> summary = builderSummary.build();
        if (Guard.isNullOrEmpty(summary)) {
            return new Ds3DiagnosticsEmptyResult();
        }
        return new Ds3DiagnosticsFullResult(cacheNearCapacity, offlineTapes, Optional.of(summary));
    }
}
