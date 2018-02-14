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

package com.spectralogic.ds3client.integration

import com.spectralogic.ds3client.commands.spectrads3.PutBucketSpectraS3Request
import com.spectralogic.ds3client.helpers.DeleteBucket
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers
import com.spectralogic.ds3client.helpers.channelbuilders.RepeatStringObjectChannelBuilder
import com.spectralogic.ds3client.integration.test.helpers.TempStorageIds
import com.spectralogic.ds3client.integration.test.helpers.TempStorageUtil
import com.spectralogic.ds3client.models.ChecksumType
import com.spectralogic.ds3client.models.bulk.Ds3Object
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import java.io.IOException
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.lessThan

class ResourceTest {
    private companion object {
        private val numBuckets = 100
        private val numObjects = 1024
        private val numThreads = numObjects
        private val objectSize = 1024 * 10
        private val baseBucketName = "putLotsaBuckets"
        private val baseFileName = "file"
        private val objectData = "Gracie"
        private val dataBufferSize = 1024
        private val ds3Client = Util.fromEnv()
        private val policyPrefix = ResourceTest::class.java.simpleName
        private var envStorageIds : TempStorageIds? = null

        @BeforeClass
        @JvmStatic
        @Throws(IOException::class)
        fun startup() {
            val envDataPolicyId = TempStorageUtil.setupDataPolicy(policyPrefix, false, ChecksumType.Type.MD5, ds3Client)
            envStorageIds = TempStorageUtil.setup(policyPrefix, envDataPolicyId, ds3Client)
        }

        @AfterClass
        @JvmStatic
        @Throws(IOException::class)
        fun teardown() {
            TempStorageUtil.teardown(policyPrefix, envStorageIds, ds3Client)
            ds3Client.close()
        }
    }

    @Test
    fun putLotsaBuckets() {
        val bucketNames = generateBucketNames()

        try {
            createBuckets(bucketNames)
            populateBuckets(bucketNames)
            assertThat(Thread.activeCount(), lessThan(numThreads * 2))
        } finally {
            deleteBuckets(bucketNames)
        }
    }

    private fun generateBucketNames() : List<String> {
        return (1 .. numBuckets)
                .map { index -> baseBucketName + index }
                .toList()
    }

    private fun createBuckets(bucketNames: List<String>) {
        bucketNames.forEach({ bucketName -> ds3Client.putBucketSpectraS3(PutBucketSpectraS3Request(bucketName)) })
    }

    @Throws(IOException::class)
    private fun populateBuckets(bucketNames: List<String>) {
        bucketNames
                .forEach({ bucketName ->
                    Ds3ClientHelpers
                            .wrap(ds3Client)
                            .startWriteJob(bucketName, generateDs3Objects())
                            .withMaxParallelRequests(numThreads)
                            .transfer(RepeatStringObjectChannelBuilder(objectData, objectData.length * 512, dataBufferSize.toLong()))
                })

    }

    private fun generateDs3Objects(): List<Ds3Object> {
        return (1 .. numObjects)
                .map { index ->
                    val ds3Object = Ds3Object()
                    ds3Object.name = baseFileName + index
                    ds3Object.size = objectSize.toLong()
                    ds3Object
                }
                .toList()
    }

    private fun deleteBuckets(bucketNames: List<String>) {
        bucketNames.forEach({ bucketName -> DeleteBucket.deleteBucket(Ds3ClientHelpers.wrap(ds3Client), bucketName) })
    }
}