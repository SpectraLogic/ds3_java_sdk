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
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.spectralogic.ds3client.helpers.ChecksumListener;
import com.spectralogic.ds3client.helpers.DataTransferredListener;
import com.spectralogic.ds3client.helpers.FailureEventListener;
import com.spectralogic.ds3client.helpers.MetadataReceivedListener;
import com.spectralogic.ds3client.helpers.ObjectCompletedListener;
import com.spectralogic.ds3client.helpers.WaitingForChunksListener;
import com.spectralogic.ds3client.helpers.events.EventRunner;
import com.spectralogic.ds3client.helpers.events.FailureEvent;
import com.spectralogic.ds3client.helpers.events.MetadataEvent;
import com.spectralogic.ds3client.models.BulkObject;
import com.spectralogic.ds3client.models.ChecksumType;
import com.spectralogic.ds3client.networking.Metadata;

import java.util.Map;
import java.util.Set;

public class EventDispatcherImpl implements EventDispatcher {
    private final EventRunner eventRunner;

    private final Set<DataTransferredObserver> dataTransferredObservers = Sets.newIdentityHashSet();
    private final Set<ObjectCompletedObserver> objectCompletedObservers = Sets.newIdentityHashSet();
    private final Set<ChecksumObserver> checksumObservers = Sets.newIdentityHashSet();
    private final Set<WaitingForChunksObserver> waitingForChunksObservers = Sets.newIdentityHashSet();
    private final Set<FailureEventObserver> failureEventObservers = Sets.newIdentityHashSet();
    private final Set<MetaDataReceivedObserver> metaDataReceivedObservers = Sets.newConcurrentHashSet();
    private final Set<BlobTransferredEventObserver> blobTransferredEventObservers = Sets.newIdentityHashSet();

    private final Map<DataTransferredListener, DataTransferredObserver> dataTransferredListeners = Maps.newConcurrentMap();
    private final Map<ObjectCompletedListener, ObjectCompletedObserver> objectCompletedListeners = Maps.newIdentityHashMap();
    private final Map<MetadataReceivedListener, MetaDataReceivedObserver> metadataReceivedListeners = Maps.newIdentityHashMap();
    private final Map<ChecksumListener, ChecksumObserver> checksumListeners = Maps.newIdentityHashMap();
    private final Map<WaitingForChunksListener, WaitingForChunksObserver> waitingForChunksListeners = Maps.newIdentityHashMap();
    private final Map<FailureEventListener, FailureEventObserver> failureEventListeners = Maps.newIdentityHashMap();

    public EventDispatcherImpl(final EventRunner eventRunner) {
        Preconditions.checkNotNull(eventRunner, "eventRunner must not be null.");
        this.eventRunner = eventRunner;
    }

    @Override
    public DataTransferredObserver attachDataTransferredObserver(final DataTransferredObserver dataTransferredObserver) {
        dataTransferredObservers.add(dataTransferredObserver);
        return dataTransferredObserver;
    }

    @Override
    public void removeDataTransferredObserver(final DataTransferredObserver dataTransferredObserver) {
        dataTransferredObservers.remove(dataTransferredObserver);
    }

    @Override
    public ObjectCompletedObserver attachObjectCompletedObserver(final ObjectCompletedObserver objectCompletedObserver) {
        objectCompletedObservers.add(objectCompletedObserver);
        return objectCompletedObserver;
    }

    @Override
    public void removeObjectCompletedObserver(final ObjectCompletedObserver objectCompletedObserver) {
        objectCompletedObservers.remove(objectCompletedObserver);
    }

    @Override
    public ChecksumObserver attachChecksumObserver(final ChecksumObserver checksumObserver) {
        checksumObservers.add(checksumObserver);
        return checksumObserver;
    }

    @Override
    public void removeChecksumObserver(final ChecksumObserver checksumObserver) {
        checksumObservers.remove(checksumObserver);
    }

    @Override
    public WaitingForChunksObserver attachWaitingForChunksObserver(final WaitingForChunksObserver waitingForChunksObserver) {
        waitingForChunksObservers.add(waitingForChunksObserver);
        return waitingForChunksObserver;
    }

