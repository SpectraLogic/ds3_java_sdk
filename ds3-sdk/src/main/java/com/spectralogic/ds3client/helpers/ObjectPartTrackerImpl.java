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

package com.spectralogic.ds3client.helpers;

import com.google.common.collect.Sets;
import com.spectralogic.ds3client.helpers.events.EventRunner;

import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

class ObjectPartTrackerImpl implements ObjectPartTracker {
    private final String name;
    private final TreeSet<ObjectPart> parts;
    private final Set<DataTransferredListener> dataTransferredListeners = Sets.newIdentityHashSet();
    private final Set<ObjectCompletedListener> objectCompletedListeners = Sets.newIdentityHashSet();
    private final EventRunner eventRunner;

    public ObjectPartTrackerImpl(final String name, final Collection<ObjectPart> parts, final EventRunner eventRunner) {
        this.name = name;
        this.parts = new TreeSet<>(ObjectPartComparator.instance());
        this.parts.addAll(parts);
        this.eventRunner = eventRunner;
        validateParts();
    }

    @Override
    public synchronized ObjectPartTracker attachDataTransferredListener(final DataTransferredListener listener) {
        this.dataTransferredListeners.add(listener);
        return this;
    }

    @Override
    public synchronized ObjectPartTracker attachObjectCompletedListener(final ObjectCompletedListener listener) {
        this.objectCompletedListeners.add(listener);
        return this;
    }

    @Override
    public void removeDataTransferredListener(final DataTransferredListener listener) {
        this.dataTransferredListeners.remove(listener);
    }

    @Override
    public void removeObjectCompletedListener(final ObjectCompletedListener listener) {
        this.objectCompletedListeners.remove(listener);
    }

    @Override
    public synchronized void completePart(final ObjectPart part) {
        final ObjectPart existingPart = this.parts.floor(part);
        if (existingPart == null) {
            throw new IllegalStateException("The object part was not available to be marked completed.");
        }
        if (part.getEnd() > existingPart.getEnd()) {
            throw new IllegalStateException("The object part was not available to be marked completed.");
        }
        this.parts.remove(existingPart);
        if (part.getOffset() > existingPart.getOffset()) {
            this.parts.add(new ObjectPart(existingPart.getOffset(), part.getOffset() - existingPart.getOffset()));
        }
        if (part.getEnd() < existingPart.getEnd()) {
            this.parts.add(new ObjectPart(part.getEnd() + 1, existingPart.getEnd() - part.getEnd()));
        }
        onDataTransferred(part.getLength());
        if (this.parts.size() == 0) {
            onObjectCompleted();
        }
    }

    @Override
    public synchronized boolean containsPart(final ObjectPart part) {
        final ObjectPart existingPart = this.parts.ceiling(part);
        return existingPart != null && existingPart.getLength() == part.getLength();
    }
    
    private synchronized void validateParts() {
        long lastEnd = -1L;
        for (final ObjectPart part : this.parts) {
            if (part.getOffset() <= lastEnd) {
                throw new InvalidParameterException();
            }
            lastEnd = part.getEnd();
        }
    }
    
    private void onDataTransferred(final long size) {
        for (final DataTransferredListener listener : this.dataTransferredListeners) {
            eventRunner.emitEvent(new Runnable() {
                @Override
                public void run() {
                    listener.dataTransferred(size);
                }
            });
        }
    }
    
    private void onObjectCompleted() {
        for (final ObjectCompletedListener listener : this.objectCompletedListeners) {
            eventRunner.emitEvent(new Runnable() {
                @Override
                public void run() {
                    listener.objectCompleted(name);
                }
            });
        }
    }
}
