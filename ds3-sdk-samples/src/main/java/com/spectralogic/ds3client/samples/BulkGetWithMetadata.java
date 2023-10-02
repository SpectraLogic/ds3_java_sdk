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

import com.google.common.base.Joiner;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.Ds3ClientBuilder;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.helpers.FileObjectGetter;
import com.spectralogic.ds3client.helpers.MetadataReceivedListener;
import com.spectralogic.ds3client.networking.Metadata;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class BulkGetWithMetadata {
    public static void main(final String[] args) throws IOException {
        try (final Ds3Client client = Ds3ClientBuilder.fromEnv().withHttps(false).build()) {

            // Wrap the Ds3Client with the helper functions
            final Ds3ClientHelpers helper = Ds3ClientHelpers.wrap(client);

            // The bucket that we will be writing to
            final String bucketName = "my_bucket";

            // Create a job to get all the objects in a bucket
            final Ds3ClientHelpers.Job job = helper.startReadAllJob(bucketName);

            // To retrieve the metadata for each file, attach a callback for the metadata
            job.attachMetadataReceivedListener(new MetadataReceivedListener() {
                @Override
                public void metadataReceived(final String objectName, final Metadata metadata) {
                    printMetadata(objectName, metadata);
                }
            });

            // The local path that files should be written to
            final Path destinationPath = Paths.get("output");

            // Start the read job using an Object Getter that will write the files
            // to the local file system.
            job.transfer(new FileObjectGetter(destinationPath));
        }
    }

    private static void printMetadata(final String objectName, final Metadata metadata) {
        final StringBuilder builder = new StringBuilder();
        final Joiner joiner = Joiner.on(", ");
        builder.append("Metadata for object ").append(objectName).append(": ");
        for (final String metadataKey : metadata.keys()) {
            final List<String> values = metadata.get(metadataKey);
            builder.append("<Key: ")
                    .append(metadataKey)
                    .append(" Values: ")
                    .append(joiner.join(values))
                    .append("> ");
        }

        System.out.println(builder);
    }
}