    @Override
    public void removeWaitingForChunksObserver(final WaitingForChunksObserver waitingForChunksObserver) {
        waitingForChunksObservers.remove(waitingForChunksObserver);
    }

    @Override
    public FailureEventObserver attachFailureEventObserver(final FailureEventObserver failureEventObserver) {
        failureEventObservers.add(failureEventObserver);
        return failureEventObserver;
    }

    @Override
    public void removeFailureEventObserver(final FailureEventObserver failureEventObserver) {
        failureEventObservers.remove(failureEventObserver);
    }

    @Override
    public MetaDataReceivedObserver attachMetadataReceivedEventObserver(final MetaDataReceivedObserver metaDataReceivedObserver) {
        metaDataReceivedObservers.add(metaDataReceivedObserver);
        return metaDataReceivedObserver;
    }

    @Override
    public void removeMetadataReceivedEventObserver(final MetaDataReceivedObserver metaDataReceivedObserver) {
        metaDataReceivedObservers.remove(metaDataReceivedObserver);
    }

    @Override
    public BlobTransferredEventObserver attachBlobTransferredEventObserver(final BlobTransferredEventObserver blobTransferredEventObserver) {
        blobTransferredEventObservers.add(blobTransferredEventObserver);
        return blobTransferredEventObserver;
    }

    @Override
    public void removeBlobTransferredEventObserver(final BlobTransferredEventObserver blobTransferredEventObserver) {
        blobTransferredEventObservers.remove(blobTransferredEventObserver);
    }


    @Override
    public void attachDataTransferredListener(final DataTransferredListener listener) {
        DataTransferredObserver dataTransferredObserver = dataTransferredListeners.get(listener);

        if (dataTransferredObserver == null) {
            dataTransferredObserver = attachDataTransferredObserver(new DataTransferredObserver(listener));
            dataTransferredListeners.put(listener, dataTransferredObserver);
        }
    }

    @Override
    public void removeDataTransferredListener(final DataTransferredListener listener) {
        final DataTransferredObserver dataTransferredObserver = dataTransferredListeners.get(listener);

        if (dataTransferredObserver != null) {
            removeDataTransferredObserver(dataTransferredObserver);
            dataTransferredListeners.remove(listener);
        }
    }

    @Override
    public void attachObjectCompletedListener(final ObjectCompletedListener listener) {
        ObjectCompletedObserver objectCompletedObserver = objectCompletedListeners.get(listener);

        if (objectCompletedObserver == null) {
            objectCompletedObserver = attachObjectCompletedObserver(new ObjectCompletedObserver(listener));
            objectCompletedListeners.put(listener, objectCompletedObserver);
        }
    }

    @Override
    public void removeObjectCompletedListener(final ObjectCompletedListener listener) {
        final ObjectCompletedObserver objectCompletedObserver = objectCompletedListeners.get(listener);

        if (objectCompletedObserver != null) {
            removeObjectCompletedObserver(objectCompletedObserver);
            objectCompletedListeners.remove(listener);
        }
    }

    @Override
    public void attachMetadataReceivedListener(final MetadataReceivedListener listener) {
        MetaDataReceivedObserver metaDataReceivedObserver = metadataReceivedListeners.get(listener);

        if (metaDataReceivedObserver == null) {
            metaDataReceivedObserver = attachMetadataReceivedEventObserver(new MetaDataReceivedObserver(listener));
            metadataReceivedListeners.put(listener, metaDataReceivedObserver);
        }
    }

    @Override
    public void removeMetadataReceivedListener(final MetadataReceivedListener listener) {
        final MetaDataReceivedObserver metaDataReceivedObserver = metadataReceivedListeners.get(listener);

        if (metaDataReceivedObserver != null) {
            removeMetadataReceivedEventObserver(metaDataReceivedObserver);
            metadataReceivedListeners.remove(listener);
        }
    }

