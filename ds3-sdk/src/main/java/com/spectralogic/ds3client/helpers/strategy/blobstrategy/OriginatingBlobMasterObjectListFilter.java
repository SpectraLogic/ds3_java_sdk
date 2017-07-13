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

package com.spectralogic.ds3client.helpers.strategy.blobstrategy;

import com.google.common.collect.Lists;
import com.spectralogic.ds3client.models.MasterObjectList;
import com.spectralogic.ds3client.models.bulk.Ds3Object;

/**
 * An implementation of {@link MasterObjectListFilter} removes from chunks blobs not originally specified in the
 * originating job's definition.  This implementation filters out chunks
 * containing blob names not contained in the list of {@link Ds3Object} used in originally creating a job in a
 * particular process and removes from the master object list any chunks that contain no blobs after having
 * applied the chunk filter.
 */
public class OriginatingBlobMasterObjectListFilter implements MasterObjectListFilter {
    private final ChunkFilter chunkFilter;

    public OriginatingBlobMasterObjectListFilter(final ChunkFilter chunkFilter) {
        this.chunkFilter = chunkFilter;
    }

    @Override
    public MasterObjectList apply(final MasterObjectList originalMasterObjectList) {
        final MasterObjectList newMasterObjectList = new MasterObjectList();

        newMasterObjectList.setAggregating(originalMasterObjectList.getAggregating());
        newMasterObjectList.setBucketName(originalMasterObjectList.getBucketName());
        newMasterObjectList.setCachedSizeInBytes(originalMasterObjectList.getCachedSizeInBytes());
        newMasterObjectList.setChunkClientProcessingOrderGuarantee(originalMasterObjectList.getChunkClientProcessingOrderGuarantee());
        newMasterObjectList.setCompletedSizeInBytes(originalMasterObjectList.getCompletedSizeInBytes());
        newMasterObjectList.setEntirelyInCache(originalMasterObjectList.getEntirelyInCache());
        newMasterObjectList.setJobId(originalMasterObjectList.getJobId());
        newMasterObjectList.setNaked(originalMasterObjectList.getNaked());
        newMasterObjectList.setName(originalMasterObjectList.getName());
        newMasterObjectList.setNodes(originalMasterObjectList.getNodes());
        newMasterObjectList.setOriginalSizeInBytes(originalMasterObjectList.getOriginalSizeInBytes());
        newMasterObjectList.setPriority(originalMasterObjectList.getPriority());
        newMasterObjectList.setRequestType(originalMasterObjectList.getRequestType());
        newMasterObjectList.setStartDate(originalMasterObjectList.getStartDate());
        newMasterObjectList.setStatus(originalMasterObjectList.getStatus());
        newMasterObjectList.setUserId(originalMasterObjectList.getUserId());
        newMasterObjectList.setUserName(originalMasterObjectList.getUserName());

        newMasterObjectList.setObjects(Lists.newArrayList(chunkFilter.apply(originalMasterObjectList.getObjects())));

        return newMasterObjectList;
    }
}
