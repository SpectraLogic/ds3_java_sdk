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

package com.spectralogic.ds3client.helpers

import com.spectralogic.ds3client.commands.DeleteBucketRequest
import com.spectralogic.ds3client.commands.DeleteObjectsRequest
import com.spectralogic.ds3client.utils.collections.asIterable
import com.spectralogic.ds3client.utils.collections.take

object DeleteBucket {
    fun deleteBucket(helpers: Ds3ClientHelpers, bucket: String) {
        deleteBucketContents(helpers, bucket)
        helpers.client.deleteBucket(DeleteBucketRequest(bucket))
    }

    fun deleteBucketContents(helpers: Ds3ClientHelpers, bucket: String) {
        val client = helpers.client
        val objIterator = helpers.listObjects(bucket).iterator()

        while(objIterator.hasNext()) {
            val subIterator = objIterator.take(1_000)
            client.deleteObjects(DeleteObjectsRequest(bucket, subIterator.asIterable()))
        }
    }
}