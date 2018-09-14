/*
 * ******************************************************************************
 *   Copyright 2014-2017 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3client.helpers;

import com.google.common.base.Preconditions;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers.ObjectChannelBuilder;
import com.spectralogic.ds3client.helpers.strategy.transferstrategy.TransferStrategy;
import com.spectralogic.ds3client.helpers.strategy.transferstrategy.TransferStrategyBuilder;

import java.io.IOException;

public class WriteJobImpl extends JobImpl {
    private MetadataAccess metadataAccess = null;
    private ChecksumFunction checksumFunction = null;
    private TransferStrategy transferStrategy;

    public WriteJobImpl(final TransferStrategy transferStrategy) {
        this.transferStrategy = transferStrategy;
    }

    public WriteJobImpl(final TransferStrategyBuilder transferStrategyBuilder) {
        super(transferStrategyBuilder);
    }

    @Override
    public void attachMetadataReceivedListener(final MetadataReceivedListener listener) {
        throw new IllegalStateException("Metadata listeners are not used with Write jobs");
    }

    @Override
    public void removeMetadataReceivedListener(final MetadataReceivedListener listener) {
        throw new IllegalStateException("Metadata listeners are not used with Write jobs");
    }

    @Override
    public Ds3ClientHelpers.Job withMetadata(final MetadataAccess access) {
        checkRunning();
        this.metadataAccess = access;
        return this;
    }

    @Override
    public Ds3ClientHelpers.Job withChecksum(final ChecksumFunction checksumFunction) {
        this.checksumFunction = checksumFunction;
        return this;
    }

    @Override
    public void transfer(final ObjectChannelBuilder channelBuilder) throws IOException {
        if (transferStrategy != null) {
            transfer();
            return;
        }

        super.transfer(channelBuilder);

        transferStrategyBuilder().withChecksumFunction(checksumFunction);
        transferStrategyBuilder().withMetadataAccess(metadataAccess);

        transferStrategy = transferStrategyBuilder().makePutTransferStrategy();
        transfer();
    }

    @Override
    public void transfer() throws IOException {
        Preconditions.checkNotNull(transferStrategy, "transferStrategy may not be null.");

        running = true;
        transferStrategy.transfer();
    }

    @Override
    public void cancel() throws IOException {
        cancel(transferStrategy);
    }
}

