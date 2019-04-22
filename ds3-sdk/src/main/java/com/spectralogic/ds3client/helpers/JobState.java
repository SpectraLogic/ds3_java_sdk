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
import com.spectralogic.ds3client.models.BulkObject;
import com.spectralogic.ds3client.models.Objects;
import com.spectralogic.ds3client.models.PhysicalPlacement;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public class JobState {
    private final Set<BlobIdentityDecorator> blobs = Sets.newConcurrentHashSet();

    private final int numBlobsInJob;

    public JobState(final Collection<Objects> chunksThatContainBlobs) {
        int numBlobsInChunks = 0;

        for (final Objects chunk : chunksThatContainBlobs) {
            for (final BulkObject blob : chunk.getObjects()) {
                blobs.add(new BlobIdentityDecorator(blob));
                ++numBlobsInChunks;
            }
        }

        numBlobsInJob = numBlobsInChunks;
    }

    public boolean blobTransferredOrFailed(final BulkObject blob) {
        return blobs.remove(new BlobIdentityDecorator(blob));
    }

    public boolean contains(final BulkObject blob) {
        return blobs.contains(new BlobIdentityDecorator(blob));
    }

    public int numBlobsInJob() {
        return numBlobsInJob;
    }

    /**
     * This class is used to know whether we have attempted to transfer a blob.  In getting the master object list from
     * a call to either getBulkJobSpectraS3 or putBulkJobSpectraS3, the resulting blobs will have their inCache property
     * marked false.  In the master object list returned from a call to either allocateChunk or
     * getJobChunksReadyForClientProcessingSpectraS3, the resulting blobs may or may not have the same value for
     * their inCache property.  Since the BlackPearl may continually send us a blob that will always fail to transfer,
     * we check to see if a blob in the master object list we get during a transfer is in the master object list we
     * get when initiating a job.  If not, we can assume that we have previously tried and failed to transfer that
     * blob and skip it.
     */
    private static class BlobIdentityDecorator {
        private final String bucket;

        private final UUID id;

        private final boolean latest;

        private final long length;

        private final String name;

        private final long offset;

        private final PhysicalPlacement physicalPlacement;

        private final long version;

        private BlobIdentityDecorator(final BulkObject blob) {
            this.bucket = blob.getBucket();
            this.id = blob.getId();
            this.latest = blob.getLatest();
            this.length = blob.getLength();
            this.name = blob.getName();
            this.offset = blob.getOffset();
            this.physicalPlacement = blob.getPhysicalPlacement();
            this.version = blob.getVersion();
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (!(o instanceof BlobIdentityDecorator)) return false;

            final BlobIdentityDecorator that = (BlobIdentityDecorator) o;

            if (latest != that.latest) return false;
            if (length != that.length) return false;
            if (offset != that.offset) return false;
            if (version != that.version) return false;
            if (bucket != null ? !bucket.equals(that.bucket) : that.bucket != null) return false;
            if (id != null ? !id.equals(that.id) : that.id != null) return false;
            if (name != null ? !name.equals(that.name) : that.name != null) return false;
            return physicalPlacement != null ? physicalPlacement.equals(that.physicalPlacement) : that.physicalPlacement == null;
        }

        @Override
        public int hashCode() {
            return java.util.Objects.hash(bucket, id, latest, length, name, offset, physicalPlacement, version);
        }

        @Override
        public String toString() {
            return "BlobIdentityDecorator{" +
                    "bucket='" + bucket + '\'' +
                    ", length=" + length +
                    ", name='" + name + '\'' +
                    ", offset=" + offset +
                    '}';
        }
    }
}
