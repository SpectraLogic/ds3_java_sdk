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

package com.spectralogic.ds3client.helpers.strategy;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.models.BulkObject;
import com.spectralogic.ds3client.models.JobNode;
import com.spectralogic.ds3client.models.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

public final class StrategyUtils {

    private static final Logger LOG = LoggerFactory.getLogger(StrategyUtils.class);

    public static Ds3Client getClient(final ImmutableMap<UUID, JobNode> nodeMap, final UUID nodeId, final Ds3Client mainClient) {
        final JobNode jobNode = nodeMap.get(nodeId);

        if (jobNode == null) {
            LOG.warn("The jobNode was not found, returning the existing client");
            return mainClient;
        }

        return mainClient.newForNode(jobNode);
    }

    public static ImmutableMap<UUID, JobNode> buildNodeMap(final Iterable<JobNode> nodes) {
        final ImmutableMap.Builder<UUID, JobNode> nodeMap = ImmutableMap.builder();
        for (final JobNode node: nodes) {
            nodeMap.put(node.getId(), node);
        }
        return nodeMap.build();
    }

    /**
     * Filters out chunks that have already been completed.  We will get the same chunk name back from the server, but it
     * will not have any objects in it, so we remove that from the list of objects that are returned.
     * @param objectsList The list to be filtered
     * @return The filtered list
     */
    public static ImmutableList<Objects> filterChunks(final Iterable<Objects> objectsList) {
        final ImmutableList.Builder<Objects> builder = ImmutableList.builder();
        for (final Objects objects : objectsList) {
            final Objects filteredChunk = filterChunk(objects);
            if (filteredChunk.getObjects().size() > 0) {
                builder.add(filteredChunk);
            }
        }
        return builder.build();
    }

    private static Objects filterChunk(final Objects objects) {
        final Objects newObjects = new Objects();
        newObjects.setChunkId(objects.getChunkId());
        newObjects.setChunkNumber(objects.getChunkNumber());
        newObjects.setNodeId(objects.getNodeId());
        newObjects.setObjects(filterObjects(objects.getObjects()));
        return newObjects;
    }

    private static ImmutableList<BulkObject> filterObjects(final List<BulkObject> list) {
        final ImmutableList.Builder<BulkObject> builder = ImmutableList.builder();
        for (final BulkObject obj : list) {
            if (!obj.getInCache()) {
                builder.add(obj);
            }
        }
        return builder.build();
    }

    public static Path resolveForSymbolic(final Path path) throws IOException {
        if (Files.isSymbolicLink(path)) {
            final Path simLink = Files.readSymbolicLink(path);
            if (!simLink.isAbsolute()) {
                // Resolve the path such that the path is relative to the symbolically
                // linked file's directory
                final Path symLinkParent = path.toAbsolutePath().getParent();
                return symLinkParent.resolve(simLink);
            }

            return simLink;
        }
        return path;
    }
}
