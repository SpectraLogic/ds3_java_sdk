/*
 * ******************************************************************************
 *   Copyright 2014-2019 Spectra Logic Corporation. All Rights Reserved.
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

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.Ds3ClientBuilder;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.helpers.FileObjectPutter;
import com.spectralogic.ds3client.helpers.MetadataAccess;
import com.spectralogic.ds3client.models.bulk.Ds3Object;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class BulkPutWithMetadata {
    public static void main(final String[] args) throws IOException {
        try (final Ds3Client client = Ds3ClientBuilder.fromEnv().withHttps(false).build()) {

            // Wrap the Ds3Client with the helper functions
            final Ds3ClientHelpers helper = Ds3ClientHelpers.wrap(client);

            // The bucket that we will be writing to
            final String bucketName = "my_bucket";

            // Make sure that the bucket exists, if it does not this will create it
            helper.ensureBucketExists(bucketName);

            // Our local path which contains all the files that we want to transfer
            final Path inputPath = Paths.get("input");

            // Get the list of files that are contained in the inputPath
            final Iterable<Ds3Object> objects = helper.listObjectsForDirectory(inputPath);

            // Create the write job with the bucket we want to write to and the list
            // of objects that will be written
            final Ds3ClientHelpers.Job job = helper.startWriteJob(bucketName, objects);

            // To put metadata with each file we need to attach the metadata with a callback
            job.withMetadata(new MetadataAccess() {
                @Override
                public Map<String, String> getMetadataValue(final String objectName) {
                    // Return a map of the metadata that you want assigned to the request object
                    final Map<String, String> metadata = new HashMap<>();
                    metadata.put("File-Format", "txt");
                    return metadata;
                }
            });

            // Start the write job using an Object Putter that will read the files
            // from the local file system and put the metadata with each file.
            job.transfer(new FileObjectPutter(inputPath));
        }
    }
}
