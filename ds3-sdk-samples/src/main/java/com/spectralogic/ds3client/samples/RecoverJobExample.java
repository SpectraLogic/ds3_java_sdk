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

package com.spectralogic.ds3client.samples;

import com.google.common.collect.Lists;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.Ds3ClientBuilder;
import com.spectralogic.ds3client.commands.GetObjectRequest;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.helpers.FileObjectPutter;
import com.spectralogic.ds3client.helpers.JobRecoveryException;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.serializer.XmlProcessingException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.security.SignatureException;
import java.util.List;

public class RecoverJobExample {

    public static void main(final String args[]) throws IOException, URISyntaxException, SignatureException, XmlProcessingException {

        // Get a client builder and then build a client instance.  This is the main entry point to the SDK.
        try (final Ds3Client client = Ds3ClientBuilder.fromEnv().withHttps(false).build()) {
            final String bucketName = "recover_get_books_job_bucket";  //The bucket we are interested in getting objects from.
            Ds3ClientHelpers helper = Ds3ClientHelpers.wrap(client);
            helper.ensureBucketExists(bucketName);

            // Our local path which contains all the files that we want to transfer
            // This example assumes that there is at least 2 files in the "input" directory
            final Path inputPath = Paths.get("input");

            // Get the list of files that are contained in the inputPath
            final Iterable<Ds3Object> objects = helper.listObjectsForDirectory(inputPath);

            // Create the write job with the bucket we want to write to and the list
            // of objects that will be written
            final Ds3ClientHelpers.Job job = helper.startWriteJob(bucketName, objects);

            // Start the write job using an Object Putter that will read the files
            // from the local file system.
            job.transfer(new FileObjectPutter(inputPath));

            // Create a local output directory to place retrieved objects into
            final Path downloadPath = FileSystems.getDefault().getPath("output");
            if (!Files.exists(downloadPath)) {
                Files.createDirectory(downloadPath);
            }

            // Get the first object
            final List<Ds3Object> objectsList = Lists.newArrayList(objects);
            final Ds3Object object1 = objectsList.get(0);
            final FileChannel channel1 = FileChannel.open(
                    downloadPath.resolve(object1.getName()),
                    StandardOpenOption.WRITE,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
            final Ds3ClientHelpers.Job readJob = helper.startReadJob(bucketName, objectsList);
            client.getObject(new GetObjectRequest(bucketName, object1.getName(), 0, readJob.getJobId(), channel1));

            /**
             * Here is where we attempt to recover from a hypothetical interruption - before we get the 2nd object,
             * and while the job is still "In Progress"
             */
            try {
                final Ds3ClientHelpers.Job recoverJob = helper.recoverReadJob(readJob.getJobId());
                final Ds3Object object2 = objectsList.get(1);
                final FileChannel channel2 = FileChannel.open(
                        downloadPath.resolve(object2.getName()),
                        StandardOpenOption.WRITE,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING
                );

                client.getObject(new GetObjectRequest(bucketName, object2.getName(), 0, recoverJob.getJobId(), channel2));
            } catch (final JobRecoveryException e) {
                System.out.println("Could not recover ReadJob " + readJob.getJobId().toString());
                e.printStackTrace();
            }
        } catch (final IOException e) {
                System.out.println("Unable to create a client from ENV.  Please verify that DS3_ENDPOINT, DS3_ACCESS_KEY, and DS3_SECRET_KEY are defined.");
        }
    }
}
