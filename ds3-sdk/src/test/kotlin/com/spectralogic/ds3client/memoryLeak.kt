package com.spectralogic.ds3client

import com.spectralogic.ds3client.helpers.Ds3ClientHelpers
import com.spectralogic.ds3client.helpers.FileObjectPutter
import com.spectralogic.ds3client.models.common.Credentials
import org.junit.Test
import java.nio.file.Paths

class memoryLeak {
    val host = "sm25-2-10g.eng.sldomain.com"
    val id = "QWRtaW5pc3RyYXRvcg=="
    val key = "xGBmFPNf"
    val clientBuilder = Ds3ClientBuilder.create(host, Credentials(id, key))
    val client = clientBuilder.withHttps(false).build()
    val wrapped = Ds3ClientHelpers.wrap(client)

    @Test
    fun findMemoryLeak() {
        while(true) {
            wrapped.ensureBucketExists("memoryleak")
            val inputPath = Paths.get("/Users/ericbergstrom/Downloads")
            val objects = wrapped.listObjectsForDirectory(inputPath)
            val job = wrapped.startWriteJob("memoryleak", objects)
            val job2 = wrapped.recoverWriteJob(job.jobId)
            job2.transfer(FileObjectPutter(inputPath));
            wrapped.deleteBucket("memoryleak")
        }
    }
}