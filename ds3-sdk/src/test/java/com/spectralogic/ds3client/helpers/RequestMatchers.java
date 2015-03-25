/*
 * ******************************************************************************
 *   Copyright 2014-2015 Spectra Logic Corporation. All Rights Reserved.
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

import com.spectralogic.ds3client.commands.*;
import com.spectralogic.ds3client.models.bulk.ChunkClientProcessingOrderGuarantee;

import org.apache.commons.io.IOUtils;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.mockito.ArgumentMatcher;

import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.SeekableByteChannel;
import java.util.UUID;

import static org.mockito.Matchers.*;

public class RequestMatchers {
    public static BulkGetRequest hasChunkOrdering(final ChunkClientProcessingOrderGuarantee chunkOrdering) {
        return argThat(new TypeSafeMatcher<BulkGetRequest>() {
            @Override
            protected boolean matchesSafely(final BulkGetRequest item) {
                return item.getChunkOrdering() == null
                        ? chunkOrdering == null
                        : item.getChunkOrdering().equals(chunkOrdering);
            }

            @Override
            public void describeTo(final Description description) {
                describe(chunkOrdering, description);
            }
            
            @Override
            protected void describeMismatchSafely(final BulkGetRequest item, final Description mismatchDescription) {
                describe(item.getChunkOrdering(), mismatchDescription);
            }

            private void describe(
                    final ChunkClientProcessingOrderGuarantee chunkOrdering,
                    final Description description) {
                description
                        .appendText("BulkGetRequest with Chunk Ordering: ")
                        .appendValue(chunkOrdering);
            }
        });
    }

    public static GetAvailableJobChunksRequest hasJobId(final UUID jobId) {
        return argThat(new TypeSafeMatcher<GetAvailableJobChunksRequest>() {
            @Override
            public void describeTo(final Description description) {
                describeRequest(jobId, description);
            }

            @Override
            protected boolean matchesSafely(final GetAvailableJobChunksRequest item) {
                return jobId.equals(item.getJobId());
            }
            
            @Override
            protected void describeMismatchSafely(
                    final GetAvailableJobChunksRequest item,
                    final Description mismatchDescription) {
                describeRequest(item.getJobId(), mismatchDescription);
            }

            private void describeRequest(final UUID jobIdValue, final Description description) {
                description
                    .appendText("GetAvailableJobChunksRequest with job id: ")
                    .appendValue(jobIdValue);
            }
        });
    }

    public static AllocateJobChunkRequest hasChunkId(final UUID chunkId) {
        return argThat(new TypeSafeMatcher<AllocateJobChunkRequest>() {
            @Override
            public void describeTo(final Description description) {
                describeRequest(chunkId, description);
            }

            @Override
            protected boolean matchesSafely(final AllocateJobChunkRequest item) {
                return chunkId.equals(item.getChunkId());
            }
            
            @Override
            protected void describeMismatchSafely(
                    final AllocateJobChunkRequest item,
                    final Description mismatchDescription) {
                describeRequest(item.getChunkId(), mismatchDescription);
            }

            private void describeRequest(final UUID chunkIdValue, final Description description) {
                description
                    .appendText("AllocateJobChunkResponse with chunk id: ")
                    .appendValue(chunkIdValue);
            }
        });
    }

    public static GetObjectRequest getRequestHas(final String bucket, final String key, final UUID jobId, final long offset) {
        return argThat(new TypeSafeMatcher<GetObjectRequest>() {
            @Override
            protected boolean matchesSafely(final GetObjectRequest item) {
                return
                        item.getBucketName().equals(bucket)
                        && item.getObjectName().equals(key)
                        && (item.getJobId() == null ? jobId == null : item.getJobId().equals(jobId))
                        && item.getOffset() == offset;
            }

            @Override
            public void describeTo(final Description description) {
                describeTransferRequest(bucket, key, jobId, offset, description);
            }
            
            @Override
            protected void describeMismatchSafely(final GetObjectRequest item, final Description mismatchDescription) {
                describeTransferRequest(
                        item.getBucketName(),
                        item.getObjectName(),
                        item.getJobId(),
                        item.getOffset(),
                        mismatchDescription
                );
            }
        });
    }

    public static PutObjectRequest putRequestHas(
            final String bucket,
            final String key,
            final UUID jobId,
            final long offset,
            final String expectedContents) {
        return argThat(new TypeSafeMatcher<PutObjectRequest>() {
            @Override
            protected boolean matchesSafely(final PutObjectRequest item) {
                return
                        item.getBucketName().equals(bucket)
                        && item.getObjectName().equals(key)
                        && (item.getJobId() == null ? jobId == null : item.getJobId().equals(jobId))
                        && item.getOffset() == offset
                        && channelToString(item.getChannel()).equals(expectedContents);
            }

            @Override
            public void describeTo(final Description description) {
                describeTransferRequest(bucket, key, jobId, offset, description)
                    .appendText(", contents: ")
                    .appendValue(expectedContents);
            }
            
            @Override
            protected void describeMismatchSafely(final PutObjectRequest item, final Description mismatchDescription) {
                describeTransferRequest(
                        item.getBucketName(),
                        item.getObjectName(),
                        item.getJobId(),
                        item.getOffset(),
                        mismatchDescription
                )
                .appendText(", contents: ")
                .appendValue(channelToString(item.getChannel()));
            }
        });
    }

    private static Description describeTransferRequest(
            final String bucket,
            final String key,
            final UUID jobId,
            final long offset,
            final Description description) {
        return description
            .appendText("Get request with bucket: ")
            .appendValue(bucket)
            .appendText(", object: ")
            .appendValue(key)
            .appendText(", job id: ")
            .appendValue(jobId)
            .appendText(", offset: ")
            .appendValue(offset);
    }

    private static String channelToString(final SeekableByteChannel channel) {
        try {
            channel.position(0);
            return IOUtils.toString(Channels.newReader(channel, "UTF-8"));
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static GetBucketRequest getBucketHas(final String bucket, final String marker) {
        return argThat(new ArgumentMatcher<GetBucketRequest>() {
            @Override
            public boolean matches(final Object argument) {
                if (!(argument instanceof GetBucketRequest)) {
                    return false;
                }
                final GetBucketRequest getBucketRequest = ((GetBucketRequest)argument);
                return
                        getBucketRequest.getBucket().equals(bucket)
                        && (marker == null
                            ? null == getBucketRequest.getNextMarker()
                            : marker.equals(getBucketRequest.getNextMarker()));
                
            }
        });
    }
}