    @Override
    public void attachChecksumListener(final ChecksumListener listener) {
        ChecksumObserver checksumObserver = checksumListeners.get(listener);

        if (checksumObserver == null) {
            checksumObserver = attachChecksumObserver(new ChecksumObserver(listener));
            checksumListeners.put(listener, checksumObserver);
        }
    }

    @Override
    public void removeChecksumListener(final ChecksumListener listener) {
        final ChecksumObserver checksumObserver = checksumListeners.get(listener);

        if (checksumObserver != null) {
            removeChecksumObserver(checksumObserver);
            checksumListeners.remove(listener);
        }
    }

    @Override
    public void attachWaitingForChunksListener(final WaitingForChunksListener listener) {
        WaitingForChunksObserver waitingForChunksObserver = waitingForChunksListeners.get(listener);

        if (waitingForChunksObserver == null) {
            waitingForChunksObserver = attachWaitingForChunksObserver(new WaitingForChunksObserver(listener));
            waitingForChunksListeners.put(listener, waitingForChunksObserver);
        }
    }

    @Override
    public void removeWaitingForChunksListener(final WaitingForChunksListener listener) {
        final WaitingForChunksObserver waitingForChunksObserver = waitingForChunksListeners.get(listener);

        if (waitingForChunksObserver != null) {
            removeWaitingForChunksObserver(waitingForChunksObserver);
            waitingForChunksListeners.remove(listener);
        }
    }

    @Override
    public void attachFailureEventListener(final FailureEventListener listener) {
        FailureEventObserver failureEventObserver = failureEventListeners.get(listener);

        if (failureEventObserver == null) {
            failureEventObserver = attachFailureEventObserver(new FailureEventObserver(listener));
            failureEventListeners.put(listener, failureEventObserver);
        }
    }

    @Override
    public void removeFailureEventListener(final FailureEventListener listener) {
        final FailureEventObserver failureEventObserver = failureEventListeners.get(listener);

        if (failureEventObserver != null) {
            removeFailureEventObserver(failureEventObserver);
            failureEventListeners.remove(listener);
        }
    }

    @Override
    public void emitFailureEvent(final FailureEvent failureEvent) {
        emitEvents(failureEventObservers, failureEvent);
    }

    private <T> void emitEvents(final Set eventObservers, final T eventData) {
        for (final Object eventObserver : eventObservers) {
            eventRunner.emitEvent(new Runnable() {
                @Override
                public void run() {
                    ((Observer<T>)eventObserver).update(eventData);
                }
            });
        }
    }

    @Override
    public void emitWaitingForChunksEvents(final int secondsToDelay) {
        emitEvents(waitingForChunksObservers, secondsToDelay);
    }

    @Override
    public void emitChecksumEvent(final BulkObject blob, final ChecksumType.Type type, final String checksum) {
        emitEvents(checksumObservers, new ChecksumEvent(blob, type, checksum));
    }

    @Override
    public void emitDataTransferredEvent(final BulkObject blob) {
        emitEvents(dataTransferredObservers, blob.getLength());
    }

    @Override
    public void emitObjectCompletedEvent(final BulkObject blob) {
        emitObjectCompletedEvent(blob.getName());
    }

    @Override
    public void emitObjectCompletedEvent(final String blobName) {
        emitEvents(objectCompletedObservers, blobName);
    }

    @Override
    public void emitMetaDataReceivedEvent(final String objectName, final Metadata metadata) {
        emitEvents(metaDataReceivedObservers, new MetadataEvent(objectName, metadata));
    }

    @Override
    public void emitBlobTransferredEvent(final BulkObject blob) {
        emitEvents(blobTransferredEventObservers, blob);
    }

    @Override
    public void emitContentLengthMismatchFailureEvent(final BulkObject ds3Object, final String endpoint, final Throwable t) {
        final FailureEvent failureEvent = FailureEvent.builder()
                .doingWhat(FailureEvent.FailureActivity.GettingObject)
                .usingSystemWithEndpoint(endpoint)
                .withCausalException(t)
                .withObjectNamed(ds3Object.getName())
                .build();
        emitFailureEvent(failureEvent);
    }
}
