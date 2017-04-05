/*
 * ****************************************************************************
 *    Copyright 2014-2017 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3client.helpers.strategy.transferstrategy;

import com.google.common.base.Preconditions;
import com.spectralogic.ds3client.helpers.MetadataReceivedListener;
import com.spectralogic.ds3client.helpers.events.MetadataEvent;

/**
 * This class provides the ability to emit an event when we receive metadata during a get operation.
 */
public class MetaDataReceivedObserver extends AbstractObserver<MetadataEvent> {
    private MetadataReceivedListener metadataReceivedListener;

    /**
     * @param metadataReceivedListener An instance of {@link MetadataReceivedListener} wrapped in an event
     *                                 updater.
     */
    public MetaDataReceivedObserver(final MetadataReceivedListener metadataReceivedListener) {
        super(new UpdateStrategy<MetadataEvent>() {
            @Override
            public void update(final MetadataEvent eventData) {
                metadataReceivedListener.metadataReceived(eventData.getObjectName(), eventData.getMetadata());
            }
        });

        Preconditions.checkNotNull(metadataReceivedListener, "metadataReceivedListener may not be null.");

        this.metadataReceivedListener = metadataReceivedListener;
    }

    /**
     * @param updateStrategy An {@link UpdateStrategy} whose implementation determines how to emit a metadata
     *                       event.
     */
    public MetaDataReceivedObserver(final UpdateStrategy<MetadataEvent> updateStrategy) {
        super(updateStrategy);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof MetaDataReceivedObserver)) return false;
        if (!super.equals(o)) return false;

        final MetaDataReceivedObserver that = (MetaDataReceivedObserver) o;

        return metadataReceivedListener != null ? metadataReceivedListener.equals(that.metadataReceivedListener) : that.metadataReceivedListener == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (metadataReceivedListener != null ? metadataReceivedListener.hashCode() : 0);
        return result;
    }
}
