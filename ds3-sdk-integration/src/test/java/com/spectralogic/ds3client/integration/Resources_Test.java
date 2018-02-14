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

package com.spectralogic.ds3client.integration;

import com.google.common.collect.ImmutableList;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.spectrads3.PutBucketSpectraS3Request;
import com.spectralogic.ds3client.helpers.DeleteBucket;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.integration.test.helpers.TempStorageIds;
import com.spectralogic.ds3client.integration.test.helpers.TempStorageUtil;
import com.spectralogic.ds3client.models.ChecksumType;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.util.UUID;

import static org.junit.Assert.assertTrue;

public class Resources_Test {
    private static final String BASE_BUCKET_NAME = "putLotsaBuckets";
    private static final int NUM_BUCKETS = 10;
    private static final int NUM_OBJECTS = 1024;
    private static final int OBJECT_SIZE = 1024 * 10;
    private static final String BASE_FILE_NAME = "file_";
    private static final String DATA = "Gracie";
    private static final int DATA_BUFFER_SIZE = 1024;
    private static final int NUM_THREADS = 1024;
    private static final Ds3Client ds3Client = Util.fromEnv();
    private static TempStorageIds envStorageIds;
    private static UUID envDataPolicyId;
    private static final String POLICY_PREFIX = Resources_Test.class.getSimpleName();

    @BeforeClass
    public static void startup() throws IOException {
        envDataPolicyId = TempStorageUtil.setupDataPolicy(POLICY_PREFIX, false, ChecksumType.Type.MD5, ds3Client);
        envStorageIds = TempStorageUtil.setup(POLICY_PREFIX, envDataPolicyId, ds3Client);
    }

    @AfterClass
    public static void teardown() throws IOException {
        TempStorageUtil.teardown(POLICY_PREFIX, envStorageIds, ds3Client);
        ds3Client.close();
    }

    @Test
    public void putLotsaBuckets() throws IOException {
        final ImmutableList<String> bucketNames = generateBucketNames(NUM_BUCKETS);

        try {
            createBuckets(ds3Client, bucketNames);
            populateBuckets(Ds3ClientHelpers.wrap(ds3Client), bucketNames);
            assertTrue(Thread.activeCount() < 2 * NUM_THREADS);
        } finally {
            deleteBuckets(Ds3ClientHelpers.wrap(ds3Client), bucketNames);
        }
    }

    private ImmutableList<String> generateBucketNames(final int numBucketNames) {
        final ImmutableList.Builder<String> bucketNameBuilder = new ImmutableList.Builder<>();

        for (int i = 0; i < numBucketNames; ++i) {
            bucketNameBuilder.add(BASE_BUCKET_NAME + "_" + i);
        }

        return bucketNameBuilder.build();
    }

    private void createBuckets(final Ds3Client ds3Client, ImmutableList<String> bucketNames) throws IOException {
        for (final String bucketName : bucketNames) {
            ds3Client.putBucketSpectraS3(new PutBucketSpectraS3Request(bucketName));
        }
    }

    private void populateBuckets(final Ds3ClientHelpers ds3ClientHelpers, final ImmutableList<String> bucketNames) throws IOException {
        final ImmutableList<Ds3Object> ds3Objects = generateDs3Objects();

        for (final String bucketName : bucketNames) {
            final Ds3ClientHelpers.Job writeJob = ds3ClientHelpers.startWriteJob(bucketName, ds3Objects)
                    .withMaxParallelRequests(NUM_THREADS);
            writeJob.transfer(new RepeatStringObjectChannelBuilder(DATA, DATA.length() * 512, DATA_BUFFER_SIZE));
        }
    }

    private ImmutableList<Ds3Object> generateDs3Objects() {
        final ImmutableList.Builder<Ds3Object> ds3ObjectBuilder = new ImmutableList.Builder<>();

        for (int i = 0; i < NUM_OBJECTS; ++i) {
            final Ds3Object ds3Object = new Ds3Object();
            ds3Object.setName(BASE_FILE_NAME + i);
            ds3Object.setSize(OBJECT_SIZE);
            ds3ObjectBuilder.add(ds3Object);
        }

        return ds3ObjectBuilder.build();
    }

    private void deleteBuckets(final Ds3ClientHelpers ds3ClientHelpers, final ImmutableList<String> bucketNames) {
        for (final String bucketName : bucketNames) {
            DeleteBucket.INSTANCE.deleteBucket(ds3ClientHelpers, bucketName);
        }
    }

    private static class RepeatStringObjectChannelBuilder implements Ds3ClientHelpers.ObjectChannelBuilder {
        private final int bufferSize;
        private final long sizeOfFiles;
        private String inputDataHeader;

        RepeatStringObjectChannelBuilder(final String inputDataHeader, final int bufferSize, final long sizeOfFile) {
            this.bufferSize = bufferSize;
            this.sizeOfFiles = sizeOfFile;
            this.inputDataHeader = inputDataHeader;
        }

        @Override public SeekableByteChannel buildChannel(final String key ) {
            return new RepeatStringByteChannel( inputDataHeader + key, this.bufferSize, this.sizeOfFiles );
        }

        private static class RepeatStringByteChannel implements SeekableByteChannel {
            final private byte[] backingArray;
            final private int bufferSize;
            final private long limit;
            private boolean isOpen;
            private int position;

            RepeatStringByteChannel( final String inputData, final int bufferSize, final long size ) {
                this.bufferSize = bufferSize;
                final byte[] bytes = new byte[ bufferSize ];
                final byte[] stringBytes = inputData.getBytes();
                int stringPosition = 0;
                for ( int i = 0; i < bufferSize; i++ ) {
                    bytes[ i ] = stringBytes[ stringPosition ];
                    if ( ++stringPosition >= inputData.length() )
                    {
                        stringPosition = 0;
                    }
                }

                backingArray = bytes;

                this.position = 0;
                this.limit = size;
                this.isOpen = true;
            }

            public void close() {
                this.isOpen = false;
            }

            public boolean isOpen() {
                return this.isOpen;
            }

            public long position() {
                return ( long ) this.position;
            }

            public SeekableByteChannel position( final long newPosition ) {
                this.position = ( int ) newPosition;
                return this;
            }

            public int read( final ByteBuffer dst ) {
                final int amountToRead = Math.min( dst.remaining(), this.bufferSize );
                dst.put( this.backingArray, 0, amountToRead );
                return amountToRead;
            }

            public long size() {
                return this.limit;
            }

            public SeekableByteChannel truncate( final long size ) {
                return this;
            }

            public int write( final ByteBuffer src ) {
                final int amountToWrite = Math.min( src.remaining(), this.bufferSize );
                src.get( this.backingArray, 0, amountToWrite );
                return amountToWrite;
            }
        }
    }
}
