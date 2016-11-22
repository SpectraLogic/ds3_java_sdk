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

package com.spectralogic.ds3client.samples;

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.Ds3ClientBuilder;
import com.spectralogic.ds3client.commands.GetBucketRequest;
import com.spectralogic.ds3client.commands.GetBucketResponse;
import com.spectralogic.ds3client.commands.GetObjectRequest;
import com.spectralogic.ds3client.commands.spectrads3.GetBulkJobSpectraS3Request;
import com.spectralogic.ds3client.commands.spectrads3.GetBulkJobSpectraS3Response;
import com.spectralogic.ds3client.models.BulkObject;
import com.spectralogic.ds3client.models.Contents;
import com.spectralogic.ds3client.models.Objects;
import com.spectralogic.ds3client.models.MasterObjectList;
import com.spectralogic.ds3client.models.bulk.Ds3Object;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class Ds3BulkGetExample {

    public static void main(final String args[]) throws IOException {

        // Get a client builder and then build a client instance.  This is the main entry point to the SDK.
        try (final Ds3Client client = Ds3ClientBuilder.fromEnv().withHttps(false).build()) {

            final String bucket = "my_bucket"; //The bucket we are interested in getting objects from.

            // Get the list of objects from the bucket that you want to perform the bulk get with.
            final GetBucketResponse response = client.getBucket(new GetBucketRequest(bucket));

            // We now need to generate the list of Ds3Objects that we want to get from DS3.
            final List<Ds3Object> objectList = new ArrayList<>();
            for (final Contents contents : response.getListBucketResult().getObjects()) {
                objectList.add(new Ds3Object(contents.getKey(), contents.getSize()));
            }

            // We are writing all the objects out to the directory output
            final Path dirPath = Paths.get("output");

            // Check to make sure output exists, if not create the directory
            if (!Files.exists(dirPath)) {
                Files.createDirectory(dirPath);
            }

            // Prime DS3 with the BulkGet command so that it can start to get objects off of tape.
            final GetBulkJobSpectraS3Response bulkResponse = client
                    .getBulkJobSpectraS3(new GetBulkJobSpectraS3Request(bucket, objectList));

            // The bulk response returns a list of lists which is designed to optimize data transmission from DS3.
            final MasterObjectList list = bulkResponse.getResult();
            for (final Objects objects : list.getObjects()) {
                for (final BulkObject obj : objects.getObjects()) {
                    final FileChannel channel = FileChannel.open(
                            dirPath.resolve(obj.getName()),
                            StandardOpenOption.WRITE,
                            StandardOpenOption.CREATE
                    );

                    // To handle the case where a file has been chunked we need to seek to the correct offset
                    // before we make the GetObject call so that when the request writes to the channel it is
                    // writing at the correct offset in the file.
                    channel.position(obj.getOffset());

                    // Perform the operation to get the object from DS3.
                    client.getObject(new GetObjectRequest(
                            bucket,
                            obj.getName(),
                            channel,
                            list.getJobId().toString(),
                            obj.getOffset()
                    ));
                }
            }
        }
    }
}