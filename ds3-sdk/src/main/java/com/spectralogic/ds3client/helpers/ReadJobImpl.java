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

import com.spectralogic.ds3client.helpers.Ds3ClientHelpers.ObjectChannelBuilder;
import com.spectralogic.ds3client.helpers.strategy.transferstrategy.MetaDataReceivedObserver;
import com.spectralogic.ds3client.helpers.strategy.transferstrategy.TransferStrategy;
import com.spectralogic.ds3client.helpers.strategy.transferstrategy.TransferStrategyBuilder;

import java.io.IOException;

class ReadJobImpl extends JobImpl {

    public ReadJobImpl(final TransferStrategyBuilder transferStrategyBuilder) {
        super(transferStrategyBuilder);
    }

    @Override
    public void attachMetadataReceivedListener(final MetadataReceivedListener listener) {
        checkRunning();
        eventDispatcher().attachMetadataReceivedEventObserver(new MetaDataReceivedObserver(listener));
    }

    @Override
    public void removeMetadataReceivedListener(final MetadataReceivedListener listener) {
        checkRunning();
        eventDispatcher().removeMetadataReceivedEventObserver(new MetaDataReceivedObserver(listener));
    }

    @Override
    public Ds3ClientHelpers.Job withMetadata(final MetadataAccess access) {
        throw new IllegalStateException("withMetadata method is not used with Read Jobs");
    }

    @Override
    public Ds3ClientHelpers.Job withChecksum(final ChecksumFunction checksumFunction) {
        throw new IllegalStateException("withChecksum is not supported on Read Jobs");
    }

    @Override
    public void transfer(final ObjectChannelBuilder channelBuilder) throws IOException {
        super.transfer(channelBuilder);

        transfer(transferStrategyBuilder().makeGetTransferStrategy());
    }

    public void transfer(final TransferStrategy transferStrategy) throws IOException {
        running = true;
        transferStrategy.transfer();
    }
}
