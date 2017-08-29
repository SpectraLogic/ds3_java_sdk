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