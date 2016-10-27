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

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.spectrads3.*;
import com.spectralogic.ds3client.models.CacheFilesystemInformation;
import com.spectralogic.ds3client.models.Tape;
import com.spectralogic.ds3client.models.TapeState;
import com.spectralogic.ds3client.utils.Guard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class Ds3DiagnosticsImpl extends Ds3Diagnostics {

    private static final Logger LOG = LoggerFactory.getLogger(Ds3DiagnosticsImpl.class);

    /** The amount of file cache utilization that triggers a warning */
    private static final double CACHE_UTILIZATION_WARNING_LEVEL = 0.95;

    private final Ds3Client client;

    public Ds3DiagnosticsImpl(final Ds3Client client) {
        this.client = client;
    }


    @Override
    public void checkCacheAvailability() throws IOException {
        final GetCacheStateSpectraS3Response response = client
                .getCacheStateSpectraS3(new GetCacheStateSpectraS3Request());
        final List<CacheFilesystemInformation> fileSystemsInfo = response.getCacheInformationResult().getFilesystems();
        if (Guard.isNullOrEmpty(fileSystemsInfo)) {
            LOG.error("There are no cache file systems");
            return;
        }
        for (final CacheFilesystemInformation fileSystemInfo : fileSystemsInfo) {
            final UUID fileSystemId = fileSystemInfo.getCacheFilesystem().getId();
            final long availableCapacity = fileSystemInfo.getAvailableCapacityInBytes();
            final long usedCapacity = fileSystemInfo.getUsedCapacityInBytes();
            final double percentUtilization = usedCapacity / availableCapacity;
            LOG.info("Cache file system {}: {} bytes available capacity, {} bytes used capacity, {}% currently utilized",
                    fileSystemId.toString(),
                    availableCapacity,
                    usedCapacity,
                    percentUtilization);

            if (percentUtilization >= CACHE_UTILIZATION_WARNING_LEVEL) {
                LOG.warn("Cache file system {} is near full with {}% currently utilized",
                        fileSystemId,
                        percentUtilization);
            }
        }
    }

    @Override
    @Nullable
    public List<Tape> getOfflineTapes() throws IOException {
        final GetTapesSpectraS3Request getTapesRequest = new GetTapesSpectraS3Request().withState(TapeState.OFFLINE);
        final GetTapesSpectraS3Response getTapesResponse = client.getTapesSpectraS3(getTapesRequest);
        final List<Tape> offlineTapes = getTapesResponse.getTapeListResult().getTapes();
        if (Guard.isNullOrEmpty(offlineTapes)) {
            LOG.info("There are no tapes with status = OFFLINE");
        } else {
            LOG.warn("There are {} tapes with status = OFFLINE", offlineTapes.size());
        }
        return offlineTapes;
    }
}
